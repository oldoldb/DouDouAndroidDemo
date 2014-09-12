package floatwindow;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import android.app.ActivityManager;
import android.app.Service;
import android.app.ActivityManager.RunningTaskInfo;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Handler;
import android.os.IBinder;

public class FloatWindowService extends Service {

	private Handler mHandler = new Handler();
	private Timer mTimer;
	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		// TODO Auto-generated method stub
		if(mTimer == null){
			mTimer = new Timer();
			mTimer.scheduleAtFixedRate(new RefreshTask(), 0, 500);
		}
		return super.onStartCommand(intent, flags, startId);
	}
	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		mTimer.cancel();
		mTimer.purge();
		mTimer = null;
	}
	
	
	private boolean isShowDesktopHome()
	{
		ActivityManager activityManager = (ActivityManager)getSystemService(ACTIVITY_SERVICE);
		List<RunningTaskInfo> runningTaskInfos = activityManager.getRunningTasks(1);
		return getApplicationNamesInDesktop().contains(runningTaskInfos.get(0).topActivity.getPackageName());
	}
	
	private List<String> getApplicationNamesInDesktop()
	{
		List<String> nameStrings = new ArrayList<String>();
		PackageManager packageManager = getPackageManager();
		Intent intent = new Intent(Intent.ACTION_MAIN);
		intent.addCategory(Intent.CATEGORY_HOME);
		List<ResolveInfo> resolveInfos = packageManager.queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
		for (ResolveInfo resolveInfo : resolveInfos){
			nameStrings.add(resolveInfo.activityInfo.packageName);
		}
		return nameStrings;
	}
	class RefreshTask extends TimerTask
	{

		@Override
		public void run() {
			// TODO Auto-generated method stub
			if(isShowDesktopHome() && !FloatWindowManager.isFloatWindowShowing()){
				mHandler.post(new Runnable() {
					
					@Override
					public void run() {
						// TODO Auto-generated method stub
						FloatWindowManager.createSmallFloatWindow(getApplicationContext());
					}
				});
			}else if(!isShowDesktopHome() && FloatWindowManager.isFloatWindowShowing()){
				mHandler.post(new Runnable() {
					
					@Override
					public void run() {
						// TODO Auto-generated method stub
						FloatWindowManager.removeSmallFloatWindow(getApplicationContext());
						FloatWindowManager.removeLargeFloatWindow(getApplicationContext());
					}
				});
			}else if(isShowDesktopHome() && FloatWindowManager.isFloatWindowShowing()){
				mHandler.post(new Runnable() {
					
					@Override
					public void run() {
						// TODO Auto-generated method stub
						FloatWindowManager.updateUsedPercent(getApplicationContext());
					}
				});
			}
		}
		
	}
	

}
