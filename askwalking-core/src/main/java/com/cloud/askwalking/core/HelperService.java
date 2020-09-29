package com.cloud.askwalking.core;

/**
 * @author niuzhiwei
 */
public interface HelperService {

    /**
     * 添加方法定义钩子
     *
     * @param uri
     */
    void addMethodDefinitionHook(String uri);

    /**
     * 修改方法定义钩子
     *
     * @param uri
     */
    void updateMethodDefinitionHook(String uri);

    /**
     * 修改方法原数据钩子
     *
     * @param uri
     */
    void updateMetadataMethodDefinitionHook(String uri);

}
