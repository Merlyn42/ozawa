package com.ozawa.hextcgdeckbuilder.hexentities;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.os.Build;
import android.view.Display;
import android.view.WindowManager;

import com.google.gson.annotations.SerializedName;
import com.ozawa.hextcgdeckbuilder.R;
import com.ozawa.hextcgdeckbuilder.UI.CardTemplate;
import com.ozawa.hextcgdeckbuilder.enums.Attribute;
import com.ozawa.hextcgdeckbuilder.enums.CardType;

/**
 * A non-resource card.
 *
 * @author Chad Kinsella
 */
public class Card extends AbstractCard {

    @SerializedName("m_Faction")
    public String faction;
    @SerializedName("m_SocketCount")
    public int socketCount;
    @SerializedName("m_AttributeFlags")
    public Attribute[] attributeFlags;
    @SerializedName("m_ResourceCost")
    public int resourceCost;
    @SerializedName("m_Threshold")
    public CardThreshold[] threshold;
    @SerializedName("m_BaseAttackValue")
    public String baseAttackValue;
    @SerializedName("m_BaseHealthValue")
    public String baseHealthValue;
    @SerializedName("m_FlavorText")
    public String flavorText;
    @SerializedName("m_ResourceSymbolImagePath")
    public String resourceSymbolImagePath;
    @SerializedName("m_Unique")
    public Boolean unique;
    @SerializedName("m_EquipmentSlots")
    public GlobalIdentifier[] equipmentSlots;

    /**
     * Creates or retrives the card image including portrait, template and text.
     * @param context The context to use to retrieve the image.
     * @return The bitmap of the card or null if no portrait is found
     * @Author Laurence Reading
     */
    

    
	@Override
    public Bitmap getCardBitmap(Context context, CardTemplate template, int maxWidth) {     		
        Resources resources = context.getResources();
        final int portraitId = resources.getIdentifier(cardImagePath.split("\\.")[0], "drawable",
                context.getPackageName());

        //no resourceID found
        if(portraitId==0)return null;

        // find the correct template
        Bitmap templateImage = template.getImage(context, maxWidth);


        //get the portrait image
        BitmapFactory.Options portraitFirstOptions = new BitmapFactory.Options();
        portraitFirstOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(resources, portraitId, portraitFirstOptions);
        //used to scale the image, use only the part of the image to determine scaling.
        int cutPortraitWidth = Double.valueOf(portraitFirstOptions.outWidth * defaultLayout.portraitRight - portraitFirstOptions.outWidth * defaultLayout.portraitLeft).intValue();
        int scale = 1;
        while (cutPortraitWidth / scale / 2 >= maxWidth)
            scale *= 2;
        //Decode with inSampleSize
        BitmapFactory.Options portraitSecondOptions = new BitmapFactory.Options();
        portraitSecondOptions = new BitmapFactory.Options();
        portraitSecondOptions.inSampleSize = scale;
        Bitmap portrait = BitmapFactory.decodeResource(resources, portraitId, portraitSecondOptions);

        Bitmap result = Bitmap.createBitmap(templateImage.getWidth(), templateImage.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas combine = new Canvas(result);
        Rect dstRect = new Rect();
        Rect srcRect = new Rect();
        dstRect.top = (int) (templateImage.getHeight() * template.top);
        dstRect.bottom = (int) (templateImage.getHeight() * template.bottom);
        dstRect.right = (int) (templateImage.getWidth() * template.right);
        dstRect.left = (int) (templateImage.getWidth() * template.left);
        srcRect.left = Double.valueOf(portrait.getWidth() * defaultLayout.portraitLeft).intValue();
        srcRect.right = Double.valueOf(portrait.getWidth() * defaultLayout.portraitRight).intValue();
        srcRect.top = Double.valueOf(portrait.getWidth() * defaultLayout.portraitTop).intValue();
        srcRect.bottom = Double.valueOf(portrait.getWidth() * defaultLayout.portraitBottom).intValue();

        combine.drawBitmap(portrait, srcRect, dstRect, null);
        combine.drawBitmap(templateImage, 0f, 0f, null);

        Paint paint = new Paint();
        paint.setTextAlign(Paint.Align.LEFT);
        paint.setTextSize(28f);
        paint.setColor(-1);
        paint.setFakeBoldText(true);

        combine.drawText(getDisplayName(name,18), templateImage.getWidth() / 2.8f , templateImage.getHeight() / 11, paint);
        paint.setTextSize(34f);
        if(resourceCost > 9){
        	combine.drawText("" + resourceCost, templateImage.getWidth() / 7.5f, templateImage.getHeight() / 6, paint);
        } else{
        	combine.drawText("" + resourceCost, templateImage.getWidth() / 6.5f, templateImage.getHeight() / 6, paint);
        }
        if (cardType[0].equals(CardType.TROOP)) {
        	paint.setTextSize(36f);
            combine.drawText(baseAttackValue, templateImage.getWidth() / 9, templateImage.getHeight() - (templateImage.getHeight() / 10), paint);
            combine.drawText(baseHealthValue, templateImage.getWidth() - (templateImage.getWidth() / 6), templateImage.getHeight() - (templateImage.getHeight() / 10), paint);
        }
        return result;
    }

	private String getDisplayName(String name, int length) {				
		if(name.length() > length){
			String displayName = "";
			String [] words = name.split(" ");
			for(String word : words){
				if((displayName + word).length() >= length){
					return displayName.concat("..");
				}
				displayName += word + " ";
			}
		}
		return name;
	}
}
