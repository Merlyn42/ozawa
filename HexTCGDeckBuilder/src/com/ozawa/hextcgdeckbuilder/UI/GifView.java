package com.ozawa.hextcgdeckbuilder.UI;

import java.io.InputStream;

import com.ozawa.hextcgdeckbuilder.util.GifDecoder;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Handler;
import android.widget.ImageView;

public class GifView extends ImageView {
	
	private GifDecoder mGifDecoder;
	private Bitmap mTmpBitmap;
	final Handler mHandler = new Handler();
	final Runnable mUpdateResults = new Runnable() { 
		public void run() { 
			if (mTmpBitmap != null && !mTmpBitmap.isRecycled()) { 
				GifView.this.setImageBitmap(mTmpBitmap); 
			} 
		}};
	
	public GifView(Context context){
		super(context); 
	}
	
	public GifView(Context context, InputStream stream) {    
		super(context);       
		playGif(stream);
	
	}
	
	private void playGif(InputStream stream){ 
		mGifDecoder = new GifDecoder(); 
		mGifDecoder.read(stream); 
		
		new Thread(new Runnable() { 
			public void run() { 
				final int n = mGifDecoder.getFrameCount(); 
					for (int i = 0; i < n; i++){ 
						mTmpBitmap = mGifDecoder.getFrame(i); 
						final int t = mGifDecoder.getDelay(i); 
						mHandler.post(mUpdateResults); 
						try { 
							Thread.sleep(t); 
						} catch (InterruptedException e) { 
							e.printStackTrace(); 
						} 
					} 
			}      
			}).start();
	}
}
