package com.cloud.askwalking.gateway.pipline.context.saas;

import com.cloud.askwalking.common.constants.GatewayConstant;
import com.cloud.askwalking.common.enums.GatewayErrorCode;
import com.cloud.askwalking.common.tool.JSONTool;
import com.cloud.askwalking.common.tool.RSATool;
import com.cloud.askwalking.core.context.GatewayInvokeContext;
import com.cloud.askwalking.gateway.pipline.CommonParamParse;
import com.cloud.askwalking.gateway.pipline.context.common.AbstractSignCheckContextHandler;
import com.google.common.collect.Sets;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.Set;

/**
 * @author niuzhiwei
 */
@Slf4j
public class SaasSignCheckContextHandler extends AbstractSignCheckContextHandler {

    private final Set<String> handleTypes = Sets.newHashSet(GatewayConstant.SAAS);

    private final Set<String> protocolTypes = Sets.newHashSet(GatewayConstant.RPC, GatewayConstant.FEIGN);

    @Override
    protected Boolean buildSignStr(GatewayInvokeContext context) {

        try {
            String outerPublicKey = context.getSaasConfigInfo().getOuterPublicKey();
            String sourceSignParam = String.valueOf(context.getServiceParam());
            String sourceParam = RSATool.decryptByPublicKey(sourceSignParam, outerPublicKey);
            String timestamp = context.getSaasConfigInfo().getTimestamp();
            String confusionParam = CommonParamParse.handleSaasParamConfusion(context.getOpenId()
                    , timestamp, sourceSignParam);
            context.getSaasConfigInfo().setConfusionParam(confusionParam);
            context.getSaasConfigInfo().setSourceSignParam(sourceSignParam);
            context.getSaasConfigInfo().setSourceParam(sourceParam);
            buildServiceParam(context, sourceParam);
        } catch (Exception e) {
            log.error("[SaasSignCheckContextHandler] Build signature string exception：", e);
            return putDebugErrorResult(context, GatewayErrorCode.SAAS_BUILD_SING_PARAM_ERROR);
        }

        return true;
    }


    @Override
    protected Boolean checkSign(GatewayInvokeContext context) {

        try {
            if (log.isDebugEnabled()) {
                log.debug("check sign request param:{}", JSONTool.toJson(context.getSaasConfigInfo()));
            }
            String confusionParam = context.getSaasConfigInfo().getConfusionParam();
            String outerPublicKey = context.getSaasConfigInfo().getOuterPublicKey();
            String timestamp = context.getSaasConfigInfo().getTimestamp();
            String outerSign = context.getSaasConfigInfo().getOuterSign();
            if (!RSATool.verify(confusionParam, timestamp, outerPublicKey, outerSign)) {
                if (log.isDebugEnabled()) {
                    log.debug("check signature error request param confusionParam:{},outerPublicKey{},outerSign{}",
                            confusionParam, outerPublicKey, outerSign);
                }
                return putDebugErrorResult(context, GatewayErrorCode.SAAS_VERIFY_FAILED);
            }
        } catch (Exception e) {
            log.error("[SaasSignCheckContextHandler] check signature system exception：", e);
            return putDebugErrorResult(context, GatewayErrorCode.SAAS_VERIFY_SYSTEM_ERROR);
        }

        return true;
    }

    /**
     * 构建请求参数
     *
     * @param context
     * @param sourceParam
     */
    private void buildServiceParam(GatewayInvokeContext context, String sourceParam) {
        Map serviceParam = JSONTool.toObject(sourceParam, Map.class);
        context.setServiceParam(serviceParam);
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

}
