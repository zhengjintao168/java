package com.baseframe.modules.api.order.dao;

import org.apache.ibatis.annotations.Param;
import org.mybatis.spring.annotation.MapperScan;

import java.util.Map;

/**
 * User: zjt
 * DateTime: 2018/8/15 18:20
 */
@MapperScan
public interface OrderApiDao {

    int createOrder(@Param("orderNo") String orderNo, @Param("orderPrice") String orderPrice,
                    @Param("istype") String istype, @Param("orderuid") String orderuid);

    Map<String, Object> queryOrderInfoByOrderNo(@Param("orderNo") String orderNo);

    int updateOrderStatusToPaySuccess(@Param("platform_trade_no") String platform_trade_no
            , @Param("orderid") String orderid, @Param("realprice") String realprice);

}