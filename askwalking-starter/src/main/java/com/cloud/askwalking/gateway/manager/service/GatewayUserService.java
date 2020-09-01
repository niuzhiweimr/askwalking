package com.cloud.askwalking.gateway.manager.service;

import com.cloud.askwalking.gateway.manager.dto.LoginDTO;
import com.cloud.askwalking.client.domain.R;

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
