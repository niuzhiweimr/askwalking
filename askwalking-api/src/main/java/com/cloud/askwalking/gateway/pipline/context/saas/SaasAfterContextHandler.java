package com.cloud.askwalking.gateway.pipline.context.saas;

import com.alibaba.fastjson.JSONObject;
import com.cloud.askwalking.common.constants.GatewayConstant;
import com.cloud.askwalking.common.enums.GatewayErrorCode;
import com.cloud.askwalking.common.utils.RSAUtils;
import com.cloud.askwalking.core.context.GatewayInvokeContext;
import com.cloud.askwalking.gateway.pipline.AbstractGatewayContextHandler;
import com.google.common.collect.Sets;
import lombok.extern.slf4j.Slf4j;

import java.util.Objects;
import java.util.Set;

/**
 * @author niuzhiwei
 */
@SuppressWarnings("ALL")
@Slf4j
public class SaasAfterContextHandler extends AbstractGatewayContextHandler {

    private final Set<String> handleTypes = Sets.newHashSet(GatewayConstant.SAAS);

    private final Set<String> protocolTypes = Sets.newHashSet(GatewayConstant.RPC, GatewayConstant.FEIGN);

    @Override
    public boolean handleGatewayInvoke(GatewayInvokeContext gatewayInvokeContext) {

        try {

            Object result = gatewayInvokeContext.getBaseResponse().getResult();
            if (Objects.isNull(result)) {
                if (log.isDebugEnabled()) {
                    log.debug("The downstream service response data is empty");
                }
                return true;
            }
            if (log.isDebugEnabled()) {
                log.debug("[SaasAfterContextHandler]The downstream service response data is:{}"
                        , JSONObject.toJSONString(result));
            }
            String resultStr = JSONObject.toJSONString(result);
            String ciphertext = RSAUtils.encryptByPrivateKey(resultStr
                    , gatewayInvokeContext.getSaasConfigInfo().getPrivateKey());
            gatewayInvokeContext.getBaseResponse().setResult(ciphertext);

        } catch (Exception e) {
            log.error("[SaasAfterContextHandler] Response to data encryption exception:", e);
            return putDebugErrorResult(gatewayInvokeContext, GatewayErrorCode.SAAS_ENCRYPTION_ERROR);
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
        return 200;
    }

}
