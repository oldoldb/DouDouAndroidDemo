package floatwindow;

import com.oldoldb.doudouandroiddemo.R;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

public class FloatWindowLargeView extends LinearLayout {

	public static int sViewWidth;
	public static int sViewHeight;
	
	public FloatWindowLargeView(final Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		LayoutInflater.from(context).inflate(R.layout.float_window_large, this);
		LinearLayout linearLayout = (LinearLayout)findViewById(R.id.large_window_layout);
		sViewWidth = linearLayout.getLayoutParams().width;
		sViewHeight = linearLayout.getLayoutParams().height;
		Button closeButton = (Button)findViewById(R.id.button_close);
		Button backButton = (Button)findViewById(R.id.button_back);
		closeButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				FloatWindowManager.removeLargeFloatWindow(context);
				FloatWindowManager.removeSmallFloatWindow(context);
				Intent intent = new Intent(getContext(), FloatWindowService.class);
				context.stopService(intent);
			}
		});
		backButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				FloatWindowManager.removeLargeFloatWindow(context);
				FloatWindowManager.createSmallFloatWindow(context);
			}
		});
	}

}
