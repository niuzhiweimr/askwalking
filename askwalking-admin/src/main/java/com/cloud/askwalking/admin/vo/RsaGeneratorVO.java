package com.cloud.askwalking.admin.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

/**
 * @author niuzhiwei
 */
@Getter
@Setter
@ToString
public class RsaGeneratorVO implements Serializable {

    @ApiModelProperty("内部私钥")
    private String innerPrivateKey;

    @ApiModelProperty("内部公钥")
    private String innerPublicKey;

    @ApiModelProperty("外部私钥")
    private String outerPrivateKey;

    @ApiModelProperty("外部公钥")
    private String outerPublicKey;
}
