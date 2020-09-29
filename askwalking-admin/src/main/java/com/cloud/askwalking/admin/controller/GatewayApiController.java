package com.cloud.askwalking.admin.controller;

import com.alibaba.fastjson.JSONObject;
import com.cloud.askwalking.admin.dto.AddConfigureApiDTO;
import com.cloud.askwalking.admin.dto.ModifyConfigureApiDTO;
import com.cloud.askwalking.admin.dto.QueryConfigureApiDTO;
import com.cloud.askwalking.admin.service.GatewayApiService;
import com.cloud.askwalking.admin.vo.GatewayApiVO;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import com.cloud.askwalking.common.domain.R;
import com.cloud.askwalking.common.exception.ErrorCode;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * api管理控制层
 *
 * @author niuzhiwei
 */
@Api(tags = "API配置管理")
@Slf4j
@RestController
public class GatewayApiController {

    @Autowired
    private GatewayApiService gatewayApiService;

    @Autowired
    private HttpServletRequest httpServletRequest;

    @Value("${swagger.token:masget!@#!23}")
    private String token;

    @PostMapping("/listApi")
    @ApiOperationSupport(order = 1)
    @ApiOperation(value = "列表", notes = "列表", responseContainer = "List", response = R.class)
    public R<List<GatewayApiVO>> listApi(QueryConfigureApiDTO request) {

        String token = httpServletRequest.getHeader("token");
        if (!this.token.equals(token)) {
            return R.fail(ErrorCode.NOT_SUPPORT_OPERATOR);
        }
        log.info("listApi requestParams：{} ", JSONObject.toJSONString(request));
        return gatewayApiService.listApi(request);
    }

    @PostMapping("/addApi")
    @ApiOperationSupport(order = 2)
    @ApiOperation(value = "添加", notes = "添加", responseContainer = "Object", response = R.class)
    public R<Boolean> addApi(AddConfigureApiDTO request) {

        String token = httpServletRequest.getHeader("token");
        if (!this.token.equals(token)) {
            return R.fail(ErrorCode.NOT_SUPPORT_OPERATOR);
        }
        log.info("addApi requestParams：{} ", JSONObject.toJSONString(request));
        return gatewayApiService.addApi(request);
    }

    @PostMapping("/updateApi")
    @ApiOperationSupport(order = 3)
    @ApiOperation(value = "修改", notes = "修改", responseContainer = "Object", response = R.class)
    public R<Boolean> updateApi(ModifyConfigureApiDTO request) {

        String token = httpServletRequest.getHeader("token");
        if (!this.token.equals(token)) {
            return R.fail(ErrorCode.NOT_SUPPORT_OPERATOR);
        }
        log.info("updateApi requestParams：{} ", JSONObject.toJSONString(request));
        return gatewayApiService.updateApi(request);
    }

    @PostMapping("/offlineApi")
    @ApiOperationSupport(order = 5)
    @ApiOperation(value = "下线", notes = "下线", responseContainer = "Object", response = R.class)
    public R<Boolean> offlineApi(String id) {

        String token = httpServletRequest.getHeader("token");
        if (!this.token.equals(token)) {
            return R.fail(ErrorCode.NOT_SUPPORT_OPERATOR);
        }
        log.info("offlineApi requestParams：{} ", id);
        return gatewayApiService.offlineApi(id);
    }

    @PostMapping("/onlineApi")
    @ApiOperationSupport(order = 4)
    @ApiOperation(value = "上线", notes = "上线", responseContainer = "Object", response = R.class)
    public R<Boolean> onlineApi(String id) {

        String token = httpServletRequest.getHeader("token");
        if (!this.token.equals(token)) {
            return R.fail(ErrorCode.NOT_SUPPORT_OPERATOR);
        }
        log.info("onlineApi requestParams：{} ", id);
        return gatewayApiService.onlineApi(id);
    }


    @PostMapping("/mock")
    @ApiOperationSupport(order = 6)
    @ApiOperation(value = "mock", notes = "mock", responseContainer = "Object", response = R.class)
    public R<Boolean> mock(String id) {

        String token = httpServletRequest.getHeader("token");
        if (!this.token.equals(token)) {
            return R.fail(ErrorCode.NOT_SUPPORT_OPERATOR);
        }
        log.info("mockApi requestParams：{} ", id);
        return gatewayApiService.mock(id);
    }
}

