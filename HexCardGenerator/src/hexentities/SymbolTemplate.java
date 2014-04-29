package hexentities;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.Map;

import json.JsonReader;

public class SymbolTemplate {

	public String templateId;
	public String cardText;
	public String imageName;
	public double sizeRatio;
	//private Bitmap image;
	public static String symbolJson = "HexCardGenerator/src/json/data/symbols.json";
	
	private static Map<String,SymbolTemplate>	ALLTEMPLATES;
	
	public static Map<String, SymbolTemplate> getAllTemplates(String symbolJson) {
		if (ALLTEMPLATES == null) {
			JsonReader jsonReader = new JsonReader();
			try {
				ALLTEMPLATES = jsonReader.deserializeJSONInputStreamToSymbolTemplates(new FileInputStream(new File(symbolJson)));
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
		}
		return ALLTEMPLATES;
	}
	
	public static SymbolTemplate findSymbolTemplate(String symbol, Map<String,SymbolTemplate> templates){
		return templates.get(symbol);
	}
	
	/*public Bitmap getImage(Context context,int width, int height) {
        if (image == null) {
        	image = decodeImage(context,width,height);
        }
		return image;
	}
	
	private Bitmap decodeImage(Context context,int width, int height){
		Resources resources = context.getResources();
        Bitmap symbolImage = BitmapFactory.decodeResource(resources, templateId, null);
        return Bitmap.createScaledBitmap(symbolImage, width,height, false);
	}*/
}
