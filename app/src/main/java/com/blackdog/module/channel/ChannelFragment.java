package com.blackdog.module.channel;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.blackdog.R;
import com.blackdog.module.base.BaseFragment;
import com.blackdog.module.channel.adapter.ChannelAdapter;
import com.blackdog.module.search.SearchActivity;
import com.blackdog.musiclibrary.model.Song;
import com.blackdog.musiclibrary.remote.base.ChannelMusicFactory;
import com.blackdog.util.SongUtil;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.lzx.starrysky.manager.MusicManager;

import java.util.ArrayList;

import static com.blackdog.module.channel.ChannelContact.REQUEST_COUNT;

public class ChannelFragment extends BaseFragment implements ChannelContact.View {


    private static final String KEY_BUNDLE_TYPE = "KEY_BUNDLE_TYPE";
    private RecyclerView mRv;
    private SwipeRefreshLayout mRefresh;
    private FloatingActionButton mSearchButton;
    private ChannelAdapter mAdapter;
    private ChannelContact.Presenter mPresenter;

    private int mType;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_channel, null, false);
        mRv = view.findViewById(R.id.rv_channel);
        mRefresh = view.findViewById(R.id.layout_refresh);
        mSearchButton = view.findViewById(R.id.btn_serch);
        return view;
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mType = getArguments().getInt(KEY_BUNDLE_TYPE);
        mPresenter = new ChannelPresenter();
        mPresenter.setView(this);
        mPresenter.setType(mType);
        mRv.setLayoutManager(new LinearLayoutManager(getContext()));
        mAdapter = new ChannelAdapter(new ArrayList<>());
        mAdapter.setPreLoadNumber(REQUEST_COUNT);
        mRv.setAdapter(mAdapter);
        addListeners();
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
                Song song = (Song) adapter.getData().get(position);
                MusicManager.getInstance().playMusicByInfo(SongUtil.transformSong(song));
            }
        });
        mSearchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SearchActivity.actionStart(getActivity());
            }
        });
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
        Fragment fragment = new ChannelFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(KEY_BUNDLE_TYPE, type);
        fragment.setArguments(bundle);
        return fragment;
    }

}
