package com.ozawa.android.hexentities;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;

import com.google.gson.annotations.SerializedName;

import com.ozawa.android.R;
import com.ozawa.android.enums.Attribute;

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



    @Override
    public Bitmap getCardBitmap(Context context) {
        Resources resources = context.getResources();
        final int resourceId = resources.getIdentifier(cardImagePath.split("\\.")[0], "drawable",
                context.getPackageName());
        if(image!=null){
            return image;
        }

        BitmapFactory.Options o = new BitmapFactory.Options();
        o.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(resources, R.drawable.diamond_action_cardtemplate,o);
        int scale=1;
        while(o.outWidth/scale/2>=200)
            scale*=2;
        //Decode with inSampleSize
        BitmapFactory.Options o2 = new BitmapFactory.Options();
        o2.inSampleSize=scale;
        Bitmap fg = BitmapFactory.decodeResource(resources, R.drawable.diamond_action_cardtemplate,o2);



        BitmapFactory.decodeResource(resources, resourceId,o);
        scale=1;
        while(o.outWidth/scale/2>=140)
            scale*=2;
        //Decode with inSampleSize
        o2 = new BitmapFactory.Options();
        o2.inSampleSize=scale;
        Bitmap bg = BitmapFactory.decodeResource(resources, resourceId,o2);

        image = Bitmap.createBitmap(fg.getWidth(), fg.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas combine = new Canvas(image);
        combine.drawBitmap(bg, 0f, 10f, null);
        combine.drawBitmap(fg, 0f, 0f,null);
        return image;
    }
}
