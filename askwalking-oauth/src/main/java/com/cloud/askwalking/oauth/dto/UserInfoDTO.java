package com.cloud.askwalking.oauth.dto;

import com.cloud.askwalking.common.tool.JSONTool;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.io.Serializable;
import java.util.Map;

/**
 * 用户信息
 *
 * @author niuzhiwei
 */
@Getter
@Builder
@ToString
public class UserInfoDTO implements Serializable {

    @ApiModelProperty("用户id")
    private Long customerId;

    @ApiModelProperty("客户手机号")
    private String customerMobile;

    @ApiModelProperty("客户编号")
    private String customerNo;

    @ApiModelProperty("客户名称")
    private String customerName;

    public Map toMap() {
        String jsonStr = JSONTool.toJson(this);
        return JSONTool.toObject(jsonStr, Map.class);
    }
}
