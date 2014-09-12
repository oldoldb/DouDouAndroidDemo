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
	private WindowManager.LayoutParams mLayoutParams;
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
		LinearLayout linearLayout = (LinearLayout)findViewById(R.id.small_window_layout);
		mViewWidth = linearLayout.getLayoutParams().width;
		mViewHeight = linearLayout.getLayoutParams().height;
		TextView textView = (TextView)findViewById(R.id.textview_percent);
		textView.setText(FloatWindowManager.getInstance().getUsedPercentValue(context));
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
		mWindowManager.updateViewLayout(this, mLayoutParams);
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
	
	

}
