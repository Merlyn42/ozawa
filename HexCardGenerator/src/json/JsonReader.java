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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class JsonReader {

    Gson gson;

    public JsonReader(){
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(Attribute[].class, new MultiValueSerializer<Attribute>(Attribute.class));
        gsonBuilder.registerTypeAdapter(ColorFlag[].class, new MultiValueSerializer<ColorFlag>(ColorFlag.class));
        gsonBuilder.registerTypeAdapter(CardType[].class, new MultiValueSerializer<CardType>(CardType.class));
        gsonBuilder.registerTypeAdapter(Boolean.class, new BooleanSerializer());
        //gsonBuilder.registerTypeAdapter(AbstractCard.class, new AbstractCardSerializer());
        gsonBuilder.registerTypeAdapter(CardTemplate.class, new CardTemplateSerializer());
        gsonBuilder.registerTypeAdapter(SymbolTemplate.class, new SymbolImageSerializer());
        gson = gsonBuilder.create();
    }

    /*public AbstractCard deserializeJSONInputStreamToCard(InputStream jsonStream){
        InputStreamReader reader = new InputStreamReader(jsonStream);
        AbstractCard newCard = gson.fromJson(reader,AbstractCard.class);
        return newCard;
    }*/
    
    public CardTemplate[] deserializeJSONInputStreamToCardTemplates(InputStream jsonStream){
        InputStreamReader reader = new InputStreamReader(jsonStream);
        CardTemplate[] templates = gson.fromJson(reader,CardTemplate[].class);
        return templates;
    }

    /*public List<AbstractCard> deserializeJSONInputStreamsToCard(ArrayList<InputStream> jsonStreams){
        AbstractCard[] newCards = new AbstractCard[jsonStreams.size()];
        for(int i = 0;i<jsonStreams.size();i++){
            newCards[i]=deserializeJSONInputStreamToCard(jsonStreams.get(i));
        }
        return Arrays.asList(newCards);
    }*/

    
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