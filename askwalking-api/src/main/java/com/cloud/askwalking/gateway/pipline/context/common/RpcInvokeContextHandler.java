package com.cloud.askwalking.gateway.pipline.context.common;

import com.alibaba.fastjson.JSONObject;
import com.cloud.askwalking.common.constants.GatewayConstant;
import com.cloud.askwalking.common.domain.R;
import com.cloud.askwalking.common.exception.ErrorCode;
import com.cloud.askwalking.core.context.GatewayInvokeContext;
import com.cloud.askwalking.gateway.pipline.AbstractGatewayContextHandler;
import com.google.common.collect.Sets;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.rpc.RpcException;
import org.apache.dubbo.rpc.service.GenericService;

import java.util.Set;
import java.util.concurrent.CompletableFuture;


/**
 * @author niuzhiwei
 */
@Slf4j
public class RpcInvokeContextHandler extends AbstractGatewayContextHandler {

    private final Set<String> handleTypes = Sets.newHashSet(GatewayConstant.API, GatewayConstant.ADMIN, GatewayConstant.SAAS);

    private final Set<String> protocolTypes = Sets.newHashSet(GatewayConstant.RPC);

    @Override
    public boolean handleGatewayInvoke(GatewayInvokeContext gatewayInvokeContext) {

        try {
            GenericService genericService = gatewayInvokeContext.getGenericService();

            //是否异步调用
            if (gatewayInvokeContext.getApiAsync()) {

                invokeAsync(gatewayInvokeContext, genericService);
            } else {

                invokeSync(gatewayInvokeContext, genericService);
            }

        } catch (RpcException e) {
            log.error("[RpcInvokeContextHandler] handler RpcException:", e);
            return putDebugErrorResult(gatewayInvokeContext, ErrorCode.MISSING_SERVER_PROVIDER);
        } catch (Exception e) {
            log.error("[RpcInvokeContextHandler] handler Exception:", e);
            return putDebugErrorResult(gatewayInvokeContext, ErrorCode.SYSTEM_ERROR);
        }

        return true;
    }

    /**
     * 同步
     *
     * @param gatewayInvokeContext
     * @param genericService
     */
    private void invokeSync(GatewayInvokeContext gatewayInvokeContext, GenericService genericService) {

        if (log.isDebugEnabled()) {
            log.debug("[RpcInvokeContextHandler] invoke sync");
        }

        Object result = genericService.$invoke(gatewayInvokeContext.getApiMethod(),
                new String[]{gatewayInvokeContext.getApiRequestClass()}, new Object[]{gatewayInvokeContext.getServiceParam()});
        String resultJson = JSONObject.toJSONString(result);
        R R = JSONObject.parseObject(resultJson, R.class);
        gatewayInvokeContext.setBaseResponse(R);
    }

    /**
     * 异步
     *
     * @param gatewayInvokeContext
     * @param genericService
     */
    private void invokeAsync(GatewayInvokeContext gatewayInvokeContext, GenericService genericService) {

        if (log.isDebugEnabled()) {
            log.debug("[RpcInvokeContextHandler] invoke async");
        }

        CompletableFuture<Object> future = genericService.$invokeAsync(gatewayInvokeContext.getApiMethod(),
                new String[]{gatewayInvokeContext.getApiRequestClass()}, new Object[]{gatewayInvokeContext.getServiceParam()});
        gatewayInvokeContext.setExecAsync(Boolean.TRUE);
        gatewayInvokeContext.setFuture(future);
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
        return 100;
    }

}
