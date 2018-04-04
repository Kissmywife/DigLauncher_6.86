package com.digissin.launcher.custom;

import android.graphics.drawable.Drawable;

/**
 * Created by Freedom on 2017/12/12.
 */

public class APPBean {

    private String appName;
    private String appPackageName;
    private Drawable appIcon;

    public Drawable getAppIcon() {
        return appIcon;
    }

    public void setAppIcon(Drawable appIcon) {
        this.appIcon = appIcon;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getAppPackageName() {
        return appPackageName;
    }

    public void setAppPackageName(String appPackageName) {
        this.appPackageName = appPackageName;
    }
}
