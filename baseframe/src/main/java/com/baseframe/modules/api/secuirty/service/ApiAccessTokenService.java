package com.baseframe.modules.api.app.secuirty.service;

import com.baseframe.modules.api.app.secuirty.model.ApiAccessTokenModel;

/**
 * User: zjt
 * DateTime: 2016/10/31 21:56
 */
public interface ApiAccessTokenService {

    Integer addApiAccessToken(ApiAccessTokenModel accessTokenModel);

    ApiAccessTokenModel queryByAccessToken(String accessToken);

    Integer updateLastAccessTime(String id);

}