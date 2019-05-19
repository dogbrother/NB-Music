package com.blackdog.util;


public class TimeUtil {
    public static String getSongDuration(int time) {
        if (time <= 0) {
            return "未知时长";
        }
        int second = time % 60;
        int minute = time / 60;
        String secondStr = second >= 10 ? String.valueOf(second) : "0" + second;
        return String.format("%d:%s", minute, secondStr);
    }

}
