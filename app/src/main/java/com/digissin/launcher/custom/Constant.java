package com.digissin.launcher.custom;

/**
 * Created by Freedom on 2017/12/2.
 */

public class Constant {

    /**
     * DX包名
     */
    public static final String PACKAGE_BT_DX = "com.mtk.bluetooth";//蓝牙电话
    public static final String PACKAGE_DRIV_DX = "com.mediatek.carcorderdemo";//行车记录仪
    public static final String PACKAGE_DRIV_DX_NEW = "com.digissin.carcorder";//行车记录仪
    public static final String PACKAGE_FM_DX = "com.mediatek.fmtx";//FM
    public static final String PACKAGE_SET_DX = "com.diggisin.mtk.settings";//设置
    public static final String PACKAGE_GALLERY_DX = "com.digissin.mtk.gallery";//图库
    public static final String PACKAGE_FLOW_DX = "com.digissin.dataflow";//流量查询
    public static final String PACKAGE_FILE_DX = "com.mediatek.filemanager";//文件管理
    public static final String PACKAGE_FACTORY_DX = "com.digissin.devicetest";//工厂测试
    public static final String PACKAGE_MAP_COPY_DX = "com.digissin.shintool";//地图拷贝DX
    public static final String PACKAGE_DATATRANSFER = "com.mediatek.datatransfer";//备份与恢复
    public static final String PACKAGE_INTERNET = "com.android.browser";//浏览器
    public static final String PACKAGE_SOUNDRECORD = "com.android.soundrecorder";//录音机
    public static final String PACKAGE_LAUNCHER = "com.digissin.launcher";//主界面
    public static final String PACKAGE_LAUNCHER_MTK = "com.digissin.software.launcher";//主界面MTK
    public static final String PACKAGE_SOS = "com.pg.software.sendMMS";//一键报警
    public static final String PACKAGE_MUSIC_ANDROID = "com.android.music";//音乐Android原生
    public static final String PACKAGE_NAVI_IGO = "com.navngo.igo.javaclient";//IGO地图
    public static final String PACKAGE_NAVI_GOOGLE = "com.google.android.apps.maps";//Google地图
    /**
     * DX_BROADCAST
     */
    public static final String NOTIFACATION_MSG_TO_RECODER = "com.pg.software.NOTIFACATION_MSG_TO_RECODER";//操作记录仪广播（拍照，录像）
    public static final String CAPTURE_DONE = "com.pg.software.CAPTURE_DONE";//抓拍图片完成广播（K：StringExtra(String)  V:FrontUrl(String)，BackUrl(String)）
    public static final String ACTION_VIDEO_KEYPOINT_START = "android.dig.action.ACTION_VIDEO_KEYPOINT_START";//录制keypoint文件
    public static final String RECORDER_CONTROL = "android.dig.action.RECORDER";//操作录像(K:msg(String) V:stop/start(String))
    public static final String RECORDER_START = "android.dig.action.RECORDER_START";//开始录制时发出的广播
    public static final String RECORDER_STOP = "android.dig.action.RECORDER_STOP";//停止录制时发出的广播
    public static final String REMOVE_NAVIGATIONBAR = "removeNavigationBar";//分全屏对外发出广播(K:remove(String) V:true/false(boolean,true:是全屏))
    public static final String MTK_FM_OPEN_CLOSE = "MTK_FM_OPEN_CLOSE";//FM状态对外广播(K:statue(String) V:true/false(boolean,true:打开))
    public static final String NODIFY_NOWFREQUENCY = "modify_nowFrequency";//FM频率改变对外广播(K:nowFrequency(String) V:88.0~108.0e(float))
    public static final String ETC_BROADCAST = "com.digissin.broadcast.ETC";
    public static final String AMPA_BROAD = "AUTONAVI_STANDARD_BROADCAST_SEND";
    public static final String VOLUME_CHANGED_ACTION = "android.media.VOLUME_CHANGED_ACTION";
    public static final String SYSTEM_VOLUMN_CHANGE_ACTION = "intent.action.volumn.change.action";
    public static final String SYSTEM_BRIGHTNESS_CHANGE_ACTION = "intent.action.brightness.change.action";

    /**
     * OTHER APPLICATION
     */
    public static final String PACKAGE_EDOG = "com.hdsc.edog";
    public static final String[] PACKAGE_NAVI_PACKAGE = {"com.autonavi.amapautolite", "com.autonavi.amapauto", "cld.navi", "com.baidu.navi", "com.baidu.navi.hb", "com.viewin.witsgo", "cn.jyuntech.map"};
    public static final String PACKAGE_KAOLA = "com.edog.car";//考拉电台
    public static final String PACKAGE_SET_ANDROID = "com.android.settings";//android原生设置
    public static final String PACKAGE_ETC = "com.digissin.etc";//ETC
    public static final String PACKAGE_TENCENT = "com.tencent.qqlive";//腾讯视频
    public static final String PACKAGE_KWMUSIC = "cn.kuwo.kwmusiccar";//酷我音乐
    public static final String PACKAGE_GPS = "com.chartcross.gpstestplus";//GPS
    public static final String PACKAGE_MAP_COPY = "com.digissin.shintools";//地图拷贝

    public static final String PACKAGE_VOLTE = "com.android.dialer";//VOLTE
    public static final String PACKAGE_CONTROLLER = "com.pg.software.controller";//蓝牙方控
    public static final String PACKAGE_MESSAGE = "com.android.mms";//短信息
    public static final String PACKAGE_ADDRESS_BOOK = "com.android.contacts";//通讯录
    public static final String PACKAGE_BLE = "com.mediatek.blemanager";//BLE管理器
    public static final String PACKAGE_SCREEN = "com.digissin.screensave";//屏保
    public static final String PACKAGE_MEDIATEK = "com.mediatek.mco";//多核观察器
    public static final String PACKAGE_QQWL = "com.daxun.deviceapp";//QQ物联
    public static final String PACKAGE_WEBCHAT = "com.txznet.webchat";
    public static final String PACKAGE_TXZSERVICE = "com.dx.sdkservice";
    /**
     * 特别标识
     */
    public static final String[] MOBIL_OPERATOR_TYPE = {"YD", "LT", "DX", "UNKNOWN"};

    //WIFI热点状态变化广播
    public static final String WIFI_AP_STATE_CHANGED_ACTION = "android.net.wifi.WIFI_AP_STATE_CHANGED";
    public static final String EXTRA_WIFI_AP_STATE = "wifi_state";
    public static final int WIFI_AP_STATE_DISABLING = 10;
    public static final int WIFI_AP_STATE_DISABLED = 11;
    public static final int WIFI_AP_STATE_ENABLING = 12;
    public static final int WIFI_AP_STATE_ENABLED = 13;
    public static final int WIFI_AP_STATE_FAILED = 14;

    public static final String ACTION_SIM_STATE_CHANGED = "android.intent.action.SIM_STATE_CHANGED";
    public static final int TYPE_4G = 10014;
    public static final int TYPE_3G = 10015;
    public static final int TYPE_2G = 10016;
    public static final int TYPE_UNKNOW = 10017;
    // 没有网络连接
    public static final int NETWORN_NONE = 0;
    // wifi连接
    public static final int NETWORN_WIFI = 1;
    // 手机网络数据连接类型
    public static final int NETWORN_2G = 2;
    public static final int NETWORN_3G = 3;
    public static final int NETWORN_4G = 4;
    public static final int NETWORN_MOBILE = 5;
}
