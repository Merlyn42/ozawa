package com.ozawa.hextcgdeckbuilder.hexentities;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;

import com.google.gson.annotations.SerializedName;
import com.ozawa.hextcgdeckbuilder.R;
import com.ozawa.hextcgdeckbuilder.UI.CardTemplate;
import com.ozawa.hextcgdeckbuilder.UI.ImageCache;
import com.ozawa.hextcgdeckbuilder.enums.CardRarity;
import com.ozawa.hextcgdeckbuilder.enums.CardType;
import com.ozawa.hextcgdeckbuilder.enums.ColorFlag;
import com.ozawa.hextcgdeckbuilder.util.HexUtil;


/**
 * An Abstract Card used to define common fields for all Hex Cards
 * 
 * @author Chad Kinsella
 */
public abstract class AbstractCard {
	
	@SerializedName("m_Id")
	public GlobalIdentifier id;
	@SerializedName("m_SetId")
	public GlobalIdentifier setID;
	@SerializedName("m_Name")
	public String name;
	@SerializedName("m_CardNumber")
	public int cardNumber;
	@SerializedName("m_ColorFlags")
	public ColorFlag[] colorFlags;
	@SerializedName("m_CardImagePath")
	public String cardImagePath;
	@SerializedName("m_CardType")
	public CardType[] cardType;
	@SerializedName("m_CardSubtype")
	public String cardSubtype;
	@SerializedName("m_CardRarity")
	public CardRarity cardRarity;
	@SerializedName("m_ArtistName")
	public String artistName;
	@SerializedName("m_GameText")
	public String gameText;
	@SerializedName("m_DesignNotes")
	public String designNotes;
	@SerializedName("m_Unlimited")
	public Boolean unlimited;
	@SerializedName("m_Tradeable")
	public Boolean tradeable;
	@SerializedName("m_HasExtendedLayout")
	public Boolean hasExtendedLayout;
	@SerializedName("m_DefaultLayout")
	public CardLayout defaultLayout;
	@SerializedName("m_ExtendedLayout")
	public CardLayout extendedLayout;
    protected Bitmap image;
    protected Bitmap portrait;
    protected int cachedImageWidthLimit;
    
    public String getID(){
		return this.id.gUID;
	}

    /**
     * Generates the card's image and returns it as a bitmap also caches the image in the Card's image variable and adds this card to the cache queue.
     * @param context The context to use to retrieve the image.
     * @return The card's image as a bitmap
     */
    
	public Bitmap getThumbnailCardBitmap(Context context){
		
		int maxWidth = HexUtil.getScreenWidth(context)/3;
		
        if (image == null || cachedImageWidthLimit !=maxWidth) {
        	image = getCardBitmap(context, CardTemplate.findCardTemplate(this, false, CardTemplate.getAllTemplates(context)), maxWidth);
        	cachedImageWidthLimit=maxWidth;
        	ImageCache.queueForRemovalFromCache(context,this);
        }
         
		return image;
    }
	
	/**
	 * Generates the card portrait image, scaled for the listview of a deck
	 * 
	 * @param mContext
	 * @return
	 */
	public Bitmap getCardPortait(Context mContext){
		int maxWidth = HexUtil.getScreenWidth(mContext)/8;
		
        if (portrait == null || cachedImageWidthLimit !=maxWidth) {
        	BitmapFactory.Options portraitOptions = new BitmapFactory.Options();
        	portraitOptions.inSampleSize = 10;
        	portrait = BitmapFactory.decodeResource(mContext.getResources(), HexUtil.getResourceID(this.cardImagePath, R.drawable.class), portraitOptions);
        	if(portrait != null){
	        	Matrix matrix = new Matrix();
	        	matrix.postScale(0.5f, 0.5f);
	        	int dimensions = (portrait.getWidth() / 18);
	        	portrait = Bitmap.createBitmap(portrait, dimensions*2, 0, dimensions*14, portrait.getHeight() - 1, matrix, true);
	        	portrait = Bitmap.createScaledBitmap(portrait, maxWidth, maxWidth, true);
	        	cachedImageWidthLimit=maxWidth;
	        	ImageCache.queueForRemovalFromCache(mContext,this);
        	}
        }
         
		return portrait;
	}
	
	
	/**
	 * called when a card is removed from the cache queue. Should free the bitmap that was cached for this position in the queue.
	 */
	public void clearImageCache(){
		//image.recycle();
		image=null;
	}
    
    public Bitmap getFullscreenCardBitmap(Context context){
        return getCardBitmap(context, CardTemplate.findCardTemplate(this, true, CardTemplate.getAllTemplates(context)), HexUtil.getScreenWidth(context));
    }
    
    public boolean isTroop(){
    	for(CardType type : this.cardType){
    		if(type == CardType.TROOP){
    			return true;
    		}
    	}
    	
    	return false;
    }

	public abstract Bitmap getCardBitmap(Context context, CardTemplate template,
			int maxWidth) ;

}
