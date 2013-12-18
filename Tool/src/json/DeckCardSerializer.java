package json;

import hexentities.AbstractCard;
import hexentities.Deck;

import java.lang.reflect.Type;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

public class DeckCardSerializer implements JsonSerializer<AbstractCard>, JsonDeserializer<AbstractCard> {

	@Override
	public AbstractCard deserialize(JsonElement arg0, Type arg1, JsonDeserializationContext arg2) throws JsonParseException {
		
		return null;
	}

	@Override
	public JsonElement serialize(AbstractCard arg0, Type arg1, JsonSerializationContext arg2) {
		
		return null;
	}
}
