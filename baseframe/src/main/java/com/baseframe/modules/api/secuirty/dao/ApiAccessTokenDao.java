package com.baseframe.modules.api.secuirty.dao;

import org.mybatis.spring.annotation.MapperScan;

/**
 * User: zjt
 * DateTime: 2016/10/31 21:57
 */
@MapperScan
public interface ApiAccessTokenDao {

    /**
     * 新增接口访问token
     * @param accessTokenModel
     * @return
     */
    Integer addApiAccessToken(com.baseframe.modules.api.secuirty.model.ApiAccessTokenModel accessTokenModel);

    /**
     * 根据用户id删除token
     * @param userId
     * @return
     */
    Integer deleteApiAccessTokenByUserId(String userId);

    /**
     * 查询token
     * @param accessToken
     * @return
     */
    com.baseframe.modules.api.secuirty.model.ApiAccessTokenModel queryByAccessToken(String accessToken);

    /**
     * 更新token最后访问时间
     * @param id
     * @return
     */
    Integer updateLastAccessTime(String id);

}