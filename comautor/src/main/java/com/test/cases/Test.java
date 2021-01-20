package com.test.cases;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import org.apache.commons.codec.binary.Base64;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

public class Test {
    public static void main(String[] args) throws Exception {
        Long timestamp = System.currentTimeMillis();
        System.out.println(timestamp);
        String secret = "SEC7ea7d50e846f14fce683015e1b8a3162ef5b913f975d59a4be5ade8b784bf9b7";

        String stringToSign = timestamp + "\n" + secret;
        Mac mac = Mac.getInstance("HmacSHA256");
        mac.init(new SecretKeySpec(secret.getBytes("UTF-8"), "HmacSHA256"));
        byte[] signData = mac.doFinal(stringToSign.getBytes("UTF-8"));
        String sign = URLEncoder.encode(new String(Base64.encodeBase64(signData)),"UTF-8");
        System.out.println(sign);
    }

    public static Map<String,Object> get(String secret){
        Map<String,Object> map = new HashMap<String,Object>();
        Long timestamp = System.currentTimeMillis();
        map.put("timestamp",timestamp);

        String stringToSign = timestamp + "\n" + secret;
        Mac mac = null;
        try {
            mac = Mac.getInstance("HmacSHA256");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        try {
            mac.init(new SecretKeySpec(secret.getBytes("UTF-8"), "HmacSHA256"));
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        byte[] signData = new byte[0];
        try {
            signData = mac.doFinal(stringToSign.getBytes("UTF-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        String sign = null;
        try {
            sign = URLEncoder.encode(new String(Base64.encodeBase64(signData)),"UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        map.put("sign",sign);
        return map;
    }
}
