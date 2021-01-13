package com.atom.dex.api.client.test;

import com.atom.dex.api.client.test.exception.AtomApiError;
import com.atom.dex.api.client.test.exception.AtomApiException;
import lombok.extern.slf4j.Slf4j;
import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

import java.io.IOException;
import java.lang.annotation.Annotation;

@Slf4j
public class AtomApiServiceGenerator {

    private static OkHttpClient.Builder httpClient = new OkHttpClient.Builder();

    private static Retrofit.Builder builder =
            new Retrofit.Builder()
                    .addConverterFactory(JacksonConverterFactory.create());

    private static Retrofit retrofit;

    public static <S> S createService(Class<S> serviceClass, String baseUrl) {

        builder.baseUrl(baseUrl);
        builder.client(httpClient.build());
        builder.addConverterFactory(JacksonConverterFactory.create());
        retrofit = builder.build();

        return retrofit.create(serviceClass);
    }

    /**
     * Execute a REST call and block until the response is received.
     */
    public static <T> T executeSync(Call<T> call) {
        try {
            Response<T> response = call.execute();
            if (response.isSuccessful()) {
                log.debug("atom response : "+ response.body());
                return response.body();
            } else {
                AtomApiError apiError = getAtomApiError(response);
                log.error("",apiError);
                throw new AtomApiException(apiError);
            }
        } catch (IOException e) {
            log.error("",e);
            throw new AtomApiException(e);
        }
    }

    /**
     * Extracts and converts the response error body into an object.
     */
    private static AtomApiError getAtomApiError(Response<?> response) throws IOException, AtomApiException {
        return (AtomApiError) retrofit.responseBodyConverter(AtomApiError.class, new Annotation[0])
                .convert(response.errorBody());
    }
}
