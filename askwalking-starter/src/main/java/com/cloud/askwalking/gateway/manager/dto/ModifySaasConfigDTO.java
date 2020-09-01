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
@ApiModel(value = "修改商户请求对象")
@Getter
@Setter
@ToString
public class ModifySaasConfigDTO implements Serializable {

    @NotBlank(message = "接入商户openId空")
    @ApiModelProperty(value = "接入商户openID", required = true)
    private String openId;

    @ApiModelProperty("接入商名称")
    private String openName;

    @ApiModelProperty("内部私钥")
    private String privateKey;

    @ApiModelProperty("内部公钥")
    private String publicKey;

    @ApiModelProperty("外部公钥")
    private String outerPublicKey;

    @ApiModelProperty(value = "接入商状态，1-准入/2-禁止", allowableValues = "1,2")
    private Byte status;
}
