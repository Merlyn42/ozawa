package json;

import java.lang.reflect.Array;
import java.lang.reflect.Type;

import com.google.gson.Gson;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;


public class MultiValueSerializer<T> implements JsonDeserializer<T[]> {
	
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
}
