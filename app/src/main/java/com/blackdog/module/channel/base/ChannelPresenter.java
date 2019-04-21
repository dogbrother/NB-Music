package com.blackdog.module.channel.base;

import com.blackdog.musiclibrary.model.Song;
import com.blackdog.musiclibrary.remote.base.BaseRequest;
import com.blackdog.musiclibrary.remote.base.ChannelMusicFactory;
import com.blackdog.util.ToastUtil;

import java.util.List;

public class ChannelPresenter implements ChannelContact.Presenter {

    private ChannelContact.View mView;
    private int mChannelType;

    @Override
    public void setView(ChannelContact.View view) {
        this.mView = view;
    }

    @Override
    public void setType(@ChannelMusicFactory.Channel int type) {
        this.mChannelType = type;
    }

    @Override
    public void request(int offset, int count) {
        ChannelMusicFactory.getRequest(mChannelType)
                .searchMusic(mView.getContext(), offset, count, "只是太爱你", new BaseRequest.RequectCallBack() {
                    @Override
                    public void onSucc(List<Song> musics) {
                        mView.getAdapter().setNewData(musics);
                        mView.onLoadComplete();
                    }

                    @Override
                    public void onError(String response) {
                        ToastUtil.show(response);
                        mView.onLoadComplete();
                    }
                });
    }
}
