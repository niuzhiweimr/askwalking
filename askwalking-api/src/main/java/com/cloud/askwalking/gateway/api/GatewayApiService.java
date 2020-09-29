package com.cloud.askwalking.gateway.api;

import com.cloud.askwalking.repository.model.ConfigureApiDO;

import java.util.List;

/**
 * @author niuzhiwei
 */
public interface GatewayApiService {

    /**
     * 获取所有接口配置
     *
     * @return
     */
    List<ConfigureApiDO> getAll();

}
