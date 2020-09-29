package com.cloud.askwalking.gateway.pipline.context.saas;

import com.cloud.askwalking.common.enums.GatewayErrorCode;
import com.cloud.askwalking.core.constants.GatewayConstant;
import com.cloud.askwalking.core.context.GatewayInvokeContext;
import com.cloud.askwalking.core.domain.SaasConfigInfo;
import com.cloud.askwalking.gateway.pipline.context.common.AbstractConfigLoadContextHandler;
import com.google.common.collect.Sets;
import com.cloud.askwalking.repository.model.SaasConfigDO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;

import java.util.Objects;
import java.util.Set;

/**
 * @author niuzhiwei
 */
@Slf4j
public class SaasConfigLoadContextHandler extends AbstractConfigLoadContextHandler {

    private final Set<String> handleTypes = Sets.newHashSet(GatewayConstant.SAAS);

    @Override
    public Set<String> handleType() {
        return this.handleTypes;
    }

    @Override
    public Boolean getConfig(GatewayInvokeContext context) {

        try {
            String openId = context.getRequestHeader("_open_id");
            if (StringUtils.isEmpty(openId)) {
                log.error("[SaasConfigLoadContextHandler] openId isNull");
                return putDebugErrorResult(context, GatewayErrorCode.SAAS_OPEN_ID_IS_NULL);
            }

            String sign = context.getRequestHeader("_sign");
            if (StringUtils.isEmpty(sign)) {
                log.error("[SaasConfigLoadContextHandler] sign isNull");
                return putDebugErrorResult(context, GatewayErrorCode.SAAS_SIGN_IS_NULL);
            }

            String timestamp = context.getRequestHeader("_timestamp");
            if (StringUtils.isEmpty(timestamp)) {
                log.error("[SaasConfigLoadContextHandler] timestamp isNull");
                return putDebugErrorResult(context, GatewayErrorCode.SAAS_TIMESTAMP_IS_NULL);
            }

            //SaasConfigService saasConfigService = applicationContext.getBean(SaasConfigService.class);
            SaasConfigDO saasConfigDO = null;//saasConfigService.getSaasByOpenId(openId);
            if (Objects.isNull(saasConfigDO)) {
                log.error("[SaasConfigLoadContextHandler] SaasConfigDO isNull");
                return putDebugErrorResult(context, GatewayErrorCode.SAAS_CONFIG_NON_EXISTENT);
            }

            SaasConfigInfo saasConfigInfo = new SaasConfigInfo();
            BeanUtils.copyProperties(saasConfigDO, saasConfigInfo);
            saasConfigInfo.setTimestamp(timestamp);
            saasConfigInfo.setOuterSign(sign);
            context.setSaasConfigInfo(saasConfigInfo);

        } catch (Exception e) {
            log.error("[SaasConfigLoadContextHandler] SaaS platform configuration loading error：", e);
            return putDebugErrorResult(context, GatewayErrorCode.SAAS_CONFIG_LOAD_ERROR);
        }

        return true;
    }
}
