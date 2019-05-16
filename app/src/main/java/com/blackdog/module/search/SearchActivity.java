package com.blackdog.module.search;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
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

    private AppCompatEditText mEtSearch;
    private ImageView mIvBack;
    private TextView mTitle;
    private TextView mTvSearch;
    private RecyclerView mRvSearch;

    private ChannelAdapter mAdapter;
    ProgressDialog mProgressDialog;

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
        mIvBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
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
                ChannelMusicFactory.getRequest(ChannelMusicFactory.CHANNEL_KUGOU)
                        .searchMusic(SearchActivity.this, 0, 20, searchText, new RequestCallBack() {
                            @Override
                            public void onSucc(List<Song> music) {
                                mProgressDialog.dismiss();
                                mAdapter.setNewData(music);
                            }

                            @Override
                            public void onError(String response) {
                                mProgressDialog.dismiss();
                                ToastUtil.show(response);
                            }
                        });
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
    }

    private void initValues() {
        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setMessage("搜索中");
        mAdapter = new ChannelAdapter(new ArrayList<>());
    }

    private void initViews() {
        mEtSearch = findViewById(R.id.et_search);
        mIvBack = findViewById(R.id.iv_back);
        mTvSearch = findViewById(R.id.tv_search);
        mTitle = findViewById(R.id.tv_title);
        mTitle.setText("搜索歌曲");
        mRvSearch = findViewById(R.id.rv_search);
        mRvSearch.setLayoutManager(new LinearLayoutManager(this));
    }

    public static void actionStart(Context context) {
        Intent intent = new Intent(context, SearchActivity.class);
        context.startActivity(intent);
    }
}
