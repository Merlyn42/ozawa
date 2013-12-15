package json;

import enums.ColorFlag;

import java.lang.reflect.Type;

import com.google.gson.Gson;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

public class ColorFlagSerializer implements JsonDeserializer<ColorFlag[]> {

	@Override
	public ColorFlag[] deserialize(JsonElement json, Type type, JsonDeserializationContext deserializationContext) throws JsonParseException {
		String string = json.getAsJsonPrimitive().getAsString();
		String[] colors = string.split("\\|");
		Gson gson = new Gson();
		ColorFlag[] flags = new ColorFlag[colors.length];
		
		for (int i = 0; i < colors.length; i++) {
			flags[i] = gson.fromJson(colors[i], ColorFlag.class);			
		}
		
		return flags;
	}
}