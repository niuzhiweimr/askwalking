package com.cloud.askwalking.gateway.pipline;

import com.cloud.askwalking.client.domain.R;
import com.cloud.askwalking.client.exception.StatusDefinition;
import com.cloud.askwalking.core.AbstractGatewayServiceDiscovery;
import com.cloud.askwalking.core.GatewayContextHandler;
import com.cloud.askwalking.core.GatewayInvoker;
import com.cloud.askwalking.core.context.GatewayInvokeContext;
import lombok.extern.slf4j.Slf4j;

/**
 * @author niuzhiwei
 */
@Slf4j
public abstract class AbstractGatewayContextHandler implements GatewayContextHandler, GatewayInvoker {

    protected volatile AbstractGatewayContextHandler next;

    protected volatile AbstractGatewayContextHandler prev;

    protected volatile AbstractGatewayContextHandler tail;

    @Override
    public void fireGatewayInvoke(GatewayInvokeContext context) {
        boolean canNext = true;
        if (canHandle(context)) {
            long start = System.currentTimeMillis();
            canNext = handleGatewayInvoke(context);
            if (log.isDebugEnabled()) {
                log.debug("[AbstractGatewayContextHandler]{}: Consume Time[{} ms]", this, start - context.getInvokeStartTime());
            }
        }
        if (canNext) {
            this.next.fireGatewayInvoke(context);
        } else {
            this.tail.fireGatewayInvoke(context);
        }
    }


    @Override
    public void initContextHandler(AbstractGatewayServiceDiscovery gatewayServiceDiscovery) {
    }

    private boolean canHandle(GatewayInvokeContext context) {
        return handleType().contains(context.getApiType());
    }

    protected boolean putDebugErrorResult(GatewayInvokeContext gatewayInvokeContext, StatusDefinition statusDefinition) {
        gatewayInvokeContext.setBaseResponse(R.fail(statusDefinition));
        return false;
    }


}
