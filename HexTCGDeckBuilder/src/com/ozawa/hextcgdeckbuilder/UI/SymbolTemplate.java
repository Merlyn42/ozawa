package com.ozawa.hextcgdeckbuilder.UI;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
	private Bitmap image;
	
	private static List<SymbolTemplate>	ALLTEMPLATES;
	
	public static List<SymbolTemplate> getAllTemplates(Context context) {
		if (ALLTEMPLATES == null) {
			Resources res = context.getResources();
			JsonReader jsonReader = new JsonReader(context);
			ALLTEMPLATES = Arrays.asList(jsonReader.deserializeJSONInputStreamToSymbolTemplates(res.openRawResource(R.raw.symbols)));
		}		
		return ALLTEMPLATES;
	}
	
	public static SymbolTemplate findSymbolTemplate(String symbol, List<SymbolTemplate> templates){
		ArrayList<SymbolTemplate> results = new ArrayList<SymbolTemplate>();
		for (SymbolTemplate template : templates) {
			if(template.cardText.equalsIgnoreCase(symbol)){
				results.add(template);
			}
		}
		if (results.size() > 1) {
			System.err.println("More than one valid template found for symbol:" + symbol);
			return null;
		} else if (results.size() == 0) {
			StringBuilder message = new StringBuilder("No valid card template found for card:");
			message.append(symbol);			
			System.err.println(message.toString());
			return null;
		}
		return results.get(0);		
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
