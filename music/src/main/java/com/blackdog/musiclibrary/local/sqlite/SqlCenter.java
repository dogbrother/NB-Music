package com.blackdog.musiclibrary.local.sqlite;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.blackdog.nbmusic.DaoMaster;
import com.blackdog.nbmusic.DaoSession;


public class SqlCenter {
    private final static String DAO_NAME = "nb-musicdb";
    private static SqlCenter sInstance = new SqlCenter();
    private Context mContext;
    private DaoSession mDaoSession;
    private DaoMaster mDaoMaster;
    private SQLiteDatabase mDb;
    private static boolean mIsInit;

    private SqlCenter() {

    }

    public static SqlCenter getInstance() {
        return sInstance;
    }

    public void init(Context context) {
        if (mIsInit) {
            return;
        }
        mIsInit = true;
        mContext = context.getApplicationContext();
        DaoMaster.DevOpenHelper devOpenHelper = new DaoMaster.DevOpenHelper(mContext, DAO_NAME, null);
        mDb = devOpenHelper.getWritableDatabase();
        mDaoMaster = new DaoMaster(mDb);
        mDaoSession = mDaoMaster.newSession();
    }

    public DaoSession getDaoSession() {
        return mDaoSession;
    }
}