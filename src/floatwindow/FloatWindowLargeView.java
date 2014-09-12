package floatwindow;

import com.oldoldb.doudouandroiddemo.R;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

public class FloatWindowLargeView extends LinearLayout {

	private static FloatWindowLargeView instanceFloatWindowLargeView = null;
	public static FloatWindowLargeView getInstance(Context context)
	{
		if(instanceFloatWindowLargeView == null){
			synchronized (FloatWindowLargeView.class) {
				if(instanceFloatWindowLargeView == null){
					instanceFloatWindowLargeView = new FloatWindowLargeView(context);
				}
			}
		}
		return instanceFloatWindowLargeView;
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

	private FloatWindowLargeView(final Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		LayoutInflater.from(context).inflate(R.layout.float_window_large, this);
		LinearLayout linearLayout = (LinearLayout)findViewById(R.id.large_window_layout);
		mViewWidth = linearLayout.getLayoutParams().width;
		mViewHeight = linearLayout.getLayoutParams().height;
		Button closeButton = (Button)findViewById(R.id.button_close);
		Button backButton = (Button)findViewById(R.id.button_back);
		closeButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				FloatWindowManager.getInstance().removeLargeFloatWindow(context);
				FloatWindowManager.getInstance().removeSmallFloatWindow(context);
				Intent intent = new Intent(getContext(), FloatWindowService.class);
				context.stopService(intent);
			}
		});
		backButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				FloatWindowManager.getInstance().removeLargeFloatWindow(context);
				FloatWindowManager.getInstance().createSmallFloatWindow(context);
			}
		});
	}

}
