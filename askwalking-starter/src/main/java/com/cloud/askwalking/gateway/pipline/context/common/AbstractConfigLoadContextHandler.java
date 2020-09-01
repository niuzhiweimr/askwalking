package com.cloud.askwalking.gateway.pipline.context.common;

import com.cloud.askwalking.core.context.GatewayInvokeContext;
import com.cloud.askwalking.gateway.pipline.AbstractGatewayContextHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

/**
 * 特殊参数下发抽象
 *
 * @author niuzhiwei
 */
@Slf4j
public abstract class AbstractConfigLoadContextHandler extends AbstractGatewayContextHandler implements ApplicationContextAware {

    protected volatile ApplicationContext applicationContext;

    @Override
    public boolean handleGatewayInvoke(GatewayInvokeContext context) {
        return getConfig(context);
    }

    /**
     * 获取配置
     *
     * @param context
     * @return Boolean
     */
    protected abstract Boolean getConfig(GatewayInvokeContext context);

    @Override
    public int getOrder() {
        return 1;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

}
