package json;

import enums.Attribute;
import hexentities.Card;
import enums.CardType;
import enums.ColorFlag;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * Class used to serialize and deserialize JSON to required Hex entities and
 * vice versa.
 * 
 * @author Chad Kinsella
 */
public class JSONSerializer {

	/**
	 * Deserialize a JSON String into a Card
	 * 
	 * @param json
	 * @return A Card deserialized from the given JSON
	 */
	public static Card deserializeJSONtoCard(String json) {
		GsonBuilder gsonBuilder = new GsonBuilder();
		gsonBuilder.registerTypeAdapter(Attribute[].class, new MultiValueSerializer<Attribute>(Attribute.class));
		gsonBuilder.registerTypeAdapter(ColorFlag[].class, new MultiValueSerializer<ColorFlag>(ColorFlag.class));
		gsonBuilder.registerTypeAdapter(CardType[].class, new MultiValueSerializer<CardType>(CardType.class));
		gsonBuilder.registerTypeAdapter(Boolean.class, new BooleanSerializer());
		gsonBuilder.setPrettyPrinting();
		Gson gson = gsonBuilder.create();

		Card newCard = gson.fromJson(json, Card.class);

		return newCard;
	}

	/**
	 * Deserialize a JSON String into a Card
	 * 
	 * @param json
	 * @return A Card deserialized from the given JSON
	 */
	public static String serializeCardToJSON(Card card) {
		GsonBuilder gsonBuilder = new GsonBuilder();
		gsonBuilder.registerTypeAdapter(Attribute[].class, new MultiValueSerializer<Attribute>(Attribute.class));
		gsonBuilder.registerTypeAdapter(ColorFlag[].class, new MultiValueSerializer<ColorFlag>(ColorFlag.class));
		gsonBuilder.registerTypeAdapter(CardType[].class, new MultiValueSerializer<CardType>(CardType.class));
		gsonBuilder.registerTypeAdapter(Boolean.class, new BooleanSerializer());
		gsonBuilder.setPrettyPrinting();
		Gson gson = gsonBuilder.create();

		String newJSON = gson.toJson(card);

		return newJSON;
	}

	/**
	 * Parse a given file to create a JSON String
	 * 
	 * @param file
	 * @return A String that should contain JSON
	 * @throws FileNotFoundException
	 */
	public static String getJSONFromFiles(File file) throws FileNotFoundException {
		String json = "";
		BufferedReader reader = new BufferedReader(new FileReader(file));
		String line;
		try {
			while ((line = reader.readLine()) != null) {
				json += line;
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				reader.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		return json;
	}
}
