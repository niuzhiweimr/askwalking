package com.cloud.askwalking.oauth.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

/**
 * 短信验证码发送请求对象
 *
 * @author niuzhiwei
 */
@Getter
@Setter
@ToString
public class SmsCodeReqDTO implements Serializable {

    @ApiModelProperty("手机号 非空")
    private String mobileNo;
}
