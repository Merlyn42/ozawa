/*******************************************************************************
 * Hex TCG Deck Builder
 *     Copyright ( C ) 2014  Chad Kinsella, Dave Kerr and Laurence Reading
 * 
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 * 
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 * 
 *     You should have received a copy of the GNU General Public License
 *     along with this program.  If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/
package com.ozawa.hextcgdeckbuilder.json;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.ozawa.hextcgdeckbuilder.UI.CardTemplate;
import com.ozawa.hextcgdeckbuilder.UI.SymbolTemplate;
import com.ozawa.hextcgdeckbuilder.enums.Attribute;
import com.ozawa.hextcgdeckbuilder.enums.CardType;
import com.ozawa.hextcgdeckbuilder.enums.ColorFlag;
import com.ozawa.hextcgdeckbuilder.hexentities.AbstractCard;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class JsonReader {

    Gson gson;

    public JsonReader(Context context){
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(Attribute[].class, new MultiValueSerializer<Attribute>(Attribute.class));
        gsonBuilder.registerTypeAdapter(ColorFlag[].class, new MultiValueSerializer<ColorFlag>(ColorFlag.class));
        gsonBuilder.registerTypeAdapter(CardType[].class, new MultiValueSerializer<CardType>(CardType.class));
        gsonBuilder.registerTypeAdapter(Boolean.class, new BooleanSerializer());
        gsonBuilder.registerTypeAdapter(AbstractCard.class, new AbstractCardSerializer());
        gsonBuilder.registerTypeAdapter(CardTemplate.class, new CardTemplateSerializer(context));
        gsonBuilder.registerTypeAdapter(SymbolTemplate.class, new SymbolImageSerializer(context));
        gson = gsonBuilder.create();
    }

    public AbstractCard deserializeJSONInputStreamToCard(InputStream jsonStream){
        InputStreamReader reader = new InputStreamReader(jsonStream);
        AbstractCard newCard = gson.fromJson(reader,AbstractCard.class);
        return newCard;
    }
    
    public CardTemplate[] deserializeJSONInputStreamToCardTemplates(InputStream jsonStream){
        InputStreamReader reader = new InputStreamReader(jsonStream);
        CardTemplate[] templates = gson.fromJson(reader,CardTemplate[].class);
        return templates;
    }

    public List<AbstractCard> deserializeJSONInputStreamsToCard(ArrayList<InputStream> jsonStreams){
        AbstractCard[] newCards = new AbstractCard[jsonStreams.size()];
        for(int i = 0;i<jsonStreams.size();i++){
            newCards[i]=deserializeJSONInputStreamToCard(jsonStreams.get(i));
        }
        return Arrays.asList(newCards);
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
