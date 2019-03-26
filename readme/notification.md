### 通知栏集成说明

如果你想使用自定义样式的通知栏，需要在初始化的时候这样做，然后在布局中配置对应 id 和命名：

```java
if (BaseUtil.getCurProcessName(this).equals("your package name")) {
    NotificationCreater creater = new NotificationCreater.Builder()
            .setTargetClass("com.lzx.nicemusic.module.main.HomeActivity")
            .build();
    MusicLibrary musicLibrary = new MusicLibrary.Builder(this)
             .setNotificationCreater(creater)
             .build();
    musicLibrary.init();
}
```

如果你想使用集成好的系统通知栏，只需要在 `NotificationCreater` 中添加 `setCreateSystemNotification(true)`

```java
  if (BaseUtil.getCurProcessName(this).equals("your package name")) {
    NotificationCreater creater = new NotificationCreater.Builder()
            .setTargetClass("com.lzx.nicemusic.module.main.HomeActivity")
            .setCreateSystemNotification(true)
            .build();
    MusicLibrary musicLibrary = new MusicLibrary.Builder(this)
             .setNotificationCreater(creater)
             .build();
    musicLibrary.init();
}
```

只要添加 setNotificationCreater()，即可集成通知栏功能了。

### 自定义通知栏

自定义通知栏对资源的 id 和资源 的命名有一些规定。 

