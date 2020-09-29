package com.cloud.askwalking.gateway.pipline.context.common;

import com.cloud.askwalking.common.constants.GatewayConstant;
import com.cloud.askwalking.common.domain.R;
import com.cloud.askwalking.common.exception.ErrorCode;
import com.cloud.askwalking.common.utils.InputStreamUtil;
import com.cloud.askwalking.core.context.GatewayInvokeContext;
import com.cloud.askwalking.gateway.pipline.AbstractGatewayContextHandler;
import com.google.common.collect.Sets;
import com.google.gson.Gson;
import feign.Request;
import feign.Response;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.openfeign.ribbon.LoadBalancerFeignClient;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author niuzhiwei
 */
@Slf4j
public class FeignInvokeContextHandler extends AbstractGatewayContextHandler {

    private final Set<String> handleTypes = Sets.newHashSet(GatewayConstant.API, GatewayConstant.ADMIN, GatewayConstant.SAAS);

    private final Set<String> protocolTypes = Sets.newHashSet(GatewayConstant.FEIGN);

    @Override
    public boolean handleGatewayInvoke(GatewayInvokeContext gatewayInvokeContext) {

        try {
            LoadBalancerFeignClient feignClient = gatewayInvokeContext.getLoadBalancerFeignClient();

            //是否异步调用
            if (gatewayInvokeContext.getApiAsync()) {

                invokeAsync(gatewayInvokeContext, feignClient);
            } else {

                invokeSync(gatewayInvokeContext, feignClient);
            }

        } catch (IOException e) {
            log.error("[FeignInvokeContextHandler] handler FeignException:", e);
            return putDebugErrorResult(gatewayInvokeContext, ErrorCode.MISSING_SERVER_PROVIDER);
        } catch (Exception e) {
            log.error("[FeignInvokeContextHandler] handler Exception:", e);
            return putDebugErrorResult(gatewayInvokeContext, ErrorCode.SYSTEM_ERROR);
        }

        return true;
    }

    /**
     * 同步
     *
     * @param gatewayInvokeContext
     * @param feignClient
     */
    private void invokeSync(GatewayInvokeContext gatewayInvokeContext, LoadBalancerFeignClient feignClient) throws IOException {

        if (log.isDebugEnabled()) {
            log.debug("[FeignInvokeContextHandler] invoke sync");
        }

        Request request = buildFeignRequest(gatewayInvokeContext);
        Response execute = feignClient.execute(request, gatewayInvokeContext.getFeignRequestOptions());

        byte[] bytes = InputStreamUtil.readInputStream(execute.body().asInputStream());
        R r = new Gson().fromJson(new String(bytes), R.class);
        gatewayInvokeContext.setBaseResponse(r);
    }

    /**
     * 构建feign接口请求
     *
     * @param gatewayInvokeContext
     * @return
     */
    private Request buildFeignRequest(GatewayInvokeContext gatewayInvokeContext) {

        byte[] body = new Gson().toJson(gatewayInvokeContext.getServiceParam()).getBytes();

        switch (gatewayInvokeContext.getRequestMethod()) {
            case GatewayConstant.HTTP_METHOD_GET:
                return Request.create(Request.HttpMethod.GET, gatewayInvokeContext.getFeignUrl()
                        , gatewayInvokeContext.getFeignHeader(), body, Charset.defaultCharset());
            case GatewayConstant.HTTP_METHOD_POST:
                return Request.create(Request.HttpMethod.POST, gatewayInvokeContext.getFeignUrl()
                        , gatewayInvokeContext.getFeignHeader(), body, Charset.defaultCharset());
            default:
                throw ErrorCode.NOT_SUPPORT_METHOD.throwError();
        }
    }

    /**
     * 异步
     *
     * @param gatewayInvokeContext
     * @param feignClient
     */
    private void invokeAsync(GatewayInvokeContext gatewayInvokeContext, LoadBalancerFeignClient feignClient) throws IOException {

        if (log.isDebugEnabled()) {
            log.debug("[FeignInvokeContextHandler] invoke async");
        }

        Request request = buildFeignRequest(gatewayInvokeContext);
        AtomicInteger exceptionNum = new AtomicInteger(0);
        CompletableFuture<Object> future = CompletableFuture.supplyAsync(() ->
        {
            try {
                return feignClient.execute(request, gatewayInvokeContext.getFeignRequestOptions());
            } catch (IOException e) {
                log.error("[FeignInvokeContextHandler] invoke async feign Exception:", e);
                exceptionNum.incrementAndGet();
            }
            return null;
        });
        if (exceptionNum.get() > 0) {
            throw new IOException("Feign 接口调用异常");
        }
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
