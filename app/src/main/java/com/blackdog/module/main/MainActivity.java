package com.blackdog.module.main;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.TextView;


import com.blackdog.R;

import com.blackdog.module.base.BaseActivity;
import com.blackdog.module.channel.ChannelFragment;
import com.blackdog.module.search.SearchActivity;
import com.blackdog.musiclibrary.remote.base.ChannelMusicFactory;


public class MainActivity extends BaseActivity {
    private final static String TAG = "MainActivity";
    private static final Fragment[] FRAGMENTS = {
            ChannelFragment.newInstance(ChannelMusicFactory.CHANNEL_KUGOU),
            ChannelFragment.newInstance(ChannelMusicFactory.CHANNEL_BAIDU)
    };

    private static final String[] TITLES = {
            "酷狗",
            "百度"
    };

    private TextView mTvTitle;
    private TabLayout mTabLayout;
    private ViewPager mVp;
    private FloatingActionButton mBtnSearch;
    private MyPageAdapter mAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initValues();
        initViews();
        addListeners();
    }

    private void initValues() {
        mAdapter = new MyPageAdapter(getSupportFragmentManager(), FRAGMENTS, TITLES);
    }

    private void addListeners() {
        mBtnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SearchActivity.actionStart(MainActivity.this, ((ChannelFragment) mAdapter.getItem(mVp.getCurrentItem())).getType());
            }
        });
        mVp.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @Override
            public void onPageSelected(int i) {
                mTvTitle.setText(TITLES[i]);
            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });
    }

    private void initViews() {
        mTabLayout = findViewById(R.id.tb_main);
        mVp = findViewById(R.id.vp_main);
        mVp.setAdapter(mAdapter);
        mTabLayout.setupWithViewPager(mVp);
        mBtnSearch = findViewById(R.id.fab_search);
        mTvTitle = findViewById(R.id.tv_title);
        mTvTitle.setText(TITLES[0]);
    }

    private static class MyPageAdapter extends FragmentPagerAdapter {

        private Fragment[] mFragmens;
        private String[] mTitles;


        public MyPageAdapter(FragmentManager fm, Fragment[] fragments, String[] titles) {
            super(fm);
            this.mFragmens = fragments;
            this.mTitles = titles;
        }


        @Override
        public Fragment getItem(int i) {
            return mFragmens[i];
        }

        @Override
        public int getCount() {
            return mFragmens.length;
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            return mTitles[position];
        }
    }

}
