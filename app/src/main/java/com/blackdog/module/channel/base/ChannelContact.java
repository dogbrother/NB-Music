package com.blackdog.module.channel.base;


import android.content.Context;

import com.blackdog.musiclibrary.remote.base.ChannelMusicFactory;
import com.chad.library.adapter.base.BaseQuickAdapter;

import java.util.List;

public interface ChannelContact {

    int REQUEST_COUNT = 10;

    interface View {
        BaseQuickAdapter getAdapter();

        void onLoadComplete();

        Context getContext();
    }


    interface Presenter {

        void setView(View view);

        void setType(@ChannelMusicFactory.Channel int type);

        void request(int offset, int count);
    }
}
