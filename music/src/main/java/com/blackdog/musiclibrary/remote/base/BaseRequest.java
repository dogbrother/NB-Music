package com.blackdog.musiclibrary.remote.base;

import com.blackdog.musiclibrary.model.Song;

import java.util.List;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

public interface BaseRequest {

    Retrofit RETROFIT = new Retrofit.Builder()
            .baseUrl("http://www.fake.com")
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build();

    interface RequectCallBack {
        void onSucc(List<Song> music);

        void onError(String response);
    }

    void searchMusic(int page, int count, String name, RequectCallBack callBack);
}
