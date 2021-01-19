package com.port.websocket.util;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.lang3.StringUtils;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

/**
 * API signature, signature specification:
 * <p>
 * http://docs.aws.amazon.com/zh_cn/general/latest/gr/signature-version-2.html
 */
@Slf4j
public class SignatureUtil {

    /**
     * Verify that the signature is valid
     *
     * @param apiKey
     * @param apiSecret
     * @param timestamp
     * @param param
     * @param actualSignature
     * @return
     */
    public static boolean validate(String apiKey, String apiSecret, String timestamp, Map<String, String> param, String actualSignature) {
        if (StringUtils.isBlank(apiKey) || StringUtils.isBlank(apiSecret) || StringUtils.isBlank(timestamp) || StringUtils.isBlank(actualSignature)) {
            return false;
        }

        String expectSignature = createSignature(apiKey, apiSecret, timestamp, param);
        return StringUtils.equalsIgnoreCase(actualSignature, expectSignature);
    }

    /**
     * Create a valid signature. This method is called by the client and will add the AccessKeyId, Timestamp,
     * SignatureVersion, SignatureMethod, Signature parameters to the passed param.
     *
     * @param apiKey    ApiKeyId.
     * @param apiSecret ApiSecretKey.
     * @param param     The original request parameters are stored in Key-Value. Note that Value is not encoded.
     */
    public static String createSignature(String apiKey, String apiSecret, String timestamp, Map<String, String> param) {

        StringBuilder sb = new StringBuilder(1024);

        SortedMap<String, String> map = new TreeMap<>();
        if (param != null) {
            map.putAll(param);
        }

        map.put(ApiConstants.ACCESS_KEY_ID, apiKey);
        map.put(ApiConstants.SIGNATURE_VERSION, ApiConstants.SIGNATURE_VERSION_VALUE);
        map.put(ApiConstants.SIGNATURE_METHOD, ApiConstants.HMAC_SHA256);
        map.put(ApiConstants.HEADER_TIMESTAMP, timestamp);

        // build signature body:
        for (Map.Entry<String, String> entry : map.entrySet()) {
            sb.append(entry.getKey()).append('=');
            if (entry.getValue() != null) {
                sb.append(urlEncode(entry.getValue()));
            }
            sb.append('&');
        }
        // remove last '&':
        sb.deleteCharAt(sb.length() - 1);

        // sign:
        Mac hmacSha256;
        try {
            hmacSha256 = Mac.getInstance(ApiConstants.HMAC_SHA256);
            SecretKeySpec secKey = new SecretKeySpec(apiSecret.getBytes(StandardCharsets.UTF_8), ApiConstants.HMAC_SHA256);
            hmacSha256.init(secKey);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("No such algorithm: " + e.getMessage());
        } catch (InvalidKeyException e) {
            throw new RuntimeException("Invalid key: " + e.getMessage());
        }

        String payload = sb.toString();
        byte[] hash = hmacSha256.doFinal(payload.getBytes(StandardCharsets.UTF_8));
        return new String(Hex.encodeHex(hash));
    }

    /**
     * Encode using standard URL Encode. Note that unlike the JDK default, spaces are encoded as %20 instead of +.
     *
     * @param s String
     * @return URL encoded string
     */
    public static String urlEncode(String s) {
        try {
            return URLEncoder.encode(s, ApiConstants.UTF_8).replaceAll("\\+", "%20");
        } catch (UnsupportedEncodingException e) {
            throw new IllegalArgumentException("UTF-8 encoding not supported!");
        }
    }
}
