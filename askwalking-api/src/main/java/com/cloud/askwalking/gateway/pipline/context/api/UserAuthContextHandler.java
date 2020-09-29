package com.cloud.askwalking.gateway.pipline.context.api;

import com.cloud.askwalking.common.constants.GatewayConstant;
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
public class UserAuthContextHandler extends AbstractGatewayContextHandler implements ApplicationContextAware {

    private final Set<String> handleTypes = Sets.newHashSet(GatewayConstant.API);

    private ApplicationContext applicationContext;

    /**
     * jwt鉴权需要调用服务(配置文件jwt.properties中加载需要调用的认证服务),当请求header里的token为login、logout时不再做认证处理，直接通过
     */
    private final String[] ignoreHeaders = {"_login", "_logout"};

    @Override
    public int getOrder() {
        return 30;
    }

    @Override
    public boolean handleGatewayInvoke(GatewayInvokeContext gatewayInvokeContext) {

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
}
