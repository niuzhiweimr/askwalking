package com.cloud.askwalking.core.domain;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author niuzhiwei
 */
@Getter
@Setter
@EqualsAndHashCode
@ToString
public class FlowControlRule {

    private double qps;

    private long avgRt;

    private long maxThread;

}
