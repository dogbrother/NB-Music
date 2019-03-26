package com.blackdog.musiclibrary.remote.http;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;


import com.lzx.starrysky.model.SongInfo;

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

public class BaiduRequest implements BaseRequest {

    private static final int MSG_CALL_BACK_ERR = 0;
    private static final int MSG_CALL_BACK_SUCC = 1;
    private static final String TAG = "BaiduRequest";
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
                        List<SongInfo> songInfos = (List<SongInfo>) msg.obj;
                        mRequectCallBack.onSucc(songInfos);
                    }
                    break;
            }
            mRequectCallBack = null;
        }
    };

    @Override
    public void searchMusic(final int page, int count, String name, final RequectCallBack callBack) {
        mRequectCallBack = callBack;
        if (TextUtils.isEmpty(name)) {
            Message.obtain(mHandler, MSG_CALL_BACK_ERR, "songName is empty").sendToTarget();
            return;
        }
        StringBuilder urlBuilder = new StringBuilder();
        urlBuilder.append("http://musicapi.qianqian.com/v1/restserver/ting?")
                .append("query=").append(name)
                .append("&method=baidu.ting.search.common")
                .append("&format=json")
                .append("&page_no=").append(page)
                .append("&page_size=").append(count);
        String url = urlBuilder.toString();
        OkHttpClient client = new OkHttpClient();
        final Request request = new Request.Builder()
                .url(url)
                .get()
                .header("referer", "http://music.baidu.com/")
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                Message.obtain(mHandler, MSG_CALL_BACK_ERR, "io error").sendToTarget();
            }

            @Override
            public void onResponse(final Call call, Response response) throws IOException {
                try {
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
                    JSONObject responseJson = new JSONObject(result);
                    JSONArray songList = responseJson.optJSONArray("song_list");
                    final List<SongInfo> musics = new ArrayList<>();
                    for (int i = 0; i < songList.length(); i++) {
                        JSONObject song = songList.getJSONObject(i);
                        SongInfo music = new SongInfo();
                        music.setSongId(song.optString("song_id"))
                                .setSongName(song.optString("title"))
                                .setArtist(song.optString("author"));
                        musics.add(music);
                    }

                    for (SongInfo music : musics) {
                        StringBuilder urlBuilder = new StringBuilder();
                        urlBuilder.append("http://music.baidu.com/data/music/links?")
                                .append("songIds=").append(music.getSongId());
                        String url = urlBuilder.toString();
                        OkHttpClient client = new OkHttpClient();
                        final Request request = new Request.Builder()
                                .url(url)
                                .get()
                                .header("referer", String.format("http://music.baidu.com/song/%s", music.getSongId()))
                                .build();
                        Response detialReponse = client.newCall(request).execute();
                        if (!detialReponse.isSuccessful()) {
                            Log.i(TAG, "detail reponse is not successful");
                            continue;
                        }
                        ResponseBody detailResultBody = detialReponse.body();
                        if (detailResultBody == null) {
                            continue;
                        }
                        String detailResult = detailResultBody.string();
                        if (TextUtils.isEmpty(detailResult)) {
                            continue;
                        }
                        JSONObject reponseJson = new JSONObject(detailResult);
                        JSONArray songListArr = reponseJson.optJSONObject("data").optJSONArray("songList");
                        if (songListArr.length() >= 1) {
                            JSONObject songDetailJson = songListArr.getJSONObject(0);
                            music.setSongUrl(songDetailJson.optString("songLink"))
                                    .setSize(String.valueOf(songDetailJson.optLong("size")))
                                    .setDuration(songDetailJson.optLong("time"))
                                    .setArtist(songDetailJson.optString("artistName"))
                                    .setVersions(songDetailJson.optString("version"));
                        }
                    }
                    Message.obtain(mHandler, MSG_CALL_BACK_SUCC, musics).sendToTarget();
                } catch (Exception e) {
                    e.printStackTrace();
                    Message.obtain(mHandler, MSG_CALL_BACK_ERR, "baidu music parse error").sendToTarget();
                }
            }
        });
    }


}
