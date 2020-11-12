package com.cloud.askwalking.common.tool;

import java.util.UUID;

/**
 * @author niuzhiwei
 */
public class IDTool {

    public static String generatePK() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }

}