package com.ozawa.hextcgdeckbuilder.hexentities;

import java.util.Arrays;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;

import com.google.gson.annotations.SerializedName;
import com.ozawa.hextcgdeckbuilder.R;
import com.ozawa.hextcgdeckbuilder.UI.CardTemplate;
import com.ozawa.hextcgdeckbuilder.UI.ImageCache;
import com.ozawa.hextcgdeckbuilder.UI.ImageCache.CacheType;
import com.ozawa.hextcgdeckbuilder.UI.ImageCache.ImageType;
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
	public GlobalIdentifier		id;
	@SerializedName("m_SetId")
	public GlobalIdentifier		setID;
	@SerializedName("m_Name")
	public String				name;
	@SerializedName("m_CardNumber")
	public int					cardNumber;
	@SerializedName("m_ColorFlags")
	public ColorFlag[]			colorFlags;
	@SerializedName("m_CardImagePath")
	public String				cardImagePath;
	@SerializedName("m_CardType")
	public CardType[]			cardType;
	@SerializedName("m_CardSubtype")
	public String				cardSubtype;
	@SerializedName("m_CardRarity")
	public CardRarity			cardRarity;
	@SerializedName("m_ArtistName")
	public String				artistName;
	@SerializedName("m_GameText")
	public String				gameText;
	@SerializedName("m_DesignNotes")
	public String				designNotes;
	@SerializedName("m_ImplNotes")
	public String				implementationNotes;
	@SerializedName("m_Unlimited")
	public Boolean				unlimited;
	@SerializedName("m_Tradeable")
	public Boolean				tradeable;
	@SerializedName("m_HasExtendedLayout")
	public Boolean				hasExtendedLayout;
	@SerializedName("m_DefaultLayout")
	public CardLayout			defaultLayout;
	@SerializedName("m_ExtendedLayout")
	public CardLayout			extendedLayout;
	@SerializedName("m_ResourceThresholdGranted")
	public ResourceThreshold[]	resourceThresholdGranted;
	protected Bitmap			image;
	protected Bitmap			portrait;
	protected int				cachedImageWidthLimit;

	public String getID() {
		return this.id.gUID;
	}

	/**
	 * Generates the card's image and returns it as a bitmap also caches the
	 * image in the Card's image variable and adds this card to the cache queue.
	 * 
	 * @param context
	 *            The context to use to retrieve the image.
	 * @return The card's image as a bitmap
	 */

	public Bitmap getThumbnailCardBitmap(Context context) {

		int maxWidth = HexUtil.getScreenWidth(context) / 3;

		if (image == null || cachedImageWidthLimit != maxWidth) {
			try {
				image = getCardBitmap(context, CardTemplate.findCardTemplate(this, false, CardTemplate.getAllTemplates(context)), maxWidth);
			} catch (OutOfMemoryError e) {
				System.err.println("Ran out of memory, dumping some images from the cache");
				ImageCache.emergencyDump(CacheType.GridView);
				image = getThumbnailCardBitmap(context);
			}
			cachedImageWidthLimit = maxWidth;
			ImageCache.getInstance(CacheType.GridView).queueForRemovalFromCache(context, this, ImageType.WithTemplate);
		}

		return image;
	}

	/**
	 * Generates the card portrait image, scaled for the listview of a deck
	 * 
	 * @param mContext
	 * @return
	 */
	public abstract Bitmap getCardPortait(Context mContext);

	/**
	 * called when a card is removed from the cache queue. Should free the
	 * bitmap that was cached for this position in the queue.
	 * 
	 * @param imageType
	 *            which type of image to free.
	 */
	public void clearImageCache(ImageType imageType) {
		switch (imageType) {
		case WithoutTemplate:
			portrait = null;
			break;
		case WithTemplate:
			image = null;
			break;

		}
	}

	public Bitmap getFullscreenCardBitmap(Context context) {
		try {
			return getCardBitmap(context, CardTemplate.findCardTemplate(this, true, CardTemplate.getAllTemplates(context)),
					HexUtil.getScreenWidth(context));
		} catch (OutOfMemoryError e) {
			System.err.println("Ran out of memory, dumping some images from the cache");
			ImageCache.emergencyDump(CacheType.GridView);
			return getCardBitmap(context, CardTemplate.findCardTemplate(this, true, CardTemplate.getAllTemplates(context)),
					HexUtil.getScreenWidth(context));
		}
	}

	public boolean isTroop() {
		return Arrays.asList(this.cardType).contains(CardType.TROOP);
	}

	public abstract Bitmap getCardBitmap(Context context, CardTemplate template, int maxWidth);

	public abstract Bitmap addCount(String count, Bitmap imageIn);

}
