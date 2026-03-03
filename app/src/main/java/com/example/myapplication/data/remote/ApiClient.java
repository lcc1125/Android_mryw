package com.example.myapplication.data.remote;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * API客户端配置
 */
public class ApiClient {

    // 后端API地址配置
    // 模拟器使用: "http://10.0.2.2:8080/api/"
    // 真机使用: "http://192.168.1.102:8080/api/"
    private static final String BASE_URL = "http://192.168.1.102:8080/api/";

    private static Retrofit retrofit = null;

    /**
     * 获取Retrofit实例
     */
    public static synchronized Retrofit getClient() {
        if (retrofit == null) {
            // 添加日志拦截器
            HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
            loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

            // 配置OkHttpClient
            OkHttpClient okHttpClient = new OkHttpClient.Builder()
                    .addInterceptor(loggingInterceptor)
                    .addInterceptor(chain -> {
                        // 添加统一的Token拦截器
                        okhttp3.Request originalRequest = chain.request();

                        // 从SharedPreferences获取Token
                        String token = com.example.myapplication.utils.SharedPreferencesManager.getToken();
                        if (token != null && !token.isEmpty()) {
                            okhttp3.Request newRequest = originalRequest.newBuilder()
                                    .header("Authorization", "Bearer " + token)
                                    .build();
                            return chain.proceed(newRequest);
                        }

                        return chain.proceed(originalRequest);
                    })
                    .connectTimeout(30, TimeUnit.SECONDS)
                    .readTimeout(30, TimeUnit.SECONDS)
                    .writeTimeout(30, TimeUnit.SECONDS)
                    .build();

            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .client(okHttpClient)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }

    /**
     * 获取API服务实例
     */
    public static <T> T getService(Class<T> serviceClass) {
        return getClient().create(serviceClass);
    }
}
