package com.service;

import com.entity.myKill;

import java.util.List;

/**
 * @author false
 * @date 20/4/10 11:02
 */

public interface IndexService {
    /**
     * 找到所有秒杀商品
     * @return
     */
    List<myKill> findAll(String str);
}
