package com.cloud.askwalking.gateway.manager.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * @author niuzhiwei
 */
@ApiModel(value = "添加API请求对象")
@Getter
@Setter
@ToString
public class AddConfigureApiDTO implements Serializable {

    @NotBlank(message = "添加API,应用名称不能空")
    @ApiModelProperty(value = "应用名称，调用服务注册名", required = true)
    private String applicationName;

    @NotBlank(message = "添加API,API类型不能空")
    @ApiModelProperty(value = "API类型", required = true, allowableValues = "API,ADMIN,SAAS")
    private String apiType;

    @NotBlank(message = "添加API,请求URI不能空")
    @ApiModelProperty(value = "请求URI", required = true)
    private String requestUri;

    @ApiModelProperty(value = "请求方式，默认为GET", allowableValues = "GET,POST")
    private String requestMethod;

    @NotBlank(message = "添加API,接口地址不能空")
    @ApiModelProperty(value = "接口地址，全类名", required = true)
    private String apiInterface;

    @ApiModelProperty(value = "接口版本")
    private String apiVersion;

    @NotBlank(message = "添加API,请求对象不能空")
    @ApiModelProperty(value = "请求对象", required = true)
    private String apiRequestClass;

    @NotBlank(message = "添加API,接口方法不能空")
    @ApiModelProperty(value = "接口方法", required = true)
    private String apiMethod;

    @ApiModelProperty(value = "接口分组")
    private String apiGroup;

    @ApiModelProperty(value = "接口名称")
    private String apiName;

    @ApiModelProperty(value = "异步支持,默认为0， 0：同步 1：异步", allowableValues = "0,1")
    private Boolean apiAsync;

    @ApiModelProperty(value = "重载支持,默认为1， 0：不支持 1：支持,对参数qps 、avgTt、maxThread生效", allowableValues = "1,0")
    private Boolean apiReload;

    @ApiModelProperty(value = "响应mock数据,此数据必须为json格式,,否则网关报错：json序列化失败")
    private String mockResponse;

    @ApiModelProperty(value = "系统保护,默认为1，开启系统保护后限流规则生效,参数 0：开启 1：不开启", allowableValues = "1,0")
    private Integer systemGuard;

    @ApiModelProperty(value = "限流类型,默认为2，1：系统限流目前不支持 2：api限流", allowableValues = "2,1")
    private Integer flowControlRuleType;

    @ApiModelProperty(value = "每秒请求数，默认-1不限制")
    private Float qps;

    @ApiModelProperty(value = "平均响应时间，默认-1不限制")
    private Long avgRt;

    @ApiModelProperty(value = "最大并发数，默认-1不限制")
    private Long maxThread;

    @ApiModelProperty(value = "签名校验，默认为1，0：不校验，1：校验", allowableValues = "1,0")
    private Integer signCheck;

    @ApiModelProperty(value = "接口状态，默认为2，参数 0：mock_response报文，1：待发布，2：已发布", allowableValues = "2,0,1")
    private Integer status;
}
