package com.cloud.askwalking.admin.config;

import com.github.xiaoymin.knife4j.spring.annotations.EnableKnife4j;
import com.github.xiaoymin.knife4j.spring.model.MarkdownFiles;
import org.apache.logging.log4j.util.Strings;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import springfox.bean.validators.configuration.BeanValidatorPluginsConfiguration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.ParameterBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.service.Parameter;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.ArrayList;
import java.util.List;

/**
 * @author niuzhiwei
 */
@Configuration
@EnableSwagger2
@EnableKnife4j
@Import(BeanValidatorPluginsConfiguration.class)
public class SwaggerConfig {

    /**
     * 全局参数
     *
     * @return List<Parameter>
     */
    private List<Parameter> parameter() {
        List<Parameter> params = new ArrayList<>();
        params.add(new ParameterBuilder().name("token")
                .description("请求令牌")
                .modelRef(new ModelRef("string"))
                .parameterType("header")
                .required(false).build());
        return params;
    }

    @Bean(value = "gatewayApiManager")
    public Docket gatewayApiManager() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(gatewayApiInfo())
                .groupName("网关接口管理")
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.cloud.askwalking.gateway.manager.controller"))
                .paths(PathSelectors.any())
                .build().globalOperationParameters(parameter());
    }

    @Bean(initMethod = "init")
    public MarkdownFiles markdownFiles() {
        return new MarkdownFiles("classpath:markdown/*");
    }


    private ApiInfo gatewayApiInfo() {
        Contact contact = new Contact("", Strings.EMPTY, Strings.EMPTY);
        return new ApiInfoBuilder()
                .title("网关接口管理")
                .contact(contact)
                .description("服务统一访问入口")
                .version("1.0")
                .build();
    }

}
