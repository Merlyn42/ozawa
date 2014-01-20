package com.ozawa.hextcgdeckbuilder.UI;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.ozawa.hextcgdeckbuilder.R;
import com.ozawa.hextcgdeckbuilder.enums.CardType;
import com.ozawa.hextcgdeckbuilder.enums.ColorFlag;
import com.ozawa.hextcgdeckbuilder.json.JsonReader;

public class SymbolTemplate {

	public int templateId;
	public String cardText;
	public String imageName;
	public double sizeRatio;
	private Bitmap image;
	
	private static Map<String,SymbolTemplate>	ALLTEMPLATES;
	
	public static Map<String, SymbolTemplate> getAllTemplates(Context context) {
		if (ALLTEMPLATES == null) {
			Resources res = context.getResources();
			JsonReader jsonReader = new JsonReader(context);
			ALLTEMPLATES = jsonReader.deserializeJSONInputStreamToSymbolTemplates(res.openRawResource(R.raw.symbols));
		}		
		return ALLTEMPLATES;
	}
	
	public static SymbolTemplate findSymbolTemplate(String symbol, Map<String,SymbolTemplate> templates){
		return templates.get(symbol);
	}
	
	public Bitmap getImage(Context context,int width, int height) {
        if (image == null) {
        	image = decodeImage(context,width,height);
        }
		return image;
	}
	
	private Bitmap decodeImage(Context context,int width, int height){
		Resources resources = context.getResources();
        Bitmap symbolImage = BitmapFactory.decodeResource(resources, templateId, null);
        return Bitmap.createScaledBitmap(symbolImage, width,height, false);
	}
}
