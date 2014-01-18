package com.ozawa.hextcgdeckbuilder.UI;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.AsyncTask;
import android.widget.ImageView;

import com.ozawa.hextcgdeckbuilder.enums.ImageGetterType;
import com.ozawa.hextcgdeckbuilder.hexentities.AbstractCard;
import com.ozawa.hextcgdeckbuilder.hexentities.Card;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

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
    	Bitmap image= Bitmap.createBitmap(imageIn);
    	String count;
    	if(customDeck.get(card)!=null){
    		count=String.valueOf(customDeck.get(card));
    	}else{
    		return image;
    	}
    	
    	Canvas combine = new Canvas(image);
    	Paint textPaint = new Paint();
    	textPaint.setTextSize( ((float)image.getHeight()) * 0.09f);
    	int buf = (int) (textPaint.getTextSize()*0.2f);
    	
    	textPaint.setTextAlign(Paint.Align.RIGHT);
    	textPaint.setColor(-1);        
    	textPaint.setAntiAlias(true);
    	Paint boxPaint = new Paint();
    	boxPaint.setColor(0x770060b0);
    	
    	int originX = (int) (((float)image.getWidth())-buf);
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
