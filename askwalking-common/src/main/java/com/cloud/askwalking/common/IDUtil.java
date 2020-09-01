package com.cloud.askwalking.common;

import java.util.UUID;

/**
 * @author niuzhiwei
 */
public class IDUtil {
    /**
     * 功能描述: <br>
     * 〈生成uuid〉
     *
     * @Param:
     * @Return:
     * @Author: luwan
     * @Date: 2020/6/29 16:10
     */
    public static String generatePK() {

        return UUID.randomUUID().toString().replaceAll("-", "");
    }

}