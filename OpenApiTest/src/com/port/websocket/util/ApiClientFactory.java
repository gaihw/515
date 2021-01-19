package com.port.websocket.util;

/**
 * A factory for creating 58coin Api client objects.
 */
public class ApiClientFactory {

    private String apiKey;

    private String apiSecret;

    private ApiClientFactory(String apiKey, String apiSecret) {
        this.apiKey = apiKey;
        this.apiSecret = apiSecret;
    }

    public static ApiClientFactory newInstance(String apiKey, String apiSecret) {
    	System.out.println("第一步，传入apikey&&apisecret，返回ApiClientFactory对象...");
        return new ApiClientFactory(apiKey, apiSecret);
    }

    public static ApiClientFactory newInstance() {
        return new ApiClientFactory(null, null);
    }

//    public ApiRestClient newRestClient() {
//    	System.out.println("第二步，调用ApiClientFactory类中的newRestClient方法，并返回ApiSocketClientImpl对象...");
//        return new ApiRestClientImpl(apiKey, apiSecret);
//    }
//
//    public ApiAsyncRestClient newAsyncRestClient() {
//        return new ApiAsyncRestClientImpl(apiKey, apiSecret);
//    }

    public ApiSocketClient newWebSocketClient() {
        return new ApiSocketClientImpl(this.apiKey, this.apiSecret, ApiServiceGenerator.getSharedClient());
    }

}