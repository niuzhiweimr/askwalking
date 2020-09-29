package com.cloud.askwalking.admin.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author niuzhiwei
 */
@ApiModel("查询API")
@Getter
@Setter
@ToString
public class QueryConfigureApiDTO {

    @ApiModelProperty(value = "应用名称，调用服务注册名")
    private String applicationName;

    @ApiModelProperty(value = "协议", allowableValues = "RPC,FEIGN")
    private String protocol;

    @ApiModelProperty(value = "请求URI")
    private String requestUri;

    @ApiModelProperty(value = "API类型", allowableValues = "API,ADMIN,SAAS")
    private String apiType;

    @ApiModelProperty(value = "接口状态，参数 0：mock_response报文，1：待发布，2：已发布", allowableValues = "0,1,2")
    private Integer status;
}
