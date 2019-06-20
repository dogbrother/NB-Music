package com.blackdog.module.channel.adapter;

import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.blackdog.R;
import com.blackdog.musiclibrary.model.Song;
import com.blackdog.util.TimeUtil;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.lzx.starrysky.manager.MusicManager;
import com.lzx.starrysky.model.SongInfo;

import java.util.List;

public class ChannelAdapter extends BaseQuickAdapter<Song, BaseViewHolder> {
    public ChannelAdapter(@Nullable List<Song> data) {
        super(R.layout.item_channel, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, Song item) {
        setTvContent(helper, item.getSongName(), "未知歌曲", "", R.id.tv_name);
        setTvContent(helper, item.getSinger(), "未知歌手", "歌手：", R.id.tv_single);
        helper.setText(R.id.tv_duration, String.format("时长:%s", TimeUtil.getSongDuration((int) item.getDuration())));
        SongInfo songInfo = MusicManager.getInstance().getNowPlayingSongInfo();
        helper.setVisible(R.id.iv_playing, false);
        if (songInfo != null && songInfo.getDownloadUrl().equals(item.getDownloadUrl())) {
            helper.setVisible(R.id.iv_playing, true);

        }
    }

    private void setTvContent(BaseViewHolder helper, String value, String defaultName, String prefix, int id) {
        if (TextUtils.isEmpty(value)) {
            helper.setText(id, defaultName);
        } else {
            helper.setText(id, prefix + value);
        }
    }
}
