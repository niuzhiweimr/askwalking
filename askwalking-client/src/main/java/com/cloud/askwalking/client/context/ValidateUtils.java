package com.cloud.askwalking.client.context;

import com.cloud.askwalking.client.domain.R;
import com.cloud.askwalking.client.exception.ErrorCode;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.validator.HibernateValidator;
import org.springframework.util.CollectionUtils;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import javax.validation.groups.Default;
import java.util.Set;
import java.util.StringJoiner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author niuzhiwei
 */
@Slf4j
public class ValidateUtils {

    private static Validator validator;

    static {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }


    /**
     * 整数
     */
    private static final Pattern V_INTEGER =Pattern.compile("^-?[1-9]\\d*$") ;

    /**
     * 正整数
     */
    private static final Pattern V_Z_INTEGER = Pattern.compile("^[1-9]\\d*$");

    /**
     * 负整数
     */
    private static final Pattern V_F_INTEGER = Pattern.compile("^-[1-9]\\d*$");

    /**
     * 数字
     */
    private static final Pattern V_NUMBER = Pattern.compile("^([+-]?)\\d*\\.?\\d+$");

    /**
     * 正数
     */
    private static final Pattern V_Z_NUMBER = Pattern.compile("^[1-9]\\d*|0$");

    /**
     * 负数
     */
    private static final Pattern V_F_NUMBER = Pattern.compile("^-[1-9]\\d*|0$");

    /**
     * 浮点数
     */
    private static final Pattern V_FLOAT = Pattern.compile("^([+-]?)\\d*\\.\\d+$");

    /**
     * 正浮点数
     */
    private static final Pattern V_Z_FLOAT = Pattern.compile("^[1-9]\\d*.\\d*|0.\\d*[1-9]\\d*$");

    /**
     * 负浮点数
     */
    private static final Pattern V_F_FLOAT = Pattern.compile("^-([1-9]\\d*.\\d*|0.\\d*[1-9]\\d*)$");

    /**
     * 非负浮点数（正浮点数 + 0）
     */
    private static final Pattern V_UN_F_FLOAT = Pattern.compile("^[1-9]\\d*.\\d*|0.\\d*[1-9]\\d*|0?.0+|0$");

    /**
     * 非正浮点数（负浮点数 + 0）
     */
    private static final Pattern V_UN_Z_FLOAT = Pattern.compile("^(-([1-9]\\d*.\\d*|0.\\d*[1-9]\\d*))|0?.0+|0$");

    /**
     * 邮件
     */
    private static final Pattern V_EMAIL = Pattern.compile("^\\w+((-\\w+)|(\\.\\w+))*\\@[A-Za-z0-9]+((\\.|-)[A-Za-z0-9]+)*\\.[A-Za-z0-9]+$");

    /**
     * 颜色
     */
    private static final Pattern V_COLOR = Pattern.compile("^[a-fA-F0-9]{6}$");

    /**
     * url
     */
    private static final Pattern V_URL = Pattern.compile("^http[s]?:\\/\\/([\\w-]+\\.)+[\\w-]+([\\w-./?%&=]*)?$");

    /**
     * 仅中文
     */
    private static final Pattern V_CHINESE = Pattern.compile("^[\\u4E00-\\u9FA5\\uF900-\\uFA2D]+$");
    /**
     * 仅中文和字母
     */
    private static final Pattern V_CHINESE_LETTER = Pattern.compile("^[A-Za-z\\u4E00-\\u9FA5\\uF900-\\uFA2D]+$");
    /**
     * 仅ACSII字符
     */
    private static final Pattern V_ASCII = Pattern.compile("^[\\x00-\\xFF]+$");

    /**
     * 邮编
     */
    private static final Pattern V_ZIPCODE = Pattern.compile("^\\d{6}$");

    /**
     * 手机
     */
    private static final Pattern V_MOBILE = Pattern.compile("^1([34578])\\d{9}$");

