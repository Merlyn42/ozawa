package json;

import hexentities.CardTemplate;

import java.lang.reflect.Type;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import enums.ColorFlag;

public class CardTemplateSerializer implements JsonDeserializer<CardTemplate> {

	public CardTemplateSerializer() {
	}

	@Override
	public CardTemplate deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {

		GsonBuilder gsonBuilder = new GsonBuilder();
		gsonBuilder.registerTypeAdapter(ColorFlag[].class, new MultiValueSerializer<ColorFlag>(ColorFlag.class));
		Gson gson = gsonBuilder.create();
		CardTemplate template = gson.fromJson(json, CardTemplate.class);

		template.templateId = "images\\" + template.templateName;
		return template;
	}

}
