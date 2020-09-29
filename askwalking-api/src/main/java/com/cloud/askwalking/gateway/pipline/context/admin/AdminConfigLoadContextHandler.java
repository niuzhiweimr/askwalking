package com.cloud.askwalking.gateway.pipline.context.admin;

import com.cloud.askwalking.common.constants.GatewayConstant;
import com.cloud.askwalking.core.context.GatewayInvokeContext;
import com.cloud.askwalking.gateway.pipline.context.common.AbstractConfigLoadContextHandler;
import com.google.common.collect.Sets;

import java.util.Set;

/**
 * 一些特殊参数的转换
 *
 * @author niuzhiwei
 */
public class AdminConfigLoadContextHandler extends AbstractConfigLoadContextHandler {

    private final Set<String> handleTypes = Sets.newHashSet(GatewayConstant.ADMIN);

    @Override
    public Set<String> handleType() {
        return this.handleTypes;
    }

    @Override
    protected Boolean getConfig(GatewayInvokeContext context) {

        return true;
    }
}
