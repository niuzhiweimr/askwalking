package com.cloud.askwalking.oauth;

import com.cloud.askwalking.common.domain.R;
import com.cloud.askwalking.oauth.dto.LogoutReqDTO;
import com.cloud.askwalking.oauth.dto.MobileOrIdLoginReqDTO;
import com.cloud.askwalking.oauth.dto.MobileOrIdLoginRspDTO;
import com.cloud.askwalking.oauth.dto.SmsCodeReqDTO;

/**
 * 客户认证顶级接口
 *
 * @author niuzhiwei
 */
public interface CustomerOauthOperatorService {

    /**
     * 如果用手机号登录，登录前获取短信验证码
     *
     * @param request
     * @return
     */
    R fetchLoginSmsCode(SmsCodeReqDTO request);

    /**
     * 用手机号+短信验证码或身份证号码+密码登录
     * 身份证号暂不支持
     *
     * @param request
     * @return
     */
    R<MobileOrIdLoginRspDTO> login(MobileOrIdLoginReqDTO request);

    /**
     * 退出登录
     *
     * @param request
     * @return
     */
    R logout(LogoutReqDTO request);
}
