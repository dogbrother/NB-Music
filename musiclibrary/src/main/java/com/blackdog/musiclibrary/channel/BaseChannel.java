package com.blackdog.musiclibrary.channel;

import com.lzx.starrysky.model.SongInfo;

import java.util.List;

public interface BaseChannel {
     List<SongInfo> search(String name);
}
