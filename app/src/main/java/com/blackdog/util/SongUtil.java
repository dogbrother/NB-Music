package com.blackdog.util;

import com.blackdog.musiclibrary.model.Song;
import com.lzx.starrysky.model.SongInfo;

import java.util.ArrayList;
import java.util.List;

public class SongUtil {
    public static SongInfo transformSong(Song song) {
        SongInfo info = new SongInfo();
        info.setSongId(String.valueOf(song.getId()));
        info.setSize(song.getSize());
        info.setDownloadUrl(song.getDownloadUrl());
        info.setSongUrl(song.getDownloadUrl());
        info.setDuration(song.getDuration());
        info.setArtist(song.getSinger());
        return info;
    }

    public static List<SongInfo> transforSong(List<Song> songs) {
        List<SongInfo> infos = new ArrayList<>();
        if (songs != null) {
            for (Song song : songs) {
                infos.add(transformSong(song));
            }
        }
        return infos;
    }


}
