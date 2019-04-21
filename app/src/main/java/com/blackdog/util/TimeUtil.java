package com.blackdog.util;

import com.blackdog.musiclibrary.model.Song;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class TimeUtil {

    private static SimpleDateFormat FORMAT_MINUTE_AND_SECONDD = new SimpleDateFormat("mm:ss", Locale.CHINA);

    public static String getMinuteAndSecond(long time) {
        return FORMAT_MINUTE_AND_SECONDD.format(new Date(time));
    }

}
