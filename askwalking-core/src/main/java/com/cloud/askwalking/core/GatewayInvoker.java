package com.cloud.askwalking.core;

import com.cloud.askwalking.core.context.GatewayInvokeContext;

/**
 * @author niuzhiwei
 */
public interface GatewayInvoker {

    /**
     * 开始处理网关调用
     *
     * @param paramGatewayInvokeContext
     */
    void fireGatewayInvoke(GatewayInvokeContext paramGatewayInvokeContext);
}
