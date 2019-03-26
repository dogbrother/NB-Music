package com.blackdog.musiclibrary.http;

import com.lzx.starrysky.model.SongInfo;

import java.util.List;

public interface BaseRequest {
    interface RequectCallBack {
        void onSucc(List<SongInfo> music);

        void onError(String response);
    }

    void searchMusic(int page,int count,String name,RequectCallBack callBack);
}
