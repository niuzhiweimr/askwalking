package com.cloud.askwalking.gateway.pipline.context.api;

import com.cloud.askwalking.common.constants.GatewayConstant;
import com.cloud.askwalking.core.context.GatewayInvokeContext;
import com.cloud.askwalking.gateway.pipline.context.common.AbstractConfigLoadContextHandler;
import com.google.common.collect.Sets;

import java.util.Set;

/**
 * @author niuzhiwei
 */
public class ConfigLoadContextHandler extends AbstractConfigLoadContextHandler {

    private final Set<String> handleTypes = Sets.newHashSet(GatewayConstant.API);

    private final Set<String> protocolTypes = Sets.newHashSet(GatewayConstant.RPC, GatewayConstant.FEIGN);

    @Override
    public Set<String> handleType() {
        return this.handleTypes;
    }

    @Override
    public Set<String> protocolType() {
        return this.protocolTypes;
    }

    @Override
    protected Boolean getConfig(GatewayInvokeContext context) {

        return true;
    }
}
