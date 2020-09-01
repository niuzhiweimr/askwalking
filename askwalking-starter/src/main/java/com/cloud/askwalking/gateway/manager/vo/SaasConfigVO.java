package com.cloud.askwalking.gateway.manager.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.util.List;

/**
 * @author niuzhiwei
 */
@Getter
@Setter
@ToString
public class SaasConfigVO implements Serializable {

    @ApiModelProperty("商户openID")
    private String openId;

    @ApiModelProperty("内部私钥")
    private String privateKey;

    @ApiModelProperty("内部公钥")
    private String publicKey;

    @ApiModelProperty("外部公钥")
    private String outerPublicKey;

    @ApiModelProperty("接入商状态，1-准入/2-禁止")
    private Byte status;

    @ApiModelProperty("商户拥有资源")
    private List<SaasResourceVO> saasResourceVOList;
}
