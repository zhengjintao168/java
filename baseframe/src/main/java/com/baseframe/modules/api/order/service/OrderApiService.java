package com.baseframe.modules.api.order.service;

import java.util.Map;

/**
 * User: zjt
 * DateTime: 2018/8/15 18:19
 */
public interface OrderApiService {

    /**
     * 创建订单
     * @param orderNo：订单号
     * @param payPrice：支付金额
     * @param istype：1支付宝 2微信
     * @param orderuid：用户名称或唯一标识等
     * @return
     */
    int createOrder(String orderNo, String payPrice, String istype, String orderuid);

    /**
     * 查询订单信息
     * @param orderNo
     * @return
     */
    Map<String,Object> queryOrderInfoByOrderNo(String orderNo);

}