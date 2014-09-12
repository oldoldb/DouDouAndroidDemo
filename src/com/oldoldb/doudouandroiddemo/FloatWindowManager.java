package com.oldoldb.doudouandroiddemo;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import android.app.ActivityManager;
import android.content.Context;
import android.graphics.PixelFormat;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.WindowManager;
import android.widget.TextView;

public class FloatWindowManager {
	
	private static FloatWindowSmallView sFloatWindowSmallView;
	private static FloatWindowLargeView sFloatWindowLargeView;
	private static WindowManager.LayoutParams sSmalLayoutParams;
	private static WindowManager.LayoutParams sLargeLayoutParams;
	private static WindowManager sWindowManager;
	private static ActivityManager sActivityManager;
	
	
	private static WindowManager getWindowManager(Context context)
	{
		if(sWindowManager == null){
			sWindowManager = (WindowManager)context.getSystemService(Context.WINDOW_SERVICE);
		}
		return sWindowManager;
	}
	private static ActivityManager getActivityManager(Context context)
	{
		if(sActivityManager == null){
			sActivityManager = (ActivityManager)context.getSystemService(Context.ACTIVITY_SERVICE);
		}
		return sActivityManager;
	}
	public static void createSmallFloatWindow(Context context)
	{
		WindowManager windowManager = getWindowManager(context);
		DisplayMetrics displayMetrics = new DisplayMetrics();
		windowManager.getDefaultDisplay().getMetrics(displayMetrics);
		int screenWidth = displayMetrics.widthPixels;
		int screenHeight = displayMetrics.heightPixels;
		if(sFloatWindowSmallView == null){
			sFloatWindowSmallView = new FloatWindowSmallView(context);
			if(sSmalLayoutParams == null){
				sSmalLayoutParams = new WindowManager.LayoutParams();
				sSmalLayoutParams.type = android.view.WindowManager.LayoutParams.TYPE_PHONE;
				sSmalLayoutParams.format = PixelFormat.RGBA_8888;
				sSmalLayoutParams.flags = android.view.WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL | android.view.WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
				sSmalLayoutParams.gravity = Gravity.LEFT | Gravity.TOP;
				sSmalLayoutParams.width = FloatWindowSmallView.sViewWidth;
				sSmalLayoutParams.height = FloatWindowSmallView.sViewHeight;
				sSmalLayoutParams.x = screenWidth;
				sSmalLayoutParams.y = screenHeight / 2;
			}
			sFloatWindowSmallView.setParams(sSmalLayoutParams);
			windowManager.addView(sFloatWindowSmallView, sSmalLayoutParams);
		}
	}
	
	public static void removeSmallFloatWindow(Context context)
	{
		if(sFloatWindowSmallView != null){
			WindowManager windowManager = getWindowManager(context);
			windowManager.removeView(sFloatWindowSmallView);
			sFloatWindowSmallView = null;
		}
	}
	
	public static void createLargeFloatWindow(Context context)
	{
		WindowManager windowManager = getWindowManager(context);
		DisplayMetrics displayMetrics = new DisplayMetrics();
		windowManager.getDefaultDisplay().getMetrics(displayMetrics);
		int screenWidth = displayMetrics.widthPixels;
		int screenHeight = displayMetrics.heightPixels;
		if(sFloatWindowLargeView == null){
			sFloatWindowLargeView = new FloatWindowLargeView(context);
			if(sLargeLayoutParams == null){
				sLargeLayoutParams = new WindowManager.LayoutParams();
				sLargeLayoutParams.x = screenWidth / 2 - FloatWindowLargeView.sViewWidth / 2;
				sLargeLayoutParams.y = screenHeight / 2 - FloatWindowLargeView.sViewHeight / 2;
				sLargeLayoutParams.type = android.view.WindowManager.LayoutParams.TYPE_PHONE;
				sLargeLayoutParams.format = PixelFormat.RGBA_8888;
				sLargeLayoutParams.gravity = Gravity.LEFT | Gravity.TOP;
				sLargeLayoutParams.width = FloatWindowLargeView.sViewWidth;
				sLargeLayoutParams.height = FloatWindowLargeView.sViewHeight;
			}
			windowManager.addView(sFloatWindowLargeView, sLargeLayoutParams);
		}
	}
	
	public static void removeLargeFloatWindow(Context context)
	{
		if(sFloatWindowLargeView != null){
			WindowManager windowManager = getWindowManager(context);
			windowManager.removeView(sFloatWindowLargeView);
			sFloatWindowLargeView = null;
		}
	}
	
	public static boolean isFloatWindowShowing()
	{
		return sFloatWindowSmallView != null || sFloatWindowLargeView != null;
	}
	public static void updateUsedPercent(Context context)
	{
		if(sFloatWindowSmallView != null){
			TextView textView = (TextView)sFloatWindowSmallView.findViewById(R.id.textview_percent);
			textView.setText(getUsedPercentValue(context));
		}
	}
	
	public static String getUsedPercentValue(Context context)
	{
		String dir = "/proc/meminfo";
		try {
			FileReader fileReader = new FileReader(dir);
			BufferedReader bufferedReader = new BufferedReader(fileReader, 2048);
			String memoryString = bufferedReader.readLine();
			String subString = memoryString.substring(memoryString.indexOf("MemTotal:"));
			bufferedReader.close();
			long totalMemorySize = Integer.parseInt(subString.replaceAll("\\D+", ""));
			long avaliableSize = getAvailableMemory(context) / 1024;
			int percent = (int)((totalMemorySize - avaliableSize) / (float)totalMemorySize * 100);
			return percent + "%";
		} catch (IOException e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return "悬浮窗";
	}
	private static long getAvailableMemory(Context context)
	{
		ActivityManager.MemoryInfo memoryInfo = new ActivityManager.MemoryInfo();
		getActivityManager(context).getMemoryInfo(memoryInfo);
		return memoryInfo.availMem;
	}
}
