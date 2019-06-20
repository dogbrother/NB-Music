package com.blackdog.module.main.adapter.model;

import com.blackdog.module.main.adapter.HolderType;
import com.blackdog.musiclibrary.remote.base.ChannelMusicFactory;
import com.chad.library.adapter.base.entity.MultiItemEntity;

public class ChannelMenuItem implements MultiItemEntity {
    private String mName;
    private int mRes;
    private @ChannelMusicFactory.Channel
    int mChannel;

    public ChannelMenuItem(@ChannelMusicFactory.Channel int channel) {
        this.mChannel = channel;
    }

    public ChannelMenuItem(@ChannelMusicFactory.Channel int channel, String name, int res) {
        this.mChannel = channel;
        mName = name;
        mRes = res;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public int getRes() {
        return mRes;
    }

    public void setRes(int res) {
        mRes = res;
    }

    @Override
    public int getItemType() {
        return HolderType.TYPE_CHANNEL;
    }

    public int getChannel() {
        return mChannel;
    }

    public void setChannel(int channel) {
        mChannel = channel;
    }
}
