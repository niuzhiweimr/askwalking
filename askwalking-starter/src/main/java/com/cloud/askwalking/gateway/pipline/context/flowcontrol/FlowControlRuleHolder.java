package com.cloud.askwalking.gateway.pipline.context.flowcontrol;

import com.alibaba.csp.sentinel.slots.block.RuleConstant;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRule;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRuleManager;
import com.alibaba.csp.sentinel.slots.system.SystemRule;
import com.alibaba.csp.sentinel.slots.system.SystemRuleManager;
import com.cloud.askwalking.core.context.GatewayInvokeContext;
import com.cloud.askwalking.core.domain.FlowControlRule;
import com.cloud.askwalking.core.domain.GatewayMethodDefinition;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author niuzhiwei
 */
@Slf4j
public class FlowControlRuleHolder {

    private static boolean reload = false;

    private static Map<String, FlowControlRule> applicationRules = new ConcurrentHashMap<>();

    private static Map<String, FlowControlRule> interfaceRules = new ConcurrentHashMap<>();


    static String getFlowEntryKey(GatewayMethodDefinition methodDefinition) {
        String application = methodDefinition.getApplicationName();
        String mdi = methodDefinition.getMDIdentify();
        return interfaceRules.containsKey(mdi) ? mdi : (FlowControlRuleHolder.applicationRules.containsKey(application) ? application : "");
    }

    public static void addApplicationRule(GatewayMethodDefinition methodDefinition) {
        FlowControlRule rule = methodDefinition.getFlowControlRule();
        if (rule == null) {
            return;
        }
        String application = methodDefinition.getApplicationName();
        applicationRules.put(application, rule);
        reload = methodDefinition.getApiReload();
    }

    public static void addInterfaceRule(GatewayMethodDefinition methodDefinition) {
        FlowControlRule rule = methodDefinition.getFlowControlRule();
        if (rule == null) {
            return;
        }
        String mdi = methodDefinition.getMDIdentify();
        interfaceRules.put(mdi, rule);
        reload = methodDefinition.getApiReload();

    }

    public static void reloadFlowRules() {
        if (reload) {
            reload = false;
            List<FlowRule> flowRules = Lists.newArrayList();
            flowRules.addAll(fetchFlowRules(interfaceRules));
            flowRules.addAll(fetchFlowRules(applicationRules));
            FlowRuleManager.loadRules(flowRules);
            log.info("[FlowControlRuleHolder]reloadFlowRules success");
        }
    }

    /**
     * 网关系统限流  没有想好暂不处理
     *
     * @param gatewayInvokeContext
     */
    protected static void loadSystemGuardRules(GatewayInvokeContext gatewayInvokeContext) {
        SystemRule rule = new SystemRule();
        rule.setAvgRt(gatewayInvokeContext.getMethodDefinition().getFlowControlRule().getAvgRt());
        rule.setQps(gatewayInvokeContext.getMethodDefinition().getFlowControlRule().getQps());
        rule.setMaxThread(gatewayInvokeContext.getMethodDefinition().getFlowControlRule().getMaxThread());
        SystemRuleManager.loadRules(Collections.singletonList(rule));
    }

    private static List<FlowRule> fetchFlowRules(Map<String, FlowControlRule> flowControlRules) {
        List<FlowRule> flowRules = Lists.newArrayList();
        for (Map.Entry<String, FlowControlRule> entry : flowControlRules.entrySet()) {
            FlowControlRule flowControlRule = entry.getValue();
            if (flowControlRule == null) {
                continue;
            }
            if (flowControlRule.getQps() > 0) {
                FlowRule rule = new FlowRule();
                rule.setResource(entry.getKey());
                rule.setCount(flowControlRule.getQps());
                rule.setGrade(RuleConstant.FLOW_GRADE_QPS);
                rule.setLimitApp(RuleConstant.LIMIT_APP_DEFAULT);
                flowRules.add(rule);
            }
            if (flowControlRule.getMaxThread() > 0) {
                FlowRule rule = new FlowRule();
                rule.setResource(entry.getKey());
                rule.setCount(flowControlRule.getMaxThread());
                rule.setGrade(RuleConstant.FLOW_GRADE_THREAD);
                rule.setLimitApp(RuleConstant.LIMIT_APP_DEFAULT);
                flowRules.add(rule);
            }
        }
        return flowRules;
    }
}
