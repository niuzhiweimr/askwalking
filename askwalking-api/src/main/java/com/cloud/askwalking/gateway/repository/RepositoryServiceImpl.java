package com.cloud.askwalking.gateway.repository;

import com.cloud.askwalking.repository.dao.ConfigureApiMapper;
import com.cloud.askwalking.repository.dao.SaasConfigMapper;
import com.cloud.askwalking.repository.dao.SaasResourceMapper;
import com.cloud.askwalking.repository.model.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * @author niuzhiwei
 */
@Slf4j
@Service
public class RepositoryServiceImpl implements RepositoryService {

    @Resource
    private ConfigureApiMapper configureApiMapper;

    @Resource
    private SaasConfigMapper saasConfigMapper;

    @Resource
    private SaasResourceMapper saasResourceMapper;

    @Override
    public List<ConfigureApiDO> getApiAll() {

        List<ConfigureApiDO> configureApiDOS;
        try {
            ConfigureApiExample configureApiExample = new ConfigureApiExample();
            configureApiExample.createCriteria().andDeleteFlagEqualTo(Boolean.FALSE);
            configureApiDOS = configureApiMapper.selectByExample(configureApiExample);
        } catch (Exception e) {
            log.error("[GatewayApiServiceImpl] Query API configuration exception：", e);
            throw new RuntimeException(e);
        }

        if (CollectionUtils.isEmpty(configureApiDOS)) {
            return new ArrayList<>(0);
        }

        return configureApiDOS;
    }

    @Override
    public ConfigureApiDO getApiByUri(String uri) {

        List<ConfigureApiDO> configureApiDOS;
        try {
            ConfigureApiExample configureApiExample = new ConfigureApiExample();
            configureApiExample.createCriteria()
                    .andRequestUriEqualTo(uri)
                    .andDeleteFlagEqualTo(Boolean.FALSE);
            configureApiDOS = configureApiMapper.selectByExample(configureApiExample);
        } catch (Exception e) {
            log.error("[GatewayApiServiceImpl] Query API configuration exception：", e);
            throw new RuntimeException(e);
        }

        if (CollectionUtils.isEmpty(configureApiDOS)) {
            return null;
        }

        return configureApiDOS.get(0);
    }


    @Override
    public SaasConfigDO getSaasByOpenId(String openId) {

        if (StringUtils.isEmpty(openId)) {
            return null;
        }

        SaasConfigExample saasConfigExample = new SaasConfigExample();
        saasConfigExample.createCriteria().andOpenIdEqualTo(openId)
                .andStatusEqualTo((byte) 1)
                .andDeleteFlagEqualTo(Boolean.FALSE);
        List<SaasConfigDO> saasConfigDOList = saasConfigMapper.selectByExample(saasConfigExample);
        if (CollectionUtils.isEmpty(saasConfigDOList)) {
            return null;
        }

        return saasConfigDOList.get(0);
    }


    @Override
    public SaasResourceDO getSaasResource(String openId, String requestUri) {

        if (StringUtils.isEmpty(openId) || StringUtils.isEmpty(requestUri)) {
            return null;
        }

        SaasResourceExample saasResourceExample = new SaasResourceExample();
        saasResourceExample.createCriteria()
                .andOpenIdEqualTo(openId)
                .andRequestUriEqualTo(requestUri)
                .andStatusEqualTo((byte) 1)
                .andDeleteFlagEqualTo(Boolean.FALSE);
        List<SaasResourceDO> saasResourceDOList = saasResourceMapper.selectByExample(saasResourceExample);
        if (CollectionUtils.isEmpty(saasResourceDOList)) {
            return null;
        }

        return saasResourceDOList.get(0);
    }
}
