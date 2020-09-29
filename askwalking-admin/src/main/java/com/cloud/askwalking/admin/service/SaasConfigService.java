package com.cloud.askwalking.admin.service;

import com.cloud.askwalking.admin.dto.AddSaasConfigDTO;
import com.cloud.askwalking.admin.dto.AddSaasResourceDTO;
import com.cloud.askwalking.admin.dto.ModifySaasResourceDTO;
import com.cloud.askwalking.admin.dto.QuerySaasConfigDTO;
import com.cloud.askwalking.repository.model.SaasConfigDO;
import com.cloud.askwalking.repository.model.SaasResourceDO;
import com.cloud.askwalking.admin.vo.SaasConfigVO;
import com.cloud.askwalking.common.domain.R;

import java.util.List;

/**
 * saas平台配置
 *
 * @author niuzhiwei
 */
public interface SaasConfigService {

    /**
     * 商户信息列表
     *
     * @param request
     * @return
     */
    R<List<SaasConfigVO>> listSaasInfo(QuerySaasConfigDTO request);

    /**
     * 通过openId查询saas平台配置
     *
     * @param openId
     * @return
     */
    SaasConfigDO getSaasByOpenId(String openId);

    /**
     * 添加商户
     *
     * @param request
     * @return
     */
    R addSaasConfig(AddSaasConfigDTO request);


    /**
     * 下线商户
     *
     * @param request 传入api配置数据ID
     * @return
     */
    R<Boolean> offlineSaas(String request);

    /**
     * 上线商户
     *
     * @param request 传入api配置数据ID
     * @return
     */
    R<Boolean> onlineSaas(String request);

    /**
     * 获取saas资源
     *
     * @param openId     商户id
     * @param requestUri 资源uri
     * @return
     */
    SaasResourceDO getSaasResource(String openId, String requestUri);

    /**
     * 添加saas商户资源
     *
     * @param request
     * @return
     */
    R addSaasResource(AddSaasResourceDTO request);

    /**
     * 删除商户资源
     *
     * @param request
     * @return
     */
    R delSaasResource(ModifySaasResourceDTO request);
}
