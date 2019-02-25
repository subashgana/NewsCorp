package com.newscorp;

import android.content.Context;
import android.net.Uri;

import com.squareup.picasso.Downloader;
import com.squareup.picasso.NetworkPolicy;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.CacheControl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.ResponseBody;

/**
 * A custom OkHttp based Picasso downloader. The primary purpose of this customization is the REWRITE_CACHE_CONTROL_INTERCEPTOR interceptor.
 */
public class OkHttpDownloader implements Downloader {

    private static OkHttpClient defaultOkHttpClient(Context context) {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.connectTimeout(RetrofitManager.API_TIMEOUT, TimeUnit.MILLISECONDS);
        builder.readTimeout(RetrofitManager.API_TIMEOUT, TimeUnit.MILLISECONDS);
        builder.writeTimeout(RetrofitManager.API_TIMEOUT, TimeUnit.MILLISECONDS);
        builder.retryOnConnectionFailure(true);
        builder.followRedirects(true);
        builder.followSslRedirects(true);
        builder.cache(new Cache(context.getCacheDir(), 300 * 1024 * 1024));
        builder.addNetworkInterceptor(REWRITE_CACHE_CONTROL_INTERCEPTOR);
        return builder.build();
    }

    /**
     * Interceptor that rewrites the server's cache-control header.
     */
    public static final Interceptor REWRITE_CACHE_CONTROL_INTERCEPTOR = new Interceptor() {
        @Override
        public okhttp3.Response intercept(Chain chain) throws IOException {
            okhttp3.Response originalResponse = chain.proceed(chain.request());
            return originalResponse.newBuilder().header("Cache-Control", String.format("max-age=%d", 2592000)).build();
        }
    };

    private final OkHttpClient client;

    public OkHttpDownloader(Context context) {
        this(defaultOkHttpClient(context));
    }

    /**
     * Create a new downloader that uses the specified OkHttp instance. A response cache will not be
     * automatically configured.
     */
    public OkHttpDownloader(OkHttpClient client) {
        this.client = client;
    }

    protected final OkHttpClient getClient() {
        return client;
    }

    @Override
    public Response load(Uri uri, int networkPolicy) throws IOException {
        CacheControl cacheControl = null;
        if (networkPolicy != 0) {
            if (NetworkPolicy.isOfflineOnly(networkPolicy)) {
                cacheControl = CacheControl.FORCE_CACHE;
            } else {
                CacheControl.Builder builder = new CacheControl.Builder();
                if (!NetworkPolicy.shouldReadFromDiskCache(networkPolicy)) {
                    builder.noCache();
                }
                if (!NetworkPolicy.shouldWriteToDiskCache(networkPolicy)) {
                    builder.noStore();
                }
                cacheControl = builder.build();
            }
        }

        Request.Builder builder = new Request.Builder().url(uri.toString());
        if (cacheControl != null) {
            builder.cacheControl(cacheControl);
        }

        okhttp3.Response response = client.newCall(builder.build()).execute();
        int responseCode = response.code();
        if (responseCode >= 300) {
            response.body().close();
            throw new ResponseException(responseCode + " " + response.message(), networkPolicy, responseCode);
        }

        boolean fromCache = response.cacheResponse() != null;

        ResponseBody responseBody = response.body();
        return new Response(responseBody.byteStream(), fromCache, responseBody.contentLength());
    }

    @Override
    public void shutdown() {
        Cache cache = client.cache();
        if (cache != null) {
            try {
                cache.close();
            } catch (IOException ignored) {
            }
        }
    }
}
