package com.ozawa.hextcgdeckbuilder;

import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.io.IOUtils;

import com.ozawa.hextcgdeckbuilder.UI.GifDecoderView;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

/**
 * Splash screen for the app
 */
public class SplashActivity extends Activity {
	
	private GifDecoderView splash;
    private Handler mHandler = new Handler();
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		InputStream stream = null;
		stream = getResources().openRawResource(R.drawable.splash);
		splash = new GifDecoderView(this);
		try {
			splash.setBytes(IOUtils.toByteArray(stream));
		} catch (IOException e) {
		}
		splash.getRootView().setBackgroundColor(getResources().getColor(android.R.color.black));
		
		setContentView(splash);
		splash.startAnimation();
		mHandler.postDelayed(new Runnable() {
            public void run() {
                startDeckActivity();
            }
        }, 5000);
	}
	
	@Override
	protected void onPause(){
		super.onPause();
		splash.stopAnimation();
		finish();
	}
	
	private void startDeckActivity(){
		Intent intent = new Intent(getBaseContext(), DeckUIActivity.class);
		startActivity(intent);
	}
}
