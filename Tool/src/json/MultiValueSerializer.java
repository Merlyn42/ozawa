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
