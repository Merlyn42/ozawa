

import hexentities.AbstractCard;
import hexentities.Card;
import hexentities.Deck;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

import org.apache.commons.io.FileUtils;

import json.JSONSerializer;

public class CardImagerMapperUtil {

	public static void generateImageAndCardJSONData(String hexLocation){
		
		//File[] cardFiles = new File("C:\\Program Files (x86)\\Hex\\Data\\Sets\\Set001\\CardDefinitions").listFiles();
		
		ArrayList<Card> allCards = new ArrayList<Card>();
		try {
			File[] cardFiles = new File(hexLocation,"\\Data\\Sets\\Set001\\CardDefinitions").listFiles();
			
			for(File cardFile : cardFiles ){
				String cardJSON = JSONSerializer.getJSONFromFiles(cardFile);
				allCards.add(JSONSerializer.deserializeJSONtoCard(cardJSON));				
			}
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
		for(AbstractCard card : allCards){
			String cardImagePath = card.cardImagePath;
			File imageFile = new File(cardImagePath);
			File newImageFile = new File("");
			try {
				FileUtils.copyFile(imageFile, newImageFile);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
}
