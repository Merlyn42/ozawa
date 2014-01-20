package com.ozawa.android.json;

import java.lang.reflect.Type;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

/**
 * JSON serializer and deserializer for Boolean values.
 * 
 * @author Chad Kinsella
 */
public class BooleanSerializer implements JsonSerializer<Boolean>, JsonDeserializer<Boolean> {
	
	@Override
	public JsonElement serialize(Boolean bool, Type type, JsonSerializationContext serializationContext) {
		return new JsonPrimitive(bool ? 1 : 0);
	}

	@Override
	public Boolean deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext deserializationContext) throws JsonParseException {
		return jsonElement.getAsInt() == 1 ? true : false;
	}
}
