package ozawa;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class JSONConverter {
	static final File HEXLOCATION = new File("E:\\Games\\Hex");
	
	
	public static Card convertJSONtoCard(String json){
		GsonBuilder gsonBuilder = new GsonBuilder();
		gsonBuilder.registerTypeAdapter(List.class, new AttributeListDeserializer());
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
		System.out.println(allCards.get(116).attributeFlags);
		System.out.println(allCards.get(24).name);
		System.out.println(allCards.get(67).name);
		System.out.println(allCards.get(76).name);
		System.out.println(allCards.get(77).name);
		
	}
}
