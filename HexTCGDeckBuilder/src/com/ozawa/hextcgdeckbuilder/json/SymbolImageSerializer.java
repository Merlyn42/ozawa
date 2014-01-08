package com.ozawa.hextcgdeckbuilder.json;

import java.lang.reflect.Type;

import android.content.Context;
import android.content.res.Resources;

import com.google.gson.Gson;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.ozawa.hextcgdeckbuilder.UI.SymbolTemplate;


public class SymbolImageSerializer implements JsonDeserializer<SymbolTemplate> {
	private Resources	resources;
	private Context		androidContext;

	public SymbolImageSerializer(Context i_androidContext) {
		androidContext = i_androidContext;
		resources = androidContext.getResources();
	}

	@Override
	public SymbolTemplate deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {

		Gson gson = new Gson();
		SymbolTemplate template = gson.fromJson(json, SymbolTemplate.class);

		template.templateId = resources.getIdentifier(template.imageName, "drawable", androidContext.getPackageName());
		int test = resources.getIdentifier("gametext_attack", "drawable", androidContext.getPackageName());	
		System.out.print(test);
		return template;
	}

}
