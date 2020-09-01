package com.cloud.askwalking.gateway.configuration;

import com.cloud.askwalking.core.AbstractGatewayServiceDiscovery;
import com.cloud.askwalking.core.domain.ApiConfig;
import com.cloud.askwalking.core.domain.FlowControlRule;
import com.cloud.askwalking.core.domain.GatewayMethodDefinition;
import com.cloud.askwalking.gateway.manager.model.ConfigureApiDO;
import com.cloud.askwalking.gateway.manager.service.GatewayApiService;
import com.cloud.askwalking.gateway.pipline.GatewayInvokePipeline;
import com.cloud.askwalking.gateway.pipline.context.flowcontrol.FlowControlRuleHolder;
import com.google.common.collect.Sets;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.util.CollectionUtils;
import org.springframework.web.servlet.handler.SimpleUrlHandlerMapping;

import java.util.*;

/**
 * @author niuzhiwei
 */
@Slf4j
@SuppressWarnings("ALL")
public class GatewayServiceDiscovery extends AbstractGatewayServiceDiscovery implements ApplicationContextAware {

    private final Set<String> GATEWAY_HANDLE_TYPE = new HashSet<>();

    private AbstractGatewayServiceRouter serviceRouter;

    private ApplicationContext applicationContext;

    public GatewayServiceDiscovery(String serviceRouterMode) {
        this.serviceRouter = GatewayServiceRouterFactory.getServiceRouter(serviceRouterMode);
    }

    @Override
    public GatewayMethodDefinition getMethodDefinition(String uri) {
        return this.serviceRouter.getMethodDefinition(uri);
    }

    @Override
    public List<String> getUriMappings() {
        return new ArrayList<>(this.serviceRouter.getRequestUris());
    }

    public void init() {
        initGatewayServices();
    }

    private void putMethodDefinition(GatewayMethodDefinition definition) {
        this.serviceRouter.putMethodDefinition(definition);
    }

    private void delMethodDefinition(String uri) {
        this.serviceRouter.delMethodDefinition(uri);
    }

    private void initGatewayServices() {
        processGatewayServices();
    }

    private void processGatewayServices() {

        try {
            GatewayApiService gatewayApiService = this.applicationContext.getBean(GatewayApiService.class);

            List<ConfigureApiDO> configureApiDOList = gatewayApiService.getAll();
            if (CollectionUtils.isEmpty(configureApiDOList)) {
                log.error("[GatewayServiceDiscovery]Gateway api Config Not Found");
            }

            configureApiDOList.forEach(this::saveGatewayMethodDefinition);

            buildHandleType(configureApiDOList);

        } catch (BeansException e) {
            log.error("[GatewayServiceDiscovery]processGatewayServices Error", e);
        }
    }

    /**
     * 保存方法配置
     *
     * @param configureApi
     */
    private void saveGatewayMethodDefinition(ConfigureApiDO configureApi) {

        GatewayMethodDefinition definition = new GatewayMethodDefinition();

        buildMethodDefinition(configureApi, definition);

        putMethodDefinition(definition);
    }

    /**
     * 构建handleMapping
     *
     * @param configureApi
     */
    private void buildHandleMapping(ConfigureApiDO configureApi) {

        SimpleUrlHandlerMapping urlHandlerMapping = (SimpleUrlHandlerMapping) applicationContext.getBean("gatewayHandlerMapping");
        Map<String, Object> urlMap = (Map<String, Object>) urlHandlerMapping.getUrlMap();
        boolean containsKey = urlMap.containsKey(configureApi.getRequestUri());
        if (!containsKey) {
            GatewayDispatcher gatewayDispatcher = applicationContext.getBean(GatewayDispatcher.class);
            urlMap.put(configureApi.getRequestUri(), gatewayDispatcher);
            urlHandlerMapping.initApplicationContext();
        }
    }

    /**
     * 构建方法定义
     *
     * @param configureApi
     * @param definition
     */
    private void buildMethodDefinition(ConfigureApiDO configureApi, GatewayMethodDefinition definition) {

        //api接口调用标识
        ApiConfig apiConfig = new ApiConfig();
        apiConfig.setApiInterface(configureApi.getApiInterface());
        apiConfig.setApiMethod(configureApi.getApiMethod());
        apiConfig.setApiRequestClass(configureApi.getApiRequestClass());
        apiConfig.setApiVersion(configureApi.getApiVersion());
        apiConfig.setApiGroup(configureApi.getApiGroup());
        apiConfig.setMockResponse(configureApi.getMockResponse());

        //限流相关
        FlowControlRule flowControlRule = new FlowControlRule();
        flowControlRule.setMaxThread(configureApi.getMaxThread());
        flowControlRule.setQps(configureApi.getQps());
        flowControlRule.setAvgRt(configureApi.getAvgRt());
        apiConfig.setFlowControlRule(flowControlRule);

        //api接口标识
        definition.setFlowControlRuleType(configureApi.getFlowControlRuleType());
        definition.setApiReload(configureApi.getApiReload());
        definition.setSystemGuard(configureApi.getSystemGuard());
        definition.setApiAsync(configureApi.getApiAsync());
        definition.setApplicationName(configureApi.getApplicationName());
        definition.setStatus(configureApi.getStatus());
        definition.setRequestUri(configureApi.getRequestUri());
        definition.setRequestMethod(configureApi.getRequestMethod());
        definition.setApiType(configureApi.getApiType());
        definition.setApiConfig(apiConfig);
        definition.setSignCheck(configureApi.getSignCheck());
    }

