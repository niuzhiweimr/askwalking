package com.cloud.askwalking.admin.service;

import com.cloud.askwalking.admin.dto.LoginDTO;
import com.cloud.askwalking.common.domain.R;

/**
 * @author niuzhiwei
 */
public interface GatewayUserService {

    /**
     * 用户登录
     *
     * @param request
     * @return
     */
    R<Boolean> login(LoginDTO request);
}
