package com.mapper.Interface;

import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;

import com.entity.mykillOrder;
/**
 * @author false
 * @date 20/4/17 17:12
 */
public interface myKillOrderMapper {

    /**
     * 插入订单信息
     * @param killid 商品id
     * @param userPhone 用户电话
     * @param money 所需金额
     * @param orderid 订单号
     * @return 更新的行数
     */
     int insertOrder(@Param("killid")Long killid,
                           @Param("money")BigDecimal money,
                           @Param("userPhone")Long userPhone,
                           @Param("orderid")Long orderid);

    /**
     * 查找订单
     * @param orderid 订单号
     * @return
     */
     mykillOrder findOrder(Long orderid);

    /**
     * 支付订单
     * @param orderid
     * @return
     */
     int pay(long orderid);

     int orderTimeOut(long orderid);
}
