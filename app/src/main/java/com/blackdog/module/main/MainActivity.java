package com.blackdog.module.main;


import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;


import com.blackdog.R;

import com.blackdog.module.base.BaseActivity;
import com.blackdog.module.channel.ChannelFragment;
import com.blackdog.module.main.adapter.ChannelMenuAdapter;
import com.blackdog.module.main.adapter.model.ChannelMenuItem;
import com.blackdog.module.main.adapter.model.TopMenuItem;
import com.blackdog.module.search.SearchActivity;
import com.blackdog.musiclibrary.remote.base.ChannelMusicFactory;
import com.blackdog.widget.CommonLinearDecoration;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.entity.MultiItemEntity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class MainActivity extends BaseActivity {
    private final static String TAG = "MainActivity";
    private DrawerLayout mDrawerLayout;
    private RecyclerView mRvChannel;
    private ChannelMenuAdapter mAdapter;
    private int mCurrentType = ChannelMusicFactory.CHANNEL_KUGOU;
    private Map<Integer, Fragment> mFragmentMap = new HashMap<>();

    @Override

    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initViews();
        initValues();
        addListeners();
        initRv();
    }

    private void addListeners() {
        mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                MultiItemEntity multiItemEntity = (MultiItemEntity) adapter.getItem(position);
                if (multiItemEntity instanceof ChannelMenuItem) {
                    ChannelMenuItem channelMenuItem = (ChannelMenuItem) multiItemEntity;
                    switchFragment(channelMenuItem.getChannel());
                    toogleDraw(mDrawerLayout);
                }
            }
        });
    }

    private void initValues() {
        mAdapter = new ChannelMenuAdapter(null);
        mAdapter.setEnableLoadMore(false);
        mCurrentType = -1;
        switchFragment(ChannelMusicFactory.CHANNEL_KUGOU);
    }

    private void initRv() {
        mRvChannel.setLayoutManager(new LinearLayoutManager(this));
        mRvChannel.addItemDecoration(new CommonLinearDecoration().setColor(getResources().getColor(R.color.light_gray)));
        mAdapter.setNewData(getAdapterDatas());
        mRvChannel.setAdapter(mAdapter);
    }

    private void initViews() {
        mDrawerLayout = findViewById(R.id.draw_layout);
        mRvChannel = findViewById(R.id.rv_channel);
    }

    private List<MultiItemEntity> getAdapterDatas() {
        List<MultiItemEntity> multiItemEntities = new ArrayList<>();
        multiItemEntities.add(new TopMenuItem());
        multiItemEntities.add(new ChannelMenuItem(ChannelMusicFactory.CHANNEL_KUGOU, "酷狗", R.drawable.icon_kugou_music));
        multiItemEntities.add(new ChannelMenuItem(ChannelMusicFactory.CHANNEL_BAIDU, "百度", R.drawable.icon_baidu_music));
        return multiItemEntities;
    }

    public void toogleDraw(View view) {
        if (mDrawerLayout.isDrawerOpen(Gravity.START)) {
            mDrawerLayout.closeDrawer(Gravity.START);
        } else {
            mDrawerLayout.openDrawer(Gravity.START);
        }
    }

    public void intentToSearch(View view) {
        SearchActivity.actionStart(this, mCurrentType);
    }

    private void switchFragment(int type) {
        if (type == mCurrentType) {
            return;
        }
        mCurrentType = type;
        if (!mFragmentMap.containsKey(mCurrentType)) {
            mFragmentMap.put(mCurrentType, ChannelFragment.newInstance(mCurrentType));
        }
        Fragment fragment = mFragmentMap.get(mCurrentType);
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.layout_content, fragment);
        transaction.commit();
    }


}
