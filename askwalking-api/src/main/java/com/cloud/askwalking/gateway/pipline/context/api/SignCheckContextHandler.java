package com.cloud.askwalking.gateway.pipline.context.api;

import com.cloud.askwalking.core.constants.GatewayConstant;
import com.cloud.askwalking.core.context.GatewayInvokeContext;
import com.cloud.askwalking.gateway.pipline.context.common.AbstractSignCheckContextHandler;
import com.google.common.collect.Sets;

import java.util.Set;

/**
 * @author niuzhiwei
 */
public class SignCheckContextHandler extends AbstractSignCheckContextHandler {

    private final Set<String> handleTypes = Sets.newHashSet(GatewayConstant.API);

    @Override
    public int getOrder() {
        return 20;
    }

    @Override
    public Set<String> handleType() {
        return this.handleTypes;
    }

    @Override
    protected Boolean buildSignStr(GatewayInvokeContext context) {
        return true;
    }

    @Override
    protected Boolean checkSign(GatewayInvokeContext context) {
        return true;
    }
}
