package com.cloud.askwalking.common.utils;

import java.security.MessageDigest;

import static org.apache.commons.lang3.StringUtils.isEmpty;

/**
 * @author 牛志伟
 */
public class MD5Helper {


    public static String getDefaultMd5String(String source) throws Exception {
        return md5String(source, "UTF-8", "");
    }

    public static String getDefaultMd5String(String source, String md5Key) throws Exception {
        return md5String(source, "UTF-8", md5Key);
    }

    public static String md5String(String source, String encoder, String mid5key) throws Exception {
        StringBuilder hexString = new StringBuilder();
        if (!isEmpty(source)) {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update((source + mid5key).getBytes(encoder));
            byte[] hash = md.digest();
            for (int i = 0; i < hash.length; i++) {
                if ((0xff & hash[i]) < 0x10) {
                    hexString.append("0" + Integer.toHexString((0xFF & hash[i])));
                } else {
                    hexString.append(Integer.toHexString(0xFF & hash[i]));
                }
            }
        }
        return hexString.toString();
    }


}
