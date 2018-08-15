package com.baseframe.modules.api.secuirty.filter;



import com.baseframe.common.context.AppContext;
import com.baseframe.common.util.StringUtil;
import com.baseframe.modules.api.secuirty.config.ApiConfig;
import com.baseframe.modules.api.secuirty.model.ApiAccessTokenModel;
import com.baseframe.modules.api.secuirty.service.ApiAccessTokenService;
import com.baseframe.modules.api.secuirty.util.ApiSignUtil;
import org.apache.commons.io.IOUtils;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


/**
 * User: zjt
 * DateTime: 2016/10/29 23:19
 * <p>
 * 对接口的安全性过滤
 */
public class ApiSecuirtyFilter extends OncePerRequestFilter {

    private static final Logger logger = LoggerFactory.getLogger(ApiSecuirtyFilter.class);

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {

        /**
         * step1: 过滤公开接口,即不需要进行安全性过滤,如支付宝回调,第三方通知回调,分享等
         */
        String uri = request.getRequestURI();
        if (uri.contains("?")) {
            uri = uri.substring(0, uri.indexOf("?"));        //只截取请求部分,去除参数,避免通过传递参数进行的恶意访问
        }

        for (String path : ApiConfig.OPEN_API_PATH_ARRAY) {

            if (uri.toLowerCase().contains(path.toLowerCase())) {
                chain.doFilter(request, response);
                return;
            }

        }

        /**
         * step2: 进行签名验证
         */
        ApiSecuirtyRequest secuirtyRequest = new ApiSecuirtyRequest(request);
        JSONObject reqJson = new JSONObject(IOUtils.toString(secuirtyRequest.getInputStream(), "utf-8"));
        JSONObject json = new JSONObject();

        //2.1 判断是否包含timestamp,sign参数
        if (StringUtil.isEmpty(reqJson.getString("timestamp")) || StringUtil.isEmpty(reqJson.getString("sign"))) {
            json.put("code", ApiConfig.RESPONSE_CODE_PARAM_ERROR);
            json.put("msg", "参数错误!");
            IOUtils.write(json.toString(), response.getWriter());
            return;
        }

        //2.2 判断请求时间跟当前时间是否超过最大间隔,防止请求url盗用 (当前限制为5分钟,即可30万毫秒)
        Long timestamp = reqJson.getLong("timestamp");
        Long currentMills = System.currentTimeMillis();
        if (Math.abs(currentMills - timestamp) > 300000) {
            json.put("code", ApiConfig.RESPONSE_CODE_REQUEST_TIMEOUT);
            json.put("msg", "请求时间无效!");
            IOUtils.write(json.toString(), response.getWriter());
            return;
        }

        //2.3 对所有请求参数和时间戳进行排序  ->  并“参数=参数值”的模式用“&”字符拼接成字符串  --> AES加密  --->  base64编码生成sign签名  --> 进行对比
        Map<String, String> params = new HashMap<String, String>();
        String[] names = JSONObject.getNames(reqJson);
        for (String key : names) {
            params.put(key, reqJson.getString(key));
        }

        try {

            String sign = reqJson.getString("sign").trim();
            String currentSign = ApiSignUtil.buildSignByMap(params).trim();

            if (!sign.equalsIgnoreCase(currentSign)) {
                json.put("code", ApiConfig.RESPONSE_CODE_SIGN_ERROR);
                json.put("msg", "签名错误!");
                IOUtils.write(json.toString(), response.getWriter());
                return;
            }

        } catch (Exception e) {
            e.printStackTrace();
            json.put("code", ApiConfig.RESPONSE_CODE_SIGN_ERROR);
            json.put("msg", "签名错误!");
            IOUtils.write(json.toString(), response.getWriter());
            return;
        }

        /**
         * step3: 进行用户身份验证,保证api调用者经过授权
         */
        //3.1 过滤无需身份认证的接口,即不需要登录就可以调用
        for (String path : ApiConfig.NOT_LOGIN_PATH_ARRAY) {

            if (uri.toLowerCase().contains(path.toLowerCase())) {
                chain.doFilter(request, response);
                return;
            }

        }

        //3.2 验证token是否有效
        String accessToken = reqJson.getString("accessToken");
        if (StringUtil.isNotEmpty(accessToken)) {

            ApiAccessTokenService apiAccessTokenService = AppContext.getApplicationContext().getBean(ApiAccessTokenService.class);
            ApiAccessTokenModel accessTokenModel = apiAccessTokenService.queryByAccessToken(accessToken);

            //判断token是否存在 , 是否在有效期内 , 如果是则更新token的最后访问时间
            if (isAvailable(accessTokenModel)) {
                apiAccessTokenService.updateLastAccessTime(accessTokenModel.getId());
            } else {
                json.put("code", ApiConfig.RESPONSE_CODE_TOKEN_TIMEOUT);
                json.put("msg", "token不存在或已失效,请重新获取!");
                IOUtils.write(json.toString(), response.getWriter());
                return;
            }

        } else {
            json.put("code", ApiConfig.RESPONSE_CODE_PARAM_ERROR);
            json.put("msg", "参数错误,缺少accessToken!");
            IOUtils.write(json.toString(), response.getWriter());
            return;
        }

        chain.doFilter(secuirtyRequest, response);                   //验证通过,放行

    }

    /**
     * 验证token是否可用
     *
     * @param accessTokenModel
     * @return
     */
    public boolean isAvailable(ApiAccessTokenModel accessTokenModel) {

        if (null == accessTokenModel) {
            return false;
        }

        //获取最后访问时间 + token有效期限
        Calendar cal = Calendar.getInstance();
        cal.setTime(accessTokenModel.getLastAccessTime());
        cal.add(Calendar.MINUTE, accessTokenModel.getValidity());

        //在当前时间的之前,即小于当前时间,代表已失效
        if (cal.before(new Date())) {
            return false;
        }

        return true;

    }

}