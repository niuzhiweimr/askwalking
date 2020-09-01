package com.cloud.askwalking.gateway.pipline.context.common;

import com.alibaba.fastjson.JSONObject;
import com.cloud.askwalking.core.context.GatewayInvokeContext;
import com.cloud.askwalking.client.domain.R;
import com.cloud.askwalking.client.exception.ErrorCode;
import com.cloud.askwalking.gateway.pipline.AbstractGatewayContextHandler;
import com.cloud.askwalking.core.domain.ApiConfig;
import com.cloud.askwalking.core.domain.GatewayMethodDefinition;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.nio.charset.StandardCharsets;
import java.util.Set;
import java.util.concurrent.ExecutionException;

/**
 * @author niuzhiwei
 */
@Slf4j
public class TailContextHandler extends AbstractGatewayContextHandler {

    @Override
    public boolean handleGatewayInvoke(GatewayInvokeContext gatewayInvokeContext) {
        return false;
    }

    @Override
    public Set<String> handleType() {
        return null;
    }

    @Override
    public void fireGatewayInvoke(GatewayInvokeContext gatewayInvokeContext) {
        try {
            if (gatewayInvokeContext.isExecAsync()) {
                finishGatewayInvoke(gatewayInvokeContext);
            } else {
                handleGatewayResponse(gatewayInvokeContext);
            }
        } finally {
            try {
                recordAccessLog(gatewayInvokeContext);
            } catch (Throwable e) {
                log.error(e.getMessage(), e);
            }
        }
    }

    private static void recordAccessLog(GatewayInvokeContext context) {
        if (context.getMethodDefinition() == null) {
            return;
        }

        log.info("[TailContextHandler] gateway invoke log GatewayMethodDefinition:{} ,requestURI:{} ,serviceParam:{} ,R:{}",
                JSONObject.toJSONString(context.getMethodDefinition()), context.getRequestURI(),
                JSONObject.toJSONString(context.getServiceParam()), JSONObject.toJSONString(context.getBaseResponse()));
    }


    private static void handleGatewayResponse(GatewayInvokeContext context) {
        processResponseHeader(context);
        processOutput(context);
    }


    private static void processOutput(GatewayInvokeContext context) {

        ServletOutputStream servletOutputStream = null;
        try {
            HttpServletResponse response = context.getResponse();
            servletOutputStream = response.getOutputStream();
            response.setContentType("application/json; charset=UTF-8");
            servletOutputStream.write(JSONObject.toJSONString(context.getBaseResponse()).getBytes(StandardCharsets.UTF_8));
            servletOutputStream.flush();
        } catch (Exception e) {
            log.error("[TailContextHandler]Http Response Error", e);
        } finally {
            if (servletOutputStream != null) {
                try {
                    servletOutputStream.close();
                } catch (Exception ex) {
                    log.error("[TailContextHandler]Http Output Close Error.", ex);
                }
            }
        }
    }

    private static void processResponseHeader(GatewayInvokeContext context) {
        GatewayMethodDefinition gatewayMethodDefinition = context.getMethodDefinition();
        if (gatewayMethodDefinition != null) {
            ApiConfig apiConfig = gatewayMethodDefinition.getApiConfig();
            if (apiConfig != null) {
                String allowedOrigins = context.getRequestHeader("Origin");
                if (!StringUtils.isEmpty(allowedOrigins)) {
                    HttpServletResponse response = context.getResponse();
                    response.setHeader("Access-Control-Allow-Origin", allowedOrigins);
                    response.setHeader("Access-Control-Allow-Credentials", Boolean.TRUE.toString());
                }
            }
        }
    }

    private static void finishGatewayInvoke(GatewayInvokeContext context) {
        R r;
        try {
            Object result = context.getFuture().get();
            String resultJson = JSONObject.toJSONString(result);
            r = JSONObject.parseObject(resultJson, R.class);
            context.setBaseResponse(r);
            handleGatewayResponse(context);
        } catch (InterruptedException | ExecutionException e) {
            log.error("[TailContextHandler] Get asynchronous result, interface response exception：", e);
            r = R.fail(ErrorCode.SYSTEM_ERROR.getErrorCode(), ErrorCode.SYSTEM_ERROR.getErrorReason());
            context.setBaseResponse(r);
            handleGatewayResponse(context);
        }
    }

    @Override
    public int getOrder() {
        return Integer.MAX_VALUE;
    }
}
