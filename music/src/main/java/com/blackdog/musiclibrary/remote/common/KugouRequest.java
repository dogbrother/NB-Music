package com.blackdog.musiclibrary.remote.common;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;


import com.blackdog.musiclibrary.local.sqlite.SqlCenter;
import com.blackdog.musiclibrary.model.Song;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class KugouRequest implements BaseRequest {

    private static final int MSG_CALL_BACK_ERR = 0;
    private static final int MSG_CALL_BACK_SUCC = 1;
    private static final String TAG = "KugouRequest";
    private RequectCallBack mRequectCallBack;
    private Handler mHandler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case MSG_CALL_BACK_ERR:
                    if (mRequectCallBack != null) {
                        String errMsg = (String) msg.obj;
                        mRequectCallBack.onError(errMsg);
                    }
                    break;
                case MSG_CALL_BACK_SUCC:
                    if (mRequectCallBack != null) {
                        List<Song> songs = (List<Song>) msg.obj;
                        mRequectCallBack.onSucc(songs);
                    }
                    break;
            }
            mRequectCallBack = null;
        }
    };

    @Override
    public void searchMusic(int page, int count, String name, RequectCallBack callBack) {
        mRequectCallBack = callBack;
        if (TextUtils.isEmpty(name)) {
            Message.obtain(mHandler, MSG_CALL_BACK_ERR, "songName is empty").sendToTarget();
            return;
        }
        StringBuilder urlBuilder = new StringBuilder();
        urlBuilder.append("http://songsearch.kugou.com/song_search_v2?")
                .append("keyword=").append(name)
                .append("&platform=WebFilter")
                .append("&format=json")
                .append("&page=").append(page)
                .append("&pagesize=").append(count);
        String url = urlBuilder.toString();
        OkHttpClient client = new OkHttpClient();
        final Request request = new Request.Builder()
                .url(url)
                .get()
                .header("referer", "http://www.kugou.com")
                .build();
        client.newCall(request)
                .enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        e.printStackTrace();
                        Message.obtain(mHandler, MSG_CALL_BACK_ERR, "io error").sendToTarget();
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        ResponseBody responseBody = response.body();
                        if (responseBody == null) {
                            Message.obtain(mHandler, MSG_CALL_BACK_ERR, "request error").sendToTarget();
                            return;
                        }
                        String result = responseBody.string();
                        if (TextUtils.isEmpty(result)) {
                            Message.obtain(mHandler, MSG_CALL_BACK_ERR, "request error").sendToTarget();
                            return;
                        }
                        JSONObject responseJson;
                        try {
                            responseJson = new JSONObject(result);
                        } catch (Exception e) {
                            e.printStackTrace();
                            Message.obtain(mHandler, MSG_CALL_BACK_ERR, "request error").sendToTarget();
                            return;
                        }
                        int errorCode = responseJson.optInt("error_code");
                        if (errorCode != 0) {
                            Log.i(TAG, "errorCode : " + errorCode);
                            onFailure(call, new IOException("errorCode is null"));
                            return;
                        }
                        JSONArray songList = responseJson.optJSONObject("data").optJSONArray("lists");
                        List<Song> songs = new ArrayList<>();
                        if (songList != null) {
                            for (int i = 0; i < songList.length(); i++) {
                                JSONObject SongJson = songList.optJSONObject(i);
                                Song song = new Song();
                                String hash = "";
                                do {
                                    String sqFileHash = SongJson.optString("SQFileHash");
                                    if (!TextUtils.isEmpty(sqFileHash) && !sqFileHash.equals("00000000000000000000000000000000")) {
                                        hash = sqFileHash;
                                        break;
                                    }
                                    String hqFileHash = SongJson.optString("HQFileHash");
                                    if (!TextUtils.isEmpty(hqFileHash) && !hqFileHash.equals("00000000000000000000000000000000")) {
                                        hash = hqFileHash;
                                        break;
                                    }
                                    hash = SongJson.optString("FileHash");
                                } while (false);
                                song.setSongName(SongJson.optString("SongName"));
                                try {
                                    requestSongDetail(song, hash);
                                    SqlCenter.getInstance().getDaoSession().getSongDao().insertOrReplace(song);
                                    Log.i(TAG, "song id : " + song.getId());
                                    songs.add(song);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                        Message.obtain(mHandler, MSG_CALL_BACK_SUCC, songs).sendToTarget();
                    }
                });
    }


    private void requestSongDetail(Song song, String hash) throws Exception {
        Request request = new Request.Builder()
                .header("referer", "http://m.kugou.com")
                .header("User-Agent", FakeHeader.IOS_USERAGENT)
                .url("http://m.kugou.com/app/i/getSongInfo.php?cmd=playInfo&hash=" + hash)
                .build();
        OkHttpClient client = new OkHttpClient();
        client.newCall(request).execute();
        Response detialReponse = client.newCall(request).execute();
        if (!detialReponse.isSuccessful()) {
            throw new Exception("parse err");
        }
        ResponseBody detailResultBody = detialReponse.body();
        if (detailResultBody == null) {
            throw new Exception("parse err");
        }
        String detailResult = detailResultBody.string();
        if (TextUtils.isEmpty(detailResult)) {
            throw new Exception("parse err");
        }
        JSONObject reponseJson = new JSONObject(detailResult);
        if (reponseJson.optInt("status") != 1) {
            throw new Exception("parse err");
        }
        song.setDownloadUrl(reponseJson.optString("url"))
                .setSize(reponseJson.optLong("fileSize") + "")
                .setSinger(reponseJson.optString("singerName"));
    }
}
