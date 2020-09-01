package com.cloud.askwalking.core.constants;

/**
 * @author niuzhiwei
 */
public class GatewayConstant {

    /**
     * api类型 API:app/h5端使用 ADMIN:管理后台使用
     */
    public static final String API = "API";

    public static final String ADMIN = "ADMIN";

    public static final String SAAS = "SAAS";


    /**
     * 限流类型
     */
    public static final Integer FLOW_CONTROL_SYSTEM = 1;

    public static final Integer FLOW_CONTROL_API = 2;

    /**
     * http请求方式
     */
    public static final String HTTP_METHOD_GET = "GET";

    public static final String HTTP_METHOD_POST = "POST";

    /**
     * service路由模式
     */
    public static final String SERVICE_ROUTER_LOCAL = "local";

    public static final String SERVICE_ROUTER_REDIS = "redis";


}
