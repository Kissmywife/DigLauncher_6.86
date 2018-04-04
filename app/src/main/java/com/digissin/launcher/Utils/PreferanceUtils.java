package com.digissin.launcher.Utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Freedom on 2017/12/21.
 */

public class PreferanceUtils {

    static String PREFERENCE_NAME = "WEATHERDATA";

    public static void saveData(Context mContext, String name, String string) {
        SharedPreferences sp = mContext.getSharedPreferences(PREFERENCE_NAME,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(name, string);
        editor.commit();
    }

    public static void saveDataInt(Context mContext, String name, int string) {
        SharedPreferences sp = mContext.getSharedPreferences(PREFERENCE_NAME,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putInt(name, string);
        editor.commit();
    }

    public static void saveDataBoolean(Context mContext, String name,
                                       boolean bool) {
        SharedPreferences sp = mContext.getSharedPreferences(PREFERENCE_NAME,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putBoolean(name, bool);
        editor.commit();
    }

    public static String getData(Context mContext, String name, String nodata) {
        SharedPreferences preferences = mContext.getSharedPreferences(
                PREFERENCE_NAME, Context.MODE_PRIVATE);
        String data = preferences.getString(name, nodata);
        return data;
    }

    public static int getDataInt(Context mContext, String name, int nodata) {
        SharedPreferences preferences = mContext.getSharedPreferences(
                PREFERENCE_NAME, Context.MODE_PRIVATE);
        int data = preferences.getInt(name, nodata);
        return data;
    }

    public static boolean getDataBoolean(Context mContext, String name,
                                         boolean nodata) {
        SharedPreferences preferences = mContext.getSharedPreferences(
                PREFERENCE_NAME, Context.MODE_PRIVATE);
        boolean data = preferences.getBoolean(name, nodata);
        return data;
    }

}
