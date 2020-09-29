# 网关saas平台接入指南
 
### 1.0版本

### 流程
`生成rsa秘钥对` --> `添加商户` --> `商户秘钥发送` --> `商户资源分配` --> `商户接入`

### 流程说明:
1.生成rsa秘钥对
调用网关`商户密钥对生成`接口生成密钥对`inner`开头内部公私钥和`outer`开头外部公私钥(注:保存好秘钥对，商户外泄后责任由`商户自己承担`)

2.添加商户
调用网关`添加商户`接口指定商户`openId`和`商户名称`，将`inner`开头内部公私钥配置传入和`outer`开头外部公钥配置传入，`status`为商户状态`1:准入/2:禁止`

3.给商户秘钥
钉钉或者邮件等方式将`outer`开头外部公私钥给予`商户`

4.商户资源分配
调用网关`添加商户资源`接口给商户分配资源，`openId`传入添加商户时分配的，`requestUri`商户所属的资源接口，`status`商户资源状态`1:准入/2:禁止`

5.商户接入
- 参考:`rsa签名示例代码`


### rsa签名示例代码：
1.java版本:
```java
 package com.trafficbu.etc.gateway.util;
 
 import javax.crypto.Cipher;
 import java.io.ByteArrayOutputStream;
 import java.security.*;
 import java.security.spec.PKCS8EncodedKeySpec;
 import java.security.spec.X509EncodedKeySpec;
 import Base64Utils;import org.apache.commons.codec.binary.Base64;
 
 /**
  * RSA公钥/私钥/签名工具包
  *
  * <p>
  * 字符串格式的密钥在未在特殊说明情况下都为BASE64编码格式<br/>
  * 由于非对称加密速度极其缓慢，一般文件不使用它来加密而是使用对称加密，<br/>
  * 非对称加密算法可以用来对对称加密的密钥加密，这样保证密钥的安全也就保证了数据的安全
  *
  * @author niuzhiwei
  */
 public class RSAUtils {
 
     /**
      * 加密算法RSA
      */
     private static final String KEY_ALGORITHM = "RSA";
 
     /**
      * 签名算法
      */
     private static final String SIGNATURE_ALGORITHM = "MD5withRSA";
  
     /**
      * RSA最大加密明文大小
      */
     private static final int MAX_ENCRYPT_BLOCK = 117;
 
     /**
      * RSA最大解密密文大小
      */
     private static final int MAX_DECRYPT_BLOCK = 128;
     
     /**
      * <p>
      * 用私钥对信息生成数字签名
      * </p>
      *
      * @param source     已加密数据字符串
      * @param timestamp  时间戳，毫秒
      * @param privateKey 私钥(BASE64编码)
      * @return
      * @throws Exception
      */
     public static String sign(String source, String timestamp, String privateKey) throws Exception {
         String temp = source + timestamp;
         return sign(temp.getBytes(), privateKey);
     }
 
     /**
      * <p>
      * 用私钥对信息生成数字签名
      * </p>
      *
      * @param data       已加密数据
      * @param privateKey 私钥(BASE64编码)
      * @return
      * @throws Exception
      */
     public static String sign(byte[] data, String privateKey) throws Exception {
         byte[] keyBytes = Base64Utils.decode(privateKey);
         PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(keyBytes);
         KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
         PrivateKey privateK = keyFactory.generatePrivate(pkcs8KeySpec);
         Signature signature = Signature.getInstance(SIGNATURE_ALGORITHM);
         signature.initSign(privateK);
         signature.update(data);
         return encode(signature.sign());
     }
     
     /**
      * <p>
      * 校验数字签名
      * </p>
      *
      * @param source    已加密数据字符串
      * @param timestamp 时间戳，毫秒
      * @param publicKey 公钥(BASE64编码)
      * @param sign      数字签名
      * @return
      * @throws Exception
      */
     public static boolean verify(String source, String timestamp, String publicKey, String sign)
             throws Exception {
         String temp = source + timestamp;
         return verify(temp.getBytes(), publicKey, sign);
     }
 
     /**
      * <p>
      * 校验数字签名
      * </p>
      *
      * @param data      已加密数据
      * @param publicKey 公钥(BASE64编码)
      * @param sign      数字签名
      * @return
      * @throws Exception
      */
     public static boolean verify(byte[] data, String publicKey, String sign)
             throws Exception {
         byte[] keyBytes = Base64Utils.decode(publicKey);
         X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keyBytes);
         KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
         PublicKey publicK = keyFactory.generatePublic(keySpec);
         Signature signature = Signature.getInstance(SIGNATURE_ALGORITHM);
         signature.initVerify(publicK);
         signature.update(data);
         return signature.verify(decode(sign));
     }
 
     /**
      * <p>
      * 私钥加密
      * </p>
      *
      * @param source     源数据字符串
      * @param privateKey 私钥(BASE64编码)
      * @return
      * @throws Exception
      */
     public static String encryptByPrivateKey(String source, String privateKey)
             throws Exception {
         byte[] bytes = encryptByPrivateKey(source.getBytes(), privateKey);
         return encode(bytes);
     }
 
     /**
      * <p>
      * 私钥加密
      * </p>
      *
      * @param data       源数据
      * @param privateKey 私钥(BASE64编码)
      * @return
      * @throws Exception
      */
     public static byte[] encryptByPrivateKey(byte[] data, String privateKey)
             throws Exception {
         byte[] keyBytes = decode(privateKey);
         PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(keyBytes);
         KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
         Key privateK = keyFactory.generatePrivate(pkcs8KeySpec);
         Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
         cipher.init(Cipher.ENCRYPT_MODE, privateK);
         int inputLen = data.length;
         ByteArrayOutputStream out = new ByteArrayOutputStream();
         int offSet = 0;
         byte[] cache;
         int i = 0;
         // 对数据分段加密  
         while (inputLen - offSet > 0) {
             if (inputLen - offSet > MAX_ENCRYPT_BLOCK) {
                 cache = cipher.doFinal(data, offSet, MAX_ENCRYPT_BLOCK);
             } else {
                 cache = cipher.doFinal(data, offSet, inputLen - offSet);
             }
             out.write(cache, 0, cache.length);
             i++;
             offSet = i * MAX_ENCRYPT_BLOCK;
         }
         byte[] encryptedData = out.toByteArray();
         out.close();
         return encryptedData;
     }
 
     /**
      * <p>
      * 公钥解密
      * </p>
      *
      * @param data      已加密数据
      * @param publicKey 公钥(BASE64编码)
      * @return
      * @throws Exception
      */
     public static String decryptByPublicKey(String data,
                                             String publicKey) throws Exception {
         byte[] bytes = decryptByPublicKey(decode(data), publicKey);
         return new String(bytes);
     }
 
     /**
      * <p>
      * 公钥解密
      * </p>
      *
      * @param encryptedData 已加密数据
      * @param publicKey     公钥(BASE64编码)
      * @return
      * @throws Exception
      */
     public static byte[] decryptByPublicKey(byte[] encryptedData,
                                             String publicKey) throws Exception {
         byte[] keyBytes = decode(publicKey);
         X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(keyBytes);
         KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
         Key publicK = keyFactory.generatePublic(x509KeySpec);
         Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
         cipher.init(Cipher.DECRYPT_MODE, publicK);
         int inputLen = encryptedData.length;
         ByteArrayOutputStream out = new ByteArrayOutputStream();
         int offSet = 0;
         byte[] cache;
         int i = 0;
         // 对数据分段解密
         while (inputLen - offSet > 0) {
             if (inputLen - offSet > MAX_DECRYPT_BLOCK) {
                 cache = cipher
                         .doFinal(encryptedData, offSet, MAX_DECRYPT_BLOCK);
             } else {
                 cache = cipher
                         .doFinal(encryptedData, offSet, inputLen - offSet);
             }
             out.write(cache, 0, cache.length);
             i++;
             offSet = i * MAX_DECRYPT_BLOCK;
         }
         byte[] decryptedData = out.toByteArray();
         out.close();
         return decryptedData;
     }
 
     /**
     * 处理saas平台参数混淆
     *
     * @param openId
     * @param timestamp
     * @param param
     * @throws Exception
     */
    public static String handleSaasParamConfusion(String openId, String timestamp, String param) throws Exception {

        StringJoiner confusionParam = new StringJoiner("&");
        confusionParam.add(openId);
        confusionParam.add(param);
        confusionParam.add(timestamp);
        confusionParam.add(param);

        return confusionParam.toString();
    }
    
    /**  
    * <p>  
    * 二进制数据编码为BASE64字符串  
    * </p>  
    *   
    * @param bytes  
    * @return  
    * @throws Exception  
    */  
    public static String encode(byte[] bytes) throws Exception {  
       return new String(Base64.encodeBase64(bytes));  
    }  
      
    /**  
    * <p>  
    * BASE64字符串解码为二进制数据  
    * </p>  
    *   
    * @param base64  
    * @return  
    * @throws Exception  
    */  
     public static byte[] decode(String base64) throws Exception {  
            return Base64.decodeBase64(base64.getBytes());
     }  
          

     public static void main(String[] args) throws Exception {
         // 外部加解密公私钥
         String outerPrivateKey = "MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBAMNMJCykbBQ3PamSqCYSkJgaM1m2OuwGZC4N3ahhECMV02pyVzHcOu+G1CgyZu5UnlnaSiGSFN94hGDckQSmOxmBbSRancqMlsVu2TCmga6Ud3TX8l+A/oR04CnucZghYW+xeqggcvHl2Zas98qPZMao7r0EMu0nCooZ0zet9J9HAgMBAAECgYAgu1o0wTn8CWYkTZgxCVuCoqGIn6owdMHFlj1YQyxZNQdCB/flO2wFSSu1sPzfZP9FP/i8t6cX7TMCqPUFOncc/CYxcMPcrdJaIJ26G6mk/OvC70bdbtG0am6xwR0yfxifXTmvH6ISUXNtgjn5eErHtPN7KKWKS/ajZF1k2tkrwQJBAO1qV49DEus/Isq3+JXBb8HtOg6XToNMxEVhxyzRsIBWLauXtYlT9LmFzbSoinRXnZPVMYs4vuGLASzgF3nNWu0CQQDSlcf4ifMRy8v26xWKC17s/7LKnnPddUO5lTjPEty2+QV51fsi7tauhibPfQgd74O0cVyTn66z20jm9wNMQHiDAkEArR8HXDsv14UBbMs8JI+aW9INrMgeg1X+JQ3IvGDnkJ3GzcJhqRMkeAge2SaxV2bwS/WBHCEz56mCIOQDmljpfQJAApzDD00+y7C4hgaijMTZV51QbX0LuOqUfX/hZYg5Xre2Hq3N4MAPv7iAUMCfUie/fYSOfnJTq32D6QGVc+O02wJBAMplo5X+IE3gKwMM+AcyanM5cWZfycDS4ZLop3Er6GAoEU+pY9ZrzKZsAt2B7x9NoXzke2g5IYybAtk5tN5heZY=";
         String outerPublicKey="MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDDTCQspGwUNz2pkqgmEpCYGjNZtjrsBmQuDd2oYRAjFdNqclcx3DrvhtQoMmbuVJ5Z2kohkhTfeIRg3JEEpjsZgW0kWp3KjJbFbtkwpoGulHd01/JfgP6EdOAp7nGYIWFvsXqoIHLx5dmWrPfKj2TGqO69BDLtJwqKGdM3rfSfRwIDAQAB";
         // 内部公钥，给外部的
         String innerPublicKey = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDDX44wPId6Sknix7/lgzH59/z00eEQkti7tiKuzkQdxerYIpAO7KrnkZr7NBWPc++NvvKrxQZ1y5XSGXvnh4KiqOtPxhSOM/ylXMxShgCAZzGiGs1QW7o9Er9AtnpvTkX3cn713YOWx796ooUmXSRd3+8XhbSO0NeEuEI1VKsQ3wIDAQAB";
        
         //时间戳
         long timestamp = System.currentTimeMillis();
         System.out.println("timdstamp:" + timestamp);
         
         //商户openId
         String openId = "masget20200628";
         
        //使用私钥加密请求数据并对加密数据使用base64编码
         String param = "{\"highwayCode\":\"JIANGSU\",\"orderId\":\"NO123456789\"}";
         String signStr = encryptByPrivateKey(param, outerPrivateKey);
         String encode = encode(signStr.getBytes());
         System.out.println("编码密文 encode: " + encode);         
         
         //生成数据签名
         String confusionParam = handleSaasParamConfusion(openId, String.valueOf(timestamp), signStr);
         System.out.println("confusionParam :" + confusionParam);
         String sign = sign(confusionParam, String.valueOf(timestamp), outerPrivateKey);
         System.out.println("数字签名 sign: " + sign);
 
         //公钥验签  
         boolean verify = verify(confusionParam, String.valueOf(timestamp), outerPublicKey, sign);
         System.out.println("验证签名 verify:"+verify);
 
     }
 }  
 
```
