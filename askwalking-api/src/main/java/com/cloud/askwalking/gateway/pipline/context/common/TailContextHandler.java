package com.cloud.askwalking.gateway.pipline.context.common;

import com.cloud.askwalking.common.domain.R;
import com.cloud.askwalking.common.exception.ErrorCode;
import com.cloud.askwalking.common.tool.InputStreamTool;
import com.cloud.askwalking.common.tool.JSONTool;
import com.cloud.askwalking.core.context.GatewayInvokeContext;
import com.cloud.askwalking.core.domain.ApiConfig;
import com.cloud.askwalking.core.domain.GatewayMethodDefinition;
import com.cloud.askwalking.gateway.pipline.AbstractGatewayContextHandler;
import feign.Response;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
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
                JSONTool.toJson(context.getMethodDefinition()), context.getRequestURI(),
                JSONTool.toJson(context.getServiceParam()), JSONTool.toJson(context.getBaseResponse()));
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
            servletOutputStream.write(JSONTool.toJson(context.getBaseResponse()).getBytes(StandardCharsets.UTF_8));
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
            if (result instanceof Response) {
                Response response = (Response) result;
                InputStream inputStream = response.body().asInputStream();
                byte[] bytes = InputStreamTool.readInputStream(inputStream);
                r = JSONTool.toObject(new String(bytes), R.class);
            } else {
                String resultJson = JSONTool.toJson(result);
                r = JSONTool.toObject(resultJson, R.class);
            }

            context.setBaseResponse(r);
            handleGatewayResponse(context);
        } catch (InterruptedException | ExecutionException | IOException e) {
            log.error("[TailContextHandler] Get asynchronous result, interface response exception：", e);
            r = R.fail(ErrorCode.SYSTEM_ERROR.getErrorCode(), ErrorCode.SYSTEM_ERROR.getErrorReason());
            context.setBaseResponse(r);
            handleGatewayResponse(context);
        }
    }

    @Override
    public Set<String> handleType() {
        return null;
    }

    @Override
    public Set<String> protocolType() {
        return null;
    }

    @Override
    public int getOrder() {
        return Integer.MAX_VALUE;
    }
}
