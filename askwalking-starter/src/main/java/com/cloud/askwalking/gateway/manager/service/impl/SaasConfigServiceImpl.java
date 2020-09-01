package com.cloud.askwalking.gateway.manager.service.impl;

import com.cloud.askwalking.common.IDUtil;
import com.cloud.askwalking.client.context.ValidateUtils;
import com.cloud.askwalking.client.domain.R;
import com.cloud.askwalking.core.constants.GatewayConstant;
import com.cloud.askwalking.gateway.manager.dao.SaasConfigMapper;
import com.cloud.askwalking.gateway.manager.dao.SaasResourceMapper;
import com.cloud.askwalking.gateway.manager.dto.*;
import com.cloud.askwalking.gateway.manager.enums.GatewayErrorCode;
import com.cloud.askwalking.gateway.manager.model.SaasConfigDO;
import com.cloud.askwalking.gateway.manager.model.SaasConfigExample;
import com.cloud.askwalking.gateway.manager.model.SaasResourceDO;
import com.cloud.askwalking.gateway.manager.model.SaasResourceExample;
import com.cloud.askwalking.gateway.manager.service.GatewayApiService;
import com.cloud.askwalking.gateway.manager.service.SaasConfigService;
import com.cloud.askwalking.gateway.manager.vo.GatewayApiVO;
import com.cloud.askwalking.gateway.manager.vo.SaasConfigVO;
import com.cloud.askwalking.gateway.manager.vo.SaasResourceVO;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @author niuzhiwei
 */
@Service
public class SaasConfigServiceImpl implements SaasConfigService {

    @Resource
    private SaasConfigMapper saasConfigMapper;

    @Resource
    private SaasResourceMapper saasResourceMapper;

    @Autowired
    private GatewayApiService gatewayApiService;

    @Override
    public R<List<SaasConfigVO>> listSaasInfo(QuerySaasConfigDTO request) {

        SaasConfigExample saasConfigExample = new SaasConfigExample();
        SaasConfigExample.Criteria criteria = saasConfigExample.createCriteria();
        if (Objects.nonNull(request) && StringUtils.isNotEmpty(request.getOpenId())) {
            criteria.andOpenIdEqualTo(request.getOpenId());
        }
        if (Objects.nonNull(request) && StringUtils.isNotEmpty(request.getStatus())) {
            criteria.andStatusEqualTo(Byte.parseByte(request.getStatus()));
        }

        List<SaasConfigDO> saasConfigDOS = saasConfigMapper.selectByExample(saasConfigExample);
        if (CollectionUtils.isEmpty(saasConfigDOS)) {
            return R.success();
        }

        List<SaasConfigVO> result = new ArrayList<>();

        saasConfigDOS.forEach(saasConfigDO -> {
            SaasConfigVO saasConfigVO = new SaasConfigVO();
            BeanUtils.copyProperties(saasConfigDO, saasConfigVO);

            SaasResourceExample saasResourceExample = new SaasResourceExample();
            saasResourceExample.createCriteria().andOpenIdEqualTo(saasConfigDO.getOpenId());
            List<SaasResourceDO> saasResourceDOS = saasResourceMapper.selectByExample(saasResourceExample);
            if (!CollectionUtils.isEmpty(saasResourceDOS)) {
                List<SaasResourceVO> saasResourceVOList = new ArrayList<>();
                saasResourceDOS.forEach(saasResourceDO -> {
                    SaasResourceVO saasResourceVO = new SaasResourceVO();
                    BeanUtils.copyProperties(saasResourceDO, saasResourceVO);
                    saasResourceVOList.add(saasResourceVO);
                });
                saasConfigVO.setSaasResourceVOList(saasResourceVOList);
            }

            result.add(saasConfigVO);
        });

        return R.success(result);
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
        List<SaasConfigDO> saasConfigDOS = saasConfigMapper.selectByExample(saasConfigExample);
        if (CollectionUtils.isEmpty(saasConfigDOS)) {
            return null;
        }

        return saasConfigDOS.get(0);
    }

    @Override
    public R addSaasConfig(AddSaasConfigDTO request) {

        R validateResult = ValidateUtils.validateResult(request);
        if (validateResult.failed()) {
            return validateResult;
        }

        SaasConfigDO saasByOpenId = getSaasByOpenId(request.getOpenId());
        if (Objects.nonNull(saasByOpenId)) {
            return R.fail(GatewayErrorCode.SAAS_OPEN_ID_CLASH);
        }

        SaasConfigDO saasConfigDO = new SaasConfigDO();
        BeanUtils.copyProperties(request, saasConfigDO);
        saasConfigDO.setId(IDUtil.generatePK());

        saasConfigMapper.insertSelective(saasConfigDO);

        return R.success();
    }

