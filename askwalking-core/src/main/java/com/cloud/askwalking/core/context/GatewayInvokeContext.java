package com.cloud.askwalking.core.context;

import com.alibaba.cloud.dubbo.metadata.DubboRestServiceMetadata;
import com.alibaba.cloud.dubbo.metadata.RestMethodMetadata;
import com.alibaba.cloud.dubbo.metadata.ServiceRestMetadata;
import com.alibaba.cloud.dubbo.service.DubboGenericServiceFactory;
import com.cloud.askwalking.core.domain.GatewayMethodDefinition;
import com.cloud.askwalking.client.domain.R;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.rpc.service.GenericService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

/**
 * @author niuzhiwei
 */
@Getter
@Setter
@EqualsAndHashCode
@ToString
public class GatewayInvokeContext {

    private long invokeStartTime;

    private HttpServletRequest request;

    private HttpServletResponse response;

    private ServiceRestMetadata serviceRestMetadata;

    private DubboGenericServiceFactory serviceFactory;

    private final Map<String, Object> dubboTranslatedAttributes = new HashMap<>();

    private GatewayMethodDefinition methodDefinition;

    private Object serviceParam;

    private R baseResponse;

    private String servletContextPath;

    private SaasConfigInfo saasConfigInfo;

    private UseAuthInfo useAuthInfo;

    private boolean execAsync;

    private CompletableFuture<Object> future;

    public GatewayInvokeContext(String servletContextPath, HttpServletRequest request, HttpServletResponse response) {
        this.servletContextPath = servletContextPath;
        this.request = request;
        this.response = response;
    }

    public String getRequestURI() {
        String requestURI = this.request.getRequestURI();
        requestURI = requestURI.replace(this.servletContextPath, "");
        return requestURI;
    }


    /**
     * 获取请求方法 GET、POST
     *
     * @return
     */
    public String getRequestMethod() {
        return this.request.getMethod();
    }

    /**
     * 获取api类型
     *
     * @return
     */
    public String getApiType() {
        return (this.methodDefinition != null) ? this.methodDefinition.getApiType() : "";
    }

    /**
     * 获取api版本
     *
     * @return
     */
    public String getApiVersion() {
        return (this.methodDefinition != null) ? this.methodDefinition.getApiConfig().getApiVersion() : "";
    }

    /**
     * 获取api方法
     *
     * @return
     */
    public String getApiMethod() {
        return (this.methodDefinition != null) ? this.methodDefinition.getApiConfig().getApiMethod() : "";
    }

    /**
     * 获取api请求类
     *
     * @return
     */
    public String getApiRequestClass() {
        return (this.methodDefinition != null) ? this.methodDefinition.getApiConfig().getApiRequestClass() : "";
    }

    /**
     * 获取api分组
     *
     * @return
     */
    public String getApiGroup() {
        return (this.methodDefinition != null) ? this.methodDefinition.getApiConfig().getApiGroup() : "";
    }

    /**
     * 获取api所属应用
     *
     * @return
     */
    public String getApplicationName() {
        return (this.methodDefinition != null) ? this.methodDefinition.getApplicationName() : "";
    }

    /**
     * 获取dubbo接口
     *
     * @return
     */
    public String getApiInterface() {
        return (this.methodDefinition != null) ? this.methodDefinition.getApiConfig().getApiInterface() : "";
    }

    /**
     * 获取api状态
     *
     * @return
     */
    public Integer getApiStatus() {
        return (this.methodDefinition != null) ? this.methodDefinition.getStatus() : 1;
    }


    public Boolean getApiAsync() {
        return (this.methodDefinition != null) ? this.methodDefinition.getApiAsync() : Boolean.FALSE;
    }


    public Integer getFlowControlRuleType() {
        return (this.methodDefinition != null) ? this.methodDefinition.getFlowControlRuleType() : 1;
    }

    /**
     * 判断是否返回mock
     *
     * @return
     */
    public Boolean getMock() {
        return (this.methodDefinition != null) ? this.methodDefinition.getStatus() == 0 : Boolean.FALSE;
    }

