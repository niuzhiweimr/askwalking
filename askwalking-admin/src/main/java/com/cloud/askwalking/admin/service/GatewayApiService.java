package com.cloud.askwalking.admin.service;

import com.cloud.askwalking.admin.dto.AddConfigureApiDTO;
import com.cloud.askwalking.admin.dto.ModifyConfigureApiDTO;
import com.cloud.askwalking.admin.dto.QueryConfigureApiDTO;
import com.cloud.askwalking.repository.model.ConfigureApiDO;
import com.cloud.askwalking.admin.vo.GatewayApiVO;
import com.cloud.askwalking.common.domain.R;

import java.util.List;

/**
 * @author niuzhiwei
 */
public interface GatewayApiService {

    /**
     * 获取所有接口配置
     *
     * @return
     */
    List<ConfigureApiDO> getAll();

    /**
     * 获取api列表
     *
     * @param request
     * @return
     */
    R<List<GatewayApiVO>> listApi(QueryConfigureApiDTO request);


    /**
     * 添加api
     *
     * @param request
     * @return
     */
    R<Boolean> addApi(AddConfigureApiDTO request);


    /**
     * 添加api
     *
     * @param request
     * @return
     */
    R<Boolean> updateApi(ModifyConfigureApiDTO request);


    /**
     * 下线api
     *
     * @param request 传入api配置数据ID
     * @return
     */
    R<Boolean> offlineApi(String request);

    /**
     * 上线api
     *
     * @param request 传入api配置数据ID
     * @return
     */
    R<Boolean> onlineApi(String request);


    /**
     * mock
     *
     * @param request 传入api配置数据ID
     * @return
     */
    R<Boolean> mock(String request);
}
