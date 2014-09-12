package floatwindow;

import java.lang.reflect.Field;
import com.oldoldb.doudouandroiddemo.R;
import android.content.Context;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class FloatWindowSmallView extends LinearLayout {

	private static FloatWindowSmallView instanceFloatWindowSmallView = null;
	public static FloatWindowSmallView getInstance(Context context)
	{
		if(instanceFloatWindowSmallView == null){
			synchronized (FloatWindowSmallView.class) {
				if(instanceFloatWindowSmallView == null){
					instanceFloatWindowSmallView = new FloatWindowSmallView(context);
				}
			}
		}
		return instanceFloatWindowSmallView;
	}
	private int mViewWidth;
	public int getmViewWidth() {
		return mViewWidth;
	}
	public void setmViewWidth(int mViewWidth) {
		this.mViewWidth = mViewWidth;
	}
	private int mViewHeight;
	public int getmViewHeight() {
		return mViewHeight;
	}
	public void setmViewHeight(int mViewHeight) {
		this.mViewHeight = mViewHeight;
	}
	private int mStatusBarHeight;
	private WindowManager mWindowManager;
	private LinearLayout mSmallWindowLayout;
	private WindowManager.LayoutParams mLayoutParams;
	private ImageView mRocketImageView;
	private int mRocketWidth;
	private int mRocketHeight;
	private boolean mIsPressed;
	private float mMoveXInScreen;
	private float mMoveYInScreen;
	private float mDownXInScreen;
	private float mDownYInScreen;
	private float mDownXInView;
	private float mDownYInView;
	private FloatWindowSmallView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		mWindowManager = (WindowManager)context.getSystemService(Context.WINDOW_SERVICE);
		LayoutInflater.from(context).inflate(R.layout.float_window_small, this);
		mSmallWindowLayout = (LinearLayout)findViewById(R.id.small_window_layout);
		mViewWidth = mSmallWindowLayout.getLayoutParams().width;
		mViewHeight = mSmallWindowLayout.getLayoutParams().height;
		mRocketImageView = (ImageView)findViewById(R.id.imageview_rocket);
		mRocketWidth = mRocketImageView.getLayoutParams().width;
		mRocketHeight = mRocketImageView.getLayoutParams().height;
		TextView textView = (TextView)findViewById(R.id.textview_percent);
		textView.setText(FloatWindowManager.getInstance().getUsedPercentValue(context));
	}
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		// TODO Auto-generated method stub
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			mIsPressed = true;
			mDownXInView = event.getX();
			mDownYInView = event.getY();
			mDownXInScreen = event.getRawX();
			mDownYInScreen = event.getRawY() - getStatusBarHeight();
			mMoveXInScreen = mDownXInScreen;
			mMoveYInScreen = mDownYInScreen;
			break;
		case MotionEvent.ACTION_MOVE:
			mMoveXInScreen = event.getRawX();
			mMoveYInScreen = event.getRawY() - getStatusBarHeight();
			updateViewStatus();
			updateViewPosition();
			break;
		case MotionEvent.ACTION_UP:
			mIsPressed = false;
			if(FloatWindowManager.getInstance().isReadyToLaunch()){
				launchRocket();
			}else{
				updateViewStatus();
				if(mDownXInScreen == mMoveXInScreen && mDownYInScreen == mMoveYInScreen){
					showLargeFloatWindow();
				}
			}
			break;
		default:
			break;
		}
		return true;
	}
	
	public void setParams(WindowManager.LayoutParams params)
	{
		mLayoutParams = params;
	}
	private void updateViewStatus()
	{
		if(mIsPressed && mRocketImageView.getVisibility() != View.VISIBLE){
			mLayoutParams.width = mRocketWidth;
			mLayoutParams.height = mRocketHeight;
			mWindowManager.updateViewLayout(this, mLayoutParams);
			mSmallWindowLayout.setVisibility(View.GONE);
			mRocketImageView.setVisibility(View.VISIBLE);
			FloatWindowManager.getInstance().createLauncher(getContext());
		}else if(!mIsPressed){
			mLayoutParams.width = mViewWidth;
			mLayoutParams.height = mViewHeight;
			mWindowManager.updateViewLayout(this, mLayoutParams);
			mSmallWindowLayout.setVisibility(View.VISIBLE);
			mRocketImageView.setVisibility(View.GONE);
			FloatWindowManager.getInstance().removeLauncher(getContext());
		}
	}
	private void updateViewPosition()
	{
		mLayoutParams.x = (int)(mMoveXInScreen - mDownXInView);
		mLayoutParams.y = (int)(mMoveYInScreen - mDownYInView);
		mWindowManager.updateViewLayout(this, mLayoutParams);
		FloatWindowManager.getInstance().updateLauncher();
	}
	
	private void launchRocket()
	{
		FloatWindowManager.getInstance().removeLauncher(getContext());
		new LaunchTask().execute();
	}
	private void showLargeFloatWindow()
	{
		FloatWindowManager.getInstance().createLargeFloatWindow(getContext());
		FloatWindowManager.getInstance().removeSmallFloatWindow(getContext());
	}
	
	private int getStatusBarHeight()
	{
		if(mStatusBarHeight == 0){
			try {
				Class<?> class1 = Class.forName("com.android.internal.R$dimen");
				Object object = class1.newInstance();
				Field field = class1.getField("status_bar_height");
				int x = (Integer)field.get(object);
				mStatusBarHeight = getResources().getDimensionPixelSize(x);
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}
		}
		return mStatusBarHeight;
	}
	
	class LaunchTask extends AsyncTask<Void, Void, Void>
	{

		@Override
		protected Void doInBackground(Void... params) {
			// TODO Auto-generated method stub
			while(mLayoutParams.y > 0){
				mLayoutParams.y -= 10;
				publishProgress();
				try {
					Thread.sleep(8);
				} catch (InterruptedException e) {
					// TODO: handle exception
					e.printStackTrace();
				}
			}
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			// TODO Auto-generated method stub
			updateViewStatus();
			mLayoutParams.x = (int)(mDownXInScreen - mDownXInView);
			mLayoutParams.y = (int)(mDownYInScreen - mDownYInView);
			mWindowManager.updateViewLayout(FloatWindowSmallView.this, mLayoutParams);
			FloatWindowManager.getInstance().killBackgroundProcess(getContext());
		}

		@Override
		protected void onProgressUpdate(Void... values) {
			// TODO Auto-generated method stub
			mWindowManager.updateViewLayout(FloatWindowSmallView.this, mLayoutParams);
		}
		
	}
	

}
