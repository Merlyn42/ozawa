package com.ozawa.hextcgdeckbuilder;

import java.io.InputStream;

import com.ozawa.hextcgdeckbuilder.UI.GifView;
import com.ozawa.hextcgdeckbuilder.database.DatabaseHandler;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

public class SplashActivity extends Activity {
	
	// Database
    public DatabaseHandler dbHandler;
    private Handler mHandler = new Handler();
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		InputStream stream = null;
		stream = getResources().openRawResource(R.drawable.splash);
		GifView splash = new GifView(this, stream);
		splash.getRootView().setBackgroundColor(getResources().getColor(android.R.color.black));
		setContentView(splash);
		
		mHandler.postDelayed(new Runnable() {
            public void run() {
                startDeckActivity();
            }
        }, 5000);
	}
	
	@Override
	protected void onPause(){
		super.onPause();
		finish();
	}
	
	private void startDeckActivity(){
		Intent intent = new Intent(getBaseContext(), DeckUIActivity.class);
		startActivity(intent);
	}
}
