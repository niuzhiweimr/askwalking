package com.cloud.askwalking.gateway.configuration;

import com.cloud.askwalking.core.domain.GatewayMethodDefinition;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author niuzhiwei
 */
@Slf4j
public class GatewayServiceLocalRouter extends AbstractGatewayServiceRouter {

    private Map<String, GatewayServiceGroup> serviceGroupMap = new ConcurrentHashMap<>();

    @Override
    public GatewayMethodDefinition getMethodDefinition(String uri) {
        if (this.serviceGroupMap.containsKey(uri)) {
            return this.serviceGroupMap.get(uri).getMethodDefinition(uri);
        }
        return null;
    }

    @Override
    public void putMethodDefinition(GatewayMethodDefinition methodDefinition) {
        String requestUri = methodDefinition.getRequestUri();
        if (this.serviceGroupMap.containsKey(requestUri)) {
            this.serviceGroupMap.get(requestUri).putMethodDefinition(methodDefinition);
        } else {
            GatewayServiceGroup serviceGroup = new GatewayServiceGroup();
            serviceGroup.putMethodDefinition(methodDefinition);
            this.serviceGroupMap.put(requestUri, serviceGroup);
        }
    }

    @Override
    public void delMethodDefinition(String uri) {

        if (!this.serviceGroupMap.containsKey(uri)) {
            return;
        }
        this.serviceGroupMap.remove(uri);
    }

    @Override
    public List<String> getRequestUris() {
        return new ArrayList<>(this.serviceGroupMap.keySet());
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) {

    }

}
