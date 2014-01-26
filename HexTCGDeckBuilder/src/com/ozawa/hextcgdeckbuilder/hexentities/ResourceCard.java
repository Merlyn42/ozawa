package com.ozawa.hextcgdeckbuilder.hexentities;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Build;
import android.view.Display;

import com.google.gson.annotations.SerializedName;
import com.ozawa.hextcgdeckbuilder.R;
import com.ozawa.hextcgdeckbuilder.UI.CardTemplate;
import com.ozawa.hextcgdeckbuilder.UI.ImageCache;
import com.ozawa.hextcgdeckbuilder.UI.ImageCache.CacheType;
import com.ozawa.hextcgdeckbuilder.UI.ImageCache.ImageType;
import com.ozawa.hextcgdeckbuilder.util.HexUtil;

/**
 * A Resource Card.
 * 
 * @author Chad Kinsella
 */
public class ResourceCard extends AbstractCard {

	@SerializedName("m_CurrentResourcesGranted")
	public int	currentResourcesGranted;
	@SerializedName("m_MaxResourcesGranted")
	public int	maxResourcesGranted;

	@Override
	public Bitmap getCardBitmap(Context context, CardTemplate template, int maxWidth) {
		Resources resources = context.getResources();
		final int resourceId = resources.getIdentifier(cardImagePath.split("\\.")[0], "drawable", context.getPackageName());

		BitmapFactory.Options o = new BitmapFactory.Options();
		o.inJustDecodeBounds = true;
		BitmapFactory.decodeResource(resources, resourceId, o);
		int scale = 1;
		while (o.outWidth / scale / 2 >= maxWidth)
			scale *= 2;
		// Decode with inSampleSize
		BitmapFactory.Options o2 = new BitmapFactory.Options();
        if (false){//Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
        	o2.inMutable = true;
        } 
		o2.inSampleSize = scale;
		Bitmap output = BitmapFactory.decodeResource(resources, resourceId, o2);
		int left = Double.valueOf(o2.outWidth * defaultLayout.portraitLeft).intValue();
		int width = Double.valueOf(o2.outWidth * defaultLayout.portraitRight).intValue() - left;
		int top = Double.valueOf(o2.outHeight * defaultLayout.portraitTop).intValue();
		int height = Double.valueOf(o2.outHeight * defaultLayout.portraitBottom).intValue() - top;
        if (true){//Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB) {
        	output=output.copy(Bitmap.Config.ARGB_8888, true);
        } 
		image = output;//Bitmap.createBitmap(output, left, top, width, height);
		return image;
	}

	@Override
	public Bitmap getCardPortait(Context mContext) {
		try {
			if (portrait == null) {
				BitmapFactory.Options portraitOptions = new BitmapFactory.Options();
				portraitOptions.inSampleSize = 4;
				portrait = BitmapFactory.decodeResource(mContext.getResources(),
						HexUtil.getResourceID(this.cardImagePath, R.drawable.class), portraitOptions);
				if (portrait != null) {
					double pL = defaultLayout.portraitLeft * portrait.getWidth();
					double pR = defaultLayout.portraitRight * portrait.getWidth();
					double pT = defaultLayout.portraitTop * portrait.getHeight();
					double pB = defaultLayout.portraitBottom * portrait.getHeight();

					int x = (int) Math.round(pL);
					int width = (int) Math.round(pR - pL);
					int y = (int) Math.round(pT);
					int height = (int) Math.round(pB - pT);
					portrait = Bitmap.createBitmap(portrait, x, y, width, height);
					ImageCache.getInstance(CacheType.ListView).queueForRemovalFromCache(mContext, this, ImageType.WithoutTemplate);
				}
			}
		} catch (OutOfMemoryError e) {
			System.err.println("Ran out of memory, dumping some images from the cache");
			ImageCache.emergencyDump(CacheType.ListView);
			return getCardPortait(mContext);
		}

		return portrait;
	}
	
	@Override
	public Bitmap addCount(String count, Bitmap imageIn) {
    	Bitmap image= Bitmap.createBitmap(imageIn);
    	Canvas combine = new Canvas(image);
    	Paint textPaint = new Paint();
    	textPaint.setTextSize( ((float)image.getHeight()) * 0.09f);
    	int buf = (int) (textPaint.getTextSize()*0.2f);
    	
    	textPaint.setTextAlign(Paint.Align.RIGHT);
    	textPaint.setColor(-1);        
    	textPaint.setAntiAlias(true);
    	Paint boxPaint = new Paint();
    	boxPaint.setColor(0x770060b0);
    	
    	int originX = (int) (((float)image.getWidth()*0.9)-buf);
    	int originY = (int) (((float)image.getHeight())*0.1107f+textPaint.getTextSize());
    	
    	Rect box = new Rect();
    	box.left=(originX-(int)textPaint.measureText(count))-buf;
    	box.right=originX+buf;
    	box.bottom=originY+buf;
    	box.top=(originY-(int)textPaint.getTextSize());
    	combine.drawRect(box, boxPaint);
    	combine.drawText(count, originX, originY, textPaint);
    	return image;
	}
	
}
