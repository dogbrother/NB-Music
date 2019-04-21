package com.blackdog;

import android.app.PendingIntent;
import android.content.Intent;
import android.os.Environment;
import android.support.multidex.MultiDex;
import android.support.multidex.MultiDexApplication;

import com.blackdog.musiclibrary.local.sqlite.SqlCenter;
import com.blackdog.receiver.NotificationReceiver;
import com.blackdog.util.AppUtil;
import com.lzx.starrysky.manager.MusicManager;
import com.lzx.starrysky.playback.download.ExoDownload;


/**
 *
 */
public class App extends MultiDexApplication {

    public static String ACTION_PLAY_OR_PAUSE = "ACTION_PLAY_OR_PAUSE";
    public static String ACTION_NEXT = "ACTION_NEXT";
    public static String ACTION_PRE = "ACTION_PRE";
    public static String ACTION_FAVORITE = "ACTION_FAVORITE";
    public static String ACTION_LYRICS = "ACTION_LYRICS";
    private static App sInstance;

    private PendingIntent getPendingIntent(String action) {
        Intent intent = new Intent(action);
        intent.setClass(this, NotificationReceiver.class);
        return PendingIntent.getBroadcast(this, 0, intent, 0);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        MultiDex.install(this);
        AppUtil.setApplication(this);
        //初始化
        MusicManager.initMusicManager(this);
        //配置通知栏
//        NotificationConstructor constructor = new NotificationConstructor.Builder()
//                .setCreateSystemNotification(false)
//                .bulid();
//
//        MusicManager.getInstance().setNotificationConstructor(constructor);

        //设置缓存
        String destFileDir = Environment.getExternalStorageDirectory().getAbsolutePath() + "/nb-music";
        ExoDownload.getInstance().setOpenCache(true); //打开缓存开关
        ExoDownload.getInstance().setShowNotificationWhenDownload(true);
        ExoDownload.getInstance().setCacheDestFileDir(destFileDir); //设置缓存文件夹
        //初始化数据库
        SqlCenter.getInstance().init(this);
        sInstance = this;
    }

    public static App getInstance() {
        return sInstance;
    }
}
