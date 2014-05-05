/*******************************************************************************
 * Hex TCG Card Generator
 *     Copyright ( C ) 2014  Chad Kinsella, Dave Kerr and Laurence Reading
 * 
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 * 
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 * 
 *     You should have received a copy of the GNU General Public License
 *     along with this program.  If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/
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
