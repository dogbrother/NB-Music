package com.blackdog.musiclibrary.model;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Unique;

@Entity
public class Song {
    @Id(autoincrement = true)
    private Long id;
    private String songName;
    private String singer;
    @Unique
    private String downloadUrl;
    private String size;
    private long duration;
    private String channelName;

    @Generated(hash = 1934683397)
    public Song(Long id, String songName, String singer, String downloadUrl,
                String size, long duration, String channelName) {
        this.id = id;
        this.songName = songName;
        this.singer = singer;
        this.downloadUrl = downloadUrl;
        this.size = size;
        this.duration = duration;
        this.channelName = channelName;
    }

    @Generated(hash = 87031450)
    public Song() {
    }

    public String getChannelName() {
        return channelName;
    }

    public Song setChannelName(String channelName) {
        this.channelName = channelName;
        return this;
    }

    public Long getId() {
        return this.id;
    }

    public Song setId(Long id) {
        this.id = id;
        return this;
    }

    public String getSongName() {
        return this.songName;
    }

    public Song setSongName(String songName) {
        this.songName = songName;
        return this;

    }

    public String getSinger() {
        return this.singer;
    }

    public Song setSinger(String singer) {
        this.singer = singer;
        return this;

    }

    public String getDownloadUrl() {
        return this.downloadUrl;
    }

    public Song setDownloadUrl(String downloadUrl) {
        this.downloadUrl = downloadUrl;
        return this;

    }

    public String getSize() {
        return this.size;
    }

    public Song setSize(String size) {
        this.size = size;
        return this;

    }

    public long getDuration() {
        return this.duration;
    }

    public Song setDuration(long duration) {
        this.duration = duration;
        return this;
    }


}
