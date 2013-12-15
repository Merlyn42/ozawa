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
import java.util.ArrayList;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class JSONSerializer {
	static final File HEXLOCATION = new File("D:\\Program Files (x86)\\Hex");
	
	
	public static Card convertJSONtoCard(String json){
		GsonBuilder gsonBuilder = new GsonBuilder();
		gsonBuilder.registerTypeAdapter(Attribute[].class, new MultiValueSerializer<Attribute>(Attribute.class));
		gsonBuilder.registerTypeAdapter(ColorFlag[].class, new MultiValueSerializer<ColorFlag>(ColorFlag.class));
		gsonBuilder.registerTypeAdapter(CardType[].class, new MultiValueSerializer<CardType>(CardType.class));
		gsonBuilder.registerTypeAdapter(Boolean.class, new BooleanSerializer());
		Gson gson = gsonBuilder.create();
		
		Card newCard = gson.fromJson(json, Card.class);
		
		return newCard;
	}
	
	public static void main(String args[]){
		ArrayList<Card> allCards = new ArrayList<Card>();
		try {
			File[] cardFiles = new File(HEXLOCATION,"\\Data\\Sets\\Set001\\CardDefinitions").listFiles();
			
			for(File cardFile : cardFiles ){
				String json = "";
				BufferedReader reader = new BufferedReader(new FileReader(cardFile));
				String line;
				try {
					while((line = reader.readLine()) != null){
						json += line;
						
					}
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}finally{
					try {
						reader.close();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				allCards.add(convertJSONtoCard(json));
				
			}
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		System.out.println(allCards.size());
		System.out.println(allCards.get(1).name);
		System.out.println(allCards.get(1).cardImagePath);
		System.out.println(allCards.get(1).gameText);
		System.out.println(allCards.get(1).flavorText);
		System.out.println(allCards.get(8).name);
		System.out.println(allCards.get(116).name);
		System.out.println(allCards.get(116).attributeFlags[0].getAttribute());
		System.out.println(allCards.get(116).unique);
		System.out.println(allCards.get(116).colorFlags[0]);
		System.out.println(allCards.get(116).cardType[0]);
		System.out.println(allCards.get(116).cardType[0].getCardType());
		System.out.println(allCards.get(116).cardRarity.getCardRarity());
		System.out.println(allCards.get(24).name);
		System.out.println(allCards.get(67).name);
		System.out.println(allCards.get(76).name);
		System.out.println(allCards.get(77).name);
		
	}
}
