package com.cloud.askwalking.gateway.pipline.context.admin;

import com.alibaba.cloud.dubbo.service.DubboGenericServiceFactory;
import com.cloud.askwalking.common.constants.GatewayConstant;
import com.cloud.askwalking.common.exception.ErrorCode;
import com.cloud.askwalking.core.context.GatewayInvokeContext;
import com.cloud.askwalking.gateway.pipline.AbstractGatewayContextHandler;
import com.google.common.collect.Sets;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import java.util.Set;

/**
 * @author niuzhiwei
 */
@Slf4j
public class AdminRpcBeforeContextHandler extends AbstractGatewayContextHandler implements ApplicationContextAware {

    private final Set<String> handleTypes = Sets.newHashSet(GatewayConstant.ADMIN);

    private final Set<String> protocolTypes = Sets.newHashSet(GatewayConstant.RPC);

    private ApplicationContext applicationContext;

    @Override
    public boolean handleGatewayInvoke(GatewayInvokeContext gatewayInvokeContext) {

        try {
            gatewayInvokeContext.setServiceFactory(this.applicationContext.getBean(DubboGenericServiceFactory.class));
            gatewayInvokeContext.preBuildRpc();
        } catch (BeansException e) {
            log.error("[AdminBeforeContextHandler] Exception in building Dubbo metadata：", e);
            return putDebugErrorResult(gatewayInvokeContext, ErrorCode.SYSTEM_ERROR);
        }

        return true;
    }

    @Override
    public Set<String> handleType() {
        return this.handleTypes;
    }

    @Override
    public Set<String> protocolType() {
        return this.protocolTypes;
    }

    @Override
    public int getOrder() {
        return 50;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
