package com.blackdog.musiclibrary.remote.baidu.request;

import android.text.TextUtils;


import com.blackdog.musiclibrary.model.RequestCallBack;
import com.blackdog.musiclibrary.model.Song;
import com.blackdog.musiclibrary.remote.baidu.model.BaiduSong;
import com.blackdog.musiclibrary.remote.base.BaseRequest;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;

public class BaiduRequest extends BaseRequest {
    private boolean requestSongDetailInfo(BaiduSong song) throws Exception {
        BaiduRequestInterface requestInterface = RETROFIT.create(BaiduRequestInterface.class);
        Call<ResponseBody> call = requestInterface.queryMusicDetail(song.getBaiduSongId());
        Response<ResponseBody> detialReponse = call.execute();
        if (!detialReponse.isSuccessful()) {
            return false;
        }
        ResponseBody detailResultBody = detialReponse.body();
        String detailResult = detailResultBody.string();
        if (TextUtils.isEmpty(detailResult)) {
            return false;
        }
        JSONObject reponseJson = new JSONObject(detailResult);
        JSONArray songListArr = reponseJson.optJSONObject("data").optJSONArray("songList");
        if (songListArr.length() >= 1) {
            JSONObject songDetailJson = songListArr.getJSONObject(0);
            song.setDownloadUrl(songDetailJson.optString("songLink"));
            song.setSongName(songDetailJson.optString("songName"));
            song.setSize(String.valueOf(songDetailJson.optLong("size")));
            song.setDuration(songDetailJson.optLong("time"));
            song.setSinger(songDetailJson.optString("artistName"));
        }
        return true;
    }


    @Override
    public void searchInternal(int page, int count, String name, final RequestCallBack callBack) {
        BaiduRequestInterface requestInterface = RETROFIT.create(BaiduRequestInterface.class);
        Observable<ResponseBody> observable = requestInterface.search(name, page, count);
        observable.subscribeOn(Schedulers.io())
                .map(new Function<ResponseBody, List<Song>>() {
                    @Override
                    public List<Song> apply(ResponseBody responseBody) throws Exception {
                        String result = responseBody.string();
                        JSONObject responseJson = new JSONObject(result);
                        JSONArray songList = responseJson.optJSONArray("song_list");
                        final List<Song> musics = new ArrayList<>();
                        for (int i = 0; i < songList.length(); i++) {
                            JSONObject song = songList.getJSONObject(i);
                            BaiduSong music = new BaiduSong();
                            music.setBaiduSongId(song.optString("song_id"));
                            try {
                                if (requestSongDetailInfo(music)) {
                                    musics.add(music);
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            musics.add(music);
                        }
                        return musics;
                    }
                })
                .doOnError(new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {

                    }
                })
                .doOnNext(new Consumer<List<Song>>() {
                    @Override
                    public void accept(List<Song> songs) throws Exception {
                        if (callBack != null)
                            callBack.onSucc(songs);
                    }
                }).observeOn(AndroidSchedulers.mainThread())
                .subscribe();
    }
}
