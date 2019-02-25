package com.newscorp;

import android.app.Application;

import java.util.HashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.Dispatcher;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitManager {
    public static RetrofitManager sInstance = new RetrofitManager();
    public OkHttpClient httpClient;
    public static int API_TIMEOUT = 20000;
    private HashMap<String, Retrofit> retrofits = new HashMap<>();

    public void init(final Application context) {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.dispatcher(new Dispatcher(Executors.newCachedThreadPool()));
        builder.connectTimeout(API_TIMEOUT, TimeUnit.MILLISECONDS);
        builder.readTimeout(API_TIMEOUT, TimeUnit.MILLISECONDS);
        builder.writeTimeout(API_TIMEOUT, TimeUnit.MILLISECONDS);
        builder.retryOnConnectionFailure(true);
        builder.followRedirects(true);
        builder.followSslRedirects(true);
        int cacheSize = 600 * 1024 * 1024; // 300 MiB
        Cache cache = new Cache(context.getCacheDir(), cacheSize);
        builder.cache(cache);
        httpClient = builder.build();
    }

    public Retrofit getClient(String baseUrl) {
        if (retrofits.get(baseUrl) == null) {
            Retrofit.Builder retrofitBuilder = new Retrofit.Builder();
            retrofitBuilder.baseUrl(baseUrl);
            retrofitBuilder.addConverterFactory(GsonConverterFactory.create());
            retrofitBuilder.client(httpClient);
            retrofits.put(baseUrl, retrofitBuilder.build());
        }
        return retrofits.get(baseUrl);
    }
}