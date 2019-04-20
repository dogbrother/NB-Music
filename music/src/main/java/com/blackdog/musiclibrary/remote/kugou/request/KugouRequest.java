package com.blackdog.musiclibrary.remote.kugou.request;

import android.text.TextUtils;


import com.blackdog.musiclibrary.model.Song;
import com.blackdog.musiclibrary.remote.base.BaseRequest;
import com.blackdog.musiclibrary.remote.kugou.model.KugouSong;

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

public class KugouRequest implements BaseRequest {

    private static final String TAG = "KugouRequest";

    @Override
    public void searchMusic(int page, int count, String name, RequectCallBack callBack) {
        searhMusicInternal(page, count, name, callBack);
    }

    private boolean requestSongDetail(KugouSong song) throws Exception {
        KugouRequestInterface kugouRequestInterface = RETROFIT.create(KugouRequestInterface.class);

        Call<ResponseBody> call = kugouRequestInterface.queryMusicDetail(song.getHash());
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
        if (reponseJson.optInt("status") != 1) {
            return false;
        }
        song.setDownloadUrl(reponseJson.optString("url"))
                .setSize(reponseJson.optLong("fileSize") + "")
                .setSinger(reponseJson.optString("singerName"));
        return true;
    }

    private void searhMusicInternal(int page, int count, String name, final RequectCallBack callBack) {
        KugouRequestInterface kugouRequestInterface = RETROFIT.create(KugouRequestInterface.class);
        Observable<ResponseBody> observable = kugouRequestInterface.searchMusic(page, count, name);
        observable.subscribeOn(Schedulers.io())
                .map(new Function<ResponseBody, List<Song>>() {
                    @Override
                    public List<Song> apply(ResponseBody response) throws Exception {
                        JSONObject responseJson;
                        responseJson = new JSONObject(response.string());
                        JSONArray songList = responseJson.optJSONObject("data").optJSONArray("lists");
                        List<Song> songs = new ArrayList<>();
                        if (songList != null) {
                            for (int i = 0; i < songList.length(); i++) {
                                JSONObject songJson = songList.optJSONObject(i);
                                KugouSong song = new KugouSong();
                                song.setHash(getHash(songJson));
                                song.setSongName(songJson.optString("SongName"));
                                try {
                                    if (requestSongDetail(song)) {
                                        songs.add(song);
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                        return songs;
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .doOnError(new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        if (callBack != null)
                            callBack.onError(throwable.getMessage());
                    }
                })
                .doOnNext(new Consumer<List<Song>>() {
                    @Override
                    public void accept(List<Song> songs) throws Exception {
                        if (callBack != null)
                            callBack.onSucc(songs);
                    }
                }).subscribe();
    }


    private String getHash(JSONObject jsonObject) {
        String hash = jsonObject.optString("FileHash");
        String sqFileHash = jsonObject.optString("SQFileHash");
        String hqFileHash = jsonObject.optString("HQFileHash");
        if (!TextUtils.isEmpty(hqFileHash) && !hqFileHash.equals("00000000000000000000000000000000")) {
            hash = hqFileHash;
        }
        if (!TextUtils.isEmpty(sqFileHash) && !sqFileHash.equals("00000000000000000000000000000000")) {
            hash = sqFileHash;
        }
        return hash;
    }
}
