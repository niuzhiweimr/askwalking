package com.cloud.askwalking.gateway.pipline.context.common;

import com.cloud.askwalking.common.exception.ErrorCode;
import com.cloud.askwalking.core.context.GatewayInvokeContext;
import com.cloud.askwalking.core.domain.GatewayMethodDefinition;
import com.cloud.askwalking.gateway.configuration.GatewayServiceDiscovery;
import com.cloud.askwalking.gateway.pipline.AbstractGatewayContextHandler;
import lombok.extern.slf4j.Slf4j;

import java.util.Set;

/**
 * @author niuzhiwei
 */
@Slf4j
public class HeadContextHandler extends AbstractGatewayContextHandler {

    private GatewayServiceDiscovery gatewayServiceDiscovery;

    public HeadContextHandler(GatewayServiceDiscovery serviceDiscovery) {
        this.gatewayServiceDiscovery = serviceDiscovery;
    }

    @Override
    public void fireGatewayInvoke(GatewayInvokeContext gatewayInvokeContext) {
        if (handleGatewayInvoke(gatewayInvokeContext)) {
            this.next.fireGatewayInvoke(gatewayInvokeContext);
        } else {
            this.tail.fireGatewayInvoke(gatewayInvokeContext);
        }
    }

    @Override
    public boolean handleGatewayInvoke(GatewayInvokeContext gatewayInvokeContext) {
        if (log.isDebugEnabled()) {
            log.debug("[GatewayInvokePipeline]Start Invoke");
        }
        gatewayInvokeContext.setInvokeStartTime(System.currentTimeMillis());
        GatewayMethodDefinition serviceDefinition = this.gatewayServiceDiscovery.getMethodDefinition(
                gatewayInvokeContext.getRequestURI());
        if (serviceDefinition == null) {
            return putDebugErrorResult(gatewayInvokeContext, ErrorCode.MISSING_METHOD_DEFINITION);
        }

        //判断方法调用是否与接口提供的方法调用相符
        if (!gatewayInvokeContext.getRequestMethod().equalsIgnoreCase(serviceDefinition.getRequestMethod())) {
            return putDebugErrorResult(gatewayInvokeContext, ErrorCode.NOT_SUPPORT_METHOD);
        }

        //判断api接口是否发布
        if (serviceDefinition.getStatus() == 1) {
            return putDebugErrorResult(gatewayInvokeContext, ErrorCode.INVALID_API);
        }
        gatewayInvokeContext.setMethodDefinition(serviceDefinition);
        return true;

    }

    @Override
    public Set<String> handleType() {
        return null;
    }

    @Override
    public Set<String> protocolType() {
        return null;
    }

    @Override
    public int getOrder() {
        return Integer.MAX_VALUE;
    }

}