    /**
     * ip地址
     */
    private static final Pattern V_IP4 = Pattern.compile("^(25[0-5]|2[0-4]\\d|[0-1]\\d{2}|[1-9]?\\d)\\.(25[0-5]|2[0-4]\\d|[0-1]\\d{2}|[1-9]?\\d)\\.(25[0-5]|2[0-4]\\d|[0-1]\\d{2}|[1-9]?\\d)\\.(25[0-5]|2[0-4]\\d|[0-1]\\d{2}|[1-9]?\\d)$");

    /**
     * 非空
     */
    private static final Pattern V_NOTEMPTY = Pattern.compile("^\\S+$");

    /**
     * 图片
     */
    private static final Pattern V_PICTURE = Pattern.compile("(.*)\\.(jpg|bmp|gif|ico|pcx|jpeg|tif|png|raw|tga)$");

    /**
     * 压缩文件
     */
    private static final Pattern V_RAR = Pattern.compile("(.*)\\.(rar|zip|7zip|tgz)$");

    /**
     * 日期
     */
    private static final Pattern V_DATE = Pattern.compile("^((((1[6-9]|[2-9]\\d)\\d{2})-(0?[13578]|1[02])-(0?[1-9]|[12]\\d|3[01]))|(((1[6-9]|[2-9]\\d)\\d{2})-(0?[13456789]|1[012])-(0?[1-9]|[12]\\d|30))|(((1[6-9]|[2-9]\\d)\\d{2})-0?2-(0?[1-9]|1\\d|2[0-8]))|(((1[6-9]|[2-9]\\d)(0[48]|[2468][048]|[13579][26])|((16|[2468][048]|[3579][26])00))-0?2-29-)) (20|21|22|23|[0-1]?\\d):[0-5]?\\d:[0-5]?\\d$");

    /**
     * QQ号码
     */
    private static final Pattern V_QQ_NUMBER = Pattern.compile("^[1-9]*[1-9][0-9]*$");

    /**
     * 电话号码的函数(包括验证国内区号,国际区号,分机号)
     */
    private static final Pattern V_TEL = Pattern.compile("^(([0\\+]\\d{2,3}-)?(0\\d{2,3})-)?(\\d{7,8})(-(\\d{3,}))?$");

    /**
     * 用来用户注册。匹配由数字、26个英文字母或者下划线组成的字符串
     */
    private static final Pattern V_USERNAME = Pattern.compile("^\\w+$");

    /**
     * 字母
     */
    private static final Pattern V_LETTER = Pattern.compile("^[A-Za-z]+$");

    /**
     * 大写字母
     */
    private static final Pattern V_LETTER_UPPER = Pattern.compile("^[A-Z]+$");

    /**
     * 小写字母
     */
    private static final Pattern V_LETTER_LOWER = Pattern.compile("^[a-z]+$");

    /**
     * 身份证，弱校验
     */
    private static final Pattern V_IDCARD = Pattern.compile("^(\\d{15}$|^\\d{18}$|^\\d{17}(\\d|X|x))$");

    /**
     * 验证密码(数字和英文同时存在)
     */
    private static final Pattern V_PASSWORD_REG = Pattern.compile("[A-Za-z]+[0-9]");

    /**
     * 验证密码长度(4-16位)，可以使用字母、数字或特殊字符
     */
    private static final Pattern V_PASSWORD_LENGTH = Pattern.compile("^([A-Z]|[a-z]|[0-9]|[`~!@#$%^&*()+=|{}':;',\\\\\\\\[\\\\\\\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]){4,16}$");

    /**
     * 验证两位数
     */
    private static final Pattern V_TWO＿POINT = Pattern.compile("^[0-9]+(.[0-9]{2})?$");

    /**
     * 验证一个月的31天
     */
    private static final Pattern V_31DAYS = Pattern.compile("^((0?[1-9])|(([12])[0-9])|30|31)$");


    private ValidateUtils() {
    }


