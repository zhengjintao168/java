package com.baseframe.modules.api.app.secuirty.model;

import java.util.Date;

/**
 * User: zjt
 * DateTime: 2016/10/31 21:31
 *
 * 接口访问相关信息
 *
 */
public class ApiAccessTokenModel {

    private String id;
    private String userId;                   //用户ID
    private String accessToken;              //访问token,用于验证api调用者的身份,登陆后随机生成
    private Date lastAccessTime;             //最后一次访问时间
    private Integer validity;                //token的有效期,单位分钟,在最后一次访问的基础上加上此时间进行验证
    private Date createTime;                //创建时间

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public Date getLastAccessTime() {
        return lastAccessTime;
    }

    public void setLastAccessTime(Date lastAccessTime) {
        this.lastAccessTime = lastAccessTime;
    }

    public Integer getValidity() {
        return validity;
    }

    public void setValidity(Integer validity) {
        this.validity = validity;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

}