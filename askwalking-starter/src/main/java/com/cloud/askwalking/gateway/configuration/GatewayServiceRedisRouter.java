package com.cloud.askwalking.gateway.configuration;

import com.cloud.askwalking.core.domain.GatewayMethodDefinition;
import org.springframework.context.ApplicationContext;

import java.util.List;

/**
 * 扩展redis 网关集群时使用 暂时不做处理后续处理
 *
 * @author niuzhiwei
 */
public class GatewayServiceRedisRouter extends AbstractGatewayServiceRouter {

    private ApplicationContext applicationContext;

    @Override
    public GatewayMethodDefinition getMethodDefinition(String uri) {
        return null;
    }

    @Override
    public void putMethodDefinition(GatewayMethodDefinition methodDefinition) {

    }

    @Override
    public void delMethodDefinition(String uri) {

    }

    @Override
    public List<String> getRequestUris() {
        return null;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }


}
