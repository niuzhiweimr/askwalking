package com.cloud.askwalking.admin.service.impl;

import com.cloud.askwalking.admin.dto.AddConfigureApiDTO;
import com.cloud.askwalking.admin.dto.ModifyConfigureApiDTO;
import com.cloud.askwalking.admin.dto.QueryConfigureApiDTO;
import com.cloud.askwalking.admin.service.GatewayApiService;
import com.cloud.askwalking.admin.vo.GatewayApiVO;
import com.cloud.askwalking.common.constants.GatewayConstant;
import com.cloud.askwalking.common.domain.R;
import com.cloud.askwalking.common.enums.GatewayErrorCode;
import com.cloud.askwalking.common.exception.ErrorCode;
import com.cloud.askwalking.common.utils.IDUtil;
import com.cloud.askwalking.common.utils.ValidateUtils;
import com.cloud.askwalking.core.HelperService;
import com.cloud.askwalking.repository.dao.ConfigureApiMapper;
import com.cloud.askwalking.repository.model.ConfigureApiDO;
import com.cloud.askwalking.repository.model.ConfigureApiExample;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @author niuzhiwei
 */
@Slf4j
@Service("gatewayApiService")
public class GatewayApiServiceImpl implements GatewayApiService {

    @Resource
    private ConfigureApiMapper configureApiMapper;

    @Reference
    private HelperService helperService;

