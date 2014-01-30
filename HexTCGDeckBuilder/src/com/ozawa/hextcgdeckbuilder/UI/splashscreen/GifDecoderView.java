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
package com.ozawa.hextcgdeckbuilder.UI.splashscreen;

import com.ozawa.hextcgdeckbuilder.util.GifDecoder;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Handler;
import android.util.AttributeSet;
import android.widget.ImageView;
 
public class GifDecoderView extends ImageView implements Runnable {
 
	private GifDecoder gifDecoder;
	private Bitmap mTmpBitmap;
	private final Handler handler = new Handler();
	private boolean animating = false;
	private Thread animationThread;
	private final Runnable updateResults = new Runnable() {
		@Override
		public void run() {
			if (mTmpBitmap != null && !mTmpBitmap.isRecycled()) {
				setImageBitmap(mTmpBitmap);
			}
		}
	};
	 
	public GifDecoderView(final Context context, final AttributeSet attrs) {
		super(context, attrs);
	}
	 
	public GifDecoderView(final Context context) {
		super(context);
	}
	 
	public void setBytes(final byte[] bytes) {
		gifDecoder = new GifDecoder();
		gifDecoder.read(bytes);
		 
		if (canStart()) {
			animationThread = new Thread(this);
			animationThread.start();
		}
	}
	 
	public void startAnimation() {
		animating = true;
		 
		if (canStart()) {
			animationThread = new Thread(this);
			animationThread.start();
		}
	}
	 
	public void stopAnimation() {
		animating = false;
		 
		if (animationThread != null) {
			animationThread.interrupt();
			animationThread = null;
		}
	}
	 
	private boolean canStart() {
		return animating && gifDecoder != null && animationThread == null;
	}
	 
	@Override
	public void run() {
		final int n = gifDecoder.getFrameCount();
		do {
			for (int i = 0; i < n; i++) {
				try {
					mTmpBitmap = gifDecoder.getNextFrame();
					handler.post(updateResults);
				} catch (final ArrayIndexOutOfBoundsException e) {
					// suppress
				}
					gifDecoder.advance();
				try {
					Thread.sleep(gifDecoder.getNextDelay());
				} catch (final InterruptedException e) {
					// suppress
				}
			}
		} while (animating);
	}
}
