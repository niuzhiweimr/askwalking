package com.cloud.askwalking.core;

import com.cloud.askwalking.core.domain.GatewayMethodDefinition;

import java.util.List;

/**
 * @author niuzhiwei
 */
public abstract class AbstractGatewayServiceDiscovery {

    /**
     * 获取方法定义
     *
     * @param uri
     * @return
     */
    public abstract GatewayMethodDefinition getMethodDefinition(String uri);

    /**
     * 获取所有uri映射
     *
     * @return
     */
    public abstract List<String> getUriMappings();

}

