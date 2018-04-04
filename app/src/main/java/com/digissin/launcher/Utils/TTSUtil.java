package com.digissin.launcher.Utils;

import java.util.Locale;

import android.content.Context;
import android.speech.tts.TextToSpeech;
import android.speech.tts.TextToSpeech.OnInitListener;
import android.text.TextUtils;

/**
 * @author slx TTS 工具类
 */
public class TTSUtil {

	private static final String TAG = "TTSUtil";
	private static TextToSpeech mTTS;
	private static TTSUtil mTTSUtils;
	private float ttSpeechRate = (float) 1.0;
	private float ttsPitch = (float) 1.0;

	public float getTtsPitch() {
		return ttsPitch;
	}

	public float getSpeechRate() {
		return ttSpeechRate;
	}

	/**
	 * 
	 * @author slx 2017-11-23 下午4:36:26
	 * @param mContext
	 * @return
	 */
	public static TTSUtil getInstance(final Context mContext) {
		if (mTTSUtils == null) {
			mTTSUtils = new TTSUtil(mContext);
		}
		return mTTSUtils;
	}

	private TTSUtil(final Context mContext) {
		mTTS = new TextToSpeech(mContext, new OnInitListener() {
			@Override
			public void onInit(int status) {
				if (status == TextToSpeech.SUCCESS) {
					int result = mTTS.setLanguage(Locale.CHINA);
					if (result != TextToSpeech.LANG_COUNTRY_AVAILABLE
							&& result != TextToSpeech.LANG_AVAILABLE) {
						LogUtils.e(TAG, "launage_no_support");
					} else {
						LogUtils.e(TAG, "tts_instance_success");
					}
				} else {
					LogUtils.e(TAG, "tts_instance_error");
				}
			}
		});
	}

	/**
	 * 设置语音播报速度
	 * 
	 * @author slx 2017-11-23 下午5:48:15
	 * @param ttSpeechRate
	 *            1正常语速，范围自定
	 */
	public void setSpeechRate(float ttSpeechRate) {
		this.ttSpeechRate = ttSpeechRate;
		mTTS.setSpeechRate(ttSpeechRate);
	}

	/**
	 * 设置语音播放音调
	 * 
	 * @author slx 2017-11-23 下午5:48:19
	 * @param ttsPitch
	 *            1正常音调，范围自定（越大越接近女声）
	 */
	public void setTTSPitch(float ttsPitch) {
		this.ttsPitch = ttsPitch;
		mTTS.setPitch(ttsPitch);
	}

	/**
	 * 文字转化为语音输出
	 * 
	 * @author slx 2017-11-23 下午5:32:14
	 * @param text
	 *            播报文字
	 * @param flush
	 *            是否打断当前播报
	 */
	public void speakText(String text, boolean flush) {
		if (!TextUtils.isEmpty(text)) {
			if (flush) {
				mTTS.speak(text, TextToSpeech.QUEUE_FLUSH, null);
				// 等效
				// if (mTTS.isSpeaking()) {
				// mTTS.stop();
				// }
				// mTTS.speak(text, TextToSpeech.QUEUE_ADD, null);
			} else {
				mTTS.speak(text, TextToSpeech.QUEUE_ADD, null);
			}
		}
	}

	/**
	 * 关闭TTS
	 * 
	 * @author slx 2017-11-23 下午4:39:54
	 */
	public void shotdownTTS() {
		if (mTTS != null) {
			mTTS.stop();
			mTTS.shutdown();
		}
	}

}
