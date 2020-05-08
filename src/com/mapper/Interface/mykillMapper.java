package com.mapper.Interface;

import com.entity.myKill;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

/**
 * @author false
 * @date 20/4/9 22:01
 */
public interface mykillMapper {

    /**
     * 查询所有秒杀商品的信息
     * @return
     */
    public List<myKill> findAll(@Param("starttime") Date starttime,@Param("endtime") Date endtime);

    /**
     * 根据主键查询当前秒杀商品的信息
     * @return
     */
    public myKill findById(long id);

    /**
     * 尝试减库存。表示在当前事务中，在库存大于1的时候再去减少库存，而隔离级别，是处理事务并发修改可能出现的错误。
     * 对于Mapper映射接口方法中存在多个参数的要加@Param()注解标识字段名称，不然Mybatis不能识别出来哪个字段相互对应
     *
     * @param killid 秒杀商品ID
     * @param killTime  秒杀时间
     * @return 返回此SQL更新的记录数，如果>=1表示更新成功
     */
    int reduceStock(@Param("killid") long killid, @Param("killTime") Date killTime);
    int addStock(long killid);
}
