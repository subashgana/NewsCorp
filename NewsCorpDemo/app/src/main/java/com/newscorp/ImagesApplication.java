package com.newscorp;

import android.app.Application;
import android.net.Uri;

import com.newscorp.BuildConfig;
import com.squareup.picasso.LruCache;
import com.squareup.picasso.Picasso;

import java.util.concurrent.Executors;

public class ImagesApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        RetrofitManager.sInstance.init(this);
        Picasso.Builder builder = new Picasso.Builder(this);
        LruCache picassoMemoryCache = new LruCache(8 * 1024 * 1024);
        builder.memoryCache(picassoMemoryCache);
        builder.executor(Executors.newCachedThreadPool());
        //Apply custom OkHttpDownloader
        builder.downloader(new OkHttpDownloader(this));
        //Print Picasso exceptions to LogCat
        builder.listener(new Picasso.Listener() {
            @Override
            public void onImageLoadFailed(Picasso picasso, Uri uri, Exception exception) {
                exception.printStackTrace();
            }
        });
        Picasso built = builder.build();
        built.setLoggingEnabled(BuildConfig.DEBUG);
        Picasso.setSingletonInstance(built);
    }
}
