package com.digissin.launcher.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.AttributeSet;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by Freedom on 2017/12/19.
 */

public class MyImageView extends android.support.v7.widget.AppCompatImageView {

    private final int REFUSH_WEATHER_VIEW = 10086;

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case REFUSH_WEATHER_VIEW:
                    setImageBitmap((Bitmap) msg.obj);
                    break;
                default:
                    break;
            }
        }
    };

    public MyImageView(Context context) {
        super(context);
    }

    public MyImageView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public MyImageView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setImageFromHttp(final String url) {
        if (TextUtils.isEmpty(url)) {
            return;
        }
        new Thread() {
            @Override
            public void run() {
                super.run();
                Bitmap pic = getBitmap(url);
                if (pic==null)
                    return;
                Message msg = mHandler.obtainMessage(REFUSH_WEATHER_VIEW);
                msg.obj = pic;
                mHandler.sendMessage(msg);
            }
        }.start();
    }

    private Bitmap getBitmap(String url) {
        Bitmap bitmap = null;
        try {
            URL mURL=new URL(url);
            HttpURLConnection conn = (HttpURLConnection) mURL
                    .openConnection();
            conn.setConnectTimeout(6000);
            conn.setDoInput(true);
            conn.setUseCaches(false);
            InputStream is = conn.getInputStream();
            bitmap = BitmapFactory.decodeStream(is);
            is.close();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }catch (IOException e) {
            e.printStackTrace();
        }
        return bitmap;
    }

}
