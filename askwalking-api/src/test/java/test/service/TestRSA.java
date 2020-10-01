package test.service;

import com.cloud.askwalking.common.tool.Base64Tool;
import com.cloud.askwalking.common.tool.RSATool;
import com.cloud.askwalking.gateway.pipline.CommonParamParse;

public class TestRSA {


    public static void main(String[] args) throws Exception {
        // 外部加解密公私钥
        String outerPrivateKey = "MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBAMNMJCykbBQ3PamSqCYSkJgaM1m2OuwGZC4N3ahhECMV02pyVzHcOu+G1CgyZu5UnlnaSiGSFN94hGDckQSmOxmBbSRancqMlsVu2TCmga6Ud3TX8l+A/oR04CnucZghYW+xeqggcvHl2Zas98qPZMao7r0EMu0nCooZ0zet9J9HAgMBAAECgYAgu1o0wTn8CWYkTZgxCVuCoqGIn6owdMHFlj1YQyxZNQdCB/flO2wFSSu1sPzfZP9FP/i8t6cX7TMCqPUFOncc/CYxcMPcrdJaIJ26G6mk/OvC70bdbtG0am6xwR0yfxifXTmvH6ISUXNtgjn5eErHtPN7KKWKS/ajZF1k2tkrwQJBAO1qV49DEus/Isq3+JXBb8HtOg6XToNMxEVhxyzRsIBWLauXtYlT9LmFzbSoinRXnZPVMYs4vuGLASzgF3nNWu0CQQDSlcf4ifMRy8v26xWKC17s/7LKnnPddUO5lTjPEty2+QV51fsi7tauhibPfQgd74O0cVyTn66z20jm9wNMQHiDAkEArR8HXDsv14UBbMs8JI+aW9INrMgeg1X+JQ3IvGDnkJ3GzcJhqRMkeAge2SaxV2bwS/WBHCEz56mCIOQDmljpfQJAApzDD00+y7C4hgaijMTZV51QbX0LuOqUfX/hZYg5Xre2Hq3N4MAPv7iAUMCfUie/fYSOfnJTq32D6QGVc+O02wJBAMplo5X+IE3gKwMM+AcyanM5cWZfycDS4ZLop3Er6GAoEU+pY9ZrzKZsAt2B7x9NoXzke2g5IYybAtk5tN5heZY=";
        String outerPublicKey = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDDTCQspGwUNz2pkqgmEpCYGjNZtjrsBmQuDd2oYRAjFdNqclcx3DrvhtQoMmbuVJ5Z2kohkhTfeIRg3JEEpjsZgW0kWp3KjJbFbtkwpoGulHd01/JfgP6EdOAp7nGYIWFvsXqoIHLx5dmWrPfKj2TGqO69BDLtJwqKGdM3rfSfRwIDAQAB";
        // 内部公钥，给外部的
        String innerPublicKey = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDDX44wPId6Sknix7/lgzH59/z00eEQkti7tiKuzkQdxerYIpAO7KrnkZr7NBWPc++NvvKrxQZ1y5XSGXvnh4KiqOtPxhSOM/ylXMxShgCAZzGiGs1QW7o9Er9AtnpvTkX3cn713YOWx796ooUmXSRd3+8XhbSO0NeEuEI1VKsQ3wIDAQAB";
        //时间戳
        long timestamp = System.currentTimeMillis();
        System.out.println("timdstamp:" + timestamp);
        //商户openId
        String openId = "masget20200628";
        //使用私钥加密请求数据并对加密数据使用base64编码
        String param = "{\"highwayCode\":\"JIANGSU\",\"orderId\":\"NO123456789\"}";
        String signStr = RSATool.encryptByPrivateKey(param, outerPrivateKey);
        String encode = Base64Tool.encode(signStr.getBytes());
        System.out.println("编码密文 encode: " + encode);
        //生成数据签名
        String confusionParam = CommonParamParse.handleSaasParamConfusion(openId, String.valueOf(timestamp), signStr);
        System.out.println("confusionParam :" + confusionParam);
        String sign = RSATool.sign(confusionParam, String.valueOf(timestamp), outerPrivateKey);
        System.out.println("数字签名 sign: " + sign);

        //公钥验签
        boolean verify = RSATool.verify(confusionParam, String.valueOf(timestamp), outerPublicKey, sign);
        System.out.println("验证签名 verify:" + verify);

    }

}
