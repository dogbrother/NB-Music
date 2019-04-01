package com.blackdog.util;

import android.app.Application;

public class AppUtil {
    private static Application mApplication;

    public static void setApplication(Application application) {
        mApplication = application;
    }

    public static Application getApplication() {
        return mApplication;
    }

    public static boolean isMainProcess() {
        return !mApplication.getApplicationInfo().processName.contains(":");
    }

}
