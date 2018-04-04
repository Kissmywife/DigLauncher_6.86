package com.digissin.launcher;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ToggleButton;

import com.digissin.launcher.Utils.IntentUtils;
import com.digissin.launcher.Utils.LogUtils;
import com.digissin.launcher.Utils.PreferanceUtils;
import com.digissin.launcher.Utils.Utils;
import com.digissin.launcher.adapter.MyVPAdapter;
import com.digissin.launcher.custom.Constant;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Context mContext = MainActivity.this;
    private Activity mActivity = MainActivity.this;

    //第一页全部图标
    private LinearLayout navigationPoint;

    private boolean isLongClick = false;
    private float y1;
    private int normal = 0;
    //内部应用  绑定服务
    private MyHandler mHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mHandler = new MyHandler(this);
        boolean isFullWind = PreferanceUtils.getDataBoolean(mContext, "isFullWind", false);
        initView(isFullWind);
        doOther();
    }

    protected void initView(boolean isFullWind) {
        List<View> viewData = new ArrayList<>();
        viewData.clear();
        ViewPager viewPagerMain = findViewById(R.id.viewPagerMain);
        navigationPoint = findViewById(R.id.navigationPoint);
        View pageFirst;
        if (isFullWind) {
            pageFirst = LayoutInflater.from(mContext).inflate(R.layout.pagerfirst_full, null, false);
            initPagerFirstFull(pageFirst);
        } else {
            pageFirst = LayoutInflater.from(mContext).inflate(R.layout.pagerfirst, null, false);
            initPagerFirst(pageFirst);
        }
        viewData.add(pageFirst);
        //初始化控件
        setPointFocors(0);
        MyVPAdapter myVPAdapter = new MyVPAdapter(viewData);
        viewPagerMain.setAdapter(myVPAdapter);
        viewPagerMain.setOffscreenPageLimit(2);//缓存4页
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

    protected void doOther() {
        registerBroadcast();//注册广播
//        if (Utils.haveSimCard(mTelephoneManager)) {
//            networkStrenth();//刷新信号图标
//        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mContext.unregisterReceiver(otherReceiver);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode==KeyEvent.KEYCODE_BACK){
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.navill:
                if (IntentUtils.haveAPP(mContext, Constant.PACKAGE_NAVI_GOOGLE)) {
                    IntentUtils.startAPP(mActivity, Constant.PACKAGE_NAVI_GOOGLE);
                }
                break;
            case R.id.musicll:
                if (IntentUtils.haveAPP(mContext, Constant.PACKAGE_MUSIC_ANDROID)) {
                    IntentUtils.startAPP(mActivity, Constant.PACKAGE_MUSIC_ANDROID);
                }
                break;
            case R.id.recordll:
                if (IntentUtils.haveAPP(mContext, Constant.PACKAGE_DRIV_DX_NEW)) {
                    IntentUtils.startAPP(mActivity, Constant.PACKAGE_DRIV_DX_NEW);
                }
                break;
            case R.id.btll:
                if (IntentUtils.haveAPP(mContext, Constant.PACKAGE_BT_DX)) {
                    IntentUtils.startAPP(mActivity, Constant.PACKAGE_BT_DX);
                }
                break;
            case R.id.flowll:
                if (IntentUtils.haveAPP(mContext, Constant.PACKAGE_FLOW_DX)) {
                    IntentUtils.startAPP(mActivity, Constant.PACKAGE_FLOW_DX);
                }
                break;
            case R.id.fmll:
                if (IntentUtils.haveAPP(mContext, Constant.PACKAGE_FM_DX)) {
                    IntentUtils.startAPP(mActivity, Constant.PACKAGE_FM_DX);
                }
                break;
            case R.id.filell:
                if (IntentUtils.haveAPP(mContext, Constant.PACKAGE_FILE_DX)) {
                    IntentUtils.startAPP(mActivity, Constant.PACKAGE_FILE_DX);
                }
                break;
            case R.id.setll:
                if (IntentUtils.haveAPP(mContext, Constant.PACKAGE_SET_DX)) {
                    IntentUtils.startAPP(mActivity, Constant.PACKAGE_SET_DX);
                }
                break;
            default:
                break;
        }
    }

    private void initPagerFirst(View view) {
        view.findViewById(R.id.navill).setOnClickListener(this);
        view.findViewById(R.id.musicll).setOnClickListener(this);
        view.findViewById(R.id.recordll).setOnClickListener(this);
        view.findViewById(R.id.flowll).setOnClickListener(this);
        view.findViewById(R.id.fmll).setOnClickListener(this);
        view.findViewById(R.id.filell).setOnClickListener(this);
        newPoint();
    }

    private void initPagerFirstFull(View view) {
        view.findViewById(R.id.navill).setOnClickListener(this);
        view.findViewById(R.id.musicll).setOnClickListener(this);
        view.findViewById(R.id.recordll).setOnClickListener(this);
        view.findViewById(R.id.btll).setOnClickListener(this);
        view.findViewById(R.id.flowll).setOnClickListener(this);
        view.findViewById(R.id.fmll).setOnClickListener(this);
        view.findViewById(R.id.filell).setOnClickListener(this);
        view.findViewById(R.id.setll).setOnClickListener(this);
        view.findViewById(R.id.setll).setOnLongClickListener(mLongClick);
        view.findViewById(R.id.setll).setOnTouchListener(mTouchListener);
        newPoint();
    }

    private void registerBroadcast() {
        //其他广播
        IntentFilter filterOther = new IntentFilter();
        filterOther.addAction(Constant.REMOVE_NAVIGATIONBAR);
        mContext.registerReceiver(otherReceiver, filterOther);

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

                default:
                    break;
            }

        }
    }

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


    private View.OnLongClickListener mLongClick = new View.OnLongClickListener() {
        @Override
        public boolean onLongClick(View v) {
            isLongClick = true;
            return true;
        }
    };

    private BroadcastReceiver otherReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            switch (action) {
                case Constant.REMOVE_NAVIGATIONBAR:
                    boolean isWindFullData = intent.getBooleanExtra("remove", false);
                    PreferanceUtils.saveDataBoolean(mContext, "isFullWind", isWindFullData);
                    initView(isWindFullData);
                    break;
                default:
                    break;
            }
        }
    };


}
