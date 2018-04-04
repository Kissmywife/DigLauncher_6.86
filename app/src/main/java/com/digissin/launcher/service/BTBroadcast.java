package com.digissin.launcher.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;

/**
 * Created by Freedom on 2018/2/8.
 */

public class BTBroadcast extends BroadcastReceiver {

    final String BT_CONNECTED = "android.bluetooth.device.action.ACL_CONNECTED";
    final String BT_DISCONNECTED = "android.bluetooth.device.action.ACL_DISCONNECTED";

    private static boolean isBTConnect = false;

    public static boolean isBTConnect() {
        return isBTConnect;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        switch (action) {
            case BT_CONNECTED:
                isBTConnect = true;
                break;
            case BT_DISCONNECTED:
                isBTConnect = false;
                break;
            default:
                break;
        }
    }

}
