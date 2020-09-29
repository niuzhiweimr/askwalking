package com.cloud.askwalking.gateway.pipline.context.api;

import com.cloud.askwalking.common.constants.GatewayConstant;
import com.cloud.askwalking.common.enums.GatewayErrorCode;
import com.cloud.askwalking.core.context.GatewayInvokeContext;
import com.cloud.askwalking.gateway.pipline.AbstractGatewayContextHandler;
import com.cloud.askwalking.oauth.JwtUtil;
import com.cloud.askwalking.oauth.dto.UserInfoDTO;
import com.cloud.askwalking.oauth.enums.JwtIgnoreHeaderEnum;
import com.google.common.collect.Sets;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jws;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.Map;
import java.util.Set;

/**
 * @author niuzhiwei
 */
@Slf4j
public class UserAuthContextHandler extends AbstractGatewayContextHandler implements ApplicationContextAware {

    private final Set<String> handleTypes = Sets.newHashSet(GatewayConstant.API);

    private final Set<String> protocolTypes = Sets.newHashSet(GatewayConstant.RPC, GatewayConstant.FEIGN);

    private ApplicationContext applicationContext;

    @Override
    public boolean handleGatewayInvoke(GatewayInvokeContext gatewayInvokeContext) {

        try {
            boolean flag = gatewayInvokeContext.getApiMethod().equalsIgnoreCase(JwtIgnoreHeaderEnum.LOGIN.getCode()) ||
                    gatewayInvokeContext.getApiMethod().equalsIgnoreCase(JwtIgnoreHeaderEnum.LOGOUT.getCode()) ||
                    gatewayInvokeContext.getApiMethod().equalsIgnoreCase(JwtIgnoreHeaderEnum.SMS_CODE.getCode());
            if (flag) {
                if (log.isDebugEnabled()) {
                    log.error("[UserAuthContextHandler] Gateway release interface:{}"
                            , gatewayInvokeContext.getApiMethod());
                }
                return true;
            }

            String jwtToken = gatewayInvokeContext.getRequestHeader("_trafficbu_token");
            if (StringUtils.isEmpty(jwtToken)) {
                return putDebugErrorResult(gatewayInvokeContext, GatewayErrorCode.JWT_TOKEN_HEADER_IS_NULL);
            }

            Boolean verify = JwtUtil.verifyToken(jwtToken);
            if (!verify) {
                return putDebugErrorResult(gatewayInvokeContext, GatewayErrorCode.JWT_AUTHORIZATION_ERROR);
            }

            //解析token
            Jws<Claims> claimsJws = JwtUtil.analysisToken(jwtToken);
            Claims body = claimsJws.getBody();
            Map userInfoMap = UserInfoDTO.builder()
                    .customerId(body.get("customerId", Long.class))
                    .customerNo(body.get("customerNo", String.class))
                    .customerName(body.get("customerName", String.class))
                    .customerMobile(body.get("customerMobile", String.class))
                    .build()
                    .toMap();

            Map serviceParam = (Map) gatewayInvokeContext.getServiceParam();
            if (CollectionUtils.isEmpty(serviceParam)) {
                serviceParam = userInfoMap;
            } else {
                serviceParam.putAll(userInfoMap);
            }
            gatewayInvokeContext.setServiceParam(serviceParam);

        } catch (IllegalArgumentException e) {
            log.error("[UserAuthContextHandler] JWT token authentication exception：", e);
            return putDebugErrorResult(gatewayInvokeContext, GatewayErrorCode.JWT_AUTHORIZATION_ERROR);
        } catch (ExpiredJwtException e1) {
            log.error("[UserAuthContextHandler] JWT token expire exception：", e1);
            return putDebugErrorResult(gatewayInvokeContext, GatewayErrorCode.JWT_EXPIRE);
        }

        return true;
    }

    @Override
    public Set<String> handleType() {
        return this.handleTypes;
    }

    @Override
    public Set<String> protocolType() {
        return this.protocolTypes;
    }

    @Override
    public int getOrder() {
        return 30;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
