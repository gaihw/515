package com.port.websocket.util;

import okhttp3.Dispatcher;
import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import org.apache.commons.lang3.StringUtils;
import retrofit2.Call;
import retrofit2.Converter;
import retrofit2.Response;
import retrofit2.Retrofit;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
 * Generates a 58coin API implementation based on @see {@link ApiService}.
 */
public class ApiServiceGenerator {

    private static final OkHttpClient sharedClient;
//    private static final Converter.Factory converterFactory = new ConverterFactory();

    static {
        Dispatcher dispatcher = new Dispatcher();
        dispatcher.setMaxRequestsPerHost(500);
        dispatcher.setMaxRequests(500);
        sharedClient = new OkHttpClient.Builder().dispatcher(dispatcher).pingInterval(20, TimeUnit.SECONDS).build();
//        sharedClient = new OkHttpClient.Builder().connectTimeout(9 * 10, TimeUnit.SECONDS).build();
    }

//    @SuppressWarnings("unchecked")
//    private static final Converter<ResponseBody, RestResult> errorBodyConverter = (Converter<ResponseBody, RestResult>) converterFactory.responseBodyConverter(RestResult.class, null,
//            null);
//
//    public static <S> S createService(Class<S> serviceClass) {
//        return createService(serviceClass, null, null);
//    }
//
//    public static <S> S createService(Class<S> serviceClass, String apiKey, String secret) {
//        Retrofit.Builder retrofitBuilder = new Retrofit.Builder().baseUrl(ApiConstants.REST_API_BASE_URL).addConverterFactory(converterFactory);
//
//        if (StringUtils.isEmpty(apiKey) || StringUtils.isEmpty(secret)) {
//            retrofitBuilder.client(sharedClient);
//        } else {
//            // `adaptedClient` will use its own interceptor, but share thread pool etc with the 'parent' client
//            
//        	//*******************
//        	AuthenticationInterceptor interceptor = new AuthenticationInterceptor(apiKey, secret);
//            OkHttpClient adaptedClient = sharedClient.newBuilder().addInterceptor(interceptor).build();
//            retrofitBuilder.client(adaptedClient);
//        }
//
//        Retrofit retrofit = retrofitBuilder.build();
//        System.out.println("调用com.coin58.api.client.impl.ApiServiceGenerator类中的createService方法，返回retrofit.create(ApiService serviceClass)");
//        return retrofit.create(serviceClass);
//    }
//
//    /**
//     * Execute a REST call and block until the response is received.
//     */
//    public static <T> T executeSync(Call<T> call) {
//        try {
//            Response<T> response = call.execute();
//            System.out.println("**********************"+response);
//            if (!response.isSuccessful()) {
//                int code = response.code();
//                throw new ApiException(HttpStatusEnum.of(code).getMessage());
//            }
//
//            RestResult restResult = (RestResult) response.body();
//            if (restResult == null) {
//                return null;
//            }
//
//            if (restResult.getCode() != 0) {
//                throw new ApiException(restResult.getMessage());
//            }
//
//            return (T) restResult.getData();
//        } catch (IOException e) {
//            throw new ApiException(e);
//        }
//    }
//
//    /**
//     * Extracts and converts the response error body into an object.
//     */
//    public static RestResult getApiError(Response<?> response) throws IOException, ApiException {
//        return errorBodyConverter.convert(response.errorBody());
//    }

    /**
     * Returns the shared OkHttpClient instance.
     */
    public static OkHttpClient getSharedClient() {
        return sharedClient;
    }
}