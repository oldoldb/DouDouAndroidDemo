package floatwindow;

import java.lang.reflect.Field;

import com.oldoldb.doudouandroiddemo.R;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

public class FloatWindowSmallView extends LinearLayout {

	public static int sViewWidth;
	public static int sViewHeight;
	private static int sStatusBarHeight;
	private WindowManager sWindowManager;
	private WindowManager.LayoutParams mLayoutParams;
	private float mMoveXInScreen;
	private float mMoveYInScreen;
	private float mDownXInScreen;
	private float mDownYInScreen;
	private float mDownXInView;
	private float mDownYInView;
	public FloatWindowSmallView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		sWindowManager = (WindowManager)context.getSystemService(Context.WINDOW_SERVICE);
		LayoutInflater.from(context).inflate(R.layout.float_window_small, this);
		LinearLayout linearLayout = (LinearLayout)findViewById(R.id.small_window_layout);
		sViewWidth = linearLayout.getLayoutParams().width;
		sViewHeight = linearLayout.getLayoutParams().height;
		TextView textView = (TextView)findViewById(R.id.textview_percent);
		textView.setText(FloatWindowManager.getUsedPercentValue(context));
	}
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		// TODO Auto-generated method stub
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
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
			updateViewPosition();
			break;
		case MotionEvent.ACTION_UP:
			if(mDownXInScreen == mMoveXInScreen && mDownYInScreen == mMoveYInScreen){
				showLargeFloatWindow();
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
	private void updateViewPosition()
	{
		mLayoutParams.x = (int)(mMoveXInScreen - mDownXInView);
		mLayoutParams.y = (int)(mMoveYInScreen - mDownYInView);
		sWindowManager.updateViewLayout(this, mLayoutParams);
	}
	
	private void showLargeFloatWindow()
	{
		FloatWindowManager.createLargeFloatWindow(getContext());
		FloatWindowManager.removeSmallFloatWindow(getContext());
	}
	
	private int getStatusBarHeight()
	{
		if(sStatusBarHeight == 0){
			try {
				Class<?> class1 = Class.forName("com.android.internal.R$dimen");
				Object object = class1.newInstance();
				Field field = class1.getField("status_bar_height");
				int x = (Integer)field.get(object);
				sStatusBarHeight = getResources().getDimensionPixelSize(x);
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}
		}
		return sStatusBarHeight;
	}
	
	

}
