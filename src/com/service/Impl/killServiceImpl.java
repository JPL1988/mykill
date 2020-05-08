package com.service.Impl;

import com.alibaba.fastjson.JSONObject;
import com.entity.myKill;
import com.entity.mykillOrder;
import com.exception.killException;
import com.exception.repeatException;
import com.mapper.Interface.myKillOrderMapper;
import com.mapper.Interface.mykillMapper;
import com.result.Exposer;
import com.result.myKillResult;
import com.service.killService;
import com.util.OrderUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import redis.clients.jedis.JedisCluster;
import sun.misc.BASE64Encoder;

import java.security.MessageDigest;
import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * @author false
 * @date 20/4/12 15:48
 */
@Component
public class killServiceImpl implements killService {
    Logger logger = Logger.getLogger(this.getClass());
    //用于生成随机参数，暴露url
    public String salt="ffdrgsoamfo_.dw26gdh";

    //商品mapper
    @Autowired
    private mykillMapper mykillMapper;

    //缓存集群
    @Autowired
    private JedisCluster jedisCluster;

    //订单mapper
    @Autowired
    private myKillOrderMapper  myKillOrderMapper;

    //订单失效,
    @Autowired
    private RedisTemplate redisTemplate;

    public myKill findById(Long id,boolean reminds){
        String key = String.valueOf(id);
        //查询缓存
        if(reminds&&jedisCluster.exists(key)){
            String str = jedisCluster.get(key);
            myKill res = JSONObject.toJavaObject(JSONObject.parseObject(str),myKill.class);
            return res;
        }
        //缓存未命中，查询数据库，更新缓存
        myKill res = mykillMapper.findById(id);
        if(res==null)
            return null;
        String str = JSONObject.toJSON(res).toString();
        jedisCluster.set(res.getKillid()+"",str);
        Date date = new Date();
        //大约在0点后过期
        int expire =(24-date.getHours())*3600;
        jedisCluster.expire(res.getKillid()+"",expire);
        return res;
    }

    /**
     * 暴露抢购连接
     * @param killid 商品编号
     * @return
     */
    public Exposer exposer(Long killid){
        myKill mykill = findById(killid,true);
        if(mykill==null){
            return new Exposer(false,"");
        }
        Date date = new Date();
        if(date.getTime()<mykill.getStartTime().getTime()||date.getTime()>mykill.getEndTime().getTime()){
            return new Exposer(false,"");
        }
        String md5 = getMD5(killid+salt);
        return new Exposer(true,md5);
    }


    @Transactional(propagation = Propagation.REQUIRED,
            isolation = Isolation.REPEATABLE_READ,
            timeout = 5,
            rollbackFor = {Exception.class,RuntimeException.class,
                    killException.class,repeatException.class})
    @Override
    public myKillResult executeKill(Long killid, String md5, Long userPhone) throws Exception{
        //检查数据异常
        if(userPhone==null){
            return new myKillResult<>(false,null,"phnoe number error");
        }else if(md5==null||!md5.equals(getMD5(killid+salt))){
            return new myKillResult<>(false,null,"url error");
        }
        //查询商品信息
        myKill mykill = findById(killid,true);
        if(mykill==null){
            return new myKillResult<>(false,null,"url error");
        }
        if(mykill.getStockCount()<=0){
            return new myKillResult<>(false,null,"已售罄，如有未付款的，您可以稍后重试");
        }
        if(mykill.getEndTime().getTime()<new Date().getTime()){
            return new myKillResult<>(false,null,"秒杀已结束，请期待下一场");
        }
        //生成订单号，执行秒杀
        long orderid = OrderUtils.getorderid(userPhone);
        //减少库存
        int successReduce=0;
        try{
            successReduce = mykillMapper.reduceStock(mykill.getKillid(),new Date());
        }catch (Exception e){
            throw new killException("秒杀失败");
        }
        if(successReduce<1){
            throw new killException("秒杀失败，已售罄，可能15分钟后仍有未付款的哦");
        }
        //插入订单
        int successInsert = 0;
        try {
            successInsert = myKillOrderMapper.insertOrder(mykill.getKillid(),mykill.getCostPrice(),userPhone,orderid);
            //把order插入消息队列,方便测试暂时为3分钟
            mykillOrder mykillOrder = myKillOrderMapper.findOrder(orderid);
            redisTemplate.opsForValue().set(orderid+"",mykillOrder);
            redisTemplate.expire(orderid+"",15, TimeUnit.MINUTES);
        }catch (Exception e){
            throw new killException("秒杀失败");
        }
        if(successInsert<1){
            throw new repeatException("限购一件，请勿重复秒杀");
        }
        //更新缓存中的库存,更新失败也代表秒杀成功，故不捕获2异常不抛出
        mykill.reduceStock();
        jedisCluster.set(mykill.getKillid()+"",JSONObject.toJSON(mykill).toString());
        return new myKillResult(true,"/mykill/success/"+killid+"/"+orderid+"/"+userPhone,"");
    }

    /**
     * 查找订单返回订单信息
     * @param orderid
     * @return 订单信息
     */
    @Override
    public mykillOrder success(long orderid) {
        mykillOrder myorder = myKillOrderMapper.findOrder(orderid);
        if(myorder==null){
            return null;
        }
        return myorder;
    }

    @Transactional
    @Override
    public boolean pay(long orderid) throws Exception{
        int line = myKillOrderMapper.pay(orderid);
        if(line==0)
            return false;
        else
            return true;
    }

    /**
     * 处理失效订单，通过redis监听过期键实现
     * 1、尝试更新订单信息为失效
     * 2、更新成功，增加库存,更新缓存中的库存。更新失败，表示该订单已被支付。不做任何处理。
     * @param orderid 订单号
     */
    @Override
    public void dealTimeoutOrder(Long orderid) {
        int line = myKillOrderMapper.orderTimeOut(orderid);
        if(line==1){
            mykillOrder order = myKillOrderMapper.findOrder(orderid);
            if(order==null){
                return;
            }
            //更新库存，
            mykillMapper.addStock(order.getKillid());
            //更新缓存，
            findById(order.getKillid(),false);
        }
    }

    /**
     * 根据字符串返回md5值
     * @param str
     * @return
     */
    private String getMD5(String str){
        try {
            //md5加密
            MessageDigest messageDigest = MessageDigest.getInstance("md5");
            byte[] res =messageDigest.digest(str.getBytes("UTF-8"));
            //base64编码
            BASE64Encoder encoder = new BASE64Encoder();
            return encoder.encode(res);
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

}
