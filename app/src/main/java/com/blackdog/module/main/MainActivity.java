package com.blackdog.module.main;


import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.blackdog.R;

import com.blackdog.module.base.BaseActivity;
import com.blackdog.module.channel.ChannelFragment;
import com.blackdog.module.main.adapter.ChannelMenuAdapter;
import com.blackdog.module.main.adapter.model.ChannelMenuItem;
import com.blackdog.module.main.adapter.model.TopMenuItem;
import com.blackdog.module.search.SearchActivity;
import com.blackdog.musiclibrary.remote.base.ChannelMusicFactory;
import com.blackdog.util.ScreenUtils;
import com.blackdog.util.ToastUtil;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.lzx.starrysky.manager.MusicManager;
import com.lzx.starrysky.manager.OnPlayerEventListener;
import com.lzx.starrysky.model.SongInfo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.animation.ValueAnimator.RESTART;

public class MainActivity extends BaseActivity {
    private final static String TAG = "MainActivity";
    private DrawerLayout mDrawerLayout;
    private RecyclerView mRvChannel;
    private ChannelMenuAdapter mAdapter;
    private TextView mTvMusicName;
    private TextView mTvSinger;
    private ImageView mIvPlayStatus;
    private int mCurrentType = ChannelMusicFactory.CHANNEL_KUGOU;
    private Map<Integer, Fragment> mFragmentMap = new HashMap<>();

    private Animation mMusicIconAnimation;
    private FrameLayout mLayoutMusic;
    private boolean mMusicIconRotaing;

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
        MusicManager.getInstance().addPlayerEventListener(new OnPlayerEventListener() {
            @Override
            public void onMusicSwitch(SongInfo songInfo) {
                mIvPlayStatus.setImageResource(R.drawable.ic_action_playback_pause);
                mTvMusicName.setText(songInfo.getSongName());
                mTvSinger.setText(TextUtils.isEmpty(songInfo.getArtist()) ? "未知歌手" : songInfo.getArtist());
                mLayoutMusic.startAnimation(mMusicIconAnimation);
                mMusicIconRotaing = true;
            }

            @Override
            public void onPlayerStart() {
                mIvPlayStatus.setImageResource(R.drawable.ic_action_playback_pause);
                SongInfo songInfo = MusicManager.getInstance().getNowPlayingSongInfo();
                mTvMusicName.setText(songInfo.getSongName());
                mTvSinger.setText(TextUtils.isEmpty(songInfo.getArtist()) ? "未知歌手" : songInfo.getArtist());
                mLayoutMusic.startAnimation(mMusicIconAnimation);
                mMusicIconRotaing = true;
            }

            @Override
            public void onPlayerPause() {
                mIvPlayStatus.setImageResource(R.drawable.ic_action_playback_play);
                mLayoutMusic.clearAnimation();
                mMusicIconRotaing = false;
            }

            @Override
            public void onPlayerStop() {
                mIvPlayStatus.setImageResource(R.drawable.ic_action_playback_play);
                mLayoutMusic.clearAnimation();
                mMusicIconRotaing = false;
            }

            @Override
            public void onPlayCompletion(SongInfo songInfo) {
                mIvPlayStatus.setImageResource(R.drawable.ic_action_playback_play);
                mLayoutMusic.clearAnimation();
                mMusicIconRotaing = false;
            }

            @Override
            public void onBuffering() {

            }

            @Override
            public void onError(int errorCode, String errorMsg) {
                ToastUtil.show("errorCode : " + errorCode + ", errMsg : " + errorMsg);
                mLayoutMusic.clearAnimation();
            }
        });

        mIvPlayStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (MusicManager.getInstance().isPlaying()) {
                    MusicManager.getInstance().pauseMusic();
                } else if (MusicManager.getInstance().getNowPlayingSongInfo() != null) {
                    MusicManager.getInstance().playMusic();
                }
            }
        });
    }

    private void initValues() {
        mAdapter = new ChannelMenuAdapter(null);
        mAdapter.setEnableLoadMore(false);
        mCurrentType = -1;
        switchFragment(ChannelMusicFactory.CHANNEL_KUGOU);

        mMusicIconAnimation = new RotateAnimation(0f, 360f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        mMusicIconAnimation.setFillAfter(true); // 设置保持动画最后的状态
        mMusicIconAnimation.setDuration(4000); // 设置动画时间
        mMusicIconAnimation.setRepeatMode(RESTART);
        mMusicIconAnimation.setRepeatCount(-1);
    }

    private void initRv() {
        mRvChannel.setLayoutManager(new LinearLayoutManager(this));
        mRvChannel.addItemDecoration(new MyDecoration());
        mAdapter.setNewData(getAdapterDatas());
        mRvChannel.setAdapter(mAdapter);
    }

    private void initViews() {
        mDrawerLayout = findViewById(R.id.draw_layout);
        mRvChannel = findViewById(R.id.rv_channel);
        mTvMusicName = findViewById(R.id.tv_musice_name);
        mTvSinger = findViewById(R.id.tv_music_singer);
        mIvPlayStatus = findViewById(R.id.iv_play_status);
        mLayoutMusic = findViewById(R.id.layout_music);
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

    private class MyDecoration extends RecyclerView.ItemDecoration {
        @Override
        public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
            super.getItemOffsets(outRect, view, parent, state);
            int position = parent.getChildAdapterPosition(view);
            if (position < 0) {
                return;
            }
            MultiItemEntity entity = mAdapter.getItem(position);
            if (entity instanceof ChannelMenuItem) {
                outRect.top = ScreenUtils.dip2px(MainActivity.this, 2);
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mMusicIconRotaing) {
            mMusicIconAnimation.reset();
            mMusicIconAnimation.start();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mMusicIconRotaing) {
            mMusicIconAnimation.cancel();
        }
    }
}
