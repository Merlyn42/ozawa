package json;

import enums.Attribute;

import java.lang.reflect.Type;

import com.google.gson.Gson;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

public class AttributeFlagSerializer implements JsonDeserializer<Attribute[]> {

	@Override
	public Attribute[] deserialize(JsonElement json, Type type, JsonDeserializationContext deserializationContext) throws JsonParseException {
		String string = json.getAsJsonPrimitive().getAsString();
		String[] attributes = string.split("\\|");
		Gson gson = new Gson();
		Attribute[] flags = new Attribute[attributes.length];
		
		for (int i = 0; i < attributes.length; i++) {
			flags[i] = gson.fromJson(attributes[i], Attribute.class);			
		}
		
		return flags;
	}
}