    /**
     * 验证是不是整数
     *
     * @param value 要验证的字符串 要验证的字符串
     * @return 如果是符合格式的字符串, 返回 <b>true </b>,否则为 <b>false </b>
     */
    public static boolean integer(String value) {
        return match(V_INTEGER, value);
    }

    /**
     * 验证是不是正整数
     *
     * @param value 要验证的字符串
     * @return 如果是符合格式的字符串, 返回 <b>true </b>,否则为 <b>false </b>
     */
    public static boolean zInteger(String value) {
        return match(V_Z_INTEGER, value);
    }

    /**
     * 验证是不是负整数
     *
     * @param value 要验证的字符串
     * @return 如果是符合格式的字符串, 返回 <b>true </b>,否则为 <b>false </b>
     */
    public static boolean fInteger(String value) {
        return match(V_F_INTEGER, value);
    }

    /**
     * 验证是不是数字
     *
     * @param value 要验证的字符串
     * @return 如果是符合格式的字符串, 返回 <b>true </b>,否则为 <b>false </b>
     */
    public static boolean number(String value) {
        return match(V_NUMBER, value);
    }

    /**
     * 验证是不是正数
     *
     * @param value 要验证的字符串
     * @return 如果是符合格式的字符串, 返回 <b>true </b>,否则为 <b>false </b>
     */
    public static boolean zNumber(String value) {
        return match(V_Z_NUMBER, value);
    }

    /**
     * 验证是不是负数
     *
     * @param value 要验证的字符串
     * @return 如果是符合格式的字符串, 返回 <b>true </b>,否则为 <b>false </b>
     */
    public static boolean fNumber(String value) {
        return match(V_F_NUMBER, value);
    }

    /**
     * 验证一个月的31天
     *
     * @param value 要验证的字符串
     * @return 如果是符合格式的字符串, 返回 <b>true </b>,否则为 <b>false </b>
     */
    public static boolean is31Days(String value) {
        return match(V_31DAYS, value);
    }

    /**
     * 验证是不是ASCII
     *
     * @param value 要验证的字符串
     * @return 如果是符合格式的字符串, 返回 <b>true </b>,否则为 <b>false </b>
     */
    public static boolean isASCII(String value) {
        return match(V_ASCII, value);
    }


    /**
     * 验证是不是中文
     *
     * @param value 要验证的字符串
     * @return 如果是符合格式的字符串, 返回 <b>true </b>,否则为 <b>false </b>
     */
    public static boolean isChinese(String value) {
        return match(V_CHINESE, value);
    }

    /**
     * 验证是不是中文或字符
     *
     * @param value 要验证的字符串
     * @return 如果是符合格式的字符串, 返回 <b>true </b>,否则为 <b>false </b>
     */
    public static boolean isChineseOrLetter(String value) {
        return match(V_CHINESE_LETTER, value);
    }


    /**
     * 验证是不是颜色
     *
     * @param value 要验证的字符串
     * @return 如果是符合格式的字符串, 返回 <b>true </b>,否则为 <b>false </b>
     */
    public static boolean isColor(String value) {
        return match(V_COLOR, value);
    }


    /**
     * 验证是不是日期
     *
     * @param value 要验证的字符串
     * @return 如果是符合格式的字符串, 返回 <b>true </b>,否则为 <b>false </b>
     */
    public static boolean isDate(String value) {
        return match(V_DATE, value);
    }

    /**
     * 验证是不是邮箱地址
     *
     * @param value 要验证的字符串
     * @return 如果是符合格式的字符串, 返回 <b>true </b>,否则为 <b>false </b>
     */
    public static boolean isEmail(String value) {
        return match(V_EMAIL, value);
    }

    /**
     * 验证是不是浮点数
     *
     * @param value 要验证的字符串
     * @return 如果是符合格式的字符串, 返回 <b>true </b>,否则为 <b>false </b>
     */
    public static boolean isFloat(String value) {
        return match(V_FLOAT, value);
    }

