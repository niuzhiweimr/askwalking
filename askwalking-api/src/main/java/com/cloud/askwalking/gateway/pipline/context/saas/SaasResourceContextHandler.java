package com.cloud.askwalking.gateway.pipline.context.saas;

import com.cloud.askwalking.common.enums.GatewayErrorCode;
import com.cloud.askwalking.core.constants.GatewayConstant;
import com.cloud.askwalking.core.context.GatewayInvokeContext;
import com.cloud.askwalking.gateway.pipline.AbstractGatewayContextHandler;
import com.google.common.collect.Sets;
import com.cloud.askwalking.repository.model.SaasResourceDO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import java.util.Objects;
import java.util.Set;

/**
 * @author niuzhiwei
 */
@Slf4j
public class SaasResourceContextHandler extends AbstractGatewayContextHandler implements ApplicationContextAware {

    private final Set<String> handleTypes = Sets.newHashSet(GatewayConstant.SAAS);

    private ApplicationContext applicationContext;

    @Override
    public boolean handleGatewayInvoke(GatewayInvokeContext gatewayInvokeContext) {

        try {
            //SaasConfigService saasConfigService = applicationContext.getBean(SaasConfigService.class);
            SaasResourceDO saasResource = null; //saasConfigService.getSaasResource(gatewayInvokeContext.getOpenId()
            //, gatewayInvokeContext.getRequestURI());
            if (Objects.isNull(saasResource)) {
                return putDebugErrorResult(gatewayInvokeContext, GatewayErrorCode.SAAS_RESOURCE_IS_NULL);
            }
        } catch (Exception e) {
            log.error("[SaasResourceContextHandler] SaaS platform Abnormal merchant resource authentication：", e);
            return putDebugErrorResult(gatewayInvokeContext, GatewayErrorCode.SAAS_RESOURCE_ERROR);
        }
        return true;
    }

    @Override
    public Set<String> handleType() {
        return this.handleTypes;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    @Override
    public int getOrder() {
        return 10;
    }
}
