package com.cloud.askwalking.common.domain;

import com.cloud.askwalking.common.exception.ErrorCode;
import com.cloud.askwalking.common.exception.StatusDefinition;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.skywalking.apm.toolkit.trace.TraceContext;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * @author niuzhiwei
 */
@SuppressWarnings("unchecked")
@Getter
@Setter
public final class R<T> implements Serializable {

    @ApiModelProperty("协议状态码")
    private Status status = new Status(0, "服务器处理成功");

    @ApiModelProperty("响应数据")
    private T result;

    @ApiModelProperty("扩展信息")
    private Map<String, Object> attachment;

    /**
     * 部分成功
     *
     * @param result
     * @return
     */
    public static R<Object> part(Object result) {
        R<Object> r = new R<>();
        r.setResult(result);
        r.addAttachment("traceId", TraceContext.traceId());
        r.addAttachment("timestamp", System.currentTimeMillis());
        r.status.setStatusCode(ErrorCode.PART_ERROR.getErrorCode(), true);
        r.status.setStatusReason(ErrorCode.PART_ERROR.getErrorReason());
        return r;
    }

    /**
     * 成功
     *
     * @param result
     * @param <T>
     * @return
     */
    public static <T> R<T> success(Object result) {
        R<T> r = new R<>();
        r.addAttachment("traceId", TraceContext.traceId());
        r.addAttachment("timestamp", System.currentTimeMillis());
        r.setResult((T) result);
        return r;
    }

    /**
     * 成功
     *
     * @return
     */
    public static <T> R<T> success() {
        R<T> r = new R<>();
        r.addAttachment("traceId", TraceContext.traceId());
        r.addAttachment("timestamp", System.currentTimeMillis());
        return r;
    }

    /**
     * 失败
     *
     * @param errorCode
     * @param errMsg
     * @param <T>
     * @return
     */
    public static <T> R<T> fail(int errorCode, String errMsg) {
        R<T> r = new R<>();
        r.addAttachment("traceId", TraceContext.traceId());
        r.addAttachment("timestamp", System.currentTimeMillis());
        r.setStatus(new Status(errorCode, errMsg));
        return r;
    }

    /**
     * 当RPC调用失败
     *
     * @param errorCode
     * @param errMsg
     * @return
     */
    public static <T> R<T> rpcFail(int errorCode, String errMsg) {
        R<T> r = new R<>();
        r.addAttachment("traceId", TraceContext.traceId());
        r.addAttachment("timestamp", System.currentTimeMillis());
        r.setStatus(new Status(errorCode, errMsg, true));
        return r;
    }

    /**
     * 返回系统错误追加系统编码
     *
     * @param <T>
     * @return
     */
    public static <T> R<T> fail() {
        return fail(ErrorCode.SYSTEM_ERROR);
    }

    /**
     * 返回的状态码自动最加当前系统编码
     *
     * @param statusDefinition
     * @return
     */
    public static <T> R<T> fail(StatusDefinition statusDefinition) {
        R<T> r = new R<>();
        r.addAttachment("traceId", TraceContext.traceId());
        r.addAttachment("timestamp", System.currentTimeMillis());
        r.setStatus(new Status(statusDefinition.getErrorCode(), statusDefinition.getErrorReason()));
        return r;
    }

    public static R<Map<String, Object>> make(String key, Object value) {
        R<Map<String, Object>> r = new R<>();
        r.setResult(new HashMap<>(0));
        r.addAttachment("traceId", TraceContext.traceId());
        r.addAttachment("timestamp", System.currentTimeMillis());
        r.result.put(key, value);
        return r;
    }

    public R<Map<String, Object>> addResult(String key, Object value) {
        Map<String, Object> result = (Map<String, Object>) this.getResult();
        result.put(key, value);
        return (R<Map<String, Object>>) this;
    }

    public boolean successful() {
        return getStatus().getStatusCode() == 0;
    }

    /**
     * 接口返回错误
     */
    public boolean failed() {
        return !successful();
    }


    public void addAttachment(String key, Object value) {
        if (attachment == null) {
            this.attachment = new HashMap<>(0);
        }
        this.attachment.put(key, value);
    }

    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this);
    }

    /**
     * 默认构造器
     */
    public R() {
    }
}
