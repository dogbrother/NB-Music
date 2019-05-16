package com.blackdog.musiclibrary.model;

import java.util.List;

public interface RequestCallBack {
    void onSucc(List<Song> music);

    void onError(String response);
}