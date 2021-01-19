package com.port.websocket.util;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okio.Buffer;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * A request interceptor that injects the API Key Header into requests, and signs messages, whenever required.
 */
public class AuthenticationInterceptor implements Interceptor {

    private final String apiKey;

    private final String secret;

    public AuthenticationInterceptor(String apiKey, String secret) {
        this.apiKey = apiKey;
        this.secret = secret;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request original = chain.request();
        Request.Builder newRequestBuilder = original.newBuilder();

        boolean isApiKeyRequired = original.header(ApiConstants.HEADER_API_KEY) != null;

        newRequestBuilder.removeHeader(ApiConstants.HEADER_API_KEY) //
                .removeHeader(ApiConstants.HEADER_TIMESTAMP).removeHeader(ApiConstants.HEADER_SIGNATURE);

        // Endpoint requires sending a valid API-KEY
        if (isApiKeyRequired) {
            String timestampStr = String.valueOf(System.currentTimeMillis());

            String signature;
            if ("GET".equalsIgnoreCase(original.method())) {
                Map<String, String> param = payloadToMap(original.url().query());
                signature = SignatureUtil.createSignature(apiKey, secret, timestampStr, param);
                System.out.println("apiKey==="+apiKey);
                System.out.println("secret==="+secret);
                System.out.println("timestampStr==="+timestampStr);
                System.out.println("param==="+param);
                System.out.println("signature==="+signature);
            } else {
                Map<String, String> param = payloadToMap(bodyToString(original.body()));
                signature = SignatureUtil.createSignature(apiKey, secret, timestampStr, param);
                System.out.println("apiKey==="+apiKey);
                System.out.println("secret==="+secret);
                System.out.println("timestampStr==="+timestampStr);
                System.out.println("param==="+param);
                System.out.println("signature==="+signature);
            }

            newRequestBuilder.addHeader(ApiConstants.HEADER_API_KEY, apiKey);
            newRequestBuilder.addHeader(ApiConstants.HEADER_TIMESTAMP, timestampStr);
            newRequestBuilder.addHeader(ApiConstants.HEADER_SIGNATURE, signature);
        }

        // Build new request after adding the necessary authentication information
        Request newRequest = newRequestBuilder.build();
        return chain.proceed(newRequest);
    }

    private static Map<String, String> payloadToMap(String payload) {
        if (payload != null && payload.trim().length() > 0) {
            return Arrays.stream(StringUtils.split(payload, "&")).map(s -> StringUtils.split(s, "=")).filter(arr -> arr != null && arr.length == 2).collect(Collectors.toMap(o -> o[0], o -> o[1]));
        }
        return new HashMap<>();
    }

    private static String bodyToString(RequestBody request) {
        try (final Buffer buffer = new Buffer()) {
            final RequestBody copy = request;
            if (copy != null) {
                copy.writeTo(buffer);
            } else {
                return null;
            }

            return buffer.readUtf8();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        final AuthenticationInterceptor that = (AuthenticationInterceptor) o;
        return Objects.equals(apiKey, that.apiKey) && Objects.equals(secret, that.secret);
    }

    @Override
    public int hashCode() {
        return Objects.hash(apiKey, secret);
    }
}