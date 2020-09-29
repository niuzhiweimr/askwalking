package com.cloud.askwalking.admin.controller;

import com.alibaba.fastjson.JSONObject;
import com.cloud.askwalking.common.domain.R;
import com.cloud.askwalking.common.exception.ErrorCode;
import com.cloud.askwalking.common.utils.RSAUtils;
import com.cloud.askwalking.admin.dto.AddSaasConfigDTO;
import com.cloud.askwalking.admin.dto.AddSaasResourceDTO;
import com.cloud.askwalking.admin.dto.ModifySaasResourceDTO;
import com.cloud.askwalking.admin.dto.QuerySaasConfigDTO;
import com.cloud.askwalking.admin.service.SaasConfigService;
import com.cloud.askwalking.admin.vo.RsaGeneratorVO;
import com.cloud.askwalking.admin.vo.SaasConfigVO;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * @author niuzhiwei
 */
@Api(tags = "saas平台配置管理")
@Slf4j
@RestController
public class SaasConfigController {

    @Autowired
    private SaasConfigService saasConfigService;

    @Autowired
    private HttpServletRequest httpServletRequest;

    @Value("${swagger.token:masget!@#!23}")
    private String token;


    @PostMapping("/listSaasInfo")
    @ApiOperation(value = "商户信息列表", notes = "商户信息列表", responseContainer = "Object", response = R.class)
    public R<List<SaasConfigVO>> listSaasInfo(QuerySaasConfigDTO request) {

        String token = httpServletRequest.getHeader("token");
        if (!this.token.equals(token)) {
            return R.fail(ErrorCode.NOT_SUPPORT_OPERATOR);
        }
        log.info("listSaasInfo requestParams：{} ", JSONObject.toJSONString(request));
        return saasConfigService.listSaasInfo(request);
    }


    @PostMapping("/addSaas")
    @ApiOperation(value = "添加商户", notes = "添加商户", responseContainer = "Object", response = R.class)
    public R addSaas(AddSaasConfigDTO request) {

        String token = httpServletRequest.getHeader("token");
        if (!this.token.equals(token)) {
            return R.fail(ErrorCode.NOT_SUPPORT_OPERATOR);
        }
        log.info("addSaas requestParams：{} ", JSONObject.toJSONString(request));
        return saasConfigService.addSaasConfig(request);
    }


    @PostMapping("/offlineSass")
    @ApiOperation(value = "下线商户", notes = "下线商户", responseContainer = "Object", response = R.class)
    public R<Boolean> offlineSass(String openId) {

        String token = httpServletRequest.getHeader("token");
        if (!this.token.equals(token)) {
            return R.fail(ErrorCode.NOT_SUPPORT_OPERATOR);
        }
        log.info("offlineSass requestParams：{} ", openId);
        return saasConfigService.offlineSaas(openId);
    }

    @PostMapping("/onlineSass")
    @ApiOperation(value = "上线商户", notes = "上线商户", responseContainer = "Object", response = R.class)
    public R<Boolean> onlineSass(String openId) {

        String token = httpServletRequest.getHeader("token");
        if (!this.token.equals(token)) {
            return R.fail(ErrorCode.NOT_SUPPORT_OPERATOR);
        }
        log.info("onlineSass requestParams：{} ", openId);
        return saasConfigService.onlineSaas(openId);
    }


    @PostMapping("/addSaasResource")
    @ApiOperation(value = "添加商户资源", notes = "添加商户资源", responseContainer = "Object", response = R.class)
    public R addSaasResource(AddSaasResourceDTO request) {

        String token = httpServletRequest.getHeader("token");
        if (!this.token.equals(token)) {
            return R.fail(ErrorCode.NOT_SUPPORT_OPERATOR);
        }
        log.info("addSaasResource requestParams：{} ", JSONObject.toJSONString(request));
        return saasConfigService.addSaasResource(request);
    }


    @PostMapping("/delSaasResource")
    @ApiOperation(value = "删除商户资源", notes = "删除商户资源", responseContainer = "Object", response = R.class)
    public R delSaasResource(ModifySaasResourceDTO request) {

        String token = httpServletRequest.getHeader("token");
        if (!this.token.equals(token)) {
            return R.fail(ErrorCode.NOT_SUPPORT_OPERATOR);
        }
        log.info("delSaasResource requestParams：{} ", JSONObject.toJSONString(request));
        return saasConfigService.delSaasResource(request);
    }


    @PostMapping("/generatorRsa")
    @ApiOperationSupport(order = 1)
    @ApiOperation(value = "商户密钥对生成", notes = "商户密钥对生成", responseContainer = "Object", response = R.class)
    public R<RsaGeneratorVO> generatorRsa() {

        String token = httpServletRequest.getHeader("token");
        if (!this.token.equals(token)) {
            return R.fail(ErrorCode.NOT_SUPPORT_OPERATOR);
        }

        RsaGeneratorVO rsaGeneratorVO = new RsaGeneratorVO();
        try {
            Map<String, Object> innerKeyPair = RSAUtils.genKeyPair();
            rsaGeneratorVO.setInnerPrivateKey(RSAUtils.getPrivateKey(innerKeyPair));
            rsaGeneratorVO.setInnerPublicKey(RSAUtils.getPublicKey(innerKeyPair));
            Map<String, Object> outerKeyPair = RSAUtils.genKeyPair();
            rsaGeneratorVO.setOuterPrivateKey(RSAUtils.getPrivateKey(outerKeyPair));
            rsaGeneratorVO.setOuterPublicKey(RSAUtils.getPublicKey(outerKeyPair));
        } catch (Exception e) {
            log.error("[RsaGeneratorController] generator exception:", e);
            return R.fail();
        }

        return R.success(rsaGeneratorVO);
    }

}
