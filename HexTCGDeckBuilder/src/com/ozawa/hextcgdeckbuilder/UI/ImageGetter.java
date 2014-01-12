package com.ozawa.hextcgdeckbuilder.UI;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.widget.ImageView;

import com.ozawa.hextcgdeckbuilder.enums.ImageGetterType;
import com.ozawa.hextcgdeckbuilder.hexentities.AbstractCard;
import com.ozawa.hextcgdeckbuilder.hexentities.Card;

import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by lreading on 18/12/13.
 */
public class ImageGetter extends AsyncTask<AbstractCard, Void, Bitmap> {
    static volatile ConcurrentHashMap<Integer,Bitmap> map;

    private ImageView iv;
    private Context context;
    private ImageGetterType imageType;
    
    public ImageGetter(ImageView v, Context iContext) {
        if (map == null)map =  new ConcurrentHashMap<Integer,Bitmap>();
        iv = v;
        this.context=iContext;
    }
    
    public ImageGetter(ImageView v, Context iContext, ImageGetterType imageType){
    	if (map == null)map =  new ConcurrentHashMap<Integer,Bitmap>();
        iv = v;
        this.context=iContext;
        this.imageType = imageType;
    }

    /**
     * Generates or retrieves a card's image. Params Should contain a single AbstractCard
     * @param params Should be a single AbstractCard.
     * @return The card's image as a Bitmap
     */
    @Override
    protected Bitmap doInBackground(AbstractCard... params) {
    	switch(imageType){
	    	case CARDTHUMBNAIL:{
	    		return params[0].getThumbnailCardBitmap(context);
	    	}
			case CARDPORTRAIT:{
				return params[0].getCardPortait(context);
			}
			case CARDTHRESHOLD:{
				if(params[0] instanceof Card){
					return ((Card)params[0]).getCardThresholdImage(context, null);
				}
				return null;
			}
			default:{
				return null;
			}
    	}
    }
    @Override
    protected void onPostExecute(Bitmap result) {
        super.onPostExecute(result);
        iv.setImageBitmap(result);
    }
}
