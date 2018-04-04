package com.digissin.launcher.custom;

import android.app.Activity;

import java.util.Stack;

/**
 * Created by Freedom on 2017/12/2.
 */

public class ActivityControl {

    private static Stack<Activity> mStack;
    private static ActivityControl mControl;

    /**
     * 单例模式
     *
     * @return
     */
    public static ActivityControl getInstance() {
        if (mControl == null) {
            mControl = new ActivityControl();
        }
        return mControl;
    }

    /**
     * 添加Activity到对宅
     *
     * @param activity
     */
    public void addActivity(Activity activity) {
        if (mStack == null) {
            mStack = new Stack<Activity>();
        }
        mStack.add(activity);
    }

    /**
     * 结束制定Activity
     *
     * @param activity
     */
    public void finishActivity(Activity activity) {
        if (mStack != null && mStack.size() > 0) {
            mStack.remove(activity);
            activity.finish();
        }
    }

    /**
     * 结束所有Activity
     *
     */
    public void finishAllActivity() {
        if (mStack != null && mStack.size() > 0) {
            for (int i = 0; i < mStack.size(); i++) {
                if (mStack.get(i) != null) {
                    mStack.get(i).finish();
                }
            }
        }
        mStack.clear();
    }

}
