package com.cloud.askwalking.gateway.manager.service;

import com.cloud.askwalking.gateway.manager.dto.AddConfigureApiDTO;
import com.cloud.askwalking.gateway.manager.dto.ModifyConfigureApiDTO;
import com.cloud.askwalking.gateway.manager.dto.QueryConfigureApiDTO;
import com.cloud.askwalking.gateway.manager.model.ConfigureApiDO;
import com.cloud.askwalking.gateway.manager.vo.GatewayApiVO;
import com.cloud.askwalking.client.domain.R;

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