    /**
     * 验证是不是正确的身份证号码
     *
     * @param value 要验证的字符串
     * @return 如果是符合格式的字符串, 返回 <b>true </b>,否则为 <b>false </b>
     */
    public static boolean isIDcard(String value) {
        return match(V_IDCARD, value);
    }

    /**
     * 验证是不是正确的IP地址
     *
     * @param value 要验证的字符串
     * @return 如果是符合格式的字符串, 返回 <b>true </b>,否则为 <b>false </b>
     */
    public static boolean isIP4(String value) {
        return match(V_IP4, value);
    }

    /**
     * 验证是不是字母
     *
     * @param value 要验证的字符串
     * @return 如果是符合格式的字符串, 返回 <b>true </b>,否则为 <b>false </b>
     */
    public static boolean isLetter(String value) {
        return match(V_LETTER, value);
    }

    /**
     * 验证是不是小写字母
     *
     * @param value 要验证的字符串
     * @return 如果是符合格式灵域的字符串, 返回 <b>true </b>,否则为 <b>false </b>
     */
    public static boolean isLetterLower(String value) {
        return match(V_LETTER_LOWER, value);
    }


    /**
     * 验证是不是大写字母
     *
     * @param value 要验证的字符串
     * @return 如果是符合格式的字符串, 返回 <b>true </b>,否则为 <b>false </b>
     */
    public static boolean isLetterUpper(String value) {
        return match(V_LETTER_UPPER, value);
    }


    /**
     * 验证是不是手机号码
     *
     * @param value 要验证的字符串
     * @return 如果是符合格式的字符串, 返回 <b>true </b>,否则为 <b>false </b>
     */
    public static boolean isMobile(String value) {
        return match(V_MOBILE, value);
    }

    /**
     * 验证是不是负浮点数
     *
     * @param value 要验证的字符串
     * @return 如果是符合格式的字符串, 返回 <b>true </b>,否则为 <b>false </b>
     */
    public static boolean fFloat(String value) {
        return match(V_F_FLOAT, value);
    }

    /**
     * 验证非空
     *
     * @param value 要验证的字符串
     * @return 如果是符合格式的字符串, 返回 <b>true </b>,否则为 <b>false </b>
     */
    public static boolean notEmpty(String value) {
        return match(V_NOTEMPTY, value);
    }

    /**
     * 验证密码的长度(6~18位)
     *
     * @param value 要验证的字符串
     * @return 如果是符合格式的字符串, 返回 <b>true </b>,否则为 <b>false </b>
     */
    public static boolean pwdLength6To18(String value) {
        return match(V_PASSWORD_LENGTH, value);
    }

    /**
     * 验证密码(数字和英文同时存在)
     *
     * @param value 要验证的字符串
     * @return 如果是符合格式的字符串, 返回 <b>true </b>,否则为 <b>false </b>
     */
    public static boolean pwdNumChar(String value) {
        return match(V_PASSWORD_REG, value);
    }

    /**
     * 验证图片
     *
     * @param value 要验证的字符串
     * @return 如果是符合格式的字符串, 返回 <b>true </b>,否则为 <b>false </b>
     */
    public static boolean isPicture(String value) {
        return match(V_PICTURE, value);
    }

    /**
     * 验证正浮点数
     *
     * @param value 要验证的字符串
     * @return 如果是符合格式的字符串, 返回 <b>true </b>,否则为 <b>false </b>
     */
    public static boolean zFloat(String value) {
        return match(V_Z_FLOAT, value);
    }

    /**
     * 验证QQ号码
     *
     * @param value 要验证的字符串
     * @return 如果是符合格式的字符串, 返回 <b>true </b>,否则为 <b>false </b>
     */
    public static boolean isQQnumber(String value) {
        return match(V_QQ_NUMBER, value);
    }

    /**
     * 验证压缩文件
     *
     * @param value 要验证的字符串
     * @return 如果是符合格式的字符串, 返回 <b>true </b>,否则为 <b>false </b>
     */
    public static boolean isRar(String value) {
        return match(V_RAR, value);
    }

