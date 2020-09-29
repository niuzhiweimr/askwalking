package com.cloud.askwalking.bootstrap;

import com.cloud.askwalking.gateway.monitor.HttpAdminServer;
import com.cloud.askwalking.gateway.monitor.ProbeRequestHandler;
import com.cloud.askwalking.gateway.configuration.GatewayDispatcher;
import com.cloud.askwalking.gateway.configuration.GatewayServiceDiscovery;
import com.cloud.askwalking.gateway.pipline.GatewayInvokePipeline;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.HandlerMapping;
import org.springframework.web.servlet.handler.SimpleUrlHandlerMapping;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author niuzhiwei
 */
@Configuration
public class GatewayAutoConfiguration {

    @Bean(initMethod = "init")
    @ConditionalOnMissingBean
    public GatewayServiceDiscovery gatewayServiceDiscovery(@Value("${gateway.service.router.mode:local}") String serviceRouterMode) {
        return new GatewayServiceDiscovery(serviceRouterMode);
    }

    @Bean(initMethod = "init")
    @ConditionalOnMissingBean
    public GatewayInvokePipeline gatewayInvokePipeline(GatewayServiceDiscovery gatewayServiceDiscovery) {
        return new GatewayInvokePipeline(gatewayServiceDiscovery);
    }

    @Bean
    @ConditionalOnMissingBean
    public GatewayDispatcher gatewayDispatcher(@Value("${server.servlet.context-path:''}") String servletContextPath, GatewayInvokePipeline pipeline) {
        return new GatewayDispatcher(servletContextPath, pipeline);
    }

    @Bean
    public ProbeRequestHandler probeRequestHandler() {
        return new ProbeRequestHandler();
    }

    @Bean
    public HttpAdminServer httpAdminServer(@Value("${http.admin.server.port:8011}") Integer port, ProbeRequestHandler probeRequestHandler) throws Exception {
        HttpAdminServer httpAdminServer = new HttpAdminServer();
        httpAdminServer.setProbeRequestHandler(probeRequestHandler);
        httpAdminServer.setPort(port);
        httpAdminServer.start();
        return httpAdminServer;
    }

    @Bean
    public HandlerMapping gatewayHandlerMapping(GatewayServiceDiscovery discovery, GatewayDispatcher dispatcher) {
        SimpleUrlHandlerMapping urlHandlerMapping = new SimpleUrlHandlerMapping();
        List<String> uriMapping = discovery.getUriMappings();
        Map<String, Object> handlerMap = new HashMap<>(uriMapping.size());
        uriMapping.forEach(uri -> handlerMap.put(uri, dispatcher));
        urlHandlerMapping.setUrlMap(handlerMap);
        urlHandlerMapping.setOrder(-1);
        return urlHandlerMapping;
    }
}
