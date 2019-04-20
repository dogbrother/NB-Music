package com.blackdog.musiclibrary.remote.kugou.request;

import com.blackdog.musiclibrary.remote.base.FakeHeader;

import io.reactivex.Observable;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Query;

public interface KugouRequestInterface {
    String URL_SEARCH = "http://songsearch.kugou.com/song_search_v2?platform=WebFilter&format=json";
    String URL_DETAIL = "http://m.kugou.com/app/i/getSongInfo.php?cmd=playInfo";

    @GET(URL_SEARCH)
    @Headers({"referer:http://www.kugou.com"})
    Observable<ResponseBody> searchMusic(@Query("page") int page, @Query("pagesize") int pagesize, @Query("keyword") String keyword);

    @GET(URL_DETAIL)
    @Headers({"referer:http://m.kugou.com", "User-Agent:" + FakeHeader.IOS_USERAGENT})
    Call<ResponseBody> queryMusicDetail(@Query("hash") String hash);
}
