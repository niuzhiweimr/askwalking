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
public class MobileOrIdLoginRspDTO implements Serializable {

    @ApiModelProperty("是否是老用户 true:老用户/false:新用户")
    private Boolean oldFlag;

    @ApiModelProperty("jwt token")
    private String token;

    /**
     * 老用户
     */
    public static final Boolean OLD_CUSTOMER = Boolean.TRUE;

    /**
     * 新用户
     */
    public static final Boolean NEW_CUSTOMER = Boolean.FALSE;

}
