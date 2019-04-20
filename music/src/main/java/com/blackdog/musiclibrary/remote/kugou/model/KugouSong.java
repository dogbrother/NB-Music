package com.blackdog.musiclibrary.remote.kugou.model;

import com.blackdog.musiclibrary.model.Song;

public class KugouSong extends Song {
    private String hash;

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }
}
