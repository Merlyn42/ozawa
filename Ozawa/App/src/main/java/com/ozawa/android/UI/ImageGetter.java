package com.ozawa.android.UI;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.os.AsyncTask;
import android.widget.ImageView;

import com.ozawa.android.R;
import com.ozawa.android.hexentities.AbstractCard;
import com.ozawa.android.hexentities.Card;

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

    /**
     * Generates or retrieves a card's iamge. Params is a two element array, the first element should be the Context the second should be the card
     * @param params A two element array, the first element should be the Context the second should be the card
     * @return The card's image as a Bitmap
     */
    @Override
    protected Bitmap doInBackground(Object... params) {
        Context context = (Context) params[0];
        AbstractCard card = (AbstractCard) params[1];

        return card.getCardBitmap(context);
    }
    @Override
    protected void onPostExecute(Bitmap result) {
        super.onPostExecute(result);
        iv.setImageBitmap(result);
    }
}
