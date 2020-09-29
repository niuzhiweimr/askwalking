package com.cloud.askwalking.core;

import com.cloud.askwalking.core.context.GatewayInvokeContext;
import org.springframework.core.Ordered;

import java.util.Set;

/**
 * @author niuzhiwei
 */
public interface GatewayContextHandler extends Ordered {

    /**
     * 处理网关调用
     *
     * @param gatewayInvokeContext
     * @return
     */
    boolean handleGatewayInvoke(GatewayInvokeContext gatewayInvokeContext);

    /**
     * 初始化上下文
     *
     * @param gatewayServiceDiscovery
     */
    void initContextHandler(AbstractGatewayServiceDiscovery gatewayServiceDiscovery);

    /**
     * 处理类型
     *
     * @return
     */
    Set<String> handleType();

    /**
     * 协议类型
     *
     * @return
     */
    Set<String> protocolType();

}
