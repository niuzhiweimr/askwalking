package com.cloud.askwalking.core.domain;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author niuzhiwei
 */
@Getter
@Setter
@ToString
@EqualsAndHashCode
public class GatewayMethodDefinition {

    private Integer systemGuard;

    private Integer flowControlRuleType;

    private String applicationName;

    private String protocol;

    private String contentType;

    private String apiType;

    private Boolean apiReload;

    private Boolean apiAsync;

    private String requestUri;

    private String requestMethod;

    private Integer signCheck;

    private Integer status;

    private ApiConfig apiConfig;

    /**
     * 限流标识 应用+接口+方法
     *
     * @return
     */
    public String getMDIdentify() {
        return this.applicationName + ":" + this.apiConfig.getApiInterface() + ":" + this.apiConfig.getApiMethod();
    }

    /**
     * 获取限流
     *
     * @return
     */
    public FlowControlRule getFlowControlRule() {

        return this.apiConfig.getFlowControlRule();
    }

    /**
     * 限流规则
     *
     * @return
     */
    public Boolean getApiReload() {

        return this.apiReload;
    }

    /**
     * 限流规则
     *
     * @return
     */
    public Integer getFlowControlRuleType() {

        return this.flowControlRuleType;
    }


    /**
     * 是否开启系统保护
     *
     * @return
     */
    public boolean getSystemGuardIsEnable() {
        return this.systemGuard == 0;
    }

}
