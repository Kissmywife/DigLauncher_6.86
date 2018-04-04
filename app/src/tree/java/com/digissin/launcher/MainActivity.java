package com.digissin.launcher;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.database.ContentObserver;
import android.media.AudioManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.BatteryManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.RemoteException;
import android.provider.Settings;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.telephony.TelephonyManager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.digissin.fm.aidl.IFMStatusService;
import com.digissin.launcher.Utils.IntentUtils;
import com.digissin.launcher.Utils.LogUtils;
import com.digissin.launcher.Utils.PreferanceUtils;
import com.digissin.launcher.Utils.Utils;
import com.digissin.launcher.adapter.MyVPAdapter;
import com.digissin.launcher.custom.Constant;
import com.digissin.launcher.custom.WeatherBean;
import com.digissin.launcher.service.BTBroadcast;
import com.digissin.launcher.service.WeatherService;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.kuwo.autosdk.api.KWAPI;
import cn.kuwo.autosdk.api.OnPlayerStatusListener;
import cn.kuwo.autosdk.api.PlayState;
import cn.kuwo.autosdk.api.PlayerStatus;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Context mContext = MainActivity.this;
    private Activity mActivity = MainActivity.this;
    private static final int REFRESH_WATER = 10086;
    private static final int REFRESH_LIGHT = 10087;
    private String locationCity = "";

    //天气相关数据
    private int[] weatherIcon = {R.mipmap.cloudy, R.mipmap.foggy, R.mipmap.hail, R.mipmap.rainy1, R.mipmap.rainy1, R.mipmap.rainy3, R.mipmap.rainy5, R.mipmap.snowy1, R.mipmap.snowy3, R.mipmap.snowy4, R.mipmap.snowy5, R.mipmap.sounderainny, R.mipmap.sun, R.mipmap.sunning};
    private Map<String, Integer> weatherIconData = new HashMap<>();
    //第一页全部图标
    private RelativeLayout etcLayout, picLayout;
    private TextView remainingDistance, currentLocation, recordMarket, drivStartText, etcLessMoney, photoBottText;
    private ImageView drivStartImage, musicPlayPause;
    private boolean canTakePhoto = false;
    private ToggleButton photoBottToggle;
    //第二页全部图标
    private RelativeLayout qqLayout, wechatLayout;
    private TextView fmSize;
    private static SeekBar soundSeekBar, lightSeekBar;
    //第三页全部图标
    private RelativeLayout edogLayout, galleryLayout;
    private ToggleButton flowToggle, btBottToggle;
    private ImageView btConnectIcon;
    private TextView flowToggleText, btConnectText, btBootText;
    private LinearLayout navigationPoint;

    private boolean isLongClick = false;
    private float y1;
    private float x1;
    private int normal = 0;
    //内部应用  绑定服务
    private IFMStatusService mFMService;
    //    private WifiManager mWifiManager;
    private TelephonyManager mTelephoneManager;
    private AudioManager mAudioManager;
    private ConnectivityManager mConnectivityManager;
    private KWAPI mKwapi;
    private boolean isMusicPlay = false;
    private long drivTimeLong;
    private MyHandler mHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mHandler = new MyHandler(this);
        initData();
        initView();
        doOther();
    }

    //更换默认字体重写
    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    protected void initView() {
        if (!WeatherService.isStart) {
            mContext.startService(new Intent(mContext, WeatherService.class));
        }
        WeatherService.setWeatherCallback(mWeatherCallBack);
        List<View> viewData = new ArrayList<>();
        viewData.clear();
        ViewPager viewPagerMain = findViewById(R.id.viewPagerMain);
        navigationPoint = findViewById(R.id.navigationPoint);
        View pageFirst = LayoutInflater.from(mContext).inflate(R.layout.pagerfirst, null, false);
        View pagerSecond = LayoutInflater.from(mContext).inflate(R.layout.pagersecond, null, false);
        View pagerThird = LayoutInflater.from(mContext).inflate(R.layout.pagerthird, null, false);
        viewData.add(pageFirst);
        viewData.add(pagerSecond);
        viewData.add(pagerThird);
        //初始化控件
        initPagerFirst(pageFirst);
        initPagerSecond(pagerSecond);
        initPagerThird(pagerThird);
        setPointFocors(0);
        MyVPAdapter myVPAdapter = new MyVPAdapter(viewData);
        viewPagerMain.setAdapter(myVPAdapter);
        viewPagerMain.setOffscreenPageLimit(4);//缓存4页
        viewPagerMain.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                setPointFocors(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
    }

    protected void initData() {
//        mWifiManager = (WifiManager) mContext.getSystemService(Context.WIFI_SERVICE);
        mTelephoneManager = (TelephonyManager) mContext.getSystemService(Context.TELEPHONY_SERVICE);
        mAudioManager = (AudioManager) mContext.getSystemService(Context.AUDIO_SERVICE);
        // 获取系统的网络服务
        mConnectivityManager = (ConnectivityManager) mContext
                .getSystemService(Context.CONNECTIVITY_SERVICE);
//        mobileNetworkInfo1 = mConnectivityManager
//                .getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
//        addWeatherData();
//
    }

    private void addWeatherData() {
        String[] weatherType = mContext.getResources().getStringArray(R.array.weatherData);
        for (int i = 0; i < weatherType.length; i++) {
            weatherIconData.put(weatherType[i], weatherIcon[i]);
        }
    }

    protected void doOther() {
        registerBroadcast();//注册广播
        bindAidl();//绑定服务
//        if (Utils.haveSimCard(mTelephoneManager)) {
//            networkStrenth();//刷新信号图标
//        }
        mKwapi = KWAPI.createKWAPI(mContext, "auto");
        mKwapi.registerPlayerStatusListener(mContext, musicListener);
    }

    @Override
    protected void onResume() {
        super.onResume();
        refreshFM();//刷新FM状态
        if (isMusicPlay) {
            musicPlayPause.setImageResource(R.drawable.home_music_btn_pause);
        } else {
            musicPlayPause.setImageResource(R.drawable.home_music_btn_play);
        }
        canTakePhoto = PreferanceUtils.getDataBoolean(mContext, "canTakePhoto", false);

        //刷新界面
        boolean isFullWind = PreferanceUtils.getDataBoolean(mContext, "isFullWind", false);
        refreshView(isFullWind);

//        //刷新天气
//        if (PreferanceUtils.getData(mContext, "mCity", "") != null && mWeatherBean == null) {
//            mWeatherBean = new WeatherBean();
//            mWeatherBean.setmCity(PreferanceUtils.getData(mContext, "mCity", ""));
//            mWeatherBean.setmNowTempature(PreferanceUtils.getData(mContext, "mNowTempature", ""));
//            mWeatherBean.setmTempratureSize(PreferanceUtils.getData(mContext, "mTempratureSize", ""));
//            mWeatherBean.setmWeather(PreferanceUtils.getData(mContext, "mWeather", ""));
//            mWeatherBean.setmWeatherPic(PreferanceUtils.getData(mContext, "mWeatherPic", ""));
//            mWeatherBean.setmWind(PreferanceUtils.getData(mContext, "mWind", ""));
//            mWeatherBean.setmPM(PreferanceUtils.getData(mContext, "mPM", ""));
//        }
//        Message msg = mHandler.obtainMessage(REFRESH_WAHER);
//        msg.obj = mWeatherBean;
//        mHandler.sendMessage(msg);

        mHandler.post(mRunnable);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(mRunnable!=null){
            mHandler.removeCallbacks(mRunnable);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        PreferanceUtils.saveDataBoolean(mContext, "isFullWind", false);
        if(mRunnable!=null){
            mHandler.removeCallbacks(mRunnable);
        }
        unbindService(mFMConnection);
        unregisterReceiver(recordReceiver);
        unregisterReceiver(fmRecord);
        unregisterReceiver(mNetWorkState);
        unregisterReceiver(otherReceiver);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.naviLayout:
                for (int i = 0; i < Constant.PACKAGE_NAVI_PACKAGE.length; i++) {
                    if (IntentUtils.haveAPP(mContext, Constant.PACKAGE_NAVI_PACKAGE[i])) {
                        IntentUtils.startAPP(mActivity, Constant.PACKAGE_NAVI_PACKAGE[i]);
                        break;
                    }
                }
                break;
            case R.id.photoBottLayout:
                if (!canTakePhoto) {
                    canTakePhoto = true;
                    photoBottToggle.setChecked(true);
                    photoBottText.setText(mContext.getString(R.string.opened));
                } else {
                    canTakePhoto = false;
                    photoBottToggle.setChecked(false);
                    photoBottText.setText(mContext.getString(R.string.closed));
                }
                PreferanceUtils.saveDataBoolean(mContext, "canTakePhoto", canTakePhoto);
                break;
            case R.id.drivLayout:
                if (IntentUtils.haveAPP(mContext, Constant.PACKAGE_DRIV_DX_NEW)) {
                    IntentUtils.startAPP(mActivity, Constant.PACKAGE_DRIV_DX_NEW);
                }
                break;
            case R.id.musicLayout:
                if (IntentUtils.haveAPP(mContext, Constant.PACKAGE_KWMUSIC)) {
                    IntentUtils.startAPP(mActivity, Constant.PACKAGE_KWMUSIC);
                }
                break;
            case R.id.musicPre:
                if (Utils.isAppRunning(mContext, Constant.PACKAGE_KWMUSIC)) {
                    mKwapi.setPlayState(mContext, PlayState.STATE_PRE);
                }
                break;
            case R.id.musicPlayPause:
                if (Utils.isAppRunning(mContext, Constant.PACKAGE_KWMUSIC)) {
                    if (isMusicPlay) {
                        mKwapi.setPlayState(mContext, PlayState.STATE_PAUSE);
                        musicPlayPause.setImageResource(R.drawable.home_music_btn_play);
                    } else {
                        mKwapi.setPlayState(mContext, PlayState.STATE_PLAY);
                        musicPlayPause.setImageResource(R.drawable.home_music_btn_pause);
                    }
                } else {
                    mKwapi.startAPP(mContext, true);
                }
                break;
            case R.id.musicNext:
                if (Utils.isAppRunning(mContext, Constant.PACKAGE_KWMUSIC)) {
                    mKwapi.setPlayState(mContext, PlayState.STATE_NEXT);
                }
                break;
            case R.id.etcLayout:
                if (IntentUtils.haveAPP(mContext, Constant.PACKAGE_ETC)) {
                    IntentUtils.startAPP(mActivity, Constant.PACKAGE_ETC);
                }
                break;
            case R.id.picLayout:
                if (canTakePhoto) {
                    mContext.sendBroadcast(new Intent(Constant.NOTIFACATION_MSG_TO_RECODER).putExtra("msg", "getphoto").putExtra("enableSound", true));
                }
                break;
            case R.id.btLayout:
                if (IntentUtils.haveAPP(mContext, Constant.PACKAGE_BT_DX)) {
                    IntentUtils.startAPP(mActivity, Constant.PACKAGE_BT_DX);
                }
                break;
            case R.id.btBottLayout:
                if (BluetoothAdapter.getDefaultAdapter().isEnabled()) {
                    btBottToggle.setChecked(false);
                    BluetoothAdapter.getDefaultAdapter().disable();
                } else {
                    btBottToggle.setChecked(true);
                    BluetoothAdapter.getDefaultAdapter().enable();
                }
                break;
            case R.id.fmLayout:
                if (IntentUtils.haveAPP(mContext, Constant.PACKAGE_FM_DX)) {
                    IntentUtils.startAPP(mActivity, Constant.PACKAGE_FM_DX);
                }
                break;
            case R.id.setLayout:
                if (IntentUtils.haveAPP(mContext, Constant.PACKAGE_SET_DX)) {
                    IntentUtils.startAPP(mActivity, Constant.PACKAGE_SET_DX);
                }
                break;
            case R.id.wechatLayout:
                if (IntentUtils.haveAPP(mContext, Constant.PACKAGE_WEBCHAT)) {
                    IntentUtils.startAPP(mActivity, Constant.PACKAGE_WEBCHAT);
                }
                break;
            case R.id.qqLayout:
                if (IntentUtils.haveAPP(mContext, Constant.PACKAGE_QQWL)) {
                    IntentUtils.startAPP(mActivity, Constant.PACKAGE_QQWL);
                }
                break;
            case R.id.flowLayout:
                if (IntentUtils.haveAPP(mContext, Constant.PACKAGE_FLOW_DX)) {
                    IntentUtils.startAPP(mActivity, Constant.PACKAGE_FLOW_DX);
                }
                break;
            case R.id.flowToggleLayout:
                if (Utils.haveSimCard(mTelephoneManager) && Utils.isFlowOpen(mTelephoneManager)) {
                    flowToggle.setChecked(false);
                    flowToggleText.setText(getString(R.string.noOpen));
                    Utils.setMobileFlowState(mTelephoneManager, false);
                } else {
                    flowToggle.setChecked(true);
                    flowToggleText.setText(getString(R.string.opened));
                    Utils.setMobileFlowState(mTelephoneManager, true);
                }
                break;
            case R.id.volteLayout:
                if (IntentUtils.haveAPP(mContext, Constant.PACKAGE_VOLTE)) {
                    IntentUtils.startAPP(mActivity, Constant.PACKAGE_VOLTE);
                }
                break;
            case R.id.edogLayout:
                if (IntentUtils.haveAPP(mContext, Constant.PACKAGE_EDOG)) {
                    IntentUtils.startAPP(mActivity, Constant.PACKAGE_EDOG);
                }
                break;
            case R.id.galleryLayout:
                if (IntentUtils.haveAPP(mContext, Constant.PACKAGE_GALLERY_DX)) {
                    IntentUtils.startAPP(mActivity, Constant.PACKAGE_GALLERY_DX);
                }
                break;
            case R.id.moreLayout:
                Intent allAPP = new Intent(mContext, AllAPP.class);
                mContext.startActivity(allAPP);
                break;
            default:
                break;
        }
    }

    private void initPagerFirst(View view) {
        view.findViewById(R.id.naviLayout).setOnClickListener(this);
        remainingDistance = view.findViewById(R.id.remainingDistance);
        currentLocation = view.findViewById(R.id.currentLocation);

        view.findViewById(R.id.drivLayout).setOnClickListener(this);
        recordMarket = view.findViewById(R.id.recordMarket);
        drivStartImage = view.findViewById(R.id.drivStartImage);
        drivStartText = view.findViewById(R.id.drivStartText);

        view.findViewById(R.id.musicLayout).setOnClickListener(this);
        view.findViewById(R.id.musicPre).setOnClickListener(this);
        view.findViewById(R.id.musicNext).setOnClickListener(this);
        musicPlayPause = view.findViewById(R.id.musicPlayPause);
        musicPlayPause.setOnClickListener(this);

        etcLayout = view.findViewById(R.id.etcLayout);
        etcLessMoney = view.findViewById(R.id.etcLessMoney);
        etcLayout.setOnClickListener(this);

        picLayout = view.findViewById(R.id.picLayout);
        picLayout.setOnClickListener(this);
        photoBottToggle = view.findViewById(R.id.photoBottToggle);
        view.findViewById(R.id.photoBottLayout).setOnClickListener(this);
        photoBottText = view.findViewById(R.id.photoBottText);
        canTakePhoto = PreferanceUtils.getDataBoolean(mContext, "canTakePhoto", false);
        photoBottToggle.setChecked(canTakePhoto);
        if (!canTakePhoto) {
            photoBottText.setText(mContext.getString(R.string.closed));
        } else {
            photoBottText.setText(mContext.getString(R.string.opened));
        }
        newPoint();
    }

    private void initPagerSecond(View view) {
        view.findViewById(R.id.btLayout).setOnClickListener(this);
        view.findViewById(R.id.btBottLayout).setOnClickListener(this);
        btBottToggle = view.findViewById(R.id.btBottToggle);
        btBootText = view.findViewById(R.id.btBootText);
        btConnectText = view.findViewById(R.id.btConnectText);
        btConnectIcon = view.findViewById(R.id.btConnectIcon);
        if (BluetoothAdapter.getDefaultAdapter().isEnabled()) {
            btBottToggle.setChecked(true);
        } else {
            btBottToggle.setChecked(false);
        }

        view.findViewById(R.id.fmLayout).setOnClickListener(this);
        fmSize = view.findViewById(R.id.fmSize);

        view.findViewById(R.id.setLayout).setOnClickListener(this);
        view.findViewById(R.id.setLayout).setOnLongClickListener(mLongClick);
        view.findViewById(R.id.setLayout).setOnTouchListener(mTouchListener);
        view.findViewById(R.id.lightLayout).setOnTouchListener(mLightTouch);
        lightSeekBar = view.findViewById(R.id.lightSeekBar);
        lightSeekBar.setMax(255);
        normal = Settings.System.getInt(mContext.getContentResolver(),
                Settings.System.SCREEN_BRIGHTNESS, 255);
        lightSeekBar.setProgress(normal);
//        lightSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
//            @Override
//            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
//                int tmpInt = seekBar.getProgress();
//                mContext.sendBroadcast(new Intent(
//                        Constant.SYSTEM_BRIGHTNESS_CHANGE_ACTION));
//                Settings.System.putInt(mContext.getContentResolver(),
//                        Settings.System.SCREEN_BRIGHTNESS, tmpInt);
//                tmpInt = Settings.System.getInt(mContext.getContentResolver(),
//                        Settings.System.SCREEN_BRIGHTNESS, -1);
//                WindowManager.LayoutParams wl = getWindow().getAttributes();
//                float tmpFloat = (float) tmpInt / 255;
//                if (tmpFloat > 0 && tmpFloat <= 1) {
//                    wl.screenBrightness = tmpFloat;
//                }
//                getWindow().setAttributes(wl);
//            }
//
//            @Override
//            public void onStartTrackingTouch(SeekBar seekBar) {
//            }
//
//            @Override
//            public void onStopTrackingTouch(SeekBar seekBar) {
//            }
//        });
        view.findViewById(R.id.soundLayout).setOnTouchListener(mSoundTouch);
        soundSeekBar = view.findViewById(R.id.soundSeekBar);
        int maxVolume = mAudioManager
                .getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        int currentVolume = mAudioManager
                .getStreamVolume(AudioManager.STREAM_MUSIC);
        soundSeekBar.setMax(maxVolume);
        soundSeekBar.setProgress(currentVolume);
//        soundSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
//            @Override
//            public void onProgressChanged(SeekBar seekBar, final int progress, boolean fromUser) {
//                AsyncTask.execute(new Runnable() {
//                    @Override
//                    public void run() {
//                        mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC,
//                                progress, 0);
//                    }
//                });
//                Intent VolumnChangeAction = new Intent(Constant.SYSTEM_VOLUMN_CHANGE_ACTION);
//                mContext.sendBroadcast(VolumnChangeAction);
//            }
//
//            @Override
//            public void onStartTrackingTouch(SeekBar seekBar) {
//            }
//
//            @Override
//            public void onStopTrackingTouch(SeekBar seekBar) {
//            }
//        });

        wechatLayout = view.findViewById(R.id.wechatLayout);
        wechatLayout.setOnClickListener(this);

        qqLayout = view.findViewById(R.id.qqLayout);
        qqLayout.setOnClickListener(this);

        newPoint();
    }

    private void initPagerThird(View view) {
        view.findViewById(R.id.flowLayout).setOnClickListener(this);
        view.findViewById(R.id.flowToggleLayout).setOnClickListener(this);
        flowToggle = view.findViewById(R.id.flowToggle);
        flowToggleText = view.findViewById(R.id.flowToggleText);
//        if (Utils.haveSimCard(mTelephoneManager) && Utils.isFlowOpen(mTelephoneManager)) {
//            flowToggle.setChecked(true);
//            flowToggleText.setText(getString(R.string.opened));
//        } else {
//            flowToggle.setChecked(false);
//            flowToggleText.setText(getString(R.string.noOpen));
//        }
        view.findViewById(R.id.volteLayout).setOnClickListener(this);

        edogLayout = view.findViewById(R.id.edogLayout);
        edogLayout.setOnClickListener(this);

        galleryLayout = view.findViewById(R.id.galleryLayout);
        galleryLayout.setOnClickListener(this);

        view.findViewById(R.id.moreLayout).setOnClickListener(this);

        newPoint();
    }

    private void registerBroadcast() {
        //记录仪相关广播
        IntentFilter filterRecord = new IntentFilter();
        filterRecord.addAction(Constant.RECORDER_START);
        filterRecord.addAction(Constant.RECORDER_STOP);
//        filterRecord.addAction(Constant.CAPTURE_DONE);
        mContext.registerReceiver(recordReceiver, filterRecord);

        //FM相关广播
        IntentFilter filterFM = new IntentFilter();
        filterFM.addAction(Constant.MTK_FM_OPEN_CLOSE);
        filterFM.addAction(Constant.NODIFY_NOWFREQUENCY);
        mContext.registerReceiver(fmRecord, filterFM);

        //网络状态改变广播
        IntentFilter filterNetWork = new IntentFilter();
        filterNetWork.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
//        filterNetWork.addAction(Intent.ACTION_TIME_TICK);
        registerReceiver(mNetWorkState, filterNetWork);

        //其他广播
        IntentFilter filterOther = new IntentFilter();
        filterOther.addAction(Constant.ETC_BROADCAST);
        filterOther.addAction(Constant.REMOVE_NAVIGATIONBAR);
        filterOther.addAction(Constant.AMPA_BROAD);
        filterOther.addAction(Constant.VOLUME_CHANGED_ACTION);
        mContext.registerReceiver(otherReceiver, filterOther);

        //控制屏幕亮度
        mContext.getContentResolver().registerContentObserver(
                Settings.System.getUriFor(Settings.System.SCREEN_BRIGHTNESS),
                true, brightnessMode);

    }


    private void bindAidl() {
        // 绑定FM应用的FMService
        Intent intentFM = new Intent("com.digissin.fm.services.FMService");
        intentFM.setPackage(Constant.PACKAGE_FM_DX);
        bindService(intentFM, mFMConnection, BIND_AUTO_CREATE);

    }

    static class MyHandler extends Handler {

        WeakReference<MainActivity> mWeak;

        MyHandler(MainActivity activity) {
            mWeak = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            MainActivity mMian = mWeak.get();
            if (mMian == null)
                return;
            switch (msg.what) {
                case REFRESH_WATER:
                    WeatherBean bean = (WeatherBean) msg.obj;
                    LogUtils.e(bean.getmWeather() + "  " + bean.getmTempratureSize() + "  " + bean.getmWeatherPic());
                    break;
                case REFRESH_LIGHT:
                    int mBrightProgress = Settings.System.getInt(
                            mMian.getContentResolver(),
                            Settings.System.SCREEN_BRIGHTNESS, 255);
                    lightSeekBar.setMax(255);
                    lightSeekBar.setProgress(mBrightProgress);
                    break;
                default:
                    break;
            }

        }
    }

//
//    private Handler mHandler = new Handler() {
//        @Override
//        public void handleMessage(Message msg) {
//            super.handleMessage(msg);
//            switch (msg.what) {
//                case REFRESH_WATHER:
//                    WeatherBean bean = (WeatherBean) msg.obj;
//                    LogUtils.e(TAG, bean.getmWeather() + "  " + bean.getmTempratureSize() + "  " + bean.getmWeatherPic());
//                    break;
//                default:
//                    break;
//            }
//        }
//    };

    private Runnable mRunnable = new Runnable() {
        @Override
        public void run() {
            refreshTime();//刷新时间
            refreshBluetooth();//刷新蓝牙状态
            if (fmSize.getText().equals(mContext.getString(R.string.textNull)) && mFMService != null) {
                try {
                    fmSize.setText(mFMService.getFMRate() + "");
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
            refreshWIFIIcon();
            currentLocation.setText(locationCity);
            mHandler.postDelayed(mRunnable, 1000 * 2);
        }
    };

    private void refreshWIFIIcon() {
//        if (mWifiManager.isWifiEnabled()) {// wifi处于激活状态
//            NetworkInfo wifiInfo = mConnectivityManager
//                    .getNetworkInfo(ConnectivityManager.TYPE_WIFI);
//            if (wifiInfo.isConnected()) {
//                topbarWifi.setVisibility(View.VISIBLE);
//                int index = WifiManager.calculateSignalLevel(mWifiManager
//                        .getConnectionInfo().getRssi(), 4);
//                refushWifi(index);
//            } else {
//                topbarWifi.setVisibility(View.GONE);
//            }
//        } else {// wifi处于非激活状态
//            topbarWifi.setVisibility(View.GONE);
//        }
    }

    private void refreshTime() {

    }

    private void refreshFM() {
        if (mFMService != null) {
            try {
                fmSize.setText(mFMService.getFMRate() + "");
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    private void refreshBluetooth() {
        if (BluetoothAdapter.getDefaultAdapter().isEnabled()) {
            btBootText.setText(R.string.opened);
            btBottToggle.setChecked(true);
            if (BTBroadcast.isBTConnect()
//                    ||Utils.isBTConnect()||isBTConnect
                    ) {
                btConnectIcon.setImageResource(R.drawable.bluetooth_icon_connected);
                btConnectText.setText(R.string.connected);
            } else {
                btConnectIcon.setImageResource(R.drawable.bluetooth_icon_unconnected);
                btConnectText.setText(R.string.noConnected);
            }
        } else {
            btBottToggle.setChecked(false);
            btConnectIcon.setImageResource(R.drawable.bluetooth_icon_unconnected);
            btConnectText.setText(R.string.noConnected);
            btBootText.setText(R.string.noOpen);
        }
    }

    private boolean isCharging(int plug, int status) {
        boolean ischarging = false;
        if (plug == BatteryManager.BATTERY_PLUGGED_AC
                || plug == BatteryManager.BATTERY_PLUGGED_USB) {
            ischarging = true;
        }
        if (status == BatteryManager.BATTERY_STATUS_CHARGING) {
            ischarging = true;
        }
        return ischarging;
    }

    private void refreshView(boolean isFullWind) {
        if (isFullWind) {
            etcLayout.setVisibility(View.VISIBLE);
            picLayout.setVisibility(View.VISIBLE);
            wechatLayout.setVisibility(View.VISIBLE);
            qqLayout.setVisibility(View.VISIBLE);
            edogLayout.setVisibility(View.VISIBLE);
            galleryLayout.setVisibility(View.VISIBLE);
        } else {
            etcLayout.setVisibility(View.GONE);
            picLayout.setVisibility(View.GONE);
            wechatLayout.setVisibility(View.GONE);
            qqLayout.setVisibility(View.GONE);
            edogLayout.setVisibility(View.GONE);
            galleryLayout.setVisibility(View.GONE);
        }
    }

    private void switchRecord(boolean status) {
        if (status) {
            mContext.sendBroadcast(new Intent(Constant.RECORDER_CONTROL).putExtra("msg", "start"));
        } else {
            mContext.sendBroadcast(new Intent(Constant.RECORDER_CONTROL).putExtra("msg", "stop"));
        }
    }

    private void switchFM(boolean status) {
        try {
            if (mFMService != null) {
                if (status) {
                    mFMService.TakeOnFM();
                } else {
                    mFMService.TakeOffFM();
                }
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    private ServiceConnection mFMConnection = new ServiceConnection() {

        @Override
        public void onServiceDisconnected(ComponentName name) {
            mFMService = null;
        }

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mFMService = IFMStatusService.Stub.asInterface(service);
        }
    };

    private void newPoint() {
        ImageView pointView = new ImageView(this);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        params.rightMargin = 10;
        params.leftMargin = 10;
        params.gravity = Gravity.CENTER_VERTICAL;
        pointView.setLayoutParams(params);
        pointView.setBackgroundResource(R.drawable.pointbg);
        navigationPoint.addView(pointView);
    }

    private void setPointFocors(int pos) {
        for (int i = 0; i < navigationPoint.getChildCount(); i++) {
            if (i == pos) {
                navigationPoint.getChildAt(i).setSelected(true);
            } else {
                navigationPoint.getChildAt(i).setSelected(false);
            }
        }
    }

    private void refreshRecordingView(boolean isRecording) {
        mHandler.removeCallbacks(mTimeRunnable);
        drivTimeLong = 0;
        if (isRecording) {
            drivStartImage.setVisibility(View.VISIBLE);
            drivStartText.setText(mContext.getResources().getString(R.string.recording));
            mHandler.post(mTimeRunnable);
        } else {
            drivStartImage.setVisibility(View.GONE);
            drivStartText.setText(mContext.getResources().getString(R.string.norecording));
            recordMarket.setText(mContext.getResources().getString(R.string.duration));
        }
    }

    private View.OnTouchListener mTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                y1 = event.getY();
            }
            if (event.getAction() == MotionEvent.ACTION_UP) {
                if (Math.abs(y1 - event.getY()) > 50 && isLongClick) {
                    if (IntentUtils.haveAPP(mContext, Constant.PACKAGE_SET_ANDROID)) {
                        Utils.makeToast(mContext, mContext.getString(R.string.androidSetting));
                        IntentUtils.startAPP(mActivity, Constant.PACKAGE_SET_ANDROID);
                    }
                    isLongClick = false;
                }
            }
            return false;
        }
    };

    private View.OnTouchListener mLightTouch = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                x1 = event.getX();
                normal = lightSeekBar.getProgress();
                if (x1 <= 50) {
                    if (normal <= 0)
                        return true;
                    normal -= 20;
                } else if (x1 >= 140) {
                    if (normal >= 255)
                        return true;
                    normal += 20;
                }
                mContext.sendBroadcast(new Intent(
                        Constant.SYSTEM_BRIGHTNESS_CHANGE_ACTION));
                Settings.System.putInt(mContext.getContentResolver(),
                        Settings.System.SCREEN_BRIGHTNESS, normal);
                normal = Settings.System.getInt(mContext.getContentResolver(),
                        Settings.System.SCREEN_BRIGHTNESS, -1);
                WindowManager.LayoutParams wl = getWindow().getAttributes();
                float tmpFloat = (float) normal / 255;
                if (tmpFloat > 0 && tmpFloat <= 1) {
                    wl.screenBrightness = tmpFloat;
                }
                getWindow().setAttributes(wl);
                lightSeekBar.setProgress(normal);
                try {
                    Thread.sleep(200);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            return true;
        }
    };

    private View.OnTouchListener mSoundTouch = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                x1 = event.getX();
                normal = soundSeekBar.getProgress();
                if (x1 <= 50) {
                    if (normal <= 0)
                        return true;
                    normal -= 1;
                } else if (x1 >= 140) {
                    if (normal >= 255)
                        return true;
                    normal += 1;
                }
                AsyncTask.execute(new Runnable() {
                    @Override
                    public void run() {
                        mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC,
                                normal, 0);
                    }
                });
                Intent VolumnChangeAction = new Intent(Constant.SYSTEM_VOLUMN_CHANGE_ACTION);
                mContext.sendBroadcast(VolumnChangeAction);
                soundSeekBar.setProgress(normal);
                try {
                    Thread.sleep(200);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            return true;
        }
    };
    private View.OnLongClickListener mLongClick = new View.OnLongClickListener() {
        @Override
        public boolean onLongClick(View v) {
            isLongClick = true;
            return true;
        }
    };

    private void takePhoto() {
        Intent makePic = new Intent(Constant.NOTIFACATION_MSG_TO_RECODER);
        makePic.putExtra("msg", "getphoto");
        mContext.sendBroadcast(makePic);
    }

    private Runnable mTimeRunnable = new Runnable() {
        @Override
        public void run() {
            drivTimeLong++;
            int hour = (int) (drivTimeLong / 60 / 60) % 60;
            int minute = (int) (drivTimeLong / 60) % 60;
            int second = (int) (drivTimeLong % 60);

            if (recordMarket != null) {
                recordMarket.setText(String.format("%02d:%02d:%02d", hour, minute,
                        second));
                if (recordMarket.getText().equals("99:59:59")) {
                    drivTimeLong = 0;
                }
            }
            mHandler.postDelayed(mTimeRunnable, 1000);
        }
    };

    private OnPlayerStatusListener musicListener = new OnPlayerStatusListener() {
        @Override
        public void onPlayerStatus(PlayerStatus playerStatus) {
            switch (playerStatus) {
                case PLAYING:
                    isMusicPlay = true;
                    musicPlayPause.setImageResource(R.drawable.home_music_btn_pause);
                    break;
                default:
                    isMusicPlay = false;
                    musicPlayPause.setImageResource(R.drawable.home_music_btn_play);
                    break;
            }
        }
    };


    private WeatherService.WeatherCallBack mWeatherCallBack = new WeatherService.WeatherCallBack() {
        @Override
        public void setWeatherBean(WeatherBean bean) {

        }

        @Override
        public void setCityName(String cityName) {
            locationCity = cityName;
        }
    };

    private BroadcastReceiver mNetWorkState = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            switch (action) {
                case ConnectivityManager.CONNECTIVITY_ACTION:
                    NetworkInfo networkInfo = mConnectivityManager.getActiveNetworkInfo();
                    if (networkInfo != null && networkInfo.isConnected()) {
                        if (!WeatherService.isStart) {
                            Intent service = new Intent(mContext,
                                    WeatherService.class);
                            startService(service);
                        }
                    }
                    break;
                case Intent.ACTION_TIME_TICK:
                    break;
                default:
                    break;
            }

        }
    };

    private BroadcastReceiver otherReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            switch (action) {
                case Constant.ETC_BROADCAST:
                    etcLessMoney.setText(intent.getFloatExtra("etcLessMoney", 000.0f) + "");
                    break;
                case Constant.REMOVE_NAVIGATIONBAR:
                    boolean isWindFullData = intent.getBooleanExtra("remove", false);
                    PreferanceUtils.saveDataBoolean(mContext, "isFullWind", isWindFullData);
                    refreshView(isWindFullData);
                    break;
                case Constant.AMPA_BROAD:
                    String lessDis = String.format("%.2f",
                            (float) intent.getIntExtra("ROUTE_REMAIN_DIS", 0) / 1000);
                    remainingDistance.setText(lessDis + " km");
                    break;
                case Constant.VOLUME_CHANGED_ACTION:
                    int currVolume = mAudioManager
                            .getStreamVolume(AudioManager.STREAM_MUSIC);
                    soundSeekBar.setProgress(currVolume);
                    break;
                default:
                    break;
            }
        }
    };

    private BroadcastReceiver fmRecord = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            refreshFM();
//            String action = intent.getAction();
//            switch (action) {
//                case Constant.MTK_FM_OPEN_CLOSE:
//                case Constant.NODIFY_NOWFREQUENCY:
//                    refushFM();
//                    break;
//                default:
//                    break;
//            }
        }
    };

    private ContentObserver brightnessMode = new ContentObserver(new Handler()) {
        @Override
        public void onChange(boolean selfChange) {
            super.onChange(selfChange);
            mHandler.sendEmptyMessageDelayed(REFRESH_LIGHT, 1000);
        }
    };

    private BroadcastReceiver recordReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            switch (action) {
                case Constant.RECORDER_START:
                    refreshRecordingView(true);
                    break;
                case Constant.RECORDER_STOP:
                    refreshRecordingView(false);
                    break;
                case Constant.CAPTURE_DONE:
//                    String FrontUrl = intent.getStringExtra("FrontUrl");
//                    String BackUrl = intent.getStringExtra("BackUrl");
//                    if (TextUtils.isEmpty(FrontUrl)) {
//
//                    }
//                    if (TextUtils.isEmpty(BackUrl)) {
//
//                    }
                    break;
                default:
                    break;

            }

        }
    };

}
