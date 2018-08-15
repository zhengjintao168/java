package com.baseframe.modules.api.order.controller;

import com.baseframe.modules.api.order.service.OrderApiService;
import com.baseframe.modules.base.controller.BaseController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * User: zjt
 * DateTime: 2018/8/15 18:19
 */
@Controller
@RequestMapping("/api/order")
public class OrderApiController extends BaseController{

    private static final Logger logger = LoggerFactory.getLogger(OrderApiController.class);

    @Autowired
    private OrderApiService orderApiService;


}