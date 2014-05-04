package hexentities;

import java.net.URL;
import java.util.Map;

import json.JsonReader;

public class SymbolTemplate {

	public URL templateId;
	public String cardText;
	public String imageName;
	public double sizeRatio;
	//private Bitmap image;
	public static String symbolJson = "json/data/symbols.json";
	
	private static Map<String,SymbolTemplate>	ALLTEMPLATES;
	
	public static Map<String, SymbolTemplate> getAllTemplates(String symbolJson) {
		if (ALLTEMPLATES == null) {
			JsonReader jsonReader = new JsonReader();
			ALLTEMPLATES = jsonReader.deserializeJSONInputStreamToSymbolTemplates(ClassLoader.getSystemResourceAsStream(symbolJson));
		}
		return ALLTEMPLATES;
	}
	
	public static SymbolTemplate findSymbolTemplate(String symbol, Map<String,SymbolTemplate> templates){
		return templates.get(symbol);
	}
}
