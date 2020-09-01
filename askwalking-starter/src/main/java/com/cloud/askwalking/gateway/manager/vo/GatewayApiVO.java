package com.cloud.askwalking.gateway.manager.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

/**
 * api列表
 *
 * @author niuzhiwei
 */
@Getter
@Setter
@ToString
public class GatewayApiVO implements Serializable {

    @ApiModelProperty("主键")
    private String id;

    @ApiModelProperty("应用名称")
    private String applicationName;

    @ApiModelProperty("API类型")
    private String apiType;

    @ApiModelProperty("请求URI")
    private String requestUri;

    @ApiModelProperty("请求方式")
    private String requestMethod;

    @ApiModelProperty("接口地址")
    private String apiInterface;

    @ApiModelProperty("接口版本")
    private String apiVersion;

    @ApiModelProperty(value = "请求对象")
    private String apiRequestClass;

    @ApiModelProperty(value = "接口方法")
    private String apiMethod;

    @ApiModelProperty(value = "接口分组")
    private String apiGroup;

    @ApiModelProperty(value = "接口名称")
    private String apiName;

    @ApiModelProperty(value = "异步支持")
    private Boolean apiAsync;

    @ApiModelProperty(value = "重载支持")
    private Boolean apiReload;

    @ApiModelProperty(value = "mock响应数据")
    private String mockResponse;

    @ApiModelProperty(value = "系统保护")
    private Integer systemGuard;

    @ApiModelProperty(value = "限流类型")
    private Integer flowControlRuleType;

    @ApiModelProperty(value = "每秒请求数")
    private Float qps;

    @ApiModelProperty(value = "平均响应时长")
    private Long avgRt;

    @ApiModelProperty(value = "最大并发数")
    private Long maxThread;

    @ApiModelProperty(value = "签名检查")
    private Integer signCheck;

    @ApiModelProperty(value = "接口状态")
    private Integer status;

}
