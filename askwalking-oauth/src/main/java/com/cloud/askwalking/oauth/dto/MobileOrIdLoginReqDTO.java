package com.cloud.askwalking.oauth.dto;

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
public class MobileOrIdLoginReqDTO implements Serializable {

    @ApiModelProperty("客户手机号")
    private String mobile;

    @ApiModelProperty("客户身份证号")
    private String idNo;

    @ApiModelProperty("短信验证码或密码")
    private String smsCodeOrPwd;
}
