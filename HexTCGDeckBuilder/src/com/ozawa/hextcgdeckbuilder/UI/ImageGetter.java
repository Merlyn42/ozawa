package com.ozawa.hextcgdeckbuilder.UI;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.AsyncTask;
import android.widget.ImageView;

import com.ozawa.hextcgdeckbuilder.enums.ImageGetterType;
import com.ozawa.hextcgdeckbuilder.hexentities.AbstractCard;
import com.ozawa.hextcgdeckbuilder.hexentities.Card;

import java.util.Map;

/**
 * Created by lreading on 18/12/13.
 */
public class ImageGetter extends AsyncTask<AbstractCard, Void, Bitmap> {

    private ImageView iv;
    private Context context;
    private ImageGetterType imageType;
    Map<AbstractCard, Integer> customDeck;

    
    public ImageGetter(ImageView v, Context iContext, ImageGetterType imageType, Map<AbstractCard, Integer> customDeck){
        iv = v;
        this.context=iContext;
        this.imageType = imageType;
        this.customDeck=customDeck;
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
	    		if(customDeck!=null){
	    			return addCount(params[0].getThumbnailCardBitmap(context),params[0]);
	    		}else{
	    			return params[0].getThumbnailCardBitmap(context);
	    		}
	    	}
			case CARDPORTRAIT:{
				return params[0].getCardPortait(context);
			}
			case CARDTHRESHOLD:{
				if(params[0] instanceof Card){
					return ((Card)params[0]).getCardThresholdImage(context, null, null);
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
    
    
    private Bitmap addCount(Bitmap imageIn,AbstractCard card){
    	String count;
    	if(customDeck.get(card)!=null){
    		count=String.valueOf(customDeck.get(card));
    	}else{
    		return imageIn;
    	}
    	return card.addCount(count,imageIn);

    }
    
}
