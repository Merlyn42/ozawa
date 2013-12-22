package com.ozawa.hextcgdeckbuilder.UI;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.os.AsyncTask;
import android.widget.ImageView;

import com.ozawa.hextcgdeckbuilder.R;
import com.ozawa.hextcgdeckbuilder.hexentities.AbstractCard;
import com.ozawa.hextcgdeckbuilder.hexentities.Card;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by lreading on 18/12/13.
 */
public class ImageGetter extends AsyncTask<AbstractCard, Void, Bitmap> {
    static volatile ConcurrentHashMap<Integer,Bitmap> map;

    private ImageView iv;
    private Context context;
    public ImageGetter(ImageView v, Context iContext) {
        if (map == null)map =  new ConcurrentHashMap<Integer,Bitmap>();
        iv = v;
        this.context=iContext;
    }

    /**
     * Generates or retrieves a card's image. Params Should contain a single AbstractCard
     * @param params Should be a single AbstractCard.
     * @return The card's image as a Bitmap
     */
    @Override
    protected Bitmap doInBackground(AbstractCard... params) {
        return params[0].getCardBitmap(context);
    }
    @Override
    protected void onPostExecute(Bitmap result) {
        super.onPostExecute(result);
        iv.setImageBitmap(result);
    }
}
