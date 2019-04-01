package com.blackdog.musiclibrary.remote.common;

import com.blackdog.musiclibrary.model.Song;

import java.util.List;

public interface BaseRequest {
    interface RequectCallBack {
        void onSucc(List<Song> music);

        void onError(String response);
    }

    void searchMusic(int page,int count,String name,RequectCallBack callBack);
}
