package com.cloud.askwalking.gateway.configuration;

import com.cloud.askwalking.core.context.GatewayInvokeContext;
import com.cloud.askwalking.gateway.pipline.GatewayInvokePipeline;
import org.springframework.web.HttpRequestHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author niuzhiwei
 */
public class GatewayDispatcher implements HttpRequestHandler {

    private GatewayInvokePipeline gatewayInvokePipeline;

    private String servletContextPath;

    public GatewayDispatcher(String servletContextPath, GatewayInvokePipeline gatewayInvokePipeline) {
        this.servletContextPath = servletContextPath;
        this.gatewayInvokePipeline = gatewayInvokePipeline;
    }

    @Override
    public void handleRequest(HttpServletRequest request, HttpServletResponse response) {
        this.gatewayInvokePipeline.fireGatewayInvoke(new GatewayInvokeContext(this.servletContextPath, request, response));
    }
}
