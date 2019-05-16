package com.blackdog.musiclibrary.local;

import android.os.AsyncTask;

import com.blackdog.musiclibrary.local.sqlite.SqlCenter;
import com.blackdog.musiclibrary.model.RequestCallBack;
import com.blackdog.musiclibrary.model.Song;

import java.util.ArrayList;
import java.util.List;

public class LocalMusicManager {

    private LocalMusicManager() {

    }

    private static LocalMusicManager sInstance = new LocalMusicManager();

    public static LocalMusicManager getInstance() {
        return sInstance;
    }

    public void save(Song song) {
        SqlCenter.getInstance().getSongDao().save(song);
    }

    public void removeMusic(Song song) {
        SqlCenter.getInstance().getSongDao().delete(song);
    }

    public void removeMusic(long songId) {
        removeMusic(new Song().setId(songId));
    }

    public void queryMusic(int offset, int count, RequestCallBack callBack) {
        new QueryAsyncTask(offset, count, callBack).execute();
    }

    private static class QueryAsyncTask extends AsyncTask<Void, Void, List<Song>> {

        private int mOffset;
        private int mCount;
        private RequestCallBack mCallBack;

        public QueryAsyncTask(int offset, int count, RequestCallBack callBack) {
            mOffset = offset;
            mCount = count;
            mCallBack = callBack;
        }

        @Override
        protected List<Song> doInBackground(Void... voids) {
            List<Song> list = SqlCenter.getInstance().getSongDao().queryBuilder().limit(mCount)
                    .offset(mOffset)
                    .build()
                    .list();
            if (list == null) {
                list = new ArrayList<>();
            }
            return list;
        }

        @Override
        protected void onPostExecute(List<Song> songs) {
            super.onPostExecute(songs);
            if (mCallBack != null) {
                mCallBack.onSucc(songs);
            }
        }
    }


}
