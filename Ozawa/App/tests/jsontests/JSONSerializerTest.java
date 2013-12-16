package jsontests;

import static org.junit.Assert.*;
import com.ozawa.android.hexentities.Card;
import com.ozawa.android.hexentities.Deck;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import json.JSONSerializer;

import org.junit.Before;
import org.junit.Test;

/**
 * Test JSONSerializer functionality
 * 
 * @author Chad Kinsella
 */
public class JSONSerializerTest {
	
	String cardJSON = "";
	String deckJSON = "";

	@Before
	public void setUp() throws Exception {
		File cardFile = new File("tests\\testhelperfiles\\ProtectorateClergymanCard");
		cardJSON = getJSONFromFiles(cardFile);
		File deckFile = new File("tests\\testhelperfiles\\ShinhaireDeck");
		deckJSON = getJSONFromFiles(deckFile);
		
	}
	
	/**
	 * Test that Card entities are correctly deserialized from JSON.
	 */
	@Test
	public void testDeserializeJSONtoCard() {
		Card card = JSONSerializer.deserializeJSONtoCard(cardJSON);
		assertTrue("Card name should equal Protectorate Clergyman", card.name.contentEquals("Protectorate Clergyman"));
	}
	
	/**
	 * Test that Deck entities are correctly deserialized from JSON.
	 */
	@Test
	public void testDeserializeJSONtoDeck() {
		Deck deck = JSONSerializer.deserializeJSONtoDeck(deckJSON);
		assertTrue("Deck name should equal Demo_Deck_Shin'hare", deck.name.contentEquals("Demo_Deck_Shin'hare"));
	}
	
	/**
	 * Utility method used to retrieve JSON from a File.
	 * 
	 * @param file
	 * @return JSON String from the content of the given File.
	 * @throws FileNotFoundException
	 */
	private String getJSONFromFiles(File file) throws FileNotFoundException {
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
