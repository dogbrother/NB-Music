package com.blackdog.musiclibrary.remote.baidu.model;

import com.blackdog.musiclibrary.model.Song;

public class BaiduSong extends Song {
    private String mBaiduSongId;

    public String getBaiduSongId() {
        return mBaiduSongId;
    }

    public void setBaiduSongId(String baiduSongId) {
        mBaiduSongId = baiduSongId;
    }
}
