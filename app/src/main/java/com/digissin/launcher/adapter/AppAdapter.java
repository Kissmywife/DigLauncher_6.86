package com.digissin.launcher.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.digissin.launcher.R;
import com.digissin.launcher.custom.APPBean;

import java.util.List;

public class AppAdapter extends BaseAdapter {
	public Context context;
	public List<APPBean> appInfos;

	public AppAdapter(Context context, List<APPBean> appInfos) {
		this.context = context;
		this.appInfos = appInfos;
	}

	@Override
	public int getCount() {
		return appInfos.size();
	}

	@Override
	public Object getItem(int arg0) {
		return appInfos.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		return arg0;
	}

	@SuppressLint({"NewApi", "WrongConstant"})
	@Override
	public View getView(int arg0, View view, ViewGroup arg2) {
		ViewHolder holder = null;
		if (view == null) {
			holder = new ViewHolder();
			view = LayoutInflater.from(context).inflate(R.layout.gird_item,
					null);
			holder.grid_img = (ImageView) view.findViewById(R.id.grid_item_img);
			holder.grid_txt = (TextView) view.findViewById(R.id.grid_item_txt);
			view.setTag(holder);
		} else {
			holder = (ViewHolder) view.getTag();
		}
		Context localContext = null;
		try {
			localContext = context.createPackageContext(appInfos
                    .get(arg0).getAppPackageName(), Context.MODE_WORLD_WRITEABLE);
			PackageInfo packageInfo = context.getPackageManager()
					.getPackageInfo(appInfos.get(arg0).getAppPackageName(),
							Context.MODE_PRIVATE);
			Bitmap bitmap = drawableToBitmap(localContext.getResources()
					.getDrawableForDensity(
							packageInfo.applicationInfo.icon, 320));
			holder.grid_img.setImageBitmap(createScaledBitmap(bitmap, 100,
					100, ImageView.ScaleType.CENTER_CROP));
			// 最后回收bitmap，防止内存溢出
			if (!bitmap.isRecycled()) {// 回收资源，避免内存溢出
				bitmap.recycle(); // 回收图片所占的内存
				System.gc(); // 提醒系统及时回收
			}
		} catch (PackageManager.NameNotFoundException e) {
			e.printStackTrace();
		}
		holder.grid_txt.setText(appInfos.get(arg0).getAppName());
		return view;
	}

	private class ViewHolder {
		public ImageView grid_img;
		public TextView grid_txt;
	}

	public static Bitmap drawableToBitmap(Drawable drawable) {
		int width = drawable.getIntrinsicWidth();
		int height = drawable.getIntrinsicHeight();
		Bitmap bitmap = Bitmap.createBitmap(width, height, drawable
				.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888
				: Bitmap.Config.RGB_565);
		Canvas canvas = new Canvas(bitmap);
		drawable.setBounds(0, 0, width, height);
		drawable.draw(canvas);
		return bitmap;
	}

	private Bitmap createScaledBitmap(Bitmap unscaledBitmap, int dstWidth,
									 int dstHeight, ImageView.ScaleType scalingLogic) {
		Rect srcRect = calculateSrcRect(unscaledBitmap.getWidth(),
				unscaledBitmap.getHeight(), dstWidth, dstHeight, scalingLogic);
		Rect dstRect = calculateDstRect(unscaledBitmap.getWidth(),
				unscaledBitmap.getHeight(), dstWidth, dstHeight, scalingLogic);
		Bitmap scaledBitmap = Bitmap.createBitmap(dstRect.width(),
				dstRect.height(), Bitmap.Config.ARGB_8888);
		Canvas canvas = new Canvas(scaledBitmap);
		canvas.drawBitmap(unscaledBitmap, srcRect, dstRect, new Paint(
				Paint.FILTER_BITMAP_FLAG));
		return scaledBitmap;
	}

	private Rect calculateSrcRect(int srcWidth, int srcHeight, int dstWidth,
								 int dstHeight, ImageView.ScaleType scalingLogic) {
		if (scalingLogic == ImageView.ScaleType.CENTER_CROP) {
			final float srcAspect = (float) srcWidth / (float) srcHeight;
			final float dstAspect = (float) dstWidth / (float) dstHeight;
			if (srcAspect > dstAspect) {
				final int srcRectWidth = (int) (srcHeight * dstAspect);
				final int srcRectLeft = (srcWidth - srcRectWidth) / 2;
				return new Rect(srcRectLeft, 0, srcRectLeft + srcRectWidth,
						srcHeight);
			} else {
				final int srcRectHeight = (int) (srcWidth / dstAspect);
				final int scrRectTop = (int) (srcHeight - srcRectHeight) / 2;
				return new Rect(0, scrRectTop, srcWidth, scrRectTop
						+ srcRectHeight);
			}
		} else {
			return new Rect(0, 0, srcWidth, srcHeight);
		}
	}

	private Rect calculateDstRect(int srcWidth, int srcHeight, int dstWidth,
								 int dstHeight, ImageView.ScaleType scalingLogic) {
		if (scalingLogic == ImageView.ScaleType.FIT_XY) {
			final float srcAspect = (float) srcWidth / (float) srcHeight;
			final float dstAspect = (float) dstWidth / (float) dstHeight;
			if (srcAspect > dstAspect) {
				return new Rect(0, 0, dstWidth, (int) (dstWidth / srcAspect));
			} else {
				return new Rect(0, 0, (int) (dstHeight * srcAspect), dstHeight);
			}
		} else {
			return new Rect(0, 0, dstWidth, dstHeight);
		}
	}

}
