package json;

import hexentities.SymbolTemplate;

import java.lang.reflect.Type;

import com.google.gson.Gson;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;


public class SymbolImageSerializer implements JsonDeserializer<SymbolTemplate> {

	public SymbolImageSerializer() {
	}

	@Override
	public SymbolTemplate deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {

		Gson gson = new Gson();
		SymbolTemplate template = gson.fromJson(json, SymbolTemplate.class);

		template.templateId = this.getClass().getResource("/images/" + template.imageName);
		return template;
	}

}