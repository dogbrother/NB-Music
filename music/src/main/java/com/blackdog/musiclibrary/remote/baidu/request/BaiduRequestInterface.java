package com.blackdog.musiclibrary.remote.baidu.request;

import com.blackdog.musiclibrary.remote.baidu.model.BaiduSong;

import io.reactivex.Observable;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Query;

public interface BaiduRequestInterface {
    String URL_SEARCH = "http://musicapi.qianqian.com/v1/restserver/ting?method=baidu.ting.search.common&format=json";
    String URL_DETAIL = "http://music.baidu.com/data/music/links";

    @GET(URL_SEARCH)
    @Headers({"referer:http://music.baidu.com/"})
    Observable<ResponseBody> search(@Query("query") String name, @Query("page_no") int page, @Query("page_size") int count);

    @GET(URL_DETAIL)
    @Headers({"referer:http://music.baidu.com/song"})
    Call<ResponseBody> queryMusicDetail(@Query("songIds") String songId);


}
