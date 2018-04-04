package com.digissin.launcher.custom;

/**
 * Created by Freedom on 2017/12/19.
 */

public class WeatherBean {

    private String mCity;
    private String mTempratureSize;
    private String mNowTempature;
    private String mWind;
    private String mWeather;
    private String mWeatherPic;
    private String mPM;

    public String getmCity() {
        return mCity;
    }

    public void setmCity(String mCity) {
        this.mCity = mCity;
    }

    public String getmTempratureSize() {
        return mTempratureSize;
    }

    public void setmTempratureSize(String mTempratureSize) {
        this.mTempratureSize = mTempratureSize;
    }


    public String getmPM() {
        return mPM;
    }

    public void setmPM(String mPM) {
        this.mPM = mPM;
    }

    public String getmNowTempature() {
        return mNowTempature;
    }

    public void setmNowTempature(String mNowTempature) {
        this.mNowTempature = mNowTempature;
    }

    public String getmWind() {
        return mWind;
    }

    public void setmWind(String mWind) {
        this.mWind = mWind;
    }

    public String getmWeather() {
        return mWeather;
    }

    public void setmWeather(String mWeather) {
        this.mWeather = mWeather;
    }

    public String getmWeatherPic() {
        return mWeatherPic;
    }

    public void setmWeatherPic(String mWeatherPic) {
        this.mWeatherPic = mWeatherPic;
    }

    @Override
    public String toString() {
        return "mCity:" + mCity + " mTempatureSize:" + mTempratureSize + "  mNowTempature:" + mNowTempature + "  mWind:" + mWind + "  mWeather:" + mWeather + " mWeatherPic:" + mWeatherPic + " mPM:" + mPM;
    }
}
