package com.example.oauth.api;

import java.util.concurrent.TimeUnit;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;

public class ApiClient {
    public static final String BASE_URL =
            "https://fastapi-auth-and-user-management-production-1531.up.railway.app/";

    private static OkHttpClient client;

    public static OkHttpClient getClient() {
        if (client == null) {
            HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
            logging.setLevel(HttpLoggingInterceptor.Level.BODY);

            client = new OkHttpClient.Builder()
                    .addInterceptor(logging)
                    .connectTimeout(30, TimeUnit.SECONDS)
                    .readTimeout(30, TimeUnit.SECONDS)
                    .build();
        }
        return client;
    }

    public static MediaType JSON() {
        return MediaType.get("application/json; charset=utf-8");
    }
}