    /**
     * 获取mock数据
     *
     * @return
     */
    public String getMockResponse() {
        return (this.methodDefinition != null) ? this.methodDefinition.getApiConfig().getMockResponse() : "";

    }


    public String getRequestParam(String name) {
        return this.request.getParameter(name);
    }

    /**
     * 获取GET请求所有参数
     *
     * @return
     */
    public Map<String, Object> getRequestParams() {

        Map<String, String[]> parameterMap = request.getParameterMap();
        Map<String, Object> param = new HashMap<>(parameterMap.size());
        parameterMap.keySet().forEach(key -> param.put(key, StringUtils.join(parameterMap.get(key))));
        return param;
    }

    /**
     * 获取header
     *
     * @param name
     * @return
     */
    public String getRequestHeader(String name) {
        return this.request.getHeader(name);
    }

    /**
     * 获取请求流
     *
     * @return
     * @throws IOException
     */
    public InputStream getInputStream() throws IOException {
        return this.request.getInputStream();
    }


    /**
     * 获取openId
     *
     * @return
     */
    public String getOpenId() {
        return (this.saasConfigInfo != null) ? this.saasConfigInfo.getOpenId() : "";
    }

    /**
     * 获取服务url
     *
     * @return
     */
    public String getUrl() {

        StringBuilder url = new StringBuilder();
        url.append(this.getRequestURI());
        if (StringUtils.isNotEmpty(this.getApiInterface())) {
            url.append("?interface=");
            url.append(this.getApiInterface());
        }
        if (StringUtils.isNotEmpty(this.getApiVersion())) {
            url.append("&version=");
            url.append(this.getApiVersion());
        }
        if (StringUtils.isNotEmpty(this.getApiGroup())) {
            url.append("&group=");
            url.append(this.getApiGroup());
        }
        return url.toString();
    }

    /**
     * 构建ServiceRestMetadata
     *
     * @return
     */
    public void buildServiceRestMetadata() {

        ServiceRestMetadata serviceRestMetadata = new ServiceRestMetadata();
        serviceRestMetadata.setUrl(this.getUrl());
        this.setServiceRestMetadata(serviceRestMetadata);
    }

    /**
     * 构建dubboTranslatedAttributes
     */
    public void buildDubboTranslatedAttributes() {

        this.getDubboTranslatedAttributes().put("protocol", "dubbo");
        this.getDubboTranslatedAttributes().put("cluster", "failover");
    }

    /**
     * 预构建RPC调用
     */
    public void preBuild() {
        this.buildServiceRestMetadata();
        this.buildDubboTranslatedAttributes();
    }

    /**
     * 获取dubbo泛化service
     *
     * @return
     */
    public GenericService getGenericService() {

        DubboRestServiceMetadata dubboRestServiceMetadata =
                new DubboRestServiceMetadata(this.getServiceRestMetadata(), new RestMethodMetadata());

        return this.serviceFactory.create(dubboRestServiceMetadata,
                this.getDubboTranslatedAttributes());
    }

    /**
     * 获取请求来源ip
     *
     * @return
     */
    public String getRemoteAddr() {
        String ip, remoteAddr = this.request.getRemoteAddr();
        String forwarded = this.request.getHeader("X-Forwarded-For");
        String realIp = this.request.getHeader("X-Real-IP");
        if (realIp == null) {
            if (forwarded == null) {
                ip = remoteAddr;
            } else {
                ip = remoteAddr + "/" + forwarded.split(",")[0];
            }
        } else if (realIp.equals(forwarded)) {
            ip = realIp;
        } else {
            if (forwarded != null) {
                forwarded = forwarded.split(",")[0];
            }
            ip = realIp + "/" + forwarded;
        }
        return ip;
    }

    public boolean isMultipart() {
        return (this.request.getContentType() != null && this.request.getContentType().contains("multipart/form-data"));
    }

    public boolean isBinaryStream() {
        return (this.request.getContentType() != null && this.request.getContentType().contains("application/octet-stream"));
    }

    public boolean isTextPlain() {
        return (this.request.getContentType() != null && this.request.getContentType().contains("text/plain"));
    }

}
