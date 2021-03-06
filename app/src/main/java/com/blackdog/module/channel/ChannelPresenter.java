package com.blackdog.module.channel;

import com.blackdog.musiclibrary.local.LocalMusicManager;
import com.blackdog.musiclibrary.model.RequestCallBack;
import com.blackdog.musiclibrary.model.Song;
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
        LocalMusicManager.getInstance().queryMusic(mChannelType, offset, count, new RequestCallBack() {
            @Override
            public void onSucc(List<Song> music) {
                mView.onLoadComplete();
                if (offset <= 0) {
                    mView.getAdapter().setNewData(music);
                } else {
                    mView.getAdapter().addData(music);
                }
                if (music.size() < count) {
                    mView.getAdapter().loadMoreEnd();
                }
            }

            @Override
            public void onError(String response) {
                ToastUtil.show(response);
                mView.onLoadComplete();
            }
        });
    }
}