    /**
     * 验证电话
     *
     * @param value 要验证的字符串
     * @return 如果是符合格式的字符串, 返回 <b>true </b>,否则为 <b>false </b>
     */
    public static boolean isTel(String value) {
        return match(V_TEL, value);
    }

    /**
     * 验证两位小数
     *
     * @param value 要验证的字符串
     * @return 如果是符合格式的字符串, 返回 <b>true </b>,否则为 <b>false </b>
     */
    public static boolean isTwoPoint(String value) {
        return match(V_TWO＿POINT, value);
    }

    /**
     * 验证非正浮点数
     *
     * @param value 要验证的字符串
     * @return 如果是符合格式的字符串, 返回 <b>true </b>,否则为 <b>false </b>
     */
    public static boolean UnZFloat(String value) {
        return match(V_UN_Z_FLOAT, value);
    }

    /**
     * 验证非负浮点数
     *
     * @param value 要验证的字符串
     * @return 如果是符合格式的字符串, 返回 <b>true </b>,否则为 <b>false </b>
     */
    public static boolean UnFFloat(String value) {
        return match(V_UN_F_FLOAT, value);
    }

    /**
     * 验证URL
     *
     * @param value 要验证的字符串
     * @return 如果是符合格式的字符串, 返回 <b>true </b>,否则为 <b>false </b>
     */
    public static boolean isUrl(String value) {
        return match(V_URL, value);
    }

    /**
     * 验证用户注册。匹配由数字、26个英文字母或者下划线组成的字符串
     *
     * @param value 要验证的字符串
     * @return 如果是符合格式的字符串, 返回 <b>true </b>,否则为 <b>false </b>
     */
    public static boolean isUserName(String value) {
        return match(V_USERNAME, value);
    }

    /**
     * 验证邮编
     *
     * @param value 要验证的字符串
     * @return 如果是符合格式的字符串, 返回 <b>true </b>,否则为 <b>false </b>
     */
    public static boolean isZipCode(String value) {
        return match(V_ZIPCODE, value);
    }


    /**
     * @param pattern 正则表达式字符串
     * @param str   要匹配的字符串
     * @return 如果str 符合 regex的正则表达式格式,返回true, 否则返回 false;
     */
    private static boolean match(Pattern pattern, String str) {
        Matcher matcher = pattern.matcher(str);
        return matcher.matches();
    }


    /**
     * 校验遇到第一个不合法的字段直接返回不合法字段，后续字段不再校验
     *
     * @param <T>
     * @param obj
     * @return
     * @throws Exception
     */
    public static <T> R validateFast(T obj) {
        Validator validator = Validation.byProvider(HibernateValidator.class)
                .configure().failFast(true).buildValidatorFactory().getValidator();
        Set<ConstraintViolation<T>> validateResult = validator.validate(obj);
        if (validateResult.size() > 0) {
            return buildValidationResult(validateResult);
        }
        return R.success();
    }

    /**
     * 校验实体，返回实体所有属性的校验结果
     *
     * @param obj
     * @param <T>
     * @return
     */
    public static <T> R validateResult(T obj) {
        Set<ConstraintViolation<T>> validateSet = validator.validate(obj, Default.class);
        if (validateSet.size() > 0) {
            return buildValidationResult(validateSet);
        }
        return R.success();
    }

    /**
     * 将异常结果封装返回
     *
     * @param validateSet
     * @param <T>
     * @return
     */
    private static <T> R buildValidationResult(Set<ConstraintViolation<T>> validateSet) {
        StringJoiner errorMsg = new StringJoiner("|");
        if (!CollectionUtils.isEmpty(validateSet)) {
            for (ConstraintViolation<T> constraintViolation : validateSet) {
                StringJoiner message = new StringJoiner(":");
                message.add(constraintViolation.getPropertyPath().toString());
                message.add(constraintViolation.getMessage());
                errorMsg.add(message.toString());
            }
        }
        return R.fail(ErrorCode.PARAM_ERROR.getErrorCode(), errorMsg.toString());
    }

}
