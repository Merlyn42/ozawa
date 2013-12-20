package com.ozawa.android.hexentities;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;

import com.google.gson.annotations.SerializedName;

import com.ozawa.android.R;
import com.ozawa.android.enums.Attribute;
import com.ozawa.android.enums.CardType;

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
        if (image != null) {
            return image;
        }
        int fgID = R.drawable.colorless_action;
        if (cardType.length > 0) {
            if (cardType[0] == CardType.TROOP || (cardType.length > 1 && cardType[1] == CardType.TROOP)) {
                if (colorFlags.length > 0) {
                    switch (colorFlags[0]) {
                        case BLOOD:
                            fgID = R.drawable.blood_troop_thumbnail;
                            break;
                        case COLORLESS:
                            fgID = R.drawable.colorless_troop_thumbnail;
                            break;
                        case DIAMOND:
                            fgID = R.drawable.diamond_troop_thumbnail;
                            break;
                        case RUBY:
                            fgID = R.drawable.ruby_troop_thumbnail;
                            break;
                        case SAPPHIRE:
                            fgID = R.drawable.sapphire_troop_thumbnail;
                            break;
                        case WILD:
                            fgID = R.drawable.wild_troop_thumbnail;
                            break;
                    }
                }
            } else if (cardType[0] == CardType.QUICKACTION || cardType[0] == CardType.BASICACTION || cardType[0] == CardType.CONSTANT || (cardType.length > 1 && (cardType[0] == CardType.QUICKACTION || cardType[0] == CardType.BASICACTION || cardType[0] == CardType.CONSTANT))) {
                if (colorFlags.length > 0) {
                    switch (colorFlags[0]) {
                        case BLOOD:
                            fgID = R.drawable.blood_action_thumbnail;
                            break;
                        case COLORLESS:
                            fgID = R.drawable.colorless_action_thumbnail;
                            break;
                        case DIAMOND:
                            fgID = R.drawable.diamond_action_thumbnail;
                            break;
                        case RUBY:
                            fgID = R.drawable.ruby_action_thumbnail;
                            break;
                        case SAPPHIRE:
                            fgID = R.drawable.sapphire_action_thumbnail;
                            break;
                        case WILD:
                            fgID = R.drawable.wild_action_thumbnail;
                            break;
                    }
                }
            }
        }

        BitmapFactory.Options o = new BitmapFactory.Options();
        o.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(resources, fgID, o);
        int scale = 1;
        while (o.outWidth / scale / 2 >= 200)
            scale *= 2;
        //Decode with inSampleSize
        BitmapFactory.Options o2 = new BitmapFactory.Options();
        o2.inSampleSize = scale;
        Bitmap fg = BitmapFactory.decodeResource(resources, fgID, o2);


        BitmapFactory.decodeResource(resources, resourceId, o);
        scale = 1;
        while (o.outWidth / scale / 2 >= 140)
            scale *= 2;
        //Decode with inSampleSize
        o2 = new BitmapFactory.Options();
        o2.inSampleSize = scale;
        Bitmap bg = BitmapFactory.decodeResource(resources, resourceId, o2);

        Paint paint = new Paint();
        paint.setTextAlign(Paint.Align.LEFT);
        paint.setTextSize(14f);
        paint.setColor(-1);
        paint.setFakeBoldText(true);

        image = Bitmap.createBitmap(fg.getWidth(), fg.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas combine = new Canvas(image);
        combine.drawBitmap(bg, 0f, 10f, null);
        combine.drawBitmap(fg, 0f, 0f, null);
        combine.drawText(name,50,20,paint);
        paint.setTextSize(16f);
        combine.drawText(""+resourceCost,fg.getWidth()/6,fg.getHeight()/6,paint);
        if(cardType[0].equals(CardType.TROOP)){
        paint.setTextSize(21f);
            combine.drawText(baseAttackValue,fg.getWidth()/9,fg.getHeight()-(fg.getWidth()/10),paint);
            combine.drawText(baseHealthValue,fg.getWidth()-(fg.getWidth()/6),fg.getHeight()-(fg.getWidth()/10),paint);
        }
        return image;
    }
}
