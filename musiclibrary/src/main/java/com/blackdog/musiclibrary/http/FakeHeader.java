package com.blackdog.musiclibrary.http;

public interface FakeHeader {

    interface CommonHeader {
        String ACCEPT = "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8";
        String ACCEPT_CHARSET = "UTF-8,*;q=0.5";
        String ACCEPT_ENCODING = "gzip,deflate,sdch";
        String ACCEPT_LANGUAGE = "en-US,en;q=0.8";
        String USER_AGENT = "Mozilla/5.0 (Windows NT 10.0; WOW64; rv:60.0) Gecko/20100101 Firefox/60.0";
        String REFERER = "https://www.google.com";
    }

    //QQ下载音乐不能没有User-Agent
    //百度下载音乐User-Agent不能是浏览器
    //下载时的headers
    interface WgetHeader {
        String ACCEPT = "*/*";
        String ACCEPT_ENCODING = "identity";
        String USER_AGENT = "Wget/1.19.5 (darwin17.5.0)";
    }

    String IOS_USERAGENT = "Mozilla/5.0 (iPhone; CPU iPhone OS 9_1 like Mac OS X) AppleWebKit/601.1.46 (KHTML, like Gecko) Version/9.0 Mobile/13B143 Safari/601.1";


}
