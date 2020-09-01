package com.cloud.askwalking.gateway.manager.vo;

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
public class SaasResourceVO implements Serializable {


    @ApiModelProperty("商户拥有资源")
    private String requestUri;

    @ApiModelProperty("商户资源状态")
    private Byte status;

}
