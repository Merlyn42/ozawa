package json;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import enums.Attribute;
import enums.CardType;
import enums.ColorFlag;
import hexentities.CardTemplate;
import hexentities.SymbolTemplate;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;

public class JsonReader {

    Gson gson;

    public JsonReader(){
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(Attribute[].class, new MultiValueSerializer<Attribute>(Attribute.class));
        gsonBuilder.registerTypeAdapter(ColorFlag[].class, new MultiValueSerializer<ColorFlag>(ColorFlag.class));
        gsonBuilder.registerTypeAdapter(CardType[].class, new MultiValueSerializer<CardType>(CardType.class));
        gsonBuilder.registerTypeAdapter(Boolean.class, new BooleanSerializer());
        gsonBuilder.registerTypeAdapter(CardTemplate.class, new CardTemplateSerializer());
        gsonBuilder.registerTypeAdapter(SymbolTemplate.class, new SymbolImageSerializer());
        gson = gsonBuilder.create();
    }
    
    public CardTemplate[] deserializeJSONInputStreamToCardTemplates(InputStream jsonStream){
        InputStreamReader reader = new InputStreamReader(jsonStream);
        CardTemplate[] templates = gson.fromJson(reader,CardTemplate[].class);
        return templates;
    }
    
    public HashMap<String,SymbolTemplate> deserializeJSONInputStreamToSymbolTemplates(InputStream jsonStream){
    	InputStreamReader reader = new InputStreamReader(jsonStream);
    	SymbolTemplate[] templates = gson.fromJson(reader, SymbolTemplate[].class);
    	HashMap<String,SymbolTemplate> result = new HashMap<String,SymbolTemplate>();
    	for(SymbolTemplate template:templates){
    		result.put(template.cardText, template);
    	}
    	return result;    
    }
}