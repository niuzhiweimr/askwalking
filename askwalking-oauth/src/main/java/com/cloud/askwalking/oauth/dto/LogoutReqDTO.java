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
public class LogoutReqDTO implements Serializable {

    @ApiModelProperty("客户编号")
    private String customerNo;
}
