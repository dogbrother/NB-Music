package com.blackdog.module.search;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.accessibility.AccessibilityEvent;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blackdog.R;
import com.blackdog.module.base.BaseActivity;
import com.blackdog.module.channel.adapter.ChannelAdapter;
import com.blackdog.musiclibrary.local.LocalMusicManager;
import com.blackdog.musiclibrary.model.RequestCallBack;
import com.blackdog.musiclibrary.model.Song;
import com.blackdog.musiclibrary.remote.base.ChannelMusicFactory;
import com.blackdog.util.SongUtil;
import com.blackdog.util.ToastUtil;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.lzx.starrysky.manager.MusicManager;

import java.util.ArrayList;
import java.util.List;

public class SearchActivity extends BaseActivity {

    private static final int REQUEST_COUNT = 15;
    private static final String TAG = "SearchActivity";

    private static final String KEY_INTENT_TYPE = "KEY_INTENT_TYPE";

    private LinearLayout mLayoutSearchRoot;
    private AppCompatEditText mEtSearch;
    private TextView mTitle;
    private TextView mTvSearch;
    private RecyclerView mRvSearch;

    private ChannelAdapter mAdapter;
    ProgressDialog mProgressDialog;
    private int mType;
    private int mPage = 0;
    private String mSearchText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        initViews();
        initValues();
        initListeners();
        mRvSearch.setAdapter(mAdapter);
    }

    private void initListeners() {
        mTvSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mEtSearch.getText() == null) {
                    ToastUtil.show("请输入歌曲名");
                    return;
                }
                String searchText = mEtSearch.getText().toString();
                if (TextUtils.isEmpty(searchText)) {
                    ToastUtil.show("请输入歌曲名");
                    return;
                }
                mProgressDialog.show();
                if (mType == -1) {
                    ToastUtil.show("type error");
                    return;
                }
                mPage = 0;
                mSearchText = mEtSearch.getText().toString();
                request(mSearchText);
            }
        });
        mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                Song song = (Song) adapter.getData().get(position);
                MusicManager.getInstance().playMusicByInfo(SongUtil.transformSong(song));
                LocalMusicManager.getInstance().save(song);
            }
        });
        mAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                request(mSearchText);
            }
        }, mRvSearch);
    }

    private void initValues() {
        mLayoutSearchRoot = findViewById(R.id.root_search);
        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setMessage("搜索中");
        mAdapter = new ChannelAdapter(new ArrayList<>());
        mAdapter.setPreLoadNumber(REQUEST_COUNT);
        mType = getIntent().getIntExtra(KEY_INTENT_TYPE, -1);
        Log.i(TAG, "type : " + mType);
    }

    private void initViews() {
        mEtSearch = findViewById(R.id.et_search);
        mTvSearch = findViewById(R.id.tv_search);
        mTitle = findViewById(R.id.tv_title);
        mTitle.setText("搜索歌曲");
        mRvSearch = findViewById(R.id.rv_search);
        mRvSearch.setLayoutManager(new LinearLayoutManager(this));
    }

    public static void actionStart(Context context, @ChannelMusicFactory.Channel int type) {
        Intent intent = new Intent(context, SearchActivity.class);
        intent.putExtra(KEY_INTENT_TYPE, type);
        context.startActivity(intent);
    }

    private void request(String searchText) {
        ChannelMusicFactory.getRequest(mType)
                .searchMusic(SearchActivity.this, mPage, REQUEST_COUNT, searchText, new RequestCallBack() {
                    @Override
                    public void onSucc(List<Song> music) {
                        mProgressDialog.dismiss();
                        if (mPage == 0) {
                            mAdapter.setNewData(music);
                        } else {
                            mAdapter.addData(music);
                        }
                        if (music.size() < REQUEST_COUNT) {
                            mAdapter.loadMoreComplete();
                        }
                        mPage++;
                    }

                    @Override
                    public void onError(String response) {
                        mProgressDialog.dismiss();
                        ToastUtil.show(response);
                        if (mAdapter.getData().size() > 0) {
                            mAdapter.loadMoreFail();
                        }
                    }
                });
    }

}
