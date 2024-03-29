package floatwindow;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

import com.oldoldb.doudouandroiddemo.R;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.content.Context;
import android.graphics.PixelFormat;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.widget.TextView;

public class FloatWindowManager {
	
	private static FloatWindowManager instanceFloatWindowManager = null;
	public static FloatWindowManager getInstance()
	{
		if(instanceFloatWindowManager == null){
			synchronized (FloatWindowManager.class) {
				if(instanceFloatWindowManager == null){
					instanceFloatWindowManager = new FloatWindowManager();
				}
			}
		}
		return instanceFloatWindowManager;
	}
	private FloatWindowSmallView mFloatWindowSmallView;
	private FloatWindowLargeView mFloatWindowLargeView;
	private RocketLauncher mRocketLauncher;
	private WindowManager.LayoutParams mSmalLayoutParams;
	private WindowManager.LayoutParams mLargeLayoutParams;
	private WindowManager.LayoutParams mLauncherLayoutParams;
	private WindowManager mWindowManager;
	private ActivityManager mActivityManager;
	private FloatWindowManager()
	{
		
	}
	public WindowManager getWindowManager(Context context)
	{
		if(mWindowManager == null){
			mWindowManager = (WindowManager)context.getSystemService(Context.WINDOW_SERVICE);
		}
		return mWindowManager;
	}
	public ActivityManager getActivityManager(Context context)
	{
		if(mActivityManager == null){
			mActivityManager = (ActivityManager)context.getSystemService(Context.ACTIVITY_SERVICE);
		}
		return mActivityManager;
	}
	public void createSmallFloatWindow(Context context)
	{
		WindowManager windowManager = getWindowManager(context);
		DisplayMetrics displayMetrics = new DisplayMetrics();
		windowManager.getDefaultDisplay().getMetrics(displayMetrics);
		int screenWidth = displayMetrics.widthPixels;
		int screenHeight = displayMetrics.heightPixels;
		if(mFloatWindowSmallView == null){
			mFloatWindowSmallView = FloatWindowSmallView.getInstance(context);
			if(mSmalLayoutParams == null){
				mSmalLayoutParams = new WindowManager.LayoutParams();
				mSmalLayoutParams.type = android.view.WindowManager.LayoutParams.TYPE_PHONE;
				mSmalLayoutParams.format = PixelFormat.RGBA_8888;
				mSmalLayoutParams.flags = android.view.WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL | android.view.WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
				mSmalLayoutParams.gravity = Gravity.LEFT | Gravity.TOP;
				mSmalLayoutParams.width = mFloatWindowSmallView.getmViewWidth();
				mSmalLayoutParams.height = mFloatWindowSmallView.getmViewHeight();
				mSmalLayoutParams.x = screenWidth;
				mSmalLayoutParams.y = screenHeight / 2;
			}
			mFloatWindowSmallView.setParams(mSmalLayoutParams);
			windowManager.addView(mFloatWindowSmallView, mSmalLayoutParams);
		}
	}
	
	public void removeSmallFloatWindow(Context context)
	{
		if(mFloatWindowSmallView != null){
			WindowManager windowManager = getWindowManager(context);
			windowManager.removeView(mFloatWindowSmallView);
			mFloatWindowSmallView = null;
		}
	}
	
	public void createLargeFloatWindow(Context context)
	{
		WindowManager windowManager = getWindowManager(context);
		DisplayMetrics displayMetrics = new DisplayMetrics();
		windowManager.getDefaultDisplay().getMetrics(displayMetrics);
		int screenWidth = displayMetrics.widthPixels;
		int screenHeight = displayMetrics.heightPixels;
		if(mFloatWindowLargeView == null){
			mFloatWindowLargeView = FloatWindowLargeView.getInstance(context);
			if(mLargeLayoutParams == null){
				mLargeLayoutParams = new WindowManager.LayoutParams();
				mLargeLayoutParams.x = screenWidth / 2 - mFloatWindowLargeView.getmViewWidth() / 2;
				mLargeLayoutParams.y = screenHeight / 2 - mFloatWindowLargeView.getmViewHeight() / 2;
				mLargeLayoutParams.type = android.view.WindowManager.LayoutParams.TYPE_PHONE;
				mLargeLayoutParams.format = PixelFormat.RGBA_8888;
				mLargeLayoutParams.gravity = Gravity.LEFT | Gravity.TOP;
				mLargeLayoutParams.width = mFloatWindowLargeView.getmViewWidth();
				mLargeLayoutParams.height = mFloatWindowLargeView.getmViewHeight();
			}
			windowManager.addView(mFloatWindowLargeView, mLargeLayoutParams);
		}
	}
	
