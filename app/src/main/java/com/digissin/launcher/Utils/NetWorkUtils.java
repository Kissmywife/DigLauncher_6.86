package com.digissin.launcher.Utils;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v4.app.ActivityCompat;
import android.telephony.TelephonyManager;

import static com.digissin.launcher.custom.Constant.MOBIL_OPERATOR_TYPE;

/**
 * Created by Freedom on 2017/12/2.
 */

public class NetWorkUtils {

    private static String imsi;

    /**
     * 判断是否有网络连接
     *
     * @param mContext
     * @return
     */
    public static boolean isNetWorkConnected(Context mContext, ConnectivityManager mConnectivityManager) {
//        ConnectivityManager mConnectivityManager = (ConnectivityManager) mContext
//                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();
        if (mNetworkInfo != null) {
            return mNetworkInfo.isAvailable();
        }
        return false;
    }

    /**
     * 是WIFI连接是否可用
     *
     * @param mContext
     * @return
     */
    public static boolean isWifiConnected(Context mContext, ConnectivityManager mConnectivityManager) {
//        ConnectivityManager mConnectivityManager = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo mNetworkInfo = mConnectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        if (mNetworkInfo != null) {
            return mNetworkInfo.isAvailable();
        }
        return false;
    }

    /**
     * 判断Mobile网络是否可用
     *
     * @param mContext
     * @return
     */
    public static boolean isMobileConnected(Context mContext, ConnectivityManager mConnectivityManager) {
//        ConnectivityManager mConnectivityManager = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo mNetworkInfo = mConnectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        if (mNetworkInfo != null) {
            return mNetworkInfo.isAvailable();
        }
        return false;
    }

    /**
     * 获取当前运营商
     *
     * @return
     */
    public static String getOperator(Context mContext, TelephonyManager mTelephonyManager) {
        if (mTelephonyManager == null)
            return MOBIL_OPERATOR_TYPE[3];
        if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            imsi = mTelephonyManager.getSubscriberId();
            if (imsi.startsWith("46000") || imsi.startsWith("46002")
                    || imsi.startsWith("46004") || imsi.startsWith("46007")) {
                return MOBIL_OPERATOR_TYPE[0];
            } else if (imsi.startsWith("46001") || imsi.startsWith("46006")
                    || imsi.startsWith("46009")) {
                return MOBIL_OPERATOR_TYPE[1];
            } else if (imsi.startsWith("46003") || imsi.startsWith("46005")
                    || imsi.startsWith("46011")) {
                return MOBIL_OPERATOR_TYPE[2];
            } else {
                return MOBIL_OPERATOR_TYPE[3];
            }
        }
        return MOBIL_OPERATOR_TYPE[3];
    }


}
