package com.cloud.askwalking.core.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

/**
 * @author niuzhiwei
 */
@Getter
@Setter
@ToString
public class UseAuthInfo implements Serializable {

    private String userId;

    private String tenantId;


}
