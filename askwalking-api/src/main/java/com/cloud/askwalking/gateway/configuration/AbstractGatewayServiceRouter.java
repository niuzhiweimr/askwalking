package com.cloud.askwalking.gateway.configuration;

import com.cloud.askwalking.core.domain.GatewayMethodDefinition;
import org.springframework.context.ApplicationContext;

import java.util.List;

/**
 * @author niuzhiwei
 */
public abstract class AbstractGatewayServiceRouter {

    /**
     * 获取方法定义
     *
     * @param uri
     * @return
     */
    public abstract GatewayMethodDefinition getMethodDefinition(String uri);

    /**
     * 保存方法定义
     *
     * @param methodDefinition
     */
    public abstract void putMethodDefinition(GatewayMethodDefinition methodDefinition);

    /**
     * 删除方法定义
     *
     * @param uri
     */
    public abstract void delMethodDefinition(String uri);

    /**
     * 获取所有uri
     *
     * @return
     */
    public abstract List<String> getRequestUris();

    /**
     * 设置spring上下文
     *
     * @param applicationContext
     */
    public abstract void setApplicationContext(ApplicationContext applicationContext);

}
