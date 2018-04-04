package com.digissin.launcher.Utils;

/**
 * Created by Freedom on 2018/1/6.
 */

public class CrashHandler implements Thread.UncaughtExceptionHandler {
    // 需求是 整个应用程序 只有一个 MyCrash-Handler
    private static CrashHandler INSTANCE;

    //1.私有化构造方法
    private CrashHandler() {

    }

    public static synchronized CrashHandler getInstance() {
        if (INSTANCE == null)
            INSTANCE = new CrashHandler();
        return INSTANCE;
    }

    @Override
    public void uncaughtException(Thread t, Throwable e) {

        //强制关闭当前的程序
        android.os.Process.killProcess(android.os.Process.myPid());
    }
}
