package ozawa.gson;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import ozawa.enums.Attribute;

import com.google.gson.*;

public class AttributeListSerializer implements JsonSerializer<List<Attribute>>,JsonDeserializer<List<Attribute>> {

	@Override
	public List<Attribute> deserialize(JsonElement json, Type arg1, JsonDeserializationContext arg2) throws JsonParseException {
		String string = json.getAsJsonPrimitive().getAsString();
		String[] atts = string.split("\\|");
		Gson gson = new Gson();
		ArrayList<Attribute> list = new ArrayList<Attribute>();
		for (String s : atts) {
			list.add(gson.fromJson(s, Attribute.class));
		}
		return list;
	}

	@Override
	public JsonElement serialize(List<Attribute> arg0, Type arg1, JsonSerializationContext arg2) {
		// TODO Auto-generated method stub
		return null;
	}
}
