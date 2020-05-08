package com.util;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * @author false
 * @date 20/4/12 21:19
 */

/**
 * 根据index页面的参数构造相应商品秒杀开始和结束时间
 */
public class myTimeUtil {

    /**
     * 将字符串转化为时间
     * @param str
     * @return
     */
    public static Date parse(String str){
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(str.substring(0,4)+'-');
        stringBuilder.append(str.substring(4,6)+'-');
        stringBuilder.append(str.substring(6,8)+' ');
        stringBuilder.append(str.substring(8,10)+":00:00");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            return sdf.parse(stringBuilder.toString());
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取结束时间，默认4小时后抢购结束
     * @param str
     * @return
     */
    public static Date endTime(String str){
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(str.substring(0,4)+'-');
        stringBuilder.append(str.substring(4,6)+'-');
        stringBuilder.append(str.substring(6,8)+' ');
        char[] s = str.substring(8,10).toCharArray();
        int x = Character.digit(s[1],10)+3;
        if(x>=10){
            stringBuilder.append((Character.digit(s[0],10)+1)+""+(x%10));
        }else {
            stringBuilder.append(s[0]+""+x);
        }
        stringBuilder.append(":59:59");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            return sdf.parse(stringBuilder.toString());
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
}
