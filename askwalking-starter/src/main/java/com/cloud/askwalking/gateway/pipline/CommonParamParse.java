package com.cloud.askwalking.gateway.pipline;

import com.alibaba.fastjson.JSON;
import com.cloud.askwalking.common.Base64Utils;
import com.cloud.askwalking.core.constants.GatewayConstant;
import com.cloud.askwalking.core.context.GatewayInvokeContext;
import lombok.extern.slf4j.Slf4j;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.StringJoiner;

/**
 * @author niuzhiwei
 */
@Slf4j
public class CommonParamParse {

    public static boolean handleParamParse(GatewayInvokeContext gatewayInvokeContext) throws Exception {

        switch (gatewayInvokeContext.getRequestMethod()) {

            case GatewayConstant.HTTP_METHOD_GET:
                gatewayInvokeContext.setServiceParam(gatewayInvokeContext.getRequestParams());
                return true;

            case GatewayConstant.HTTP_METHOD_POST:
                StringBuilder sb = new StringBuilder();
                try {
                    InputStream inputStream = gatewayInvokeContext.getInputStream();
                    byte[] b = new byte[4096];
                    while (inputStream.read(b) != -1) {
                        sb.append(new String(b, StandardCharsets.UTF_8));
                    }
                    String requestStr = sb.toString();
                    Map<String, Object> params = JSON.parseObject(requestStr);
                    gatewayInvokeContext.setServiceParam(params);
                } catch (Exception e) {
                    log.error("[CommonParamParseUtil]#handleParamParse Get body body request parameter conversion exception" +
                            "：{},requestParams:{}", e, sb);
                    throw new Exception("POST请求参数转换异常");
                }
                return true;
            default:
                log.error("[CommonParamParseUtil] Request parameter parsing failed, parameter is empty" +
                        ", HTTP request mode：{}", gatewayInvokeContext.getRequestMethod());
                return false;
        }
    }

    /**
     * 处理saas平台参数转化
     *
     * @param gatewayInvokeContext
     * @return
     * @throws Exception
     */
    public static boolean handleSaasParamParse(GatewayInvokeContext gatewayInvokeContext) throws Exception {

        StringBuilder sb = new StringBuilder();
        try {
            InputStream inputStream = gatewayInvokeContext.getInputStream();
            byte[] b = new byte[4096];
            while (inputStream.read(b) != -1) {
                sb.append(new String(b, StandardCharsets.UTF_8));
            }

            byte[] decode = Base64Utils.decode(sb.toString());
            String requestStr = new String(decode, StandardCharsets.UTF_8);
            gatewayInvokeContext.setServiceParam(requestStr);
        } catch (Exception e) {
            log.error("[CommonParamParseUtil]#handleSaasParamParse Get body body request parameter conversion exception"
                    + "：{},requestParams:{}", e, sb);
            throw new Exception("POST请求参数转换异常");
        }
        return true;
    }


    /**
     * 处理saas平台参数混淆
     *
     * @param openId
     * @param timestamp
     * @param param
     * @throws Exception
     */
    public static String handleSaasParamConfusion(String openId, String timestamp, String param) throws Exception {

        StringJoiner confusionParam = new StringJoiner("&");
        confusionParam.add(openId);
        confusionParam.add(param);
        confusionParam.add(timestamp);
        confusionParam.add(param);

        return confusionParam.toString();
    }


}
