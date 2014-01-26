package com.ozawa.hextcgdeckbuilder.json;

import java.lang.reflect.Type;

import android.content.Context;
import android.content.res.Resources;

import com.google.gson.Gson;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.ozawa.hextcgdeckbuilder.UI.CardTemplate;

public class CardTemplateSerializer implements JsonDeserializer<CardTemplate> {

	private Resources	resources;
	private Context		androidContext;

	public CardTemplateSerializer(Context i_androidContext) {
		androidContext = i_androidContext;
		resources = androidContext.getResources();
	}

	@Override
	public CardTemplate deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {

		Gson gson = new Gson();
		CardTemplate template = gson.fromJson(json, CardTemplate.class);

		template.templateId = resources.getIdentifier(template.templateName, "drawable", androidContext.getPackageName());

		return template;
	}

}
