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
@ApiModel(value = "添加商户资源请求对象")
@Getter
@Setter
@ToString
public class AddSaasResourceDTO implements Serializable {

    @NotBlank(message = "接入商户openId不能空")
    @ApiModelProperty(value = "商户openId", required = true)
    private String openId;

    @NotBlank(message = "请求URI不能空")
    @ApiModelProperty(value = "请求URI", required = true)
    private String requestUri;


    @ApiModelProperty(value = "商户资源接入状态，1-准入/2-禁止", required = true, allowableValues = "1,2")
    private Byte status;
}
