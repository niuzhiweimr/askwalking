package com.cloud.askwalking.client.domain;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.util.Map;

/**
 * @author niuzhiwei
 */
@Getter
@Setter
@ToString
public abstract class BaseRequest implements Serializable {

    @ApiModelProperty("来源")
    private String source;

    @ApiModelProperty("时间戳")
    private Long timestamp = System.currentTimeMillis();

    @ApiModelProperty("扩展信息")
    private Map<String, Object> attachment;


}
