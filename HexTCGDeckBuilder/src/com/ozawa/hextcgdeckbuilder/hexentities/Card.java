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
        paint.setColor(-1);        
        paint.setAntiAlias(true); 

        if(template.fullCard){
        	drawFullImageText(combine,templateImage,paint,resources);
        } else {
        	drawThumbnailText(combine,templateImage,paint);
        }

        return result;
    }
	
	private void drawFullImageText(Canvas combine,Bitmap templateImage,Paint paint, Resources resources) {
		paint.setTextSize(20f);	
		combine.drawText(name, templateImage.getWidth() / 6 , templateImage.getHeight() / 14, paint);        
        if(resourceCost > 9){
        	combine.drawText("" + resourceCost, templateImage.getWidth() / 14f, templateImage.getHeight() / 14, paint);
        } else{
        	combine.drawText("" + resourceCost, templateImage.getWidth() / 13f, templateImage.getHeight() / 14, paint);
        }
        
        paint.setTextSize(12f);
        drawGameText(gameText,62,combine,templateImage,paint,resources);        
        
        if (cardType[0].equals(CardType.TROOP)) {
        	paint.setTextSize(28f);
            combine.drawText(baseAttackValue, templateImage.getWidth() / 17, templateImage.getHeight() - (templateImage.getHeight() / 25), paint);
            combine.drawText(baseHealthValue, templateImage.getWidth() - (templateImage.getWidth() / 10), templateImage.getHeight() - (templateImage.getHeight() / 25), paint);
        }		
	}
	
	private void drawGameText(String gameText, int length, Canvas combine, Bitmap templateImage,Paint paint, Resources resources){
		if(gameText.length() < length)
			drawTextWithImages(gameText, templateImage, combine, paint,0,resources);
		else{
			float line = 0f;
			String displayText = "";
			String [] words = gameText.split(" ");
			for(String word : words){
				if((displayText + word).length() >= length){
						drawTextWithImages(displayText,templateImage,combine,paint,line,resources);
						displayText = "";
						line += .05f;
				}
				displayText += word + " ";				
			}
			drawTextWithImages(displayText,templateImage,combine,paint,line,resources);		
		}
	}
	
	
	private void drawTextWithImages(String displayText,Bitmap templateImage,Canvas combine,Paint paint, float line, Resources resources){
		String delims = "[\\[\\]<>]";
		String []stuff = displayText.split(delims);
		float width = templateImage.getWidth() / 14;
		for(int i = 0; i < stuff.length; i++){
			if(i % 2 == 0){
				if(stuff[i].equals("")){ 
					continue;
				}else {
					Bitmap startImage = textAsBitmap(stuff[i], paint, templateImage);
					combine.drawBitmap(startImage, width, templateImage.getHeight() / (1.425f - line), paint);
					width += startImage.getWidth();
				}
			} else {
				Bitmap symbolImage = getSymbolImage(stuff[i], resources);
				combine.drawBitmap(symbolImage, width, templateImage.getHeight() / (1.425f - line), paint);
				width += symbolImage.getWidth();
			}			
		}
	}

	private Bitmap textAsBitmap(String DisplayText, Paint paint, Bitmap templateImage){
		int width = (int)(paint.measureText(DisplayText) + 0.5f);
		float baseline = (int)(-paint.ascent() + 0.5f);
		int height = (int)(baseline + paint.descent() + 0.5f);
		Bitmap displayTextImage = Bitmap.createBitmap(width,height,Bitmap.Config.ARGB_8888);
		Canvas canvas = new Canvas(displayTextImage);
		canvas.drawText(DisplayText, 0, baseline, paint);
		return displayTextImage;
	}
	
	private Bitmap getSymbolImage(String image, Resources resources){		
		return Bitmap.createScaledBitmap(BitmapFactory.decodeResource(resources, R.drawable.gametext_bloodshard),14,18,false);				
	}

	private void drawThumbnailText(Canvas combine,Bitmap templateImage,Paint paint){
		paint.setTextSize(28f);
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
