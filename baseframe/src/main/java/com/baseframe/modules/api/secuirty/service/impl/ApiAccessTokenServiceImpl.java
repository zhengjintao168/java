package com.baseframe.modules.api.app.secuirty.service.impl;

import com.baseframe.modules.api.app.secuirty.dao.ApiAccessTokenDao;
import com.baseframe.modules.api.app.secuirty.model.ApiAccessTokenModel;
import com.baseframe.modules.api.app.secuirty.service.ApiAccessTokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * User: zjt
 * DateTime: 2016/10/31 21:57
 */
@Service
public class ApiAccessTokenServiceImpl implements ApiAccessTokenService{

    @Autowired
    private ApiAccessTokenDao apiAccessTokenDao;

    @Override
    public Integer addApiAccessToken(ApiAccessTokenModel accessTokenModel) {

        Integer count = 0;

        //删除访问用户当前的token
        count += apiAccessTokenDao.deleteApiAccessTokenByUserId(accessTokenModel.getUserId());

        //重新添加
        count += apiAccessTokenDao.addApiAccessToken(accessTokenModel);

        return count;
    }

    @Override
    public ApiAccessTokenModel queryByAccessToken(String accessToken) {
        return apiAccessTokenDao.queryByAccessToken(accessToken);
    }

    @Override
    public Integer updateLastAccessTime(String id) {
        return apiAccessTokenDao.updateLastAccessTime(id);
    }

}