    /**
     * 修改元数据方法定义钩子
     * TODO:当前方法处理维度太大后续细化，1.1版本优化时做
     */
    public void updateMetadataMethodDefinitionHook() {

        try {

            GatewayApiService gatewayApiService = this.applicationContext.getBean(GatewayApiService.class);

            List<ConfigureApiDO> configureApiDOList = gatewayApiService.getAll();
            if (CollectionUtils.isEmpty(configureApiDOList)) {
                log.warn("[GatewayServiceDiscovery]offlineMethodDefinitionHook Gateway api Config Not Found");
                return;
            }

            configureApiDOList.forEach(this::saveGatewayMethodDefinition);

        } catch (Exception e) {
            log.error("[GatewayServiceDiscovery]offlineMethodDefinitionHook Error", e);
        }
    }

    /**
     * 更新方法定义钩子
     * TODO:当前方法处理维度太大后续细化，1.1版本优化时做
     */
    public void updateMethodDefinitionHook() {

        try {

            GatewayApiService gatewayApiService = this.applicationContext.getBean(GatewayApiService.class);

            List<ConfigureApiDO> configureApiDOList = gatewayApiService.getAll();
            if (CollectionUtils.isEmpty(configureApiDOList)) {
                log.warn("[GatewayServiceDiscovery]updateMethodDefinitionHook Gateway api Config Not Found");
                return;
            }

            configureApiDOList.forEach(this::saveGatewayMethodDefinition);

            this.getUriMappings().forEach(uri -> {
                GatewayMethodDefinition methodDefinition = this.getMethodDefinition(uri);
                //添加api限流 目前不做系统限流
                FlowControlRuleHolder.addInterfaceRule(methodDefinition);
            });

            FlowControlRuleHolder.reloadFlowRules();

        } catch (Exception e) {
            log.error("[GatewayServiceDiscovery]updateMethodDefinitionHook Error", e);
        }
    }


    /**
     * 添加方法定义钩子
     * TODO:当前方法处理维度太大后续细化，1.1版本优化时做
     */
    public void addMethodDefinitionHook() {

        try {

            GatewayApiService gatewayApiService = this.applicationContext.getBean(GatewayApiService.class);

            List<ConfigureApiDO> configureApiDOList = gatewayApiService.getAll();
            if (CollectionUtils.isEmpty(configureApiDOList)) {
                log.warn("[GatewayServiceDiscovery]addMethodDefinitionHook Gateway api Config Not Found");
                return;
            }

            configureApiDOList.forEach(this::saveGatewayMethodDefinition);

            this.getUriMappings().forEach(uri -> {
                GatewayMethodDefinition methodDefinition = this.getMethodDefinition(uri);
                //添加api限流 目前不做系统限流
                FlowControlRuleHolder.addInterfaceRule(methodDefinition);
            });

            buildHandleType(configureApiDOList);

            configureApiDOList.forEach(this::buildHandleMapping);

            buildGatewayInvokePipeline();

            FlowControlRuleHolder.reloadFlowRules();

        } catch (Exception e) {
            log.error("[GatewayServiceDiscovery]addMethodDefinitionHook Error", e);
        }
    }

    /**
     * 构建处理类型
     *
     * @param configureApiDOList
     */
    private void buildHandleType(List<ConfigureApiDO> configureApiDOList) {

        configureApiDOList.forEach(configureApiDO -> {
            if (!this.GATEWAY_HANDLE_TYPE.contains(configureApiDO.getApiType())) {
                GATEWAY_HANDLE_TYPE.add(configureApiDO.getApiType());
            }
        });
    }

    /**
     * 构建网关调用流水线
     */
    private void buildGatewayInvokePipeline() {
        GatewayInvokePipeline gatewayInvokePipeline = this.applicationContext.getBean(GatewayInvokePipeline.class);
        gatewayInvokePipeline.init();
    }

    public boolean matchHandleType(Set<String> handleType) {
        Set<String> tmp = Sets.newHashSet(handleType);
        tmp.retainAll(this.GATEWAY_HANDLE_TYPE);
        return !tmp.isEmpty();
    }


    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
        this.serviceRouter.setApplicationContext(applicationContext);
    }
}
