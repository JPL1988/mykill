package com.interceptor;

import com.alibaba.fastjson.JSONObject;
import com.entity.myKill;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import redis.clients.jedis.JedisCluster;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

/**
 * @author false
 * @date 20/4/24 11:06
 */
public class myInterceptor extends HandlerInterceptorAdapter {

    public static Semaphore semaphore = new Semaphore(100);

    private Logger logger = Logger.getLogger(this.getClass());
    //缓存集群
    @Autowired
    private JedisCluster jedisCluster;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String[] requestURI = request.getRequestURI().split("/");
        String key = requestURI[4];
        String s = jedisCluster.get(key);
        myKill mykill = (myKill) JSONObject.toJavaObject(JSONObject.parseObject(s), myKill.class);
        if(mykill==null){
            soldOut(response,"url error");
            return false;
        }
        if(mykill.getStockCount()<=0){
            soldOut(response,"已售罄，请15分钟后重试");
            return false;
        }
        boolean b = semaphore.tryAcquire(1, 60, TimeUnit.SECONDS);
        if(b==false){
            soldOut(response,"秒杀失败,请重试");
            return false;
        }
        return super.preHandle(request, response, handler);
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        semaphore.release();
        super.postHandle(request, response, handler, modelAndView);
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        semaphore.release();
        super.afterCompletion(request, response, handler, ex);
    }

    private void soldOut(HttpServletResponse response, String error) throws IOException {
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json; charset=utf-8");
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("success","false");
        jsonObject.put("error",error);
        logger.debug("json_______:"+jsonObject.toJSONString());
        response.getWriter().append(jsonObject.toJSONString());
    }
}
