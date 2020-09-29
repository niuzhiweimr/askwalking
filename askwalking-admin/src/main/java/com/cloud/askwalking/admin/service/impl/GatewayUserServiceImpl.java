package com.cloud.askwalking.admin.service.impl;

import com.cloud.askwalking.common.utils.MD5Helper;
import com.cloud.askwalking.common.utils.ValidateUtils;
import com.cloud.askwalking.common.domain.R;
import com.cloud.askwalking.common.exception.ErrorCode;
import com.cloud.askwalking.repository.dao.UserMapper;
import com.cloud.askwalking.admin.dto.LoginDTO;
import com.cloud.askwalking.common.enums.GatewayErrorCode;
import com.cloud.askwalking.repository.model.UserDO;
import com.cloud.askwalking.repository.model.UserExample;
import com.cloud.askwalking.admin.service.GatewayUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.List;


/**
 * @author niuzhiwei
 */
@Slf4j
@Service("GatewayUserService")
public class GatewayUserServiceImpl implements GatewayUserService {

    @Resource
    private UserMapper userMapper;


    @Override
    public R<Boolean> login(LoginDTO request) {

        R validateResult = ValidateUtils.validateResult(request);
        if (validateResult.failed()) {
            return validateResult;
        }

        UserExample userExample = new UserExample();
        userExample.createCriteria().andUserNameEqualTo(request.getUserName());
        List<UserDO> userDOS = userMapper.selectByExample(userExample);
        if (CollectionUtils.isEmpty(userDOS)) {
            return R.fail(GatewayErrorCode.USER_NON);
        }

        String password;
        try {
            password = MD5Helper.getDefaultMd5String(request.getPassword());
        } catch (Exception e) {
            log.error("[GatewayUserService] MD5加密异常：", e);
            return R.fail(ErrorCode.SYSTEM_ERROR);
        }

        if (!userDOS.get(0).getPassword().equals(password)) {
            return R.fail(GatewayErrorCode.PASSWORD_ERROR);
        }

        return R.success(Boolean.TRUE);
    }
}
