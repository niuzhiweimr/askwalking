package com.cloud.askwalking.gateway.pipline;

import com.cloud.askwalking.gateway.pipline.context.common.HeadContextHandler;
import com.cloud.askwalking.gateway.pipline.context.common.TailContextHandler;
import com.cloud.askwalking.core.GatewayInvoker;
import com.cloud.askwalking.core.GatewayContextHandler;
import com.cloud.askwalking.core.context.GatewayInvokeContext;
import com.cloud.askwalking.gateway.configuration.GatewayServiceDiscovery;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.OrderComparator;

import java.util.List;
import java.util.ServiceLoader;
import java.util.stream.Collectors;

/**
 * @author niuzhiwei
 */
@Slf4j
public class GatewayInvokePipeline implements GatewayInvoker, ApplicationContextAware {

    private static final OrderComparator INSTANCE = new OrderComparator();

    private AbstractGatewayContextHandler head;

    private AbstractGatewayContextHandler tail;

    private GatewayServiceDiscovery discovery;

    private ApplicationContext context;

    public GatewayInvokePipeline(GatewayServiceDiscovery gatewayServiceDiscovery) {
        this.discovery = gatewayServiceDiscovery;
        initBaseContextHandler();
    }

    public void init() {
        initSpiContextHandler();
    }

    /**
     * java spi
     */
    private void initSpiContextHandler() {
        ServiceLoader<GatewayContextHandler> loader = ServiceLoader.load(GatewayContextHandler.class);
        List<GatewayContextHandler> handlers = Lists.newArrayList(loader).stream()
                .filter(h -> this.discovery.matchHandleType(h.handleType())).collect(Collectors.toList());
        handlers.stream().filter(h -> h instanceof ApplicationContextAware)
                .forEach(h -> ((ApplicationContextAware) h).setApplicationContext(this.context));
        handlers.forEach(h -> h.initContextHandler(this.discovery));
        handlers.sort(INSTANCE);
        addLast(handlers);
    }

    private void initBaseContextHandler() {
        this.tail = new TailContextHandler();
        this.head = new HeadContextHandler(this.discovery);
        this.head.next = this.tail;
        this.head.tail = this.tail;
        this.tail.prev = this.head;
    }

    /**
     * 处理当前对象上下文关系
     *
     * @param newCtx
     */
    private void addLast(AbstractGatewayContextHandler newCtx) {
        AbstractGatewayContextHandler prev = this.tail.prev;
        newCtx.prev = prev;
        newCtx.next = this.tail;
        newCtx.tail = this.tail;
        prev.next = newCtx;
        this.tail.prev = newCtx;
    }

    private void addLast(List<GatewayContextHandler> newCtxList) {
        for (GatewayContextHandler newCtx : newCtxList) {
            AbstractGatewayContextHandler handler = (AbstractGatewayContextHandler) newCtx;
            addLast(handler);
            if (log.isDebugEnabled()) {
                log.debug("[GatewayInvokePipeline]Add GatewayContextHandler: {},next: {},tail: {},prev: {},prev.next: {}"
                        , handler.getClass().getName(), handler.next.getClass().getName(), handler.tail.getClass().getName()
                        , handler.prev.getClass().getName(), handler.prev.next.getClass().getName());
            }
        }
    }

    @Override
    public void fireGatewayInvoke(GatewayInvokeContext gatewayInvokeContext) {
        this.head.fireGatewayInvoke(gatewayInvokeContext);
    }


    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.context = applicationContext;
    }
}
