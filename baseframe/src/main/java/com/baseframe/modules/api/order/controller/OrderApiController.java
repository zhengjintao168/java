package com.baseframe.modules.api.order.controller;

import com.baseframe.common.util.OrderNoUtil;
import com.baseframe.common.util.pay.PaiPaiPayUtil;
import com.baseframe.modules.api.order.service.OrderApiService;
import com.baseframe.modules.base.controller.BaseController;
import com.baseframe.modules.sysmgr.log.annotation.SystemControllerLog;
import com.baseframe.modules.sysmgr.role.model.RoleModel;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

/**
 * User: zjt
 * DateTime: 2018/8/15 18:19
 */
@Controller
@RequestMapping("/api/order")
public class OrderApiController extends BaseController {

    private static final Logger logger = LoggerFactory.getLogger(OrderApiController.class);

    @Autowired
    private OrderApiService orderApiService;

    /**
     * 创建订单
     *
     * @param request
     * @param response
     * @param payPrice
     * @param istype
     * @param orderuid
     * @throws Exception
     */
    @RequestMapping(value = "/createOrder", produces = "application/json")
    @SystemControllerLog(module = "订单接口", operation = "创建订单")
    public void createOrder(HttpServletRequest request, HttpServletResponse response, String payPrice, String istype, String orderuid) throws Exception {

        String orderNo = OrderNoUtil.getInstance().generaterUniqueOrderNo("");
        int count = orderApiService.createOrder(orderNo, payPrice, istype, orderuid);

        JSONObject json = new JSONObject();
        if (count > 0) {
            json.put("success", true);
            json.put("msg", "创建订单成功");

            Map<String,Object> data = new HashMap<>();
            data.put("orderNo" , orderNo);
            json.put("data", data);

        } else {
            json.put("success", false);
            json.put("msg", "创建订单失败");
        }

        writeJson(response, json.toString());

    }

    /**
     * 获取支付信息
     *
     * @param request
     * @param response
     * @param orderNo
     * @throws Exception
     */
    @RequestMapping(value = "/getPayInfo", produces = "application/json")
    @SystemControllerLog(module = "获取支付信息", operation = "获取支付信息")
    public void getPayInfo(HttpServletRequest request, HttpServletResponse response, String orderNo) throws Exception {

        JSONObject json = new JSONObject();

        Map<String, Object> orderInfo = orderApiService.queryOrderInfoByOrderNo(orderNo);
        if (orderInfo == null) {
            json.put("success", false);
            json.put("msg", "订单不存在！");
            writeJson(response, json.toString());
            return;
        }

        String orderUid = orderInfo.get("order_uid").toString();
        String payPrice = orderInfo.get("pay_price").toString();
        String istype = orderInfo.get("istype").toString();
        String notify_url = "www.qq.com";
        String return_url = "www.qq.com";
        String goodsname = "测试商品";

        //生成支付密钥（此操作只能在服务端进行）
        String key = PaiPaiPayUtil.buildSign(payPrice, istype, notify_url, return_url, orderNo, orderUid, goodsname);

        Map<String,Object> payInfo = new HashMap<>();
        payInfo.put("uid" , PaiPaiPayUtil.UID);
        payInfo.put("price" , payPrice);
        payInfo.put("istype" , istype);
        payInfo.put("notify_url" , notify_url);
        payInfo.put("return_url" , return_url);
        payInfo.put("orderid" , orderNo);
        payInfo.put("orderuid" , orderUid);
        payInfo.put("goodsname" , goodsname);
        payInfo.put("key" , key);

        json.put("success", true);
        json.put("msg", "获取支付密钥成功");
        json.put("data", payInfo);

        writeJson(response, json.toString());

    }

}