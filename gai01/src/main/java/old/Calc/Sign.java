package old.Calc;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.Hex;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

public class Sign {
    public static void main(String[] args) {
//        System.out.println(urlEncode(sign("AccessKeyId%3D65341b08-7adc-46a0-bd9b-906c23755e13%26SignatureMethod%3DHmacSHA256%26SignatureVersion%3D2%26Timestamp%3D1611113700559%26streams%3DSPOT%40ORDER%3ABTC_USDT", "0A1A00957981761B1E6038A5CD9854E2")));
//        System.out.println(urlEncode(createSignature("AccessKeyId%3D65341b08-7adc-46a0-bd9b-906c23755e13%26SignatureMethod%3DHmacSHA256%26SignatureVersion%3D2%26Timestamp%3D1611113700559%26streams%3DSPOT%40ORDER%3ABTC_USDT", "0A1A00957981761B1E6038A5CD9854E2")));
        System.out.println(createSignature("AccessKeyId=65341b08-7adc-46a0-bd9b-906c23755e13&SignatureMethod=HmacSHA256&SignatureVersion=2&Timestamp=1611113700559&streams=SPOT%40ORDER%3ABTC_USDT", "0A1A00957981761B1E6038A5CD9854E2"));


        //样例:
        String apiKey = "65341b08-7adc-46a0-bd9b-906c23755e13";
        String apiSecret = "0A1A00957981761B1E6038A5CD9854E2";
        String timestamp = "1611113700559";
        Map<String,String> paramMap = new HashMap();
        StringBuilder sb = new StringBuilder(1024);
        SortedMap<String, String> map = new TreeMap<>();
        map.putAll(paramMap);
        map.put("AccessKeyId", apiKey);
        map.put("SignatureVersion", "2");
        map.put("SignatureMethod", "HmacSHA256");
        map.put("Timestamp", timestamp);
        map.put("streams","SPOT@ORDER:BTC_USDT");

// build signature body:
        for (Map.Entry<String, String> entry : map.entrySet()) {
            sb.append(entry.getKey()).append('=');
            if (entry.getValue() != null) {
                //此处参数值做URLEncode.encode()处理
                sb.append(urlEncode(entry.getValue()));
            }
            sb.append('&');
        }
// remove last '&':
        sb.deleteCharAt(sb.length() - 1);
//        System.out.println("sb="+sb);

        Mac hmacSha256;
        try {
            hmacSha256 = Mac.getInstance("HmacSHA256");
            SecretKeySpec secKey = new SecretKeySpec(apiSecret.getBytes(StandardCharsets.UTF_8),"HmacSHA256");
            hmacSha256.init(secKey);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("No such algorithm: " + e.getMessage());
        } catch (InvalidKeyException e) {
            throw new RuntimeException("Invalid key: " + e.getMessage());
        }
        String payload = sb.toString();
        System.out.println("payload="+payload);
        byte[] hash = hmacSha256.doFinal(payload.getBytes(StandardCharsets.UTF_8));

        for (byte b: hash
        ) {
            System.out.print(b);
        }
        System.out.println();
        System.out.println(new String(Hex.encodeHex(hash)));
    }

    public static String sign(String content, String appkey) {

        String result = null;
        try {
            Mac hmacSha256 = Mac.getInstance("HmacSHA256");
            byte[] keyBytes = appkey.getBytes("UTF-8");
            hmacSha256.init(new SecretKeySpec(keyBytes, 0, keyBytes.length, "HmacSHA256"));

            byte[] hmacSha256Bytes = hmacSha256.doFinal(content.getBytes("UTF-8"));
            result = new String(Base64.encodeBase64(hmacSha256Bytes), "UTF-8");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public static String urlEncode(String s) {
        try {
            return URLEncoder.encode(s, "UTF-8").replaceAll("\\+", "%20");
        } catch (UnsupportedEncodingException e) {

            throw new IllegalArgumentException("UTF-8 encoding not supported!");
        }
    }


    public static String createSignature(String message, String apiSecret) {
        Mac hmacSha256;
        try {
            hmacSha256 = Mac.getInstance("HmacSHA256");
            SecretKeySpec secKey = new SecretKeySpec(apiSecret.getBytes(StandardCharsets.UTF_8), "HmacSHA256");
            hmacSha256.init(secKey);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("No such algorithm: " + e.getMessage());
        } catch (InvalidKeyException e) {
            throw new RuntimeException("Invalid key: " + e.getMessage());
        }

        String payload = message.toString();
        byte[] hash = hmacSha256.doFinal(payload.getBytes(StandardCharsets.UTF_8));
        return new String(Hex.encodeHex(hash));
    }
}
