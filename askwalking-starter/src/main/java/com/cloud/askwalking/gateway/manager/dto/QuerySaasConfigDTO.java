package com.cloud.askwalking.gateway.manager.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

/**
 * @author niuzhiwei
 */
@ApiModel("查询商户信息")
@Getter
@Setter
@ToString
public class QuerySaasConfigDTO implements Serializable {

    @ApiModelProperty("商户openID")
    private String openId;

    @ApiModelProperty(value = "接入商状态，1-准入/2-禁止", allowableValues = "1,2")
    private String status;
}