    @Override
    public R<Boolean> offlineSaas(String request) {

        if (StringUtils.isEmpty(request)) {
            return R.fail(GatewayErrorCode.SAAS_OPEN_ID_IS_NULL);
        }

        SaasConfigDO saasByOpenId = getSaasByOpenId(request);
        if (Objects.isNull(saasByOpenId)) {
            return R.fail(GatewayErrorCode.SAAS_RESOURCE_MERCHANT_IS_NULL);
        }

        saasByOpenId.setStatus((byte) 2);
        saasConfigMapper.updateByPrimaryKeySelective(saasByOpenId);

        return R.success();
    }

    @Override
    public R<Boolean> onlineSaas(String request) {

        if (StringUtils.isEmpty(request)) {
            return R.fail(GatewayErrorCode.SAAS_OPEN_ID_IS_NULL);
        }

        SaasConfigExample saasConfigExample = new SaasConfigExample();
        saasConfigExample.createCriteria().andOpenIdEqualTo(request)
                .andDeleteFlagEqualTo(Boolean.FALSE);
        List<SaasConfigDO> saasConfigDOS = saasConfigMapper.selectByExample(saasConfigExample);
        if (CollectionUtils.isEmpty(saasConfigDOS)) {
            return null;
        }
        if (Objects.isNull(saasConfigDOS.get(0))) {
            return R.fail(GatewayErrorCode.SAAS_RESOURCE_MERCHANT_IS_NULL);
        }

        saasConfigDOS.get(0).setStatus((byte) 1);
        saasConfigMapper.updateByPrimaryKeySelective(saasConfigDOS.get(0));

        return R.success();
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
        List<SaasResourceDO> saasResourceDOS = saasResourceMapper.selectByExample(saasResourceExample);
        if (CollectionUtils.isEmpty(saasResourceDOS)) {
            return null;
        }

        return saasResourceDOS.get(0);
    }

    @Override
    public R addSaasResource(AddSaasResourceDTO request) {

        R validateResult = ValidateUtils.validateResult(request);
        if (validateResult.failed()) {
            return validateResult;
        }

        SaasConfigDO saasByOpenId = getSaasByOpenId(request.getOpenId());
        if (Objects.isNull(saasByOpenId)) {
            return R.fail(GatewayErrorCode.SAAS_RESOURCE_MERCHANT_IS_NULL);
        }

        SaasResourceDO saasResource = getSaasResource(request.getOpenId(), request.getRequestUri());
        if (Objects.nonNull(saasResource)) {
            return R.fail(GatewayErrorCode.SAAS_RESOURCE_CLASH);
        }

        QueryConfigureApiDTO queryConfigureApiDTO = new QueryConfigureApiDTO();
        queryConfigureApiDTO.setRequestUri(request.getRequestUri());
        queryConfigureApiDTO.setApiType(GatewayConstant.SAAS);
        R<List<GatewayApiVO>> configureApiBaseResponse = gatewayApiService.listApi(queryConfigureApiDTO);
        List<GatewayApiVO> result = configureApiBaseResponse.getResult();
        if (CollectionUtils.isEmpty(result)) {
            return R.fail(GatewayErrorCode.SAAS_RESOURCE_API_IS_NULL);
        }

        SaasResourceDO saasResourceDO = new SaasResourceDO();
        BeanUtils.copyProperties(request, saasResourceDO);
        saasResourceDO.setId(IDUtil.generatePK());
        saasResourceMapper.insertSelective(saasResourceDO);

        return R.success();
    }

    @Override
    public R delSaasResource(ModifySaasResourceDTO request) {

        R validateResult = ValidateUtils.validateResult(request);
        if (validateResult.failed()) {
            return validateResult;
        }

        SaasResourceExample saasResourceExample = new SaasResourceExample();
        saasResourceExample.createCriteria()
                .andOpenIdEqualTo(request.getOpenId())
                .andRequestUriEqualTo(request.getRequestUri());

        saasResourceMapper.deleteByExample(saasResourceExample);

        return R.success();
    }
}
