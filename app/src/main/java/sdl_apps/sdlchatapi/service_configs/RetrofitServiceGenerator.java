package sdl_apps.sdlchatapi.service_configs;

import android.text.TextUtils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static retrofit2.Retrofit.Builder;

public class RetrofitServiceGenerator {

    private static final String API_BASE_URL = "https://adarekar.pythonanywhere.com/";

    private static OkHttpClient.Builder httpClient = new OkHttpClient.Builder();

    private static Builder builder =
            new Builder()
                    .baseUrl(API_BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create());

    public static <S> S createService(Class<S> serviceClass) {
        return createService(serviceClass, null);
    }

    public static <S> S createService(
            Class<S> serviceClass, final String authToken) {
        if (!TextUtils.isEmpty(authToken)) {
            AuthenticationInterceptor interceptor =
                    new AuthenticationInterceptor(authToken);

            if (!httpClient.interceptors().contains(interceptor)) {
                httpClient.addInterceptor(interceptor);

            }
        }
        builder.client(httpClient.build());
        Retrofit retrofit = builder.build();
        return retrofit.create(serviceClass);
    }

    public static  <S> S config(
            Class<S> serviceClass
    ) {
        OkHttpClient.Builder clientBuilder = new OkHttpClient.Builder();
        clientBuilder.
                addInterceptor(new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
                .connectTimeout(60, TimeUnit.SECONDS)
                .readTimeout(60, TimeUnit.SECONDS)
                .writeTimeout(60, TimeUnit.SECONDS);

        Gson gson = new GsonBuilder().create();

        Builder retrofitBuilder = new Builder()
                .baseUrl(API_BASE_URL)
                .addConverterFactory( GsonConverterFactory.create(gson));

        Retrofit retrofit = retrofitBuilder
                .client(clientBuilder.build())
                .build();

        return retrofit.create(serviceClass);

    }

}
