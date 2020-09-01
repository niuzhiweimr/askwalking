package com.cloud.askwalking.core.context;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

/**
 * saas平台配置
 *
 * @author niuzhiwei
 */
@Getter
@Setter
@ToString
public class SaasConfigInfo {

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

    @ApiModelProperty("到期时间")
    private Date expireTime;

    @ApiModelProperty("签名验证时间戳")
    private String timestamp;

    @ApiModelProperty("原始加密请求参数")
    private String sourceSignParam;

    @ApiModelProperty("原始明文请求参数")
    private String sourceParam;

    @ApiModelProperty("混淆后参数")
    private String confusionParam;

    @ApiModelProperty("外部签名字符串")
    private String outerSign;


}
