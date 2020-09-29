package com.cloud.askwalking.gateway.pipline.context.admin;

import com.cloud.askwalking.common.constants.GatewayConstant;
import com.cloud.askwalking.gateway.pipline.CommonParamParse;
import com.cloud.askwalking.core.context.GatewayInvokeContext;
import com.cloud.askwalking.gateway.pipline.AbstractGatewayContextHandler;
import com.google.common.collect.Sets;
import com.cloud.askwalking.common.exception.ErrorCode;
import lombok.extern.slf4j.Slf4j;

import java.util.Set;

/**
 * 不做复杂处理直接转换为json
 *
 * @author niuzhiwei
 */
@Slf4j
public class AdminParamParseContextHandler extends AbstractGatewayContextHandler {

    private final Set<String> handleTypes = Sets.newHashSet(GatewayConstant.ADMIN);

    @Override
    public boolean handleGatewayInvoke(GatewayInvokeContext gatewayInvokeContext) {
        boolean result;
        try {
            result = CommonParamParse.handleParamParse(gatewayInvokeContext);
        } catch (Exception e) {
            return putDebugErrorResult(gatewayInvokeContext, ErrorCode.PARAM_ERROR);
        }
        return result;
    }

    @Override
    public Set<String> handleType() {
        return this.handleTypes;
    }

    @Override
    public int getOrder() {
        return 10;
    }
}
