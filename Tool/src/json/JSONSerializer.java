package json;

import enums.Attribute;
import hexentities.AbstractCard;
import hexentities.Card;
import hexentities.Deck;
import enums.CardType;
import enums.ColorFlag;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * Class used to serialize and deserialize JSON to required Hex entities and vice versa.
 * 
 * @author Chad Kinsella
 */
public class JSONSerializer {
	static final File HEXLOCATION = new File("D:\\Program Files (x86)\\Hex");
	
	/**
	 * Deserialize a JSON String into a Card
	 * 
	 * @param json
	 * @return A Card deserialized from the given JSON
	 */
	public static Card deserializeJSONtoCard(String json){
		GsonBuilder gsonBuilder = new GsonBuilder();
		gsonBuilder.registerTypeAdapter(Attribute[].class, new MultiValueSerializer<Attribute>(Attribute.class));
		gsonBuilder.registerTypeAdapter(ColorFlag[].class, new MultiValueSerializer<ColorFlag>(ColorFlag.class));
		gsonBuilder.registerTypeAdapter(CardType[].class, new MultiValueSerializer<CardType>(CardType.class));
		gsonBuilder.registerTypeAdapter(Boolean.class, new BooleanSerializer());
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
	public static String serializeCardToJSON(AbstractCard card){
		GsonBuilder gsonBuilder = new GsonBuilder();
		gsonBuilder.registerTypeAdapter(Attribute[].class, new MultiValueSerializer<Attribute>(Attribute.class));
		gsonBuilder.registerTypeAdapter(ColorFlag[].class, new MultiValueSerializer<ColorFlag>(ColorFlag.class));
		gsonBuilder.registerTypeAdapter(CardType[].class, new MultiValueSerializer<CardType>(CardType.class));
		gsonBuilder.registerTypeAdapter(Boolean.class, new BooleanSerializer());
		Gson gson = gsonBuilder.create();
		
		String newJSON = gson.toJson(card);
		
		return newJSON;
	}
	
	/**
	 * Deserialize a JSON String into a Deck
	 * 
	 * @param json
	 * @return A Deck deserialized from the given JSON
	 */
	public static Deck deserializeJSONtoDeck(String json){
		Gson gson = new Gson();
		
		Deck newDeck = gson.fromJson(json, Deck.class);
		
		return newDeck;
	}
	
	public static void main(String args[]){
		ArrayList<Card> allCards = new ArrayList<Card>();
		ArrayList<Deck> allDecks = new ArrayList<Deck>();
		try {
			File[] cardFiles = new File(HEXLOCATION,"\\Data\\Sets\\Set001\\CardDefinitions").listFiles();
			
			for(File cardFile : cardFiles ){
				String cardJSON = getJSONFromFiles(cardFile);
				allCards.add(deserializeJSONtoCard(cardJSON));
				
			}
			
			File[] deckFiles = new File(HEXLOCATION, "\\Data\\Decks").listFiles();
			for(File deckFile : deckFiles){
				String deckJSON = getJSONFromFiles(deckFile);
				allDecks.add(deserializeJSONtoDeck(deckJSON));
			}
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
		System.out.println("******* Cards *******");
		System.out.println(allCards.size());
		System.out.println(allCards.get(1).name);
		System.out.println(allCards.get(1).id.gUID);
		System.out.println(allCards.get(1).cardImagePath);
		System.out.println(allCards.get(1).gameText);
		System.out.println(allCards.get(8).name);
		System.out.println(allCards.get(116).name);
		System.out.println(allCards.get(116).attributeFlags[0].getAttribute());
		System.out.println(allCards.get(116).unique);
		System.out.println(allCards.get(116).colorFlags[0]);
		System.out.println(allCards.get(116).cardType[0]);
		System.out.println(allCards.get(116).cardType[0].getCardType());
		System.out.println(allCards.get(116).cardRarity.getCardRarity());
		System.out.println(allCards.get(116).flavorText);
		System.out.println(allCards.get(24).name);
		System.out.println(allCards.get(67).name);
		System.out.println(allCards.get(76).name);
		System.out.println(allCards.get(77).name);
		
		System.out.println("******* Decks *******");
		System.out.println(allDecks.size());
		System.out.println(allDecks.get(0).name);
		System.out.println(allDecks.get(1).name);
		System.out.println(allDecks.get(2).name);
		System.out.println(allDecks.get(3).name);
		System.out.println(allDecks.get(4).name);
		
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
			while((line = reader.readLine()) != null){
				json += line;				
			}
		} catch (IOException e) {
			e.printStackTrace();
		}finally{
			try {
				reader.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}			
		
		return json;
	}
}
