package com.cloud.askwalking.common.domain;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * @author niuzhiwei
 */
@Getter
@Setter
public class Status implements Serializable {

    @ApiModelProperty("状态码，0表示成功其他都为失败")
    private int statusCode = 0;

    @ApiModelProperty("系统编码，错误码响应系统")
    private int systemCode = -1;

    @ApiModelProperty("状态信息")
    private String statusReason;

    public Status() {

    }


    public Status(int statusCode, String message) {
        this(statusCode, message, true);
    }

    /**
     * @param statusCode
     * @param message
     * @param appendSysCode 是否追加系统编码
     */
    public Status(int statusCode, String message, boolean appendSysCode) {
        if (appendSysCode) {
            if (statusCode != 0) {
                this.systemCode = SystemManager.appendSysCode(statusCode);
                this.statusCode = statusCode;
            }
        } else {
            this.statusCode = statusCode;
        }
        this.statusReason = message;
    }

    @Override
    public String toString() {
        return "{\"statusCode\":" + statusCode + ",\"statusReason\":\"" + statusReason + "\"}";
    }

    public int getStatusCode() {
        return statusCode;
    }

    /**
     * 注意该种方式只在是使用序列化框架是使用，如果编码是调用请使用
     * <p/>
     * <code>
     * setStatusCode(int statusCode, boolean appendSysCode)
     * </code>
     *
     * @param statusCode
     */
    @SuppressWarnings("unused")
    private void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    /**
     * 注意编程式方式调用
     *
     * @param statusCode
     * @param appendSysCode
     */
    public void setStatusCode(int statusCode, boolean appendSysCode) {
        if (statusCode != 0) {
            if (appendSysCode) {
                this.statusCode = SystemManager.appendSysCode(statusCode);
            } else {
                this.statusCode = statusCode;
            }
        }
    }

    public String getStatusReason() {
        return statusReason;
    }

    public void setStatusReason(String statusReason) {
        this.statusReason = statusReason;
    }
}
