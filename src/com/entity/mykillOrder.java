package com.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @author false
 * @date 20/4/9 21:32
 */

public class mykillOrder implements Serializable {
    private long orderid;//订单号
    private long killid; //秒杀到的商品ID
    private BigDecimal money; //支付金额

    private long userPhone; //秒杀用户的手机号

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime; //创建时间

    private int status; //订单状态， -1:无效 0:成功 1:已付款

    public long getOrderid() {
        return orderid;
    }

    public void setOrderid(long orderid) {
        this.orderid = orderid;
    }

    public long getKillid() {
        return killid;
    }

    public void setKillid(long killid) {
        this.killid = killid;
    }

    public BigDecimal getMoney() {
        return money;
    }

    public void setMoney(BigDecimal money) {
        this.money = money;
    }

    public long getUserPhone() {
        return userPhone;
    }

    public void setUserPhone(long userPhone) {
        this.userPhone = userPhone;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }


    @Override
    public String toString() {
        return "SeckillOrder{" +
                "seckillId=" + killid +
                ", money=" + money +
                ", createTime=" + createTime +
                ", status=" + status +
                '}';
    }
}
