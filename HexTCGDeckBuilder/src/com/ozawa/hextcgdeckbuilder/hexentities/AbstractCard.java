package com.ozawa.hextcgdeckbuilder.hexentities;

import java.util.Arrays;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.Point;
import android.os.Build;
import android.view.Display;
import android.view.WindowManager;

import com.google.gson.annotations.SerializedName;
import com.ozawa.hextcgdeckbuilder.DeckUIActivity;
import com.ozawa.hextcgdeckbuilder.R;
import com.ozawa.hextcgdeckbuilder.UI.CardTemplate;
import com.ozawa.hextcgdeckbuilder.UI.ImageCache;
import com.ozawa.hextcgdeckbuilder.enums.CardRarity;
import com.ozawa.hextcgdeckbuilder.enums.CardType;
import com.ozawa.hextcgdeckbuilder.enums.ColorFlag;


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
     * Generates the card's image and returns it as a bitmap
     * @param context The context to use to retrieve the image.
     * @return The card's image as a bitmap
     */
    
	public Bitmap getThumbnailCardBitmap(Context context){
		
		int maxWidth = getScreenWidth(context)/3;
		
        if (image == null || cachedImageWidthLimit !=maxWidth) {
        	image = getCardBitmap(context, CardTemplate.findCardTemplate(this, false, CardTemplate.getAllTemplates(context)), maxWidth);
        	cachedImageWidthLimit=maxWidth;
        	ImageCache.addToCache(this);
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
		int maxWidth = getScreenWidth(mContext)/8;
		
        if (portrait == null || cachedImageWidthLimit !=maxWidth) {
        	BitmapFactory.Options portraitOptions = new BitmapFactory.Options();
        	portraitOptions.inSampleSize = 10;
        	portrait = BitmapFactory.decodeResource(mContext.getResources(), ((DeckUIActivity) mContext).getResourceID(this.cardImagePath, R.drawable.class), portraitOptions);
        	if(portrait != null){
	        	Matrix matrix = new Matrix();
	        	matrix.postScale(0.5f, 0.5f);
	        	int dimensions = (portrait.getWidth() / 18);
	        	portrait = Bitmap.createBitmap(portrait, dimensions*2, 0, dimensions*14, portrait.getHeight() - 1, matrix, true);
	        	portrait = Bitmap.createScaledBitmap(portrait, maxWidth, maxWidth, true);
	        	cachedImageWidthLimit=maxWidth;
	        	ImageCache.addToCache(this);
        	}
        }
         
		return portrait;
	}
	
	public void clearImageCache(){
		//image.recycle();
		image=null;
	}
    
	@SuppressLint("NewApi")
    private int getScreenWidth(Context context){
    	int measuredWidth = 0;
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Point size = new Point();
        //different methods based on SDK version
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            wm.getDefaultDisplay().getSize(size);
            measuredWidth = size.x;
        } else {
            Display d = wm.getDefaultDisplay();
            measuredWidth = d.getWidth();
        }
        return measuredWidth;
    }

    
    public Bitmap getFullscreenCardBitmap(Context context){
        return getCardBitmap(context, CardTemplate.findCardTemplate(this, true, CardTemplate.getAllTemplates(context)), getScreenWidth(context));
    }

	public abstract Bitmap getCardBitmap(Context context, CardTemplate template,
			int maxWidth) ;

}
