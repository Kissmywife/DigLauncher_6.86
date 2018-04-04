package com.digissin.launcher.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.digissin.launcher.Utils.LogUtils;
import com.digissin.launcher.Utils.PreferanceUtils;
import com.digissin.launcher.custom.WeatherBean;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

public class WeatherService extends Service implements AMapLocationListener {

    public static boolean isStart;
    //声明AMapLocationClient类对象
    public AMapLocationClient mLocationClient = null;
    //声明AMapLocationClientOption对象
    public AMapLocationClientOption mLocationOption = null;
    private static WeatherBean bean;
    private static Context mContext;
    private static String TAG = "WeatherService";
    private static String httpUrl = "http://apis.baidu.com/showapi_open_bus/weather_showapi/point";
    private static String httpArg = "";

    private String lastAddress = "";
    private static WeatherCallBack mCallback;
//    private static boolean getWeatherSuccess = false;

    public static void setWeatherCallback(WeatherCallBack weatherCallBcak) {
        mCallback = weatherCallBcak;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        isStart = true;
        mContext = getApplicationContext();
        startLocation();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        isStart = false;
        mLocationClient.stopLocation();//停止定位后，本地定位服务并不会被销毁
        mLocationClient.onDestroy();//销毁定位客户端，同时销毁本地定位服务。
    }

    private void startLocation() {
        //初始化定位
        mLocationClient = new AMapLocationClient(mContext);
        //设置定位回调监听
        mLocationClient.setLocationListener(this);
        //初始化AMapLocationClientOption对象
        mLocationOption = new AMapLocationClientOption();
        //设置定位模式为AMapLocationMode.Hight_Accuracy，高精度模式。
        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Battery_Saving);
        //设置定位间隔,单位毫秒,默认为2000ms，最低1000ms。
        mLocationOption.setInterval(1000 * 20);
        //设置是否返回地址信息（默认返回地址信息）
        mLocationOption.setNeedAddress(true);
        //设置是否允许模拟位置,默认为true，允许模拟位置
        mLocationOption.setMockEnable(true);
        //单位是毫秒，默认30000毫秒，建议超时时间不要低于8000毫秒。
        mLocationOption.setHttpTimeOut(1000 * 20);
        //关闭缓存机制
        mLocationOption.setLocationCacheEnable(true);
        mLocationOption.setWifiScan(false);
        //给定位客户端对象设置定位参数
        mLocationClient.setLocationOption(mLocationOption);
        //启动定位
        mLocationClient.startLocation();
    }

    @Override
    public void onLocationChanged(AMapLocation aMapLocation) {
        if (aMapLocation != null) {
            if (aMapLocation.getErrorCode() == 0) {
//                //可在其中解析amapLocation获取相应内容。
//                lon = aMapLocation.getLongitude();
//                lat = aMapLocation.getLatitude();
//                httpArg = "lng="
//                        + lon
//                        + "&lat="
//                        + lat
//                        + "&from=5&needMoreDay=0&needIndex=0&needAlarm=0&need3HourForcast=0";
//                if (!aMapLocation.getCity().equals(laseCityName) || !getWeatherSuccess) {
//                    laseCityName = aMapLocation.getCity();
//                    getWeatherData();
//                }
                if (mCallback != null && !lastAddress.equals(aMapLocation.getStreet())) {
                    mCallback.setCityName(aMapLocation.getStreet());
                    lastAddress = aMapLocation.getStreet();
                }
            } else {
                LogUtils.e(TAG, "location Error, ErrCode:"
                        + aMapLocation.getErrorCode() + ", errInfo:"
                        + aMapLocation.getErrorInfo());
                stopSelf();
            }
        }
    }

    private static void getWeatherData() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                request(httpUrl, httpArg);
            }
        }).start();
    }

    // 该天气使用的是易源的天气 http://apistore.baidu.com/apiworks/servicedetail/515.html
    public static void request(String httpUrl, String httpArg) {
        BufferedReader reader = null;
        StringBuffer sbf = new StringBuffer();
        URL url = null;
        LogUtils.e(TAG, httpUrl + "?" + httpArg);
        try {
            url = new URL(httpUrl + "?" + httpArg);
            HttpURLConnection connection = null;
            connection = (HttpURLConnection) url
                    .openConnection();
            connection.setRequestMethod("GET");
            // 填入apikey到HTTP header
            connection.setRequestProperty("apikey",
                    "1bcad8501b88cf96484a1c78be747e1a");
            connection.connect();
            InputStream is = connection.getInputStream();
            reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
            String strRead = null;
            while ((strRead = reader.readLine()) != null) {
                sbf.append(strRead);
                sbf.append("\r\n");
            }
            reader.close();
            if (!" ".equals(sbf.toString())) {
                LogUtils.i(TAG, "天气数据：" + sbf.toString());
                JSONObject WeatherData = new JSONObject(sbf.toString());
                JSONObject allWeather = WeatherData
                        .getJSONObject("showapi_res_body");// 所有天气数据
                if ("0".equals(allWeather.getString("ret_code"))) { // ret_code==0表示数据查询成功
//                    getWeatherSuccess = true;
                    JSONObject cityInfo = allWeather
                            .getJSONObject("cityInfo");// 当前地理位置信息
                    String currentCity = cityInfo.getString("c7") + "." + cityInfo.getString("c3");// 当前城市
                    JSONObject f1 = allWeather.getJSONObject("f1");// 当天天气
                    String day_air_temperature = f1
                            .getString("day_air_temperature");// 白天气温（最高气温）
                    String night_air_temperature = f1
                            .getString("night_air_temperature");// 夜间气温（最低气温）
                    JSONObject now = allWeather.getJSONObject("now");// 现在时间的天气指数
                    String now_temperature = now.getString("temperature");// 当前温度
                    String weather = now.getString("weather");// 天气情况
                    String weather_pic = now.getString("weather_pic");// 天气图标
                    String wind_direction = now.getString("wind_direction");// 天气情况
                    JSONObject aqiDetail = now.getJSONObject("aqiDetail");// 空气质量
                    String pm2_5 = aqiDetail.getString("pm2_5");// 空气质量PM2_5
                    //数据存储
                    if (bean == null)
                        bean = new WeatherBean();
                    bean.setmCity(currentCity);
                    bean.setmNowTempature(now_temperature);
                    bean.setmTempratureSize(day_air_temperature + "~" + night_air_temperature + "℃");
                    bean.setmWeather(weather);
                    bean.setmWeatherPic(weather_pic);
                    bean.setmWind(wind_direction);
                    bean.setmPM(pm2_5);
                    PreferanceUtils.saveData(mContext, "mCity", currentCity);
                    PreferanceUtils.saveData(mContext, "mNowTempature", now_temperature);
                    PreferanceUtils.saveData(mContext, "mTempratureSize", day_air_temperature + "~" + night_air_temperature + "℃");
                    PreferanceUtils.saveData(mContext, "mWeather", weather);
                    PreferanceUtils.saveData(mContext, "mWeatherPic", weather_pic);
                    PreferanceUtils.saveData(mContext, "mWind", wind_direction);
                    PreferanceUtils.saveData(mContext, "mPM", pm2_5);
                    LogUtils.i(TAG, bean.toString());
                    if (mCallback != null) {
                        mCallback.setWeatherBean(bean);
                    }
                } else {
                    request(httpUrl, httpArg);
                }
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public interface WeatherCallBack {
        void setWeatherBean(WeatherBean bean);

        void setCityName(String cityName);
    }

}
