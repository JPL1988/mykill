package com.service;

import com.entity.myKill;
import com.entity.mykillOrder;
import com.result.Exposer;
import com.result.myKillResult;

import java.math.BigDecimal;

/**
 * @author false
 * @date 20/4/12 15:47
 */
public interface killService {

    myKill findById(Long id,boolean reminds);
    Exposer exposer(Long killid);
    myKillResult executeKill(Long killid,String md5,Long userPhone) throws Exception;
    mykillOrder success(long orderid);
    boolean pay(long orderid) throws Exception;
    void dealTimeoutOrder(Long orderid);
}
