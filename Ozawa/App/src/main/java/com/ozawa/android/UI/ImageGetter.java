package com.ozawa.android.UI;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.os.AsyncTask;
import android.widget.ImageView;

import com.ozawa.android.R;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by lreading on 18/12/13.
 */
public class ImageGetter extends AsyncTask<Object, Void, Bitmap> {
    static volatile ConcurrentHashMap<Integer,Bitmap> map;

    private ImageView iv;
    public ImageGetter(ImageView v) {
        if (map == null)map =  new ConcurrentHashMap<Integer,Bitmap>();
        iv = v;
    }
    @Override
    protected Bitmap doInBackground(Object... params) {
        Bitmap b;
        if((b = map.get((int)params[1]))!=null){
            return b;
        }

        BitmapFactory.Options o = new BitmapFactory.Options();
        o.inJustDecodeBounds = true;

        BitmapFactory.decodeResource((Resources)params[0], R.drawable.diamond_action_cardtemplate,o);

        int scale=1;
        while(o.outWidth/scale/2>=200)
            scale*=2;

        //Decode with inSampleSize
        BitmapFactory.Options o2 = new BitmapFactory.Options();
        o2.inSampleSize=scale;

        Bitmap fg = BitmapFactory.decodeResource((Resources)params[0], R.drawable.diamond_action_cardtemplate,o2);



        BitmapFactory.decodeResource((Resources)params[0], (Integer)params[1],o);

        scale=1;
        while(o.outWidth/scale/2>=140)
            scale*=2;

        //Decode with inSampleSize
        o2 = new BitmapFactory.Options();
        o2.inSampleSize=scale;

        Bitmap bg = BitmapFactory.decodeResource((Resources)params[0], (Integer)params[1],o2);

        Bitmap cardImage;

        cardImage = Bitmap.createBitmap(fg.getWidth(), fg.getHeight(), Bitmap.Config.ARGB_8888);

        Canvas combine = new Canvas(cardImage);

        combine.drawBitmap(bg, 0f, 10f, null);
        combine.drawBitmap(fg, 0f, 0f,null);
        map.put((int)params[1],cardImage);
        return cardImage;
    }
    @Override
    protected void onPostExecute(Bitmap result) {
        super.onPostExecute(result);
        iv.setImageBitmap(result);
    }

    private Bitmap decodeFile(File f){
        try {
            //Decode image size
            BitmapFactory.Options o = new BitmapFactory.Options();
            o.inJustDecodeBounds = true;
            BitmapFactory.decodeStream(new FileInputStream(f),null,o);

            //The new size we want to scale to
            final int REQUIRED_SIZE=70;

            //Find the correct scale value. It should be the power of 2.
            int scale=1;
            while(o.outWidth/scale/2>=REQUIRED_SIZE && o.outHeight/scale/2>=REQUIRED_SIZE)
                scale*=2;

            //Decode with inSampleSize
            BitmapFactory.Options o2 = new BitmapFactory.Options();
            o2.inSampleSize=scale;


            return BitmapFactory.decodeStream(new FileInputStream(f), null, o2);
        } catch (FileNotFoundException e) {}
        return null;
    }
}
