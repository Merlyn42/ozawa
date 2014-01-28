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
package com.ozawa.hextcgdeckbuilder.UI;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.widget.ImageView;

import com.ozawa.hextcgdeckbuilder.enums.ImageGetterType;
import com.ozawa.hextcgdeckbuilder.hexentities.AbstractCard;
import com.ozawa.hextcgdeckbuilder.hexentities.Card;

import java.util.Map;

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
