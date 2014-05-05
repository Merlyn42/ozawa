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

import java.lang.reflect.Array;
import java.lang.reflect.Type;

import com.google.gson.Gson;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

/**
 * Generic serializer and deserializer for JSON values that consist of multiple values separated by "|".
 * 
 * @author Chad Kinsella
 *
 * @param <T>
 */
public class MultiValueSerializer<T> implements JsonDeserializer<T[]>,JsonSerializer<T[]> {
	
	private Class<T> classType;

    public MultiValueSerializer(Class<T> c) {
        this.classType = c;
    }

	@SuppressWarnings("unchecked")
	@Override
	public T[] deserialize(JsonElement json, Type type, JsonDeserializationContext deserializationContext) throws JsonParseException {
		String string = json.getAsJsonPrimitive().getAsString();
		String[] values = string.split("\\|");
		Gson gson = new Gson();
		
		T[] flags = (T[]) Array.newInstance(classType, values.length);
		
		for (int i = 0; i < values.length; i++) {
			flags[i] = gson.fromJson(values[i], classType);			
		}
		return flags;
	}

	@Override
	public JsonElement serialize(T[] arg0, Type arg1,
			JsonSerializationContext arg2) {
		StringBuilder builder = new StringBuilder();
		Gson gson = new Gson();
		boolean first= true;
		for(T element : arg0){
			if(first){
				first = false;
			}else{
				builder.append("|");
			}
			builder.append(gson.toJson(element));
		}
		return new JsonPrimitive(builder.toString());
	}
}
