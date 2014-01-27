/*******************************************************************************
 * Hex TCG Deck Builder
 *     Copyright ( C ) 2014  Chad Kinsella, Dave Kerr and Laurence Reading
 * 
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 * 
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 * 
 *     You should have received a copy of the GNU General Public License
 *     along with this program.  If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/
package com.ozawa.hextcgdeckbuilder;

import java.io.InputStream;

import com.ozawa.hextcgdeckbuilder.UI.GifDecoderView;
import com.ozawa.hextcgdeckbuilder.util.HexUtil;

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
		splash.setBytes(HexUtil.toByteArray(stream));
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
