package com.ozawa.android.hexentities;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Point;
import android.os.Build;
import android.view.Display;
import android.view.WindowManager;

import com.google.gson.annotations.SerializedName;

/**
 * A Resource Card.
 * 
 * @author Chad Kinsella
 */
public class ResourceCard extends AbstractCard {
	
	@SerializedName("m_ResourceThresholdGranted")
	public String resourceThresholdGranted;
	@SerializedName("m_CurrentResourcesGranted")
	public int currentResourcesGranted;
	@SerializedName("m_MaxResourcesGranted")
	public int maxResourcesGranted;

    @Override
    public Bitmap getCardBitmap(Context context) {
        if (image != null) {
            return image;
        }
        Resources resources = context.getResources();
        final int resourceId = resources.getIdentifier(cardImagePath.split("\\.")[0], "drawable",
                context.getPackageName());


        //Find screen size for scaling the image.
        int Measuredwidth = 0;
        int Measuredheight = 0;
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Point size = new Point();
        //different methods based on SDK version
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            wm.getDefaultDisplay().getSize(size);
            Measuredwidth = size.x;
            Measuredheight = size.y;
        } else {
            Display d = wm.getDefaultDisplay();
            Measuredwidth = d.getWidth();
            Measuredheight = d.getHeight();
        }

        BitmapFactory.Options o = new BitmapFactory.Options();
        o.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(resources, resourceId, o);
        int scale = 1;
        while (o.outWidth / scale / 2 >= Measuredwidth/3)
            scale *= 2;
        //Decode with inSampleSize
        BitmapFactory.Options o2 = new BitmapFactory.Options();

     //   o2.inSampleSize = scale;
        Bitmap fg = BitmapFactory.decodeResource(resources, resourceId, o2);
        image = Bitmap.createBitmap(fg.getWidth(), fg.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas combine = new Canvas(image);
        image = fg;
        return fg;
    }
}
