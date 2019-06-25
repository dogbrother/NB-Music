package com.blackdog.musiclibrary.remote.kugou.request;

import android.text.TextUtils;


import com.blackdog.musiclibrary.model.RequestCallBack;
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

public class KugouRequest extends BaseRequest {

    private static final String TAG = "KugouRequest";

    @Override
    public void searchInternal(int page, int count, String name, final RequestCallBack callBack) {
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
                                song.setSinger(songJson.optString("SingerName"));
                                song.setDuration(songJson.optInt("Duration"));
                                song.setSize(songJson.optLong("FileSize") + "");
                                try {
                                    songs.add(song);
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

    @Override
    public void searchDetail(final Song song, final RequestCallBack callBack) {
        final KugouSong kugouSong;
        if (!(song instanceof KugouSong)) {
            if (callBack != null) {
                callBack.onError("song convert error");
            }
            return;
        }
        kugouSong = (KugouSong) song;
        try {
            KugouRequestInterface kugouRequestInterface = RETROFIT.create(KugouRequestInterface.class);

            Observable<ResponseBody> observable = kugouRequestInterface.queryMusicDetail(kugouSong.getHash());
            observable.subscribeOn(Schedulers.io())
                    .map(new Function<ResponseBody, KugouSong>() {
                        @Override
                        public KugouSong apply(ResponseBody responseBody) throws Exception {
                            JSONObject reponseJson = new JSONObject(responseBody.string());
                            if (reponseJson.optInt("status") == 1) {
                                kugouSong.setDownloadUrl(reponseJson.optString("url"))
                                        .setChannelName("酷狗")
                                        .setSize(reponseJson.optLong("fileSize") + "");
                            }

                            return kugouSong;
                        }
                    })
                    .observeOn(AndroidSchedulers.mainThread())
                    .doOnError(new Consumer<Throwable>() {
                        @Override
                        public void accept(Throwable throwable) throws Exception {
                            if (callBack != null) {
                                callBack.onError(throwable.getMessage());
                            }
                        }
                    })
                    .doOnNext(new Consumer<KugouSong>() {
                        @Override
                        public void accept(KugouSong kugouSong) throws Exception {
                            if (callBack != null) {
                                List list = new ArrayList();
                                list.add(kugouSong);
                                callBack.onSucc(list);
                            }
                        }
                    })
                    .subscribe();


        } catch (Exception e) {
            e.printStackTrace();
        }

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
