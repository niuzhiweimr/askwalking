package com.cloud.askwalking.gateway.manager.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * 添加商户
 *
 * @author niuzhiwei
 */
@ApiModel(value = "添加商户请求对象")
@Getter
@Setter
@ToString
public class AddSaasConfigDTO implements Serializable {

    @NotBlank(message = "接入商户openId不能空")
    @ApiModelProperty(value = "接入商授权id", required = true)
    private String openId;

    @NotBlank(message = "接入商户名称不能空")
    @ApiModelProperty(value = "接入商名称", required = true)
    private String openName;

    @NotBlank(message = "内部私钥不能空")
    @ApiModelProperty(value = "内部私钥", required = true)
    private String privateKey;

    @NotBlank(message = "内部公钥不能空")
    @ApiModelProperty(value = "内部公钥", required = true)
    private String publicKey;

    @NotBlank(message = "外部公钥不能空")
    @ApiModelProperty(value = "外部公钥", required = true)
    private String outerPublicKey;

    @ApiModelProperty(value = "接入商状态，1-准入/2-禁止", required = true, allowableValues = "1,2")
    private Byte status;

}
