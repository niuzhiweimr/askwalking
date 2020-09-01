package com.cloud.askwalking.core.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author niuzhiwei
 */
@Getter
@Setter
@ToString
public class ApiConfig {

    private String apiInterface;

    private String apiVersion;

    private String apiRequestClass;

    private String apiMethod;

    private String apiGroup;

    private String mockResponse;

    private FlowControlRule flowControlRule;


}
