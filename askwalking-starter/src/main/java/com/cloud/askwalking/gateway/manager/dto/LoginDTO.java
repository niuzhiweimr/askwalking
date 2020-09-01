package com.cloud.askwalking.gateway.manager.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * @author niuzhiwei
 */
@Getter
@Setter
@ToString
public class LoginDTO implements Serializable {

    @NotBlank(message = "登陆用户名不能空")
    @ApiModelProperty("用户名")
    private String userName;

    @NotBlank(message = "登陆密码不能空")
    @ApiModelProperty("密码")
    private String password;
}
