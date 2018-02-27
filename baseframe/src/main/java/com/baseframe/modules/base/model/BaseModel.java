package com.baseframe.modules.base.model;

import com.baseframe.common.util.JsonUtil;
import com.baseframe.modules.generate.model.ColumnModel;

import java.util.ArrayList;
import java.util.List;

/**
 * User: zjt
 * DateTime: 2016/10/6 10:16
 *
 * 模型类的基类
 */
public class BaseModel {

    @Override
    public String toString() {
        return JsonUtil.parseToJsonStr(this);
    }

}