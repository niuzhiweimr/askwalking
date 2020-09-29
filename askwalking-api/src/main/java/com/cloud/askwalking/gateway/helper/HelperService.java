package com.cloud.askwalking.gateway.helper;

import com.cloud.askwalking.repository.model.ConfigureApiDO;
import com.cloud.askwalking.repository.model.SaasConfigDO;
import com.cloud.askwalking.repository.model.SaasResourceDO;

import java.util.List;

/**
 * 1.api变更刷新相关
 * 2.api配置获取相关
 * 3.sass平台配置相关
 *
 * @author niuzhiwei
 */
public interface HelperService {

    /**
     * 获取所有API接口配置
     *
     * @return
     */
    List<ConfigureApiDO> getApiAll();

    /**
     * 通过uri获取API接口配置
     *
     * @param uri
     * @return
     */
    ConfigureApiDO getApiByUri(String uri);


    /**
     * 通过openId查询saas平台配置
     *
     * @param openId
     * @return
     */
    SaasConfigDO getSaasByOpenId(String openId);


    /**
     * 获取saas资源
     *
     * @param openId     商户id
     * @param requestUri 资源uri
     * @return
     */
    SaasResourceDO getSaasResource(String openId, String requestUri);

}
