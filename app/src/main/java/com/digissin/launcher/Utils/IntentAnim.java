package com.digissin.launcher.Utils;

import android.app.Activity;
import android.text.TextUtils;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;

public class IntentAnim implements AnimationListener {
	public Activity mActivity;
	public String pkgName;
	public String appName;

	public IntentAnim(Activity mActivity, String pkString, String appName) {
		this.mActivity = mActivity;
		this.pkgName = pkString;
		this.appName = appName;
	}

	@Override
	public void onAnimationEnd(Animation arg0) {
		if(TextUtils.isEmpty(appName)){
			IntentUtils.startAPP(mActivity,pkgName);
		}else{
			if(!IntentUtils.isSystemApp(mActivity,pkgName)){
				IntentUtils.uninstallApp(mActivity,pkgName);
			}
		}
	}

	@Override
	public void onAnimationRepeat(Animation arg0) {

	}

	@Override
	public void onAnimationStart(Animation arg0) {

	}



}