NotificationCreater 是一个配置通知栏一些属性的类，里面有一些属性：
```java
public class NotificationCreater implements Parcelable {
    private boolean isCreateSystemNotification;
    private boolean isNotificationCanClearBySystemBtn;
    private String targetClass;
    private String contentTitle;
    private String contentText;
    private PendingIntent startOrPauseIntent;
    private PendingIntent nextIntent;
    private PendingIntent preIntent;
    private PendingIntent closeIntent;
    private PendingIntent favoriteIntent;
    private PendingIntent lyricsIntent;
    private PendingIntent playIntent;
    private PendingIntent pauseIntent;
    private PendingIntent stopIntent;
    private PendingIntent downloadIntent;
    ...
}
```
1. targetClass 一定要设置，他是你点击通知栏后转跳的界面，填的是完整路径。
2. contentTitle 和 contentText 对应着 Notification 的 contentTitle 和 contentText，如果不设置，默认值为歌曲的歌名和艺术家名。
3. 后面的 PendingIntent 是通知栏里面一些按钮的点击处理 PendingIntent，lib 里面实现了 startOrPauseIntent，closeIntent，nextIntent 和 preIntent。其他需要自己 set 进去。具体用法下面会说到。
4. isCreateSystemNotification 为是否使用系统通知栏，如果使用则传 true ，不传或 false 为使用自定义通知栏
5. isNotificationCanClearBySystemBtn 字段的作用是在使用自定义通知栏的时候，暂停播放时可以点击清除按钮清除通知栏，
具体看 [Issues#20](https://github.com/lizixian18/MusicLibrary/issues/20) 的描述。


#### 一些约定

不同的手机通知栏背景色有的是浅色背景，有的是深色背景，当自定义通知栏的时候，就需要根据不同的背景颜色来使用不同的资源文件。
自定义通知栏有两种 RemoteView ，一个是普通的 ContentView ，一个是 BigContentView ,所以需要两个布局，而资源图片也需要两套，对应着不同的背景色。    
 
ContentView 和 BigContentView 效果如下所示:    
<a href="art/light2.png"><img src="art/light2.png" width="25%"/></a>
<a href="art/light1.png"><img src="art/light1.png" width="25%"/></a>  

#### 命名约定
 
##### 布局命名

请将你的两个布局按下面规则命名。

 | RemoteView 类型 | 命名  |
 | :--------   | :------   |
 | RemoteView    | view_notify_play.xml     |
 | BigRemoteView | view_notify_big_play.xml |
 
 **注意**  
 因为通知栏布局中的字体颜色是跟随系统的，所以请不要给布局文件中的 TextView 设置 textColor。如果是 title 的 TextView,请使用 ` style="@style/notification_title"` ,如果是 content
 的 TextView ,请使用 `@style/notification_info` ,这样颜色就会跟随系统了，例子：
 
 ```xml
<LinearLayout
    android:id="@+id/ll_custom_button2"
    android:layout_width="0.0dp"
    android:layout_height="wrap_content"
    android:layout_gravity="center"
    android:layout_weight="1.0"
    android:orientation="vertical"
    android:paddingBottom="2.0dp"
    android:paddingLeft="13.0dp"
    android:paddingTop="2.0dp">

    <TextView
        android:id="@+id/txt_notifySongName"
        style="@style/notification_title"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:ellipsize="end"
        android:lines="1"
        android:singleLine="true"
        android:text="Nice Music"
        android:textSize="16sp"
        android:textStyle="normal"/>

    <TextView
        android:id="@+id/txt_notifyArtistName"
        style="@style/notification_info"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:layout_marginTop="5dp"
        android:ellipsize="end"
        android:fadingEdge="horizontal"
        android:lines="1"
        android:singleLine="true"
        android:text="听你所想"
        android:textSize="13sp"/>
</LinearLayout>
 ```
 
具体可以参考 [NiceMusic](https://github.com/lizixian18/NiceMusic) 
 
 
##### id 命名

如果你的布局中的控件有下面说到的按钮或者封面，歌名和艺术家等元素，请将他们的控件 id 按约定命名，四个布局都一样，如果没有的话就不用管了。

| 通知栏控件名称  |   命名  |
| :--------     |   :----------   |
| 播放按钮       | img_notifyPlay    |
| 暂停按钮       | img_notifyPause    |
| 停止按钮       | img_notifyStop    |
| 播放或暂停按钮  | img_notifyPlayOrPause |
| 下一首按钮     | img_notifyNext    |
| 上一首按钮     | img_notifyPre    |
| 关闭按钮       | img_notifyClose    |
| 喜欢或收藏按钮  | img_notifyFavorite    |
| 桌面歌词按钮    | img_notifyLyrics    |
| 下载按钮       | img_notifyDownload    |
| 封面图片       | img_notifyIcon    |
| 歌名TextView   | txt_notifySongName    |
| 艺术家TextView  | txt_notifyArtistName    |

 
##### 资源命名
 
为了更好的UI效果，lib 中的通知栏上一首、下一首、播放、暂停、播放或暂停这五个按钮使用的资源是 `selector`，`selector` 里面就是你对应的 normal 和 pressed 图片了。    
因为上一首和下一首这两个按钮还需要判断是否有上一首和是否有下一首，而且没有上一首和下一首的时候你可能需要不同的样式，例如置灰等，所以对这两个按钮的图片资源命名也有一些约定。  
同样的，如果你的布局中有相应的资源，请将他们按约定命名，没有就不用管。
 
| 通知栏背景色  | 资源名称  |   命名  |
| :-------- | :--------   | :------   |
| 浅色背景   | 播放按钮 selector | notify_btn_light_play_selector.xml | 
| 浅色背景   | 暂停按钮 selector | notify_btn_light_pause_selector.xml | 
| 浅色背景   | 下一首按钮 selector | notify_btn_light_prev_selector.xml | 
| 浅色背景   | 上一首按钮 selector | notify_btn_light_prev_selector.xml | 
| 浅色背景   | 下一首按钮当没有下一首时的图片资源 | notify_btn_light_next_pressed | 
| 浅色背景   | 上一首按钮当没有上一首时的图片资源 | notify_btn_light_prev_pressed | 
| 浅色背景   | 喜欢或收藏按钮的图片资源 | notify_btn_light_favorite_normal | 
| 浅色背景   | 桌面歌词按钮的图片资源 | notify_btn_light_lyrics_normal | 
| 深色背景   | 播放按钮 selector | notify_btn_dark_play_selector.xml | 
| 深色背景   | 暂停按钮 selector | notify_btn_dark_pause_selector.xml | 
| 深色背景   | 下一首按钮 selector | notify_btn_dark_next_selector.xml | 
| 深色背景   | 上一首按钮 selector | notify_btn_dark_prev_selector.xml | 
| 深色背景   | 下一首按钮当没有下一首时的图片资源 | notify_btn_dark_next_pressed | 
| 深色背景   | 上一首按钮当没有上一首时的图片资源 | notify_btn_dark_prev_pressed | 
| 深色背景   | 喜欢或收藏按钮的图片资源 | notify_btn_dark_favorite_normal | 
| 深色背景   | 桌面歌词按钮的图片资源 | notify_btn_dark_lyrics_normal | 
| 深白通用   | 喜欢按钮被选中时的图片资源 | notify_btn_favorite_checked | 
| 深白通用   | 桌面歌词按钮选中时的图片资源 | notify_btn_lyrics_checked | 
| 深白通用   | 通知栏 smallIcon 图片资源 | icon_notification | 
| 深白通用   | 下载按钮暂 | 暂时没什么规定，可以随便命名 | 
 
 
如果除了上面说到的这么多按钮中没有包含你需要的，请提 issue，我会加上去。

**按约定创建好布局和资源后，你什么都不用做，通知栏已经集成好了。**

#### 点击事件

下面来说说按钮对应的点击事件：

点击事件是以广播的形式去执行的，目前支持的点击事件有上面 NotificationCreater 里面的 10 种：  
1. 播放或者暂停
2. 下一首
3. 上一首
4. 关闭通知栏
5. 喜欢或收藏
6. 桌面歌词
7. 播放
8. 暂停
9. 停止  
10. 下载

默认实现了 播放或者暂停、关闭通知栏、上一首、下一首的点击事件，其他需要自己实现，或者全部都自己实现，
实现方法如下：

1. 新建一个广播，比如 MyPlayerReceiver。
2. 定义你想实现的点击事件对应的 action ,比如我想实现关闭通知栏，喜欢或收藏和桌面歌词三个点击事件：
```java
 public static final String closeActionName = "com.lzx.nicemusic.android.Action_CLOSE";
 public static final String favoriteActionName = "com.lzx.nicemusic.android.Action_FAVORITE";
 public static final String lyricsActionName = "com.lzx.nicemusic.android.Action_Lyrics";
```
```xml
<receiver
    android:name=".receiver.MyPlayerReceiver"
    android:exported="false">
    <intent-filter>
        <action android:name="com.lzx.nicemusic.android.Action_CLOSE"/>
        <action android:name="com.lzx.nicemusic.android.Action_FAVORITE"/>
        <action android:name="com.lzx.nicemusic.android.Action_Lyrics"/>
    </intent-filter>
</receiver>
```

3. 然后新建对应的 PendingIntent
```java
PendingIntent closeIntent = getPendingIntent(closeActionName);
PendingIntent favoriteIntent = getPendingIntent(favoriteActionName);
PendingIntent lyricsIntent = getPendingIntent(lyricsActionName);

private PendingIntent getPendingIntent(String action) {
    Intent intent = new Intent(action);
    intent.setClass(this, MyPlayerReceiver.class);
    return PendingIntent.getBroadcast(this, 0, intent, 0);
}
```

4. 最后赋值给 NotificationCreater 即可
```java
NotificationCreater creater = new NotificationCreater.Builder()
    .setTargetClass("com.lzx.nicemusic.module.main.HomeActivity")
    .setCloseIntent(closeIntent)
    .setFavoriteIntent(favoriteIntent)
    .setLyricsIntent(lyricsIntent)
    .build();
```

其他点击事件也是一样。接下来，就可以在你自己的 MyPlayerReceiver 里面根据不同的 action 来进行操作了.

点击通知栏转跳到指定页面的时候，会默认传递当前的 SongInfo 信息。通过：
```java
SongInfo mSongInfo = intent.getParcelableExtra("songInfo");
```
来接收。 会接收到一个名为 `songInfo` 的 SongInfo 音频信息。

如果你想在点击通知栏的时候传递更多参数到指定的界面，则需要调用下面的方法更新 ContentIntent:
```java
Bundle bundle = new Bundle();
bundle.putString("name", "我是新添加的参数");
MusicManager.get().updateNotificationContentIntent(bundle, null);
```

然后在接收的时候这样：
```java
Bundle bundle = intent.getBundleExtra("bundleInfo");
if (bundle != null) {
    LogUtil.i("bundle = " + bundle.getString("name"));
}
```

`updateNotificationContentIntent(Bundle bundle, String targetClass)` 方法有两个参数：  
第一个是 Bundle ，因为 Bundle 实现了 Parcelable 接口，所以可以用于 IPC 数据通信，因此把你需要传递的数据封装成一个 Bundle 即可。在接收数据的时候，会接收到一个名为 `bundleInfo` 的 Bundle 数据，然后再从 Bundle 里面获取你对应的数据即可。  
第二个是 targetClass，targetClass 是你需要转跳的界面，跟初始化的时候那个 targetClass 是一样的，填的是完整路径。如果你想更改转跳的界面，则传递此参数，如果不需要，则传 null 即可。

因为 `updateNotificationContentIntent` 这个方法的原理是更新 Notification 的 ContentIntent，所以当你调用此方法时，请确保通知栏已经被创建了，不然是没有效的。



如果你的通知栏中有喜欢或收藏按钮或者有是否显示桌面歌词按钮（类似网易云音乐），让按钮变成选中或者未选中状态，需要调用以下方法来更新UI：
```java
//更新喜欢或收藏按钮UI为是否选中
MusicManager.get().updateNotificationFavorite(boolean isFavorite);

//更新是否显示桌面歌词UI为是否选中
MusicManager.get().updateNotificationLyrics(boolean isChecked);
```


### 系统通知栏

系统通知栏只需要在初始化的时候把开关打开，就已经集成好了，在系统通知栏中，有三个按钮，分别是上一首下一首和播放/暂停
这三个按钮的颜色是会跟随你项目中的主题颜色 `colorPrimary` 的改变而改变的。  

同样的，跟自定义通知栏一样，点击系统通知栏的时候也会得到 `songInfo` 参数，你也可以通过 `updateNotificationContentIntent` 方法
添加自己的参数，也可以自定义 action 自己控制按钮的点击操作。




具体 demo 见 [NiceMusic](https://github.com/lizixian18/NiceMusic) 


