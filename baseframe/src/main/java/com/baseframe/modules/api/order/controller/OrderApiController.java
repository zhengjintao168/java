package com.baseframe.modules.api.order.controller;

import com.baseframe.common.util.IpUtil;
import com.baseframe.common.util.OrderNoUtil;
import com.baseframe.common.util.pay.PaiPaiPayUtil;
import com.baseframe.modules.api.order.service.OrderApiService;
import com.baseframe.modules.base.controller.BaseController;
import com.baseframe.modules.sysmgr.log.annotation.SystemControllerLog;
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

    private static final String NOTIFY_URL = "/api/order/paySuccessNotify";
    private static final String RETURN_URL = "/static/result/paySuccess.html";

    /**
     * 创建订单
     *
     * @param request
     * @param response
     * @param orderPrice
     * @param istype
     * @param orderuid
     * @throws Exception
     */
    @RequestMapping(value = "/createOrder", produces = "application/json")
    @SystemControllerLog(module = "订单接口", operation = "创建订单")
    public void createOrder(HttpServletRequest request, HttpServletResponse response, String orderPrice, String istype, String orderuid) throws Exception {

        String orderNo = OrderNoUtil.getInstance().generaterUniqueOrderNo("");
        int count = orderApiService.createOrder(orderNo, orderPrice, istype, orderuid);

        JSONObject json = new JSONObject();
        if (count > 0) {
            json.put("success", true);
            json.put("msg", "创建订单成功");

            Map<String, Object> data = new HashMap<>();
            data.put("orderNo", orderNo);
            json.put("data", data);

        } else {
            json.put("success", false);
            json.put("msg", "创建订单失败");
        }

        writeJson(response, json.toString());

    }

    /**
     * 获取支付信息 - 前端直接表单提交，跳转到默认的支付与页面
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
        String orderPrice = orderInfo.get("order_price").toString();
        String istype = orderInfo.get("istype").toString();
        String goodsname = "测试商品";
        String notifyUrl = IpUtil.getBaseURL(request) + NOTIFY_URL;
        String returnUrl = IpUtil.getBaseURL(request) + RETURN_URL;

        //生成支付密钥（此操作只能在服务端进行）
        Map<String, String> params = new HashMap<>();
        params.put("uid", PaiPaiPayUtil.UID);
        params.put("price", orderPrice);
        params.put("istype", istype);
        params.put("notify_url", notifyUrl);
        params.put("return_url", returnUrl);
        params.put("orderid", orderNo);
        params.put("orderuid", orderUid);
        params.put("goodsname", goodsname);
        String key = PaiPaiPayUtil.buildPaySign(params);

        //返回参数给客户端
        Map<String, Object> payInfo = new HashMap<>();
        payInfo.put("uid", PaiPaiPayUtil.UID);
        payInfo.put("price", orderPrice);
        payInfo.put("istype", istype);
        payInfo.put("notify_url", notifyUrl);
        payInfo.put("return_url", returnUrl);
        payInfo.put("orderid", orderNo);
        payInfo.put("orderuid", orderUid);
        payInfo.put("goodsname", goodsname);
        payInfo.put("key", key);

        json.put("success", true);
        json.put("msg", "获取支付信息成功");
        json.put("data", payInfo);

        writeJson(response, json.toString());

    }

    /**
     * 获取支付二维码 - 用于自定义表单页面
     *
     * @param request
     * @param response
     * @param orderNo
     * @throws Exception
     */
    @RequestMapping(value = "/getPayQrCode", produces = "application/json")
    @SystemControllerLog(module = "获取支付二维码", operation = "获取支付二维码")
    public void getPayQrCode(HttpServletRequest request, HttpServletResponse response, String orderNo) throws Exception {

        JSONObject json = new JSONObject();

        Map<String, Object> orderInfo = orderApiService.queryOrderInfoByOrderNo(orderNo);
        if (orderInfo == null) {
            json.put("success", false);
            json.put("msg", "订单不存在！");
            writeJson(response, json.toString());
            return;
        }

        String orderUid = orderInfo.get("order_uid").toString();
        String orderPrice = orderInfo.get("order_price").toString();
        String istype = orderInfo.get("istype").toString();
        String goodsname = "测试商品";
        String notifyUrl = IpUtil.getBaseURL(request) + NOTIFY_URL;
        String returnUrl = IpUtil.getBaseURL(request) + RETURN_URL;

        //获取支付二维码
        String result = PaiPaiPayUtil.getPayQrCode(orderPrice, istype, notifyUrl, returnUrl, orderNo, orderUid, goodsname);
        writeJson(response, result);

    }

    /**
     * 查询订单支付状态
     *
     * @param request
     * @param response
     * @param orderNo
     * @throws Exception
     */
    @RequestMapping(value = "/getStatusByOrderId", produces = "application/json")
    @SystemControllerLog(module = "订单查询接口", operation = "查询订单支付状态")
    public void getStatusByOrderId(HttpServletRequest request, HttpServletResponse response, String orderNo) throws Exception {

        String result = PaiPaiPayUtil.getStatusByOrderId(orderNo);
        writeJson(response, result);

    }

    /**
     * 支付成功回调通知
     *
     * @param request
     * @param response
     * @param platform_trade_no
     * @param orderid
     * @param price
     * @param realprice
     * @param orderuid
     * @param key
     * @throws Exception
     */
    @RequestMapping(value = "/paySuccessNotify", produces = "application/json")
    @SystemControllerLog(module = "支付成功回调", operation = "支付成功回调")
    public void paySuccessNotify(HttpServletRequest request, HttpServletResponse response
            , String platform_trade_no, String orderid, String price
            , String realprice, String orderuid, String key) throws Exception {

        Map<String, String> params = new HashMap<>();
        params.put("platform_trade_no", platform_trade_no);
        params.put("orderid", orderid);
        params.put("price", price);
        params.put("realprice", realprice);
        params.put("orderuid", orderuid);

        String myKey = PaiPaiPayUtil.buildPaySign(params);
        if (key.equals(myKey)) {
            logger.info("回调成功,单号为：" + orderid);

            //修改订单状态为支付成功
            orderApiService.updateOrderStatusToPaySuccess(platform_trade_no, orderid, realprice);

            response.getWriter().print("OK");
            response.getWriter().flush();
        }

    }

}