package com.digissin.launcher.custom;

import android.app.Application;
import android.content.Context;
import android.text.TextUtils;

import com.digissin.launcher.R;
import com.digissin.launcher.Utils.CrashHandler;
import com.tencent.bugly.crashreport.CrashReport;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;


/**
 * Created by Freedom on 2018/1/6.
 */

public class BaseApplication extends Application {


    @Override
    public void onCreate() {
        super.onCreate();

        //更换默认字体
        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/typeface.ttf")
                .setFontAttrId(R.attr.fontPath)
                .build());

//        //屏蔽错误弹框
//        CrashHandler handler = CrashHandler.getInstance();
//        Thread.setDefaultUncaughtExceptionHandler(handler);
//        //提交错误到bugly
//        Context context = getApplicationContext();
//        // 获取当前包名
//        String packageName = context.getPackageName();
//        // 获取当前进程名
//        String processName = getProcessName(android.os.Process.myPid());
//        // 设置是否为上报进程
//        CrashReport.UserStrategy strategy = new CrashReport.UserStrategy(context);
//        strategy.setUploadProcess(processName == null || processName.equals(packageName));
//        // 初始化Bugly
//        CrashReport.initCrashReport(context, "690835adf1", false, strategy);

    }

    private static String getProcessName(int pid) {
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader("/proc/" + pid + "/cmdline"));
            String processName = reader.readLine();
            if (!TextUtils.isEmpty(processName)) {
                processName = processName.trim();
            }
            return processName;
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        } finally {
            try {
                if (reader != null) {
                    reader.close();
                }
            } catch (IOException exception) {
                exception.printStackTrace();
            }
        }
        return null;
    }
}
