package com.cloud.askwalking.gateway.configuration;

import com.cloud.askwalking.core.domain.GatewayMethodDefinition;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author niuzhiwei
 */
public class GatewayServiceGroup {

    private Map<String, GatewayMethodDefinition> methodDefinitionMap = new ConcurrentHashMap<>();

    GatewayMethodDefinition getMethodDefinition(String uri) {
        return this.methodDefinitionMap.get(uri);
    }

    void putMethodDefinition(GatewayMethodDefinition methodDefinition) {
        this.methodDefinitionMap.put(methodDefinition.getRequestUri(), methodDefinition);
    }
}
