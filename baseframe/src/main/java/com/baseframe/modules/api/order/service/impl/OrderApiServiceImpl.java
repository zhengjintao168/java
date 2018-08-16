package com.baseframe.modules.api.order.service.impl;

import com.baseframe.modules.api.order.dao.OrderApiDao;
import com.baseframe.modules.api.order.service.OrderApiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * User: zjt
 * DateTime: 2018/8/15 18:20
 */
@Service
public class OrderApiServiceImpl implements OrderApiService {

    @Autowired
    private OrderApiDao orderApiDao;

    @Override
    public int createOrder(String orderNo, String payPrice, String istype, String orderuid) {
        return orderApiDao.createOrder(orderNo, payPrice, istype, orderuid);
    }

    @Override
    public Map<String, Object> queryOrderInfoByOrderNo(String orderNo) {
        return orderApiDao.queryOrderInfoByOrderNo(orderNo);
    }


}