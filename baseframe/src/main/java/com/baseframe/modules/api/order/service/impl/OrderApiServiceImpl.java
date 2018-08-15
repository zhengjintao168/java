package com.baseframe.modules.api.order.service.impl;

import com.baseframe.modules.api.order.dao.OrderApiDao;
import com.baseframe.modules.api.order.service.OrderApiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * User: zjt
 * DateTime: 2018/8/15 18:20
 */
@Service
public class OrderApiServiceImpl implements OrderApiService{

    @Autowired
    private OrderApiDao orderApiDao;

}