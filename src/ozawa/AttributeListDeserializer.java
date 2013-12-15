package ozawa;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import ozawa.Card.Attribute;

import com.google.gson.*;

public class AttributeListDeserializer implements JsonDeserializer<List<Attribute>> {

	@Override
	public List<Attribute> deserialize(JsonElement json, Type arg1, JsonDeserializationContext arg2) throws JsonParseException {
		String string = json.getAsJsonPrimitive().getAsString();
		String[] atts = string.split("\\|");
		Gson gson = new Gson();
		ArrayList<Attribute> list = new ArrayList<Attribute>();
		for (String s : atts) {
			list.add(gson.fromJson(s, Card.Attribute.class));
		}
		return list;
	}
}
