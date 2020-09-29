package com.cloud.askwalking.common.enums;

import com.cloud.askwalking.common.exception.StatusDefinition;
import lombok.Getter;

/**
 * @author niuzhiwei
 */
@Getter
public enum GatewayErrorCode implements StatusDefinition {

    /**
     * 用户相关 2001 ~ 2015
     */
    USER_NON(2001, "用户不存在"),
    PASSWORD_ERROR(2002, "密码错误"),

    /**
     * API管理相关 2016 ~ 2030
     */
    ADD_API_DB_ERROR(2016, "添加API数据库异常"),
    UPDATE_API_DB_ERROR(2017, "添加API数据库异常"),
    ADD_API_PARAMS_ERROR(2018, "添加API参数错误"),
    OFFLINE_API_NON_EXISTENT(2019, "下线API系统无此API"),
    ONLINE_API_NON_EXISTENT(2020, "上线API系统无此API"),
    MOCK_API_NON_EXISTENT(2021, "mock系统无此API"),

    /**
     * SAAS相关 2030 ~ 2050
     */
    SAAS_CONFIG_LOAD_ERROR(2030, "配置加载错误"),
    SAAS_OPEN_ID_IS_NULL(2031, "openId空"),
    SAAS_TIMESTAMP_IS_NULL(2032, "timestamp空"),
    SAAS_SIGN_IS_NULL(2033, "sign空"),
    SAAS_CONFIG_NON_EXISTENT(2034, "无效openId"),
    SAAS_RESOURCE_ERROR(2035, "商户资源鉴权异常"),
    SAAS_RESOURCE_IS_NULL(2036, "商户无此资源权限,请联系开通"),
    SAAS_BUILD_SING_PARAM_ERROR(2037, "构建签名参数错误"),
    SAAS_VERIFY_FAILED(2038, "商户验签失败"),
    SAAS_VERIFY_SYSTEM_ERROR(2039, "验签系统错误"),
    SAAS_ENCRYPTION_ERROR(2040, "响应数据加密错误"),
    SAAS_OPEN_ID_CLASH(2041, "商户openId已存在"),
    SAAS_RESOURCE_CLASH(2042, "商户RUI资源已存在"),
    SAAS_RESOURCE_MERCHANT_IS_NULL(2043, "商户无效"),
    SAAS_RESOURCE_API_IS_NULL(2044, "资源URI无效"),

    /**
     * SAAS相关 2051 ~ 2090
     */
    JWT_AUTHORIZATION_ERROR(2051, "jwt鉴权失败"),
    JWT_TOKEN_HEADER_IS_NULL(2052, "jwt token header空"),
    JWT_EXPIRE(2053, "jwt token过期"),
    JWT_AUTHORIZATION_TOKEN_IS_NULL(2054, "jwt获取认证令牌空"),
    ;


    private int errorCode;
    private String errorReason;

    @Override
    public int getErrorCode() {
        return this.errorCode;
    }

    @Override
    public String getErrorReason() {
        return this.errorReason;
    }

    private GatewayErrorCode(int errorCode, String errorReason) {
        this.errorCode = errorCode;
        this.errorReason = errorReason;
    }

    public static boolean isMember(int code) {

        for (GatewayErrorCode errorCode : GatewayErrorCode.values()) {
            if (code == errorCode.getErrorCode()) {
                return true;
            }
        }
        return false;
    }
}