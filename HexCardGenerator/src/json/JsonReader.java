/*******************************************************************************
 * Hex TCG Card Generator
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