package com.blackdog.module.main;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatEditText;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.blackdog.R;
import com.blackdog.musiclibrary.local.sqlite.SqlCenter;
import com.blackdog.musiclibrary.remote.baidu.request.BaiduRequest;
import com.blackdog.musiclibrary.remote.kugou.request.KugouRequest;
import com.blackdog.util.SongUtil;
import com.blackdog.musiclibrary.model.Song;
import com.blackdog.musiclibrary.remote.base.BaseRequest;
import com.lzx.starrysky.manager.MediaSessionConnection;
import com.lzx.starrysky.manager.MusicManager;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private AppCompatEditText mEtSong;
    private final String TAG = "MainActivity";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mEtSong = findViewById(R.id.et_song);
        MediaSessionConnection.getInstance().connect();
        new Thread(new Runnable() {
            @Override
            public void run() {
                List<Song> songs = SqlCenter.getInstance().getDaoSession().loadAll(Song.class);
                for (Song song : songs) {
                    Log.i(TAG, "id : " + song.getId());
                    Log.i(TAG, "songUrl : " + song.getDownloadUrl());
                }
            }
        }).start();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        MediaSessionConnection.getInstance().disconnect();
    }

    public void searchSong(View view) {
        String songName = mEtSong.getText().toString();
        new BaiduRequest().searchMusic(1, 5, songName, new BaseRequest.RequectCallBack() {
            @Override
            public void onSucc(final List<Song> musics) {
                if (musics.size() > 0) {
                    MusicManager.getInstance().playMusicByInfo(SongUtil.transformSong(musics.get(0)));
                }else{
                    showToast("no music");
                }
            }

            @Override
            public void onError(final String response) {
                showToast("requestMusicErr : " + response);
            }
        });
    }

    private void showToast(String msg) {
        if (TextUtils.isEmpty(msg)) {
            return;
        }
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }
}
