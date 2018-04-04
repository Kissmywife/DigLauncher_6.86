package com.digissin.launcher.Utils;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;

import com.digissin.launcher.R;
import com.digissin.launcher.custom.APPBean;
import com.digissin.launcher.custom.Constant;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by Freedom on 2017/12/4.
 */
public class IntentUtils {

    static ArrayList<APPBean> mData = null;

    /**
     * 是否含有相关APP
     *
     * @param mContext
     * @param packageName
     * @return
     */
    public static synchronized boolean haveAPP(Context mContext, String packageName) {
        boolean haveApp = false;
        List<ApplicationInfo> app = mContext.getPackageManager().getInstalledApplications(PackageManager.GET_UNINSTALLED_PACKAGES);
        int appNum = app.size();
        for (int i = 0; i < appNum; i++) {
            if (app.get(i).packageName.equals(packageName)) {
                haveApp = true;
                break;
            }
        }
        if (!haveApp)
            Utils.makeToast(mContext, mContext.getString(R.string.no_app));
        return haveApp;
    }

    /**
     * 通过包名打开应用
     *
     * @param mActivity
     * @param packagename
     */
    public static void startAPP(Activity mActivity, String packagename) {
        Intent intent = mActivity.getPackageManager().getLaunchIntentForPackage(packagename);
        mActivity.startActivity(intent);
        mActivity.overridePendingTransition(0, 0);
    }

    /**
     * 通过包名类名打开应用
     *
     * @param mContext
     * @param packageName
     * @param className
     */
    public static void startActivity(Context mContext, String packageName, String className) {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        ComponentName name = new ComponentName(packageName, className);
        intent.addCategory(Intent.CATEGORY_LAUNCHER);
        intent.setComponent(name);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
        mContext.startActivity(intent);
    }

    /**
     * 获取筛选的APP数据集合
     *
     * @param mContext
     * @param allShow
     * @return
     */
    public static ArrayList<APPBean> getAllApp(Context mContext, boolean allShow) {
        if (mData == null) {
            mData = new ArrayList<>();
        }
        PackageManager packageManager = mContext.getPackageManager();
        Intent intent = new Intent(Intent.ACTION_MAIN, null);
        intent.addCategory(Intent.CATEGORY_LAUNCHER);
        final List<ResolveInfo> infos = packageManager.queryIntentActivities(intent, 0);
        int appNum = infos.size();
        Collections.sort(infos, new ResolveInfo.DisplayNameComparator(packageManager));
        if (infos == null || appNum <= 0) {
            return null;
        }
        if (allShow) {
            for (int i = 0; i < appNum; i++) {//获取所有APP
                ResolveInfo info = infos.get(i);
                APPBean appInfo = new APPBean();
                appInfo.setAppName(info.loadLabel(packageManager).toString());
                appInfo.setAppIcon(info.loadIcon(packageManager));
                appInfo.setAppPackageName(info.activityInfo.packageName);
                mData.add(appInfo);
            }
            return mData;
        } else {
            for (int i = 0; i < appNum; i++) {//获取部分APP，带筛选
                ResolveInfo info = infos.get(i);

                if (info.activityInfo.packageName.equals(Constant.PACKAGE_GPS) ||
                        info.activityInfo.packageName.equals(Constant.PACKAGE_KWMUSIC) ||
                        info.activityInfo.packageName.equals(Constant.PACKAGE_FACTORY_DX) ||
                        info.activityInfo.packageName.equals(Constant.PACKAGE_MAP_COPY) ||
                        info.activityInfo.packageName.equals(Constant.PACKAGE_VOLTE) ||
                        info.activityInfo.packageName.equals(Constant.PACKAGE_NAVI_PACKAGE[0]) ||
                        info.activityInfo.packageName.equals(Constant.PACKAGE_EDOG) ||
                        info.activityInfo.packageName.equals(Constant.PACKAGE_KAOLA) ||
                        info.activityInfo.packageName.equals(Constant.PACKAGE_INTERNET) ||
                        info.activityInfo.packageName.equals(Constant.PACKAGE_SOUNDRECORD) ||
                        info.activityInfo.packageName.equals(Constant.PACKAGE_GALLERY_DX) ||
                        info.activityInfo.packageName.equals(Constant.PACKAGE_SET_DX) ||
                        info.activityInfo.packageName.equals(Constant.PACKAGE_SET_ANDROID) ||
                        info.activityInfo.packageName.equals(Constant.PACKAGE_TENCENT) ||
                        info.activityInfo.packageName.equals(Constant.PACKAGE_ADDRESS_BOOK) ||
                        info.activityInfo.packageName.equals(Constant.PACKAGE_MESSAGE) ||
                        info.activityInfo.packageName.equals(Constant.PACKAGE_BLE) ||
                        info.activityInfo.packageName.equals(Constant.PACKAGE_BT_DX) ||
                        info.activityInfo.packageName.equals(Constant.PACKAGE_DRIV_DX) ||
                        info.activityInfo.packageName.equals(Constant.PACKAGE_DRIV_DX_NEW) ||
                        info.activityInfo.packageName.equals(Constant.PACKAGE_FM_DX) ||
                        info.activityInfo.packageName.equals(Constant.PACKAGE_LAUNCHER) ||
                        info.activityInfo.packageName.equals(Constant.PACKAGE_LAUNCHER_MTK) ||
                        info.activityInfo.packageName.equals(Constant.PACKAGE_SCREEN) ||
                        info.activityInfo.packageName.equals(Constant.PACKAGE_MEDIATEK) ||
                        info.activityInfo.packageName.equals(Constant.PACKAGE_QQWL) ||
                        info.activityInfo.packageName.equals(Constant.PACKAGE_TXZSERVICE) ||
                        info.activityInfo.packageName.equals(Constant.PACKAGE_MAP_COPY_DX) ||
                        info.activityInfo.packageName.equals(Constant.PACKAGE_CONTROLLER) ||
                        info.activityInfo.packageName.equals(Constant.PACKAGE_DATATRANSFER) ||
                        info.activityInfo.packageName.equals(Constant.PACKAGE_FLOW_DX)) {
                    continue;
                } else {
                    APPBean appInfo = new APPBean();
                    appInfo.setAppName(info.loadLabel(packageManager).toString());
                    appInfo.setAppIcon(info.loadIcon(packageManager));
                    appInfo.setAppPackageName(info.activityInfo.packageName);
                    mData.add(appInfo);
                }
            }
            return mData;
        }
    }

    /**
     * 判断应用是否系统及应用
     *
     * @param context
     * @param packageName
     * @return
     */
    public static boolean isSystemApp(Context context, String packageName) {
        try {
            PackageInfo pInfo = context.getPackageManager().getPackageInfo(
                    packageName, 0);
            if ((pInfo.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) != 0
                    || (pInfo.applicationInfo.flags & ApplicationInfo.FLAG_UPDATED_SYSTEM_APP) != 0) {
                return true;
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 卸载非系统级应用
     *
     * @param context
     * @param packageName
     */
    public static void uninstallApp(Context context, String packageName) {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_DELETE);
        intent.setData(Uri.parse("package:" + packageName));
        context.startActivity(intent);
    }

}
