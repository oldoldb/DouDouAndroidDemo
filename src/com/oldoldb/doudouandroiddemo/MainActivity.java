package com.oldoldb.doudouandroiddemo;

import floatwindow.FloatWindowService;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class MainActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button startFloatWindowButton = (Button)findViewById(R.id.button_start_float_window);
        startFloatWindowButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(MainActivity.this, FloatWindowService.class);
				startService(intent);
				finish();
			}
		});
    }

}
