package com.lzx.musiclib;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatEditText;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.blackdog.musiclibrary.remote.http.BaiduRequest;
import com.blackdog.musiclibrary.remote.http.BaseRequest;
import com.blackdog.musiclibrary.remote.http.KugouRequest;
import com.lzx.starrysky.manager.MediaSessionConnection;
import com.lzx.starrysky.manager.MusicManager;
import com.lzx.starrysky.model.SongInfo;

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
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        MediaSessionConnection.getInstance().disconnect();
    }

    public void searchSong(View view) {
        String songName = mEtSong.getText().toString();
        new KugouRequest().searchMusic(1, 5, songName, new BaseRequest.RequectCallBack() {
            @Override
            public void onSucc(final List<SongInfo> musics) {
                MusicManager.getInstance().playMusicByInfo(musics.get(0));
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
