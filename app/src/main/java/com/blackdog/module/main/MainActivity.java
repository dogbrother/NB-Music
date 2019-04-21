package com.blackdog.module.main;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;


import com.blackdog.R;

import com.blackdog.module.channel.base.ChannelFragment;
import com.blackdog.musiclibrary.remote.base.ChannelMusicFactory;
import com.lzx.starrysky.manager.MediaSessionConnection;


public class MainActivity extends FragmentActivity {

    private final String TAG = "MainActivity";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        MediaSessionConnection.getInstance().connect();
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        Fragment testFragment = ChannelFragment.newInstance(ChannelMusicFactory.CHANNEL_KUGOU);
        fragmentTransaction.add(R.id.layout_content, testFragment);
        fragmentTransaction.commit();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        MediaSessionConnection.getInstance().disconnect();
    }

}
