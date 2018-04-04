package com.digissin.launcher.Utils;

import android.util.Log;

/**
 * Created by Freedom on 2017/12/11.
 */

public class LogUtils {

    static boolean showLog = true;
    static String TAG="Launcher";

    public static void e(String tag, Object text) {
        if (showLog)
            Log.e(tag, text + "");
    }

    public static void e(Object text){
        e(TAG,text);
    }

    public static void d(String tag, Object text) {
        if (showLog)
            Log.d(tag, text + "");
    }

    public static void i(String tag, Object text) {
        if (showLog)
            Log.i(tag, text + "");
    }
}
