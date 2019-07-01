package com.blackdog.musiclibrary.local;

import android.os.AsyncTask;
import android.text.TextUtils;
import android.util.Log;

import com.blackdog.greendao.SongDao;
import com.blackdog.musiclibrary.local.sqlite.SqlCenter;
import com.blackdog.musiclibrary.model.RequestCallBack;
import com.blackdog.musiclibrary.model.Song;
import com.blackdog.musiclibrary.remote.base.ChannelMusicFactory;

import org.greenrobot.greendao.query.QueryBuilder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LocalMusicManager {

    private static final String TAG = "LocalMusicManager";

    private LocalMusicManager() {

    }

    private static LocalMusicManager sInstance = new LocalMusicManager();

    public static LocalMusicManager getInstance() {
        return sInstance;
    }

    public void save(Song song) {
        if (song == null || TextUtils.isEmpty(song.getDownloadUrl())) {
            Log.i(TAG, "download url is empty");
            return;
        }
        Song saveSong = SqlCenter.getInstance().getSongDao().queryBuilder()
                .where(SongDao.Properties.SongName.eq(song.getSongName()))
                .where(SongDao.Properties.Singer.eq(song.getSinger()))
                .where(SongDao.Properties.Size.eq(song.getSize()))
                .where(SongDao.Properties.Duration.eq(song.getDuration()))
                .build().unique();
        if (saveSong != null) {
            song.setId(saveSong.getId());
            SqlCenter.getInstance().getSongDao().update(song);
        } else {
            SqlCenter.getInstance().getSongDao().save(song);
            if (mSongChangeListeners.containsKey(song.getChannelName())) {
                List<SongChangeListener> listeners = mSongChangeListeners.get(song.getChannelName());
                for (SongChangeListener listener : listeners) {
                    listener.onAdd(song);
                }
            }
        }
    }

    public void removeMusic(Song song) {
        SqlCenter.getInstance().getSongDao().delete(song);
        if (mSongChangeListeners.containsKey(song.getChannelName())) {
            List<SongChangeListener> listeners = mSongChangeListeners.get(song.getChannelName());
            for (SongChangeListener listener : listeners) {
                listener.onDelete(song);
            }
        }
    }

    public void queryMusic(int channelId, int offset, int count, RequestCallBack callBack) {
        new QueryAsyncTask(channelId, offset, count, callBack).execute();
    }

    public void queryMusic(int offset, int count, RequestCallBack callBack) {
        queryMusic(-1, offset, count, callBack);
    }

    private static class QueryAsyncTask extends AsyncTask<Void, Void, List<Song>> {

        private int mChannelId;
        private int mOffset;
        private int mCount;
        private RequestCallBack mCallBack;

        public QueryAsyncTask(int channelId, int offset, int count, RequestCallBack callBack) {
            mOffset = offset;
            mCount = count;
            mChannelId = channelId;
            mCallBack = callBack;
        }

        @Override
        protected List<Song> doInBackground(Void... voids) {
            QueryBuilder<Song> queryBuilder = SqlCenter.getInstance().getSongDao()
                    .queryBuilder()
                    .limit(mCount)
                    .offset(mOffset);
            if (mChannelId != -1) {
                queryBuilder = queryBuilder.where(SongDao.Properties.ChannelName.eq(ChannelMusicFactory.getChannelName(mChannelId)));
            }
            List<Song> list = queryBuilder
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


    public interface SongChangeListener {
        void onAdd(Song song);

        void onDelete(Song song);
    }

    private Map<String, List<SongChangeListener>> mSongChangeListeners = new HashMap<>();


    public void registerSongChangeListener(String channel, SongChangeListener listener) {
        if (!mSongChangeListeners.containsKey(channel)) {
            mSongChangeListeners.put(channel, new ArrayList<SongChangeListener>());
        }
        List<SongChangeListener> listeners = mSongChangeListeners.get(channel);
        listeners.add(listener);
        mSongChangeListeners.put(channel, listeners);
    }

    public void unRegisterSongChannelListener(String channel, SongChangeListener listener) {
        if (mSongChangeListeners.containsKey(channel)) {
            List<SongChangeListener> listeners = mSongChangeListeners.get(channel);
            listeners.remove(listener);
            mSongChangeListeners.put(channel, listeners);
        }
    }


}
