package com.cloud.askwalking.oauth.enums;

import lombok.Getter;

/**
 * @author niuzhiwei
 */

@Getter
public enum JwtIgnoreHeaderEnum {

    /**
     * jwt不过滤请求
     */
    LOGIN("login", "登陆"),
    LOGOUT("logout", "登出"),
    SMS_CODE("fetchLoginSmsCode", "短信验证码");

    JwtIgnoreHeaderEnum(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    private String code;

    private String desc;

    public static boolean isMember(String code) {
        for (JwtIgnoreHeaderEnum jwtIgnoreHeaderCode : JwtIgnoreHeaderEnum.values()) {
            if (code.equals(jwtIgnoreHeaderCode.getCode())) {
                return true;
            }
        }
        return false;
    }
}
