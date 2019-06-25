package com.blackdog.musiclibrary.remote.base;

import android.content.Context;
import android.text.TextUtils;

import com.blackdog.musiclibrary.model.RequestCallBack;
import com.blackdog.musiclibrary.model.Song;
import com.blackdog.util.NetWorkUtil;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

public abstract class BaseRequest {

    protected static final Retrofit RETROFIT = new Retrofit.Builder()
            .baseUrl("http://www.fake.com")
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build();

    public void searchMusic(Context context, int page, int count, String name, RequestCallBack callBack) {
        if (TextUtils.isEmpty(name)) {
            callBack.onError("name is null");
            return;
        }
        if (!NetWorkUtil.isNetworkConnected(context)) {
            callBack.onError("network err");
            return;
        }
        searchInternal(page, count, name, callBack);
    }

    protected abstract void searchInternal(int page, int count, String name, RequestCallBack callBack);

    public abstract void searchDetail(Song song, RequestCallBack callBack);

}
