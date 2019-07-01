package com.blackdog.module.channel;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.blackdog.R;
import com.blackdog.module.base.BaseFragment;
import com.blackdog.module.channel.adapter.ChannelAdapter;
import com.blackdog.musiclibrary.local.LocalMusicManager;
import com.blackdog.musiclibrary.model.RequestCallBack;
import com.blackdog.musiclibrary.model.Song;
import com.blackdog.musiclibrary.remote.base.ChannelMusicFactory;
import com.blackdog.util.SongUtil;
import com.blackdog.util.ToastUtil;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.loadmore.SimpleLoadMoreView;
import com.lzx.starrysky.manager.MusicManager;
import com.lzx.starrysky.manager.OnPlayerEventListener;
import com.lzx.starrysky.model.SongInfo;

import java.util.ArrayList;
import java.util.List;

import static com.blackdog.module.channel.ChannelContact.REQUEST_COUNT;

public class ChannelFragment extends BaseFragment implements ChannelContact.View, LocalMusicManager.SongChangeListener, OnPlayerEventListener {

    private static final String KEY_BUNDLE_TYPE = "KEY_BUNDLE_TYPE";
    private RecyclerView mRv;
    private SwipeRefreshLayout mRefresh;
    private ChannelAdapter mAdapter = new ChannelAdapter(null);
    ;
    private ChannelContact.Presenter mPresenter;
    private int mType;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_channel, null, false);
        mRv = view.findViewById(R.id.rv_channel);
        mRefresh = view.findViewById(R.id.layout_refresh);
        return view;
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initValues();
        addListeners();
        LocalMusicManager.getInstance().registerSongChangeListener(ChannelMusicFactory.getChannelName(mType), this);
        mPresenter.request(0, REQUEST_COUNT);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        LocalMusicManager.getInstance().unRegisterSongChannelListener(ChannelMusicFactory.getChannelName(mType), this);
        MusicManager.getInstance().removePlayerEventListener(this);
    }

    private void initValues() {
        //presenter
        mType = getArguments().getInt(KEY_BUNDLE_TYPE);
        mPresenter = new ChannelPresenter();
        mPresenter.setView(this);
        mPresenter.setType(mType);
        //recycler
        mRv.setLayoutManager(new LinearLayoutManager(getContext()));
        mAdapter.setPreLoadNumber(REQUEST_COUNT);
        mAdapter.setEnableLoadMore(true);
        mAdapter.setLoadMoreView(new SimpleLoadMoreView());
        mRv.setAdapter(mAdapter);
    }

    private void addListeners() {
        mRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mRefresh.setRefreshing(true);
                mPresenter.request(0, REQUEST_COUNT);
            }
        });
        mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                MusicManager.getInstance().playMusic(SongUtil.transforSong(mAdapter.getData()), position);
            }
        });
        mAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                int count = mAdapter.getData().size();
                mPresenter.request(count, REQUEST_COUNT);
            }
        }, mRv);
        mAdapter.setOnItemLongClickListener(new BaseQuickAdapter.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(BaseQuickAdapter adapter, View view, int position) {
                Song song = (Song) adapter.getData().get(position);
                new AlertDialog.Builder(getContext())
                        .setTitle("提示")
                        .setMessage("是否删除" + song.getSongName())
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                LocalMusicManager.getInstance().removeMusic(song);
                                if (MusicManager.getInstance().getNowPlayingSongInfo().getSongId().equals(song.getId() + "")) {
                                    MusicManager.getInstance().skipToNext();
                                }
                                dialog.dismiss();
                            }
                        })
                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        }).show();
                return true;
            }
        });
        MusicManager.getInstance().addPlayerEventListener(this);
    }

    @Override
    public BaseQuickAdapter getAdapter() {
        return mAdapter;
    }

    @Override
    public void onLoadComplete() {
        mRefresh.setRefreshing(false);
    }

    public static Fragment newInstance(@ChannelMusicFactory.Channel int type) {
        ChannelFragment fragment = new ChannelFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(KEY_BUNDLE_TYPE, type);
        fragment.setArguments(bundle);
        return fragment;
    }

    public int getType() {
        return mType;
    }


    @Override
    public void onAdd(Song song) {
        mAdapter.addData(song);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onDelete(Song song) {
        mAdapter.getData().remove(song);
        mAdapter.notifyDataSetChanged();
    }


    @Override
    public void onMusicSwitch(SongInfo songInfo) {
    }

    @Override
    public void onPlayerStart() {
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onPlayerPause() {

    }

    @Override
    public void onPlayerStop() {

    }

    @Override
    public void onPlayCompletion(SongInfo songInfo) {
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onBuffering() {

    }

    @Override
    public void onError(int errorCode, String errorMsg) {
        ToastUtil.show(errorMsg);
    }
}