    @Override
    public List<ConfigureApiDO> getAll() {

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
    public R<List<GatewayApiVO>> listApi(QueryConfigureApiDTO request) {

        List<ConfigureApiDO> configureApiDOS;

        try {
            ConfigureApiExample configureApiExample = new ConfigureApiExample();
            ConfigureApiExample.Criteria criteria = configureApiExample.createCriteria();
            if (!StringUtils.isEmpty(request.getApplicationName())) {
                criteria.andApplicationNameEqualTo(request.getApplicationName());
            }
            if (!StringUtils.isEmpty(request.getRequestUri())) {
                criteria.andRequestUriEqualTo(request.getRequestUri());
            }
            if (!StringUtils.isEmpty(request.getApiType())) {
                criteria.andApiTypeEqualTo(request.getApiType());
            }
            if (Objects.nonNull(request.getStatus())) {
                criteria.andStatusEqualTo(request.getStatus());
            }
            configureApiDOS = configureApiMapper.selectByExample(configureApiExample);
        } catch (Exception e) {
            log.error("[GatewayApiServiceImpl] Get API list exception：", e);
            return R.fail(ErrorCode.SYSTEM_ERROR);
        }

        List<GatewayApiVO> result = new ArrayList<>();

        configureApiDOS.forEach(configureApiDO -> {
            GatewayApiVO gatewayApiVO = new GatewayApiVO();
            BeanUtils.copyProperties(configureApiDO, gatewayApiVO);
            result.add(gatewayApiVO);
        });

        return R.success(result);
    }

    @Override
    public R<Boolean> addApi(AddConfigureApiDTO request) {

        R validateResult = ValidateUtils.validateResult(request);
        if (validateResult.failed()) {
            return validateResult;
        }

        if (!StringUtils.isEmpty(request.getFlowControlRuleType())
                && request.getFlowControlRuleType().equals(GatewayConstant.FLOW_CONTROL_SYSTEM)) {
            return R.fail(GatewayErrorCode.ADD_API_PARAMS_ERROR.getErrorCode()
                    , "系统限流目前不支持,请修改[flowControlRuleType]参数");
        }

        ConfigureApiExample configureApiExample = new ConfigureApiExample();
        configureApiExample.createCriteria()
                .andRequestUriEqualTo(request.getRequestUri())
                .andDeleteFlagEqualTo(Boolean.FALSE);
        List<ConfigureApiDO> configureApiDOS = configureApiMapper.selectByExample(configureApiExample);
        if (!CollectionUtils.isEmpty(configureApiDOS)) {
            return R.fail(GatewayErrorCode.ADD_API_PARAMS_ERROR.getErrorCode(), "添加API,[requestUri]已经存在请更换");
        }

        ConfigureApiDO configureApiDO = new ConfigureApiDO();
        BeanUtils.copyProperties(request, configureApiDO);
        configureApiDO.setId(IDUtil.generatePK());

        try {
            configureApiMapper.insertSelective(configureApiDO);
        } catch (Exception e) {
            log.error("Add API operation database exception：", e);
            return R.fail(GatewayErrorCode.ADD_API_DB_ERROR);
        }

        //调用api添加钩子
        helperService.addMethodDefinitionHook(request.getRequestUri());

        return R.success(Boolean.TRUE);
    }

    @Override
    public R<Boolean> updateApi(ModifyConfigureApiDTO request) {

        R validateResult = ValidateUtils.validateResult(request);
        if (validateResult.failed()) {
            return validateResult;
        }

        if (!StringUtils.isEmpty(request.getFlowControlRuleType())
                && request.getFlowControlRuleType().equals(GatewayConstant.FLOW_CONTROL_SYSTEM)) {
            return R.fail(GatewayErrorCode.ADD_API_PARAMS_ERROR.getErrorCode()
                    , "系统限流目前不支持,请修改[flowControlRuleType]参数");
        }


        ConfigureApiDO configureApiDO = configureApiMapper.selectByPrimaryKey(request.getId());
        BeanUtils.copyProperties(request, configureApiDO);
        try {
            int update = configureApiMapper.updateByPrimaryKeySelective(configureApiDO);
            if (update <= 0) {
                return R.fail(GatewayErrorCode.ADD_API_PARAMS_ERROR.getErrorCode()
                        , "修改API不存在，请检查[id]参数是否正确");
            }
        } catch (Exception e) {
            log.error("Modify API operation database exception：", e);
            return R.fail(GatewayErrorCode.UPDATE_API_DB_ERROR);
        }

        //调用api更新钩子
        helperService.updateMethodDefinitionHook(configureApiDO.getRequestUri());

        return R.success(Boolean.TRUE);
    }

    @Override
    public R<Boolean> offlineApi(String request) {

        if (StringUtils.isEmpty(request)) {
            return R.fail(GatewayErrorCode.ADD_API_PARAMS_ERROR.getErrorCode(), "下线API,[id]空");
        }

        ConfigureApiDO configureApiDO;
        try {
            configureApiDO = configureApiMapper.selectByPrimaryKey(request);
        } catch (Exception e) {
            log.error("Offline API, query database exception：", e);
            return R.fail(GatewayErrorCode.ADD_API_DB_ERROR);
        }

        if (configureApiDO == null) {
            return R.fail(GatewayErrorCode.OFFLINE_API_NON_EXISTENT);
        }

        //更新API为待发布
        configureApiDO.setStatus(1);

        try {
            configureApiMapper.updateByPrimaryKeySelective(configureApiDO);
        } catch (Exception e) {
            log.error("Offline API, database update exception：", e);
            return R.fail(GatewayErrorCode.ADD_API_DB_ERROR);
        }

        //调用元数据修改钩子
        helperService.updateMetadataMethodDefinitionHook(configureApiDO.getRequestUri());

        return R.success(Boolean.TRUE);
    }

    @Override
    public R<Boolean> onlineApi(String request) {

        if (StringUtils.isEmpty(request)) {
            return R.fail(GatewayErrorCode.ADD_API_PARAMS_ERROR.getErrorCode(), "上线API,[id]空");
        }

        ConfigureApiDO configureApiDO;
        try {
            configureApiDO = configureApiMapper.selectByPrimaryKey(request);
        } catch (Exception e) {
            log.error("online API, query database exception：", e);
            return R.fail(GatewayErrorCode.ADD_API_DB_ERROR);
        }

        if (configureApiDO == null) {
            return R.fail(GatewayErrorCode.ONLINE_API_NON_EXISTENT);
        }

        //更新API为已发布
        configureApiDO.setStatus(2);

        try {
            configureApiMapper.updateByPrimaryKeySelective(configureApiDO);
        } catch (Exception e) {
            log.error("online API, database update exception：", e);
            return R.fail(GatewayErrorCode.ADD_API_DB_ERROR);
        }

        //调用元数据修改钩子
        helperService.updateMetadataMethodDefinitionHook(configureApiDO.getRequestUri());

        return R.success(Boolean.TRUE);
    }

    @Override
    public R<Boolean> mock(String request) {

        if (StringUtils.isEmpty(request)) {
            return R.fail(GatewayErrorCode.ADD_API_PARAMS_ERROR.getErrorCode(), "mock,[id]空");
        }

        ConfigureApiDO configureApiDO;
        try {
            configureApiDO = configureApiMapper.selectByPrimaryKey(request);
        } catch (Exception e) {
            log.error("mock,query database exception：", e);
            return R.fail(GatewayErrorCode.ADD_API_DB_ERROR);
        }

        if (configureApiDO == null) {
            return R.fail(GatewayErrorCode.MOCK_API_NON_EXISTENT);
        }

        //更新API为mock
        configureApiDO.setStatus(0);

        try {
            configureApiMapper.updateByPrimaryKeySelective(configureApiDO);
        } catch (Exception e) {
            log.error("mock,database update exception：", e);
            return R.fail(GatewayErrorCode.ADD_API_DB_ERROR);
        }

        //调用元数据修改钩子
        helperService.updateMetadataMethodDefinitionHook(configureApiDO.getRequestUri());

        return R.success(Boolean.TRUE);
    }
}
