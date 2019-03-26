package com.lzx.starrysky.model;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.Nullable;

import java.util.Objects;


/**
 * 面向用户的音频信息
 */
public class SongInfo implements Parcelable {
    private String songId = ""; //音乐id
    private String songName = ""; //音乐标题
    private String songCover = ""; //音乐封面
    private String songHDCover = ""; //专辑封面(高清)
    private String songSquareCover = ""; //专辑封面(正方形)
    private String songRectCover = ""; //专辑封面(矩形)
    private String songRoundCover = ""; //专辑封面(圆形)
    private String songNameKey = "";
    private Bitmap songCoverBitmap;
    private String songUrl = ""; //音乐播放地址
    private String genre = ""; //类型（流派）
    private String type = ""; //类型
    private String size = "0"; //音乐大小
    private long duration = -1; //音乐长度
    private String artist = ""; //音乐艺术家
    private String artistKey = "";
    private String artistId = ""; //音乐艺术家id
    private String downloadUrl = ""; //音乐下载地址
    private String site = ""; //地点
    private int favorites = 0; //喜欢数
    private int playCount = 0; //播放数
    private int trackNumber = -1; //媒体的曲目号码（序号：1234567……）
    private String language = "";//语言
    private String country = ""; //地区
    private String proxyCompany = "";//代理公司
    private String publishTime = "";//发布时间
    private String year = ""; //录制音频文件的年份
    private String modifiedTime = ""; //最后修改时间
    private String description = ""; //音乐描述
    private String versions = ""; //版本
    private String mimeType = "";

    private String albumId = "";    //专辑id
    private String albumName = "";  //专辑名称
    private String albumNameKey = "";
    private String albumCover = ""; //专辑封面
    private String albumHDCover = ""; //专辑封面(高清)
    private String albumSquareCover = ""; //专辑封面(正方形)
    private String albumRectCover = ""; //专辑封面(矩形)
    private String albumRoundCover = ""; //专辑封面(圆形)
    private String albumArtist = "";     //专辑艺术家
    private int albumSongCount = 0;      //专辑音乐数
    private int albumPlayCount = 0;      //专辑播放数

    public String getSongId() {
        return songId;
    }

    public SongInfo setSongId(String songId) {
        this.songId = songId;
        return this;
    }

    public String getSongName() {
        return songName;
    }

    public SongInfo setSongName(String songName) {
        this.songName = songName;
        return this;

    }

    public String getSongCover() {
        return songCover;
    }

    public SongInfo setSongCover(String songCover) {
        this.songCover = songCover;
        return this;

    }

    public String getSongHDCover() {
        return songHDCover;
    }

    public SongInfo setSongHDCover(String songHDCover) {
        this.songHDCover = songHDCover;
        return this;

    }

    public String getSongSquareCover() {
        return songSquareCover;
    }

    public SongInfo setSongSquareCover(String songSquareCover) {
        this.songSquareCover = songSquareCover;
        return this;

    }

    public String getSongRectCover() {
        return songRectCover;
    }

    public SongInfo setSongRectCover(String songRectCover) {
        this.songRectCover = songRectCover;
        return this;

    }

    public String getSongRoundCover() {
        return songRoundCover;
    }

    public SongInfo setSongRoundCover(String songRoundCover) {
        this.songRoundCover = songRoundCover;
        return this;

    }

    public String getSongNameKey() {
        return songNameKey;
    }

    public SongInfo setSongNameKey(String songNameKey) {
        this.songNameKey = songNameKey;
        return this;

    }

    public Bitmap getSongCoverBitmap() {
        return songCoverBitmap;
    }

    public SongInfo setSongCoverBitmap(Bitmap songCoverBitmap) {
        this.songCoverBitmap = songCoverBitmap;
        return this;

    }

    public String getSongUrl() {
        return songUrl;
    }

    public SongInfo setSongUrl(String songUrl) {
        this.songUrl = songUrl;
        return this;

    }

    public String getGenre() {
        return genre;
    }

    public SongInfo setGenre(String genre) {
        this.genre = genre;
        return this;

    }

    public String getType() {
        return type;
    }

    public SongInfo setType(String type) {
        this.type = type;
        return this;

    }

    public String getSize() {
        return size;
    }

    public SongInfo setSize(String size) {
        this.size = size;
        return this;

    }

    public long getDuration() {
        return duration;
    }

