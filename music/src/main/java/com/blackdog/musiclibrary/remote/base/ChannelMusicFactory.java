package com.blackdog.musiclibrary.remote.base;

import android.support.annotation.IntDef;

import com.blackdog.musiclibrary.remote.baidu.request.BaiduRequest;
import com.blackdog.musiclibrary.remote.kugou.request.KugouRequest;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public class ChannelMusicFactory {

    public static final int CHANNEL_BAIDU = 1;
    public static final int CHANNEL_KUGOU = 2;

    @Retention(RetentionPolicy.SOURCE)
    @IntDef({CHANNEL_BAIDU, CHANNEL_KUGOU})
    public @interface Channel {
    }


    public static BaseRequest getRequest(@Channel int channelId) {
        switch (channelId) {
            case CHANNEL_BAIDU:
                return new BaiduRequest();
            case CHANNEL_KUGOU:
                return new KugouRequest();
            default:
                return null;
        }
    }

}
