package com.cloud.askwalking.gateway.pipline.context.common;

import com.cloud.askwalking.common.constants.GatewayConstant;
import com.cloud.askwalking.common.domain.R;
import com.cloud.askwalking.common.exception.ErrorCode;
import com.cloud.askwalking.common.tool.JSONTool;
import com.cloud.askwalking.core.context.GatewayInvokeContext;
import com.cloud.askwalking.gateway.pipline.AbstractGatewayContextHandler;
import com.google.common.collect.Sets;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.Set;

/**
 * @author niuzhiwei
 */
@Slf4j
public class MockInvokeContextHandler extends AbstractGatewayContextHandler {

    private final Set<String> handleTypes = Sets.newHashSet(GatewayConstant.API
            , GatewayConstant.ADMIN, GatewayConstant.SAAS);

    private final Set<String> protocolTypes = Sets.newHashSet(GatewayConstant.RPC, GatewayConstant.FEIGN);

    @Override
    public boolean handleGatewayInvoke(GatewayInvokeContext gatewayInvokeContext) {

        try {
            if (gatewayInvokeContext.getMock()) {
                Map map = JSONTool.toObject(gatewayInvokeContext.getMockResponse(), Map.class);
                R<Object> r = R.success(map);
                gatewayInvokeContext.setBaseResponse(r);
                return false;
            }
        } catch (Exception e) {
            log.error("[MockInvokeContextHandler] handler Exception:", e);
            return putDebugErrorResult(gatewayInvokeContext, ErrorCode.JSON_SERIALIZE_FAIL);
        }

        return true;
    }

    @Override
    public Set<String> handleType() {
        return this.handleTypes;
    }

    @Override
    public Set<String> protocolType() {
        return this.protocolTypes;
    }

    @Override
    public int getOrder() {
        return 0;
    }
}
