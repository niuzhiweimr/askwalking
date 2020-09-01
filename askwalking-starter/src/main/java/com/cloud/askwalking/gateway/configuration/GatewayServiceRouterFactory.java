package com.cloud.askwalking.gateway.configuration;

import com.cloud.askwalking.core.constants.GatewayConstant;
import lombok.extern.slf4j.Slf4j;

/**
 * @author niuzhiwei
 */
@Slf4j
public class GatewayServiceRouterFactory {

    /**
     * 获取service路由 注意：非单例工厂仅对应系统初始化获取所指定路由时使用
     *
     * @param serviceRouterMode
     * @return
     */
    public static AbstractGatewayServiceRouter getServiceRouter(String serviceRouterMode) {

        if (log.isDebugEnabled()) {
            log.debug("[GatewayServiceRouterFactory] serviceRouterMode :{}", serviceRouterMode);
        }

        switch (serviceRouterMode) {

            case GatewayConstant.SERVICE_ROUTER_LOCAL:
                return new GatewayServiceLocalRouter();

            case GatewayConstant.SERVICE_ROUTER_REDIS:
                return new GatewayServiceRedisRouter();

            default:
                return null;
        }
    }


}
