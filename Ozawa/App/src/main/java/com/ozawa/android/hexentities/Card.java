package com.ozawa.android.hexentities;

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
        if (image != null) {
            return image;
        }
        Resources resources = context.getResources();
        final int portraitId = resources.getIdentifier(cardImagePath.split("\\.")[0], "drawable",
                context.getPackageName());

        int Measuredwidth = 0;
        int Measuredheight = 0;
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Point size = new Point();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            wm.getDefaultDisplay().getSize(size);
            Measuredwidth = size.x;
            Measuredheight = size.y;
        } else {
            Display d = wm.getDefaultDisplay();
            Measuredwidth = d.getWidth();
            Measuredheight = d.getHeight();
        }

        int fgID = R.drawable.colorless_action_thumbnail;
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


        BitmapFactory.Options templateFirstOptions = new BitmapFactory.Options();
        templateFirstOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(resources, fgID, templateFirstOptions);
        int scale = 1;
        while (templateFirstOptions.outWidth / scale / 2 >= Measuredwidth / 3)
            scale *= 2;
        //Decode with inSampleSize
        BitmapFactory.Options templateSecondOptions = new BitmapFactory.Options();
        templateSecondOptions.inSampleSize = scale;
        Bitmap template = BitmapFactory.decodeResource(resources, fgID, templateSecondOptions);

        BitmapFactory.Options portraitFirstOptions = new BitmapFactory.Options();
        portraitFirstOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(resources, portraitId, portraitFirstOptions);

        int cutPortraitWidth = Double.valueOf(portraitFirstOptions.outWidth * defaultLayout.portraitRight - portraitFirstOptions.outWidth * defaultLayout.portraitLeft).intValue();

        scale = 1;
        while (cutPortraitWidth / scale / 2 >= Measuredwidth / 3)
            scale *= 2;
        //Decode with inSampleSize
        BitmapFactory.Options portraitSecondOptions = new BitmapFactory.Options();
        portraitSecondOptions = new BitmapFactory.Options();
        portraitSecondOptions.inSampleSize = scale;
        Bitmap portrait = BitmapFactory.decodeResource(resources, portraitId, portraitSecondOptions);

        Paint paint = new Paint();
        paint.setTextAlign(Paint.Align.LEFT);
        paint.setTextSize(14f);
        paint.setColor(-1);
        paint.setFakeBoldText(true);

        image = Bitmap.createBitmap(template.getWidth(), template.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas combine = new Canvas(image);
        Rect dstRect = new Rect();
        Rect srcRect = new Rect();
        dstRect.top = (int) (template.getHeight() * 0.1086);
        dstRect.bottom = (int) (template.getHeight() * 0.7918);
        dstRect.right = (int) (template.getWidth() * 0.9387);
        dstRect.left = (int) (template.getWidth() * 0.0660);
        srcRect.left = Double.valueOf(portrait.getWidth() * defaultLayout.portraitLeft).intValue();
        srcRect.right = Double.valueOf(portrait.getWidth() * defaultLayout.portraitRight).intValue();
        srcRect.top = Double.valueOf(portrait.getWidth() * defaultLayout.portraitTop).intValue();
        srcRect.bottom = Double.valueOf(portrait.getWidth() * defaultLayout.portraitBottom).intValue();

        combine.drawBitmap(portrait, srcRect, dstRect, null);
        combine.drawBitmap(template, 0f, 0f, null);


        combine.drawText(name, 50, 20, paint);
        paint.setTextSize(16f);
        combine.drawText("" + resourceCost, template.getWidth() / 6, template.getHeight() / 6, paint);
        if (cardType[0].equals(CardType.TROOP)) {
            paint.setTextSize(21f);
            combine.drawText(baseAttackValue, template.getWidth() / 9, template.getHeight() - (template.getWidth() / 10), paint);
            combine.drawText(baseHealthValue, template.getWidth() - (template.getWidth() / 6), template.getHeight() - (template.getWidth() / 10), paint);
        }
        return image;
    }
}
