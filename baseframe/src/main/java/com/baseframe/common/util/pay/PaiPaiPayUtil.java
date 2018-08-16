package com.baseframe.common.util.pay;

import com.baseframe.common.util.HttpClientUtil;
import com.baseframe.common.util.encrypt.Md5Util;
import org.json.JSONObject;

import java.util.*;

/**
 * User: zjt
 * DateTime: 2018/8/16 09:03
 * <p>
 * 派派支付工具类
 */
public class PaiPaiPayUtil {

    private static final String PAY_SERVER_URL = "https://www.yellwoshop.cn/pay";

    public static final String UID = "10430";                                                  //必填。您的商户唯一标识，注册后在设置里获得。
    private static final String TOKEN = "1a9b42a6b6bf6af1f2940dd23d95aa06";                     //加密token,不可存储在客户端

    /**
     * 生成签名
     *
     * @return
     */
    public static String buildSign(String price, String istype, String notify_url, String return_url, String orderid, String orderuid, String goodsname) {

        Map<String, String> params = new HashMap<String, String>();
        params.put("uid", UID);
        params.put("price", price);
        params.put("istype", istype);
        params.put("notify_url", notify_url);
        params.put("return_url", return_url);
        params.put("orderid", orderid);
        params.put("orderuid", orderuid);
        params.put("goodsname", goodsname);
        params.put("token", TOKEN);

        //1, 对参数进行排序（注意需要包含token）
        List<String> keys = new ArrayList<String>(params.keySet());
        Collections.sort(keys);

        //2, 拼接字符串，md5 32加密完成签名生成
        StringBuilder key = new StringBuilder();
        for (int i = 0; i < keys.size(); i++) {
            String paramKey = keys.get(i);
            String paramValue = params.get(paramKey);
            key.append(paramValue);
        }

        return Md5Util.md5_32(key.toString()).toLowerCase();

    }


}