package com.newscorp;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.http.GET;
import retrofit2.http.Path;

public class ApiService extends Service {

    private static final String COMMAND = "COMMAND";
    private static final int LOAD_IMAGES = 123;
    private static final String IMAGES_PATH = "photos";
    private static final String IMAGES_BASEURL = "http://jsonplaceholder.typicode.com/";

    public static void getImages(Context context) {
        Intent intent = new Intent(context, ApiService.class);
        intent.putExtra(COMMAND, LOAD_IMAGES);
        context.startService(intent);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent == null) {
            return START_NOT_STICKY;
        }
        switch (intent.getIntExtra(COMMAND, -1)) {
            case LOAD_IMAGES:
                ImagesApi imagesApi = RetrofitManager.sInstance.getClient(IMAGES_BASEURL).create(ImagesApi.class);
                Call call = imagesApi.getImages(IMAGES_PATH);
                call.enqueue(imagesCallback);
                break;
        }
        return START_NOT_STICKY;
    }

    /**
     * Having the Retrofit callback ensure that the callback is always received independent of the Fragment availability
     * Results are then dispatched using a sticky broadcast, such that if the Fragment is paused it will received
     * the callback when it resumed automatically, response callbacks are therefore not lost
     */
    private final Callback<List<ImageModel>> imagesCallback = new Callback<List<ImageModel>>() {
        @Override
        public void onResponse(Call<List<ImageModel>> call, Response<List<ImageModel>> response) {
            EventBus.getDefault().postSticky(new ImagesApiResponse(response.body()));
        }

        @Override
        public void onFailure(Call<List<ImageModel>> call, Throwable t) {
            EventBus.getDefault().postSticky(new ImagesApiResponse(null));
        }
    };

    //Reserved for IPC predominantly
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    public interface ImagesApi {

        @GET("{path}")
        Call<List<ImageModel>> getImages(@Path("path") String path);
    }
}
