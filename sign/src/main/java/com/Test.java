package com;

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.codec.binary.Base64;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

public class Test {
    public static void main(String[] args) throws Exception {
        Long timestamp = System.currentTimeMillis();
//        System.out.println(timestamp);
//        String secret = "SEC7ea7d50e846f14fce683015e1b8a3162ef5b913f975d59a4be5ade8b784bf9b7";

        String secret = System.getProperty("secret");
        String stringToSign = timestamp + "\n" + secret;
        Mac mac = Mac.getInstance("HmacSHA256");
        mac.init(new SecretKeySpec(secret.getBytes("UTF-8"), "HmacSHA256"));
        byte[] signData = mac.doFinal(stringToSign.getBytes("UTF-8"));
        String sign = URLEncoder.encode(new String(Base64.encodeBase64(signData)),"UTF-8");
//        System.out.println(sign);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("timestamp",timestamp);
        jsonObject.put("sign",sign);
        System.out.println(jsonObject);//'{"sign":"HwOkJLU8noBAdUylpQwo1%2FdSjEZVKPIAPgKxv6WToPo%3D","timestamp":1611109331124}'
    }
}
