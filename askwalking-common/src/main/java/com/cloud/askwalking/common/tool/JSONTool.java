package com.cloud.askwalking.common.tool;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;

/**
 * @author niuzhiwei
 */
@Slf4j
public class JSONTool {

    private final static ObjectMapper mapper;

    static {
        mapper = new ObjectMapper();
        //忽略 在json字符串中存在，但是在java对象中不存在对应属性的情况。防止错误
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        //对象所有字段全部列入
        mapper.setSerializationInclusion(JsonInclude.Include.ALWAYS);
        // 忽略空bean转json错误
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        //设置可用单引号
        mapper.configure(JsonParser.Feature.ALLOW_SINGLE_QUOTES, true);
        //设置字段可以不用双引号包括
        mapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES, true);
    }

    /**
     * json转对象
     *
     * @param jsonStr   json串
     * @param classType 对象类型
     * @return 对象
     */
    public static <T> T toObject(String jsonStr, Class<T> classType) {

        if (StringUtils.isEmpty(jsonStr)) {
            throw new RuntimeException("Json string is empty");
        }

        try {
            return mapper.readValue(jsonStr, classType);
        } catch (IOException e) {
            log.error("json to entity error.", e);
            throw new RuntimeException(e);
        }
    }

    /**
     * 对象转json
     *
     * @param obj 对象
     * @return json串
     */
    public static String toJson(Object obj) {
        if (obj instanceof String) {
            return (String) obj;
        }
        try {
            return mapper.writeValueAsString(obj);
        } catch (IOException e) {
            log.error("obj to json error.", e);
            throw new RuntimeException(e);
        }
    }

    /**
     * 对象转json(格式化的json)
     *
     * @param obj 对象
     * @return 格式化的json串
     */
    public static String toJsonWithFormat(Object obj) {
        if (obj == null) {
            throw new RuntimeException("object is empty");
        }

        if (obj instanceof String) {
            return (String) obj;
        }

        try {
            return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(obj);
        } catch (IOException e) {
            log.error("obj to json error.", e);
            throw new RuntimeException(e);
        }
    }
}
