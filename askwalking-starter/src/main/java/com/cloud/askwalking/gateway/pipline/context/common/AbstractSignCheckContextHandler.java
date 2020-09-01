package com.cloud.askwalking.gateway.pipline.context.common;

import com.cloud.askwalking.core.context.GatewayInvokeContext;
import com.cloud.askwalking.gateway.pipline.AbstractGatewayContextHandler;
import lombok.extern.slf4j.Slf4j;

/**
 * @author niuzhiwei
 */
@Slf4j
public abstract class AbstractSignCheckContextHandler extends AbstractGatewayContextHandler {

    @Override
    public boolean handleGatewayInvoke(GatewayInvokeContext context) {
        return buildSignStr(context) && checkSign(context);
    }

    /**
     * 构建待签名字符串
     *
     * @param context
     * @return
     */
    protected abstract Boolean buildSignStr(GatewayInvokeContext context);

    /**
     * 检查签名
     *
     * @param context
     * @return
     */
    protected abstract Boolean checkSign(GatewayInvokeContext context);

}
