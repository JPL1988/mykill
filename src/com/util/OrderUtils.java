package com.util;

import java.util.concurrent.ThreadLocalRandom;

/**
 * @author false
 * @date 20/4/21 15:38
 */
public class OrderUtils {

    public static long getorderid(long phone){
        long time = System.currentTimeMillis();
        String t=String.valueOf(time);
        StringBuilder res = new StringBuilder().append(t.substring(t.length()-6));
        res.append(String.valueOf(phone).substring(7));
        int rand = ThreadLocalRandom.current().nextInt(10000000,99999999);
        res.append(rand);
        return Long.valueOf(res.toString());
    }
}
