package com.blackdog.util;

import android.text.TextUtils;
import android.widget.Toast;

import com.blackdog.App;

public class ToastUtil {
    private static Toast sToast;

    public static void show(String msg) {
        if (TextUtils.isEmpty(msg)) {
            return;
        }
        if (sToast == null) {
            sToast = Toast.makeText(App.getInstance(), msg, Toast.LENGTH_SHORT);
        } else {
            sToast.setText(msg);
        }
        sToast.show();
    }
}