    public SongInfo setDuration(long duration) {
        this.duration = duration;
        return this;

    }

    public String getArtist() {
        return artist;
    }

    public SongInfo setArtist(String artist) {
        this.artist = artist;
        return this;

    }

    public String getArtistKey() {
        return artistKey;
    }

    public SongInfo setArtistKey(String artistKey) {
        this.artistKey = artistKey;
        return this;

    }

    public String getArtistId() {
        return artistId;
    }

    public SongInfo setArtistId(String artistId) {
        this.artistId = artistId;
        return this;

    }

    public String getDownloadUrl() {
        return downloadUrl;
    }

    public SongInfo setDownloadUrl(String downloadUrl) {
        this.downloadUrl = downloadUrl;
        return this;

    }

    public String getSite() {
        return site;
    }

    public SongInfo setSite(String site) {
        this.site = site;
        return this;

    }

    public int getFavorites() {
        return favorites;
    }

    public SongInfo setFavorites(int favorites) {
        this.favorites = favorites;
        return this;

    }

    public int getPlayCount() {
        return playCount;
    }

    public SongInfo setPlayCount(int playCount) {
        this.playCount = playCount;
        return this;

    }

    public int getTrackNumber() {
        return trackNumber;
    }

    public SongInfo setTrackNumber(int trackNumber) {
        this.trackNumber = trackNumber;
        return this;

    }

    public String getLanguage() {
        return language;
    }

    public SongInfo setLanguage(String language) {
        this.language = language;
        return this;

    }

    public String getCountry() {
        return country;
    }

    public SongInfo setCountry(String country) {
        this.country = country;
        return this;

    }

    public String getProxyCompany() {
        return proxyCompany;
    }

    public SongInfo setProxyCompany(String proxyCompany) {
        this.proxyCompany = proxyCompany;
        return this;

    }

    public String getPublishTime() {
        return publishTime;
    }

    public SongInfo setPublishTime(String publishTime) {
        this.publishTime = publishTime;
        return this;

    }

    public String getYear() {
        return year;
    }

    public SongInfo setYear(String year) {
        this.year = year;
        return this;

    }

    public String getModifiedTime() {
        return modifiedTime;
    }

    public SongInfo setModifiedTime(String modifiedTime) {
        this.modifiedTime = modifiedTime;
        return this;

    }

    public String getDescription() {
        return description;
    }

    public SongInfo setDescription(String description) {
        this.description = description;
        return this;

    }

    public String getVersions() {
        return versions;
    }

    public SongInfo setVersions(String versions) {
        this.versions = versions;
        return this;

    }

    public String getMimeType() {
        return mimeType;
    }

    public SongInfo setMimeType(String mimeType) {
        this.mimeType = mimeType;
        return this;

    }

    public String getAlbumId() {
        return albumId;
    }

    public SongInfo setAlbumId(String albumId) {
        this.albumId = albumId;
        return this;

    }

    public String getAlbumName() {
        return albumName;
    }

    public SongInfo setAlbumName(String albumName) {
        this.albumName = albumName;
        return this;

    }

    public String getAlbumNameKey() {
        return albumNameKey;
    }

    public SongInfo setAlbumNameKey(String albumNameKey) {
        this.albumNameKey = albumNameKey;
        return this;

    }

    public String getAlbumCover() {
        return albumCover;
    }

    public SongInfo setAlbumCover(String albumCover) {
        this.albumCover = albumCover;
        return this;

    }

    public String getAlbumHDCover() {
        return albumHDCover;
    }

    public SongInfo setAlbumHDCover(String albumHDCover) {
        this.albumHDCover = albumHDCover;
        return this;

    }

    public String getAlbumSquareCover() {
        return albumSquareCover;
    }

    public SongInfo setAlbumSquareCover(String albumSquareCover) {
        this.albumSquareCover = albumSquareCover;
        return this;

    }

    public String getAlbumRectCover() {
        return albumRectCover;
    }

    public SongInfo setAlbumRectCover(String albumRectCover) {
        this.albumRectCover = albumRectCover;
        return this;

    }

    public String getAlbumRoundCover() {
        return albumRoundCover;
    }

    public SongInfo setAlbumRoundCover(String albumRoundCover) {
        this.albumRoundCover = albumRoundCover;
        return this;

    }

    public String getAlbumArtist() {
        return albumArtist;
    }

