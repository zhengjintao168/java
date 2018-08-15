package com.baseframe.modules.api.secuirty.config;

/**
 * User: zjt
 * DateTime: 2016/10/31 09:47
 *
 * 接口的一些相关配置,如不需要进行过滤的接口,不需要登录的接口
 * 全局错误码定义等
 */
public interface ApiConfig {

    /**
     * 公开接口: 即不需要进行安全性过滤,如支付宝回调,第三方通知回调,分享等
     */
    String[] OPEN_API_PATH_ARRAY = {"/notify","/uploadImg","/share","/test"};

    /**
     * 不需要登录即可调用的接口(即无需token认证)
     */
    String[] NOT_LOGIN_PATH_ARRAY  = {"/user/login","",""};

    /**
     * 全局响应吗 - 1XX: 客户端错误
     */
    Integer RESPONSE_CODE_PARAM_ERROR = 101;                   //参数错误(例如缺少appkey,签名,时间戳,业务参数等)
    Integer RESPONSE_CODE_REQUEST_TIMEOUT = 102;               //请求时间无效(防止请求盗用)
    Integer RESPONSE_CODE_SIGN_ERROR = 103;                    //签名错误
    Integer RESPONSE_CODE_TOKEN_TIMEOUT = 104;                 //token失效

    /**
     * 全局响应吗 - 2XX: 服务端错误
     */
    Integer RESPONSE_CODE_SERVER_ERROR = 201;                  //服务器内部错误

    /**
     * 全局响应吗
     * 0: 请求成功
     * 其它: 与业务相关信息,例如验证码错误,密码错误,支付失败等....'
     */
    Integer RESPONSE_CODE_SUCCESS = 0;

}