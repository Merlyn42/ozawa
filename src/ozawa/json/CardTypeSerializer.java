package json;

import enums.CardType;

import java.lang.reflect.Type;

import com.google.gson.Gson;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

public class CardTypeSerializer implements JsonDeserializer<CardType[]> {

	@Override
	public CardType[] deserialize(JsonElement json, Type type, JsonDeserializationContext deserializationContext) throws JsonParseException {
		String string = json.getAsJsonPrimitive().getAsString();
		String[] cardTypes = string.split("\\|");
		Gson gson = new Gson();
		CardType[] flags = new CardType[cardTypes.length];
		
		for (int i = 0; i < cardTypes.length; i++) {
			flags[i] = gson.fromJson(cardTypes[i], CardType.class);			
		}
		
		return flags;
	}
}