    public SongInfo setAlbumArtist(String albumArtist) {
        this.albumArtist = albumArtist;
        return this;

    }

    public int getAlbumSongCount() {
        return albumSongCount;
    }

    public SongInfo setAlbumSongCount(int albumSongCount) {
        this.albumSongCount = albumSongCount;
        return this;

    }

    public int getAlbumPlayCount() {
        return albumPlayCount;
    }

    public SongInfo setAlbumPlayCount(int albumPlayCount) {
        this.albumPlayCount = albumPlayCount;
        return this;

    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.songId);
        dest.writeString(this.songName);
        dest.writeString(this.songCover);
        dest.writeString(this.songHDCover);
        dest.writeString(this.songSquareCover);
        dest.writeString(this.songRectCover);
        dest.writeString(this.songRoundCover);
        dest.writeString(this.songNameKey);
        dest.writeParcelable(this.songCoverBitmap, flags);
        dest.writeString(this.songUrl);
        dest.writeString(this.genre);
        dest.writeString(this.type);
        dest.writeString(this.size);
        dest.writeLong(this.duration);
        dest.writeString(this.artist);
        dest.writeString(this.artistKey);
        dest.writeString(this.artistId);
        dest.writeString(this.downloadUrl);
        dest.writeString(this.site);
        dest.writeInt(this.favorites);
        dest.writeInt(this.playCount);
        dest.writeInt(this.trackNumber);
        dest.writeString(this.language);
        dest.writeString(this.country);
        dest.writeString(this.proxyCompany);
        dest.writeString(this.publishTime);
        dest.writeString(this.year);
        dest.writeString(this.modifiedTime);
        dest.writeString(this.description);
        dest.writeString(this.versions);
        dest.writeString(this.mimeType);
        dest.writeString(this.albumId);
        dest.writeString(this.albumName);
        dest.writeString(this.albumNameKey);
        dest.writeString(this.albumCover);
        dest.writeString(this.albumHDCover);
        dest.writeString(this.albumSquareCover);
        dest.writeString(this.albumRectCover);
        dest.writeString(this.albumRoundCover);
        dest.writeString(this.albumArtist);
        dest.writeInt(this.albumSongCount);
        dest.writeInt(this.albumPlayCount);
    }

    public SongInfo() {
    }

    protected SongInfo(Parcel in) {
        this.songId = in.readString();
        this.songName = in.readString();
        this.songCover = in.readString();
        this.songHDCover = in.readString();
        this.songSquareCover = in.readString();
        this.songRectCover = in.readString();
        this.songRoundCover = in.readString();
        this.songNameKey = in.readString();
        this.songCoverBitmap = in.readParcelable(Bitmap.class.getClassLoader());
        this.songUrl = in.readString();
        this.genre = in.readString();
        this.type = in.readString();
        this.size = in.readString();
        this.duration = in.readLong();
        this.artist = in.readString();
        this.artistKey = in.readString();
        this.artistId = in.readString();
        this.downloadUrl = in.readString();
        this.site = in.readString();
        this.favorites = in.readInt();
        this.playCount = in.readInt();
        this.trackNumber = in.readInt();
        this.language = in.readString();
        this.country = in.readString();
        this.proxyCompany = in.readString();
        this.publishTime = in.readString();
        this.year = in.readString();
        this.modifiedTime = in.readString();
        this.description = in.readString();
        this.versions = in.readString();
        this.mimeType = in.readString();
        this.albumId = in.readString();
        this.albumName = in.readString();
        this.albumNameKey = in.readString();
        this.albumCover = in.readString();
        this.albumHDCover = in.readString();
        this.albumSquareCover = in.readString();
        this.albumRectCover = in.readString();
        this.albumRoundCover = in.readString();
        this.albumArtist = in.readString();
        this.albumSongCount = in.readInt();
        this.albumPlayCount = in.readInt();
    }

    public static final Parcelable.Creator<SongInfo> CREATOR = new Parcelable.Creator<SongInfo>() {
        @Override
        public SongInfo createFromParcel(Parcel source) {
            return new SongInfo(source);
        }

        @Override
        public SongInfo[] newArray(int size) {
            return new SongInfo[size];
        }
    };

    @Override
    public boolean equals(@Nullable Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        SongInfo songInfo = (SongInfo) obj;
        return Objects.equals(songId, songInfo.getSongId()) &&
                Objects.equals(songUrl, songInfo.getSongUrl());
    }
}
