package com.ozawa.hextcgdeckbuilder.hexentities;

import java.util.Arrays;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.os.Build;
import android.view.Display;
import android.view.WindowManager;

import com.google.gson.annotations.SerializedName;
import com.ozawa.hextcgdeckbuilder.R;
import com.ozawa.hextcgdeckbuilder.UI.CardTemplate;
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
    protected int cachedImageWidthLimit;

    /**
     * Generates the card's image and returns it as a bitmap
     * @param context The context to use to retrieve the image.
     * @return The card's image as a bitmap
     */
    
	public Bitmap getThumbnailCardBitmap(Context context){
		return getCardBitmap(context, CardTemplate.findCardTemplate(this, false, CardTemplate.getAllTemplates(context)), getScreenWidth(context)/3);
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
