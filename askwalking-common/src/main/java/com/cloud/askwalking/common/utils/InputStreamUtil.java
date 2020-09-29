package com.cloud.askwalking.common.utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * @author niuzhiwei
 */
public class InputStreamUtil {

    /**
     * 读取应流
     *
     * @param inputStream
     * @return
     * @throws IOException
     */
    public static byte[] readInputStream(InputStream inputStream) throws IOException {
        byte[] buffer = new byte[1024];
        int len;
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        while ((len = inputStream.read(buffer)) != -1) {
            bos.write(buffer, 0, len);
        }
        bos.close();
        return bos.toByteArray();
    }
}