	public void removeLargeFloatWindow(Context context)
	{
		if(mFloatWindowLargeView != null){
			WindowManager windowManager = getWindowManager(context);
			windowManager.removeView(mFloatWindowLargeView);
			mFloatWindowLargeView = null;
		}
	}
	
	public void createLauncher(Context context) {  
        WindowManager windowManager = getWindowManager(context);  
        DisplayMetrics displayMetrics = new DisplayMetrics();
		windowManager.getDefaultDisplay().getMetrics(displayMetrics);
		int screenWidth = displayMetrics.widthPixels;
		int screenHeight = displayMetrics.heightPixels;
        if (mRocketLauncher == null) {  
        	mRocketLauncher = RocketLauncher.getInstance(context);
            if (mLauncherLayoutParams == null) {  
            	mLauncherLayoutParams = new WindowManager.LayoutParams();
            	mLauncherLayoutParams.x = screenWidth / 2 - mRocketLauncher.getmWidth() / 2;  
            	mLauncherLayoutParams.y = screenHeight - mRocketLauncher.getHeight();
            	mLauncherLayoutParams.type = LayoutParams.TYPE_PHONE;  
            	mLauncherLayoutParams.format = PixelFormat.RGBA_8888;  
            	mLauncherLayoutParams.gravity = Gravity.LEFT | Gravity.TOP;  
            	mLauncherLayoutParams.width = mRocketLauncher.getmWidth();  
            	mLauncherLayoutParams.height = mRocketLauncher.getmHeight();
            }  
            windowManager.addView(mRocketLauncher, mLauncherLayoutParams);  
        }  
    }  
	
    public void removeLauncher(Context context) {  
        if (mRocketLauncher != null) {  
            WindowManager windowManager = getWindowManager(context);  
            windowManager.removeView(mRocketLauncher);  
            mRocketLauncher = null;  
        }  
    }  
  
    public void updateLauncher() {  
        if (mRocketLauncher != null) {  
        	mRocketLauncher.updateLauncherStatus(isReadyToLaunch());  
        }  
    }
    public boolean isReadyToLaunch() {  
    	if(mLauncherLayoutParams == null){
    		createLauncher(mFloatWindowSmallView.getContext());
    	}
        if ((mSmalLayoutParams.x > mLauncherLayoutParams.x && mSmalLayoutParams.x  
                + mSmalLayoutParams.width < mLauncherLayoutParams.x  
                + mLauncherLayoutParams.width)  
                && (mSmalLayoutParams.y + mSmalLayoutParams.height > mLauncherLayoutParams.y)) {  
            return true;  
        }  
        return false;  
    }  
	public boolean isFloatWindowShowing()
	{
		return mFloatWindowSmallView != null || mFloatWindowLargeView != null;
	}
	public void updateUsedPercent(Context context)
	{
		if(mFloatWindowSmallView != null){
			TextView textView = (TextView)mFloatWindowSmallView.findViewById(R.id.textview_percent);
			textView.setText(getUsedPercentValue(context));
		}
	}
	
	public String getUsedPercentValue(Context context)
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
	private long getAvailableMemory(Context context)
	{
		ActivityManager.MemoryInfo memoryInfo = new ActivityManager.MemoryInfo();
		getActivityManager(context).getMemoryInfo(memoryInfo);
		return memoryInfo.availMem;
	}
	
	public void killBackgroundProcess(Context context)
	{
		List<RunningTaskInfo> runningTaskInfos = mActivityManager.getRunningTasks(Integer.MAX_VALUE);
		for(RunningTaskInfo runningTaskInfo : runningTaskInfos){
			String packageName = runningTaskInfo.topActivity.getPackageName();
			mActivityManager.killBackgroundProcesses(packageName);
		}
	}
}
