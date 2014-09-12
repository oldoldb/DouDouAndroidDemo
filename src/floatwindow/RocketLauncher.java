package floatwindow;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.oldoldb.doudouandroiddemo.R;

public class RocketLauncher extends LinearLayout{

	private static RocketLauncher instance;
	
	private int mWidth;
	public int getmWidth() {
		return mWidth;
	}
	public void setmWidth(int mWidth) {
		this.mWidth = mWidth;
	}

	private int mHeight;
	public int getmHeight() {
		return mHeight;
	}
	public void setmHeight(int mHeight) {
		this.mHeight = mHeight;
	}

	private ImageView mlauncherImageView;
	
	public static RocketLauncher getInstance(Context context)
	{
		if(instance == null){
			synchronized (RocketLauncher.class) {
				if(instance == null){
					instance = new RocketLauncher(context);
				}
			}
		}
		return instance;
	}
	private RocketLauncher(Context context)
	{
		super(context);
		LayoutInflater.from(context).inflate(R.layout.launcher, this);
		mlauncherImageView = (ImageView)findViewById(R.id.imageview_launcher);
		mWidth = mlauncherImageView.getLayoutParams().width;
		mHeight = mlauncherImageView.getLayoutParams().height;
	}
	
	public void updateLauncherStatus(boolean isReadyToLaunch)
	{
		if(isReadyToLaunch){
			mlauncherImageView.setImageResource(R.drawable.launcher_bg_fire);
		}else{
			mlauncherImageView.setImageResource(R.drawable.launcher_bg_hold);
		}
	}
}
