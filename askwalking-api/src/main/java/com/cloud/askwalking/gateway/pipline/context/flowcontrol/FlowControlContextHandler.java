package com.cloud.askwalking.gateway.pipline.context.flowcontrol;

import com.alibaba.csp.sentinel.Entry;
import com.alibaba.csp.sentinel.SphU;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.alibaba.csp.sentinel.slots.block.flow.FlowException;
import com.alibaba.csp.sentinel.slots.system.SystemBlockException;
import com.cloud.askwalking.common.constants.GatewayConstant;
import com.cloud.askwalking.common.exception.ErrorCode;
import com.cloud.askwalking.core.AbstractGatewayServiceDiscovery;
import com.cloud.askwalking.core.context.GatewayInvokeContext;
import com.cloud.askwalking.core.domain.GatewayMethodDefinition;
import com.cloud.askwalking.gateway.pipline.AbstractGatewayContextHandler;
import com.google.common.collect.Sets;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.util.Set;

/**
 * 暂不处理限流功能
 *
 * @author niuzhiwei
 */
@Slf4j
public class FlowControlContextHandler extends AbstractGatewayContextHandler {

    private final Set<String> handleTypes = Sets.newHashSet(GatewayConstant.API, GatewayConstant.ADMIN, GatewayConstant.SAAS);

    @Override
    public void initContextHandler(AbstractGatewayServiceDiscovery gatewayServiceDiscovery) {

        gatewayServiceDiscovery.getUriMappings().forEach(uri -> {
            GatewayMethodDefinition methodDefinition = gatewayServiceDiscovery.getMethodDefinition(uri);
            //添加api限流 目前不做系统限流
            FlowControlRuleHolder.addInterfaceRule(methodDefinition);
        });

        FlowControlRuleHolder.reloadFlowRules();
    }

    @Override
    public void fireGatewayInvoke(GatewayInvokeContext context) {
        String entryKey = FlowControlRuleHolder.getFlowEntryKey(context.getMethodDefinition());
        if (!context.getMethodDefinition().getSystemGuardIsEnable()) {
            handleGatewayInvoke(context);
            return;
        }
        if (StringUtils.isEmpty(entryKey)) {
            entryKey = context.getMethodDefinition().getMDIdentify();
        }
        Entry entry = null;
        try {
            entry = SphU.entry(entryKey);
            handleGatewayInvoke(context);
        } catch (FlowException flowEx) {
            log.error("[SentinelEntryContextHandler]Gateway Request Blocked, Cause FlowException: {}", context.getMethodDefinition().getMDIdentify());
            buildFlowControlRuleResponse(context);
            this.tail.fireGatewayInvoke(context);
        } catch (SystemBlockException systemEx) {
            log.error("[SentinelEntryContextHandler]Gateway Request Blocked, Cause SystemBlockException: {}", context.getMethodDefinition().getMDIdentify());
            buildFlowControlRuleResponse(context);
            this.tail.fireGatewayInvoke(context);
        } catch (BlockException blockEx) {
            log.error("[SentinelEntryContextHandler]Gateway Request Blocked, Cause BlockException: {}", context.getMethodDefinition().getMDIdentify());
            buildFlowControlRuleResponse(context);
            this.tail.fireGatewayInvoke(context);
        } catch (Throwable t) {
            log.error("[SentinelEntryContextHandler]Gateway Request Unknown Error: {}", t.getMessage());
        } finally {
            if (entry != null) {
                entry.exit();
            }
        }
    }

    /**
     * 构建限流响应
     *
     * @param context
     */
    private void buildFlowControlRuleResponse(GatewayInvokeContext context) {
        /**
         * 限流类型：1：系统限流目前不做 2：api限流
         */
        Integer apiFlowControl = 1;
        if (context.getFlowControlRuleType().equals(apiFlowControl)) {
            putDebugErrorResult(context, ErrorCode.SYSTEM_LIMITER);
        } else {
            putDebugErrorResult(context, ErrorCode.API_LIMITER);
        }
    }


    @Override
    public boolean handleGatewayInvoke(GatewayInvokeContext gatewayInvokeContext) {
        this.next.fireGatewayInvoke(gatewayInvokeContext);
        return true;
    }

    @Override
    public Set<String> handleType() {
        return this.handleTypes;
    }

    @Override
    public int getOrder() {
        return Integer.MIN_VALUE;
    }
}
