
import hexentities.AbstractCard;
import hexentities.Card;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import org.apache.commons.io.FileUtils;

import json.JSONSerializer;

public class CardImagerMapperUtil {
	private static File baseLocation = new File("C:\\data");
	private static File newImageLocation = new File(baseLocation,
			"\\res\\drawable-hdpi\\");
	private static String imageName = "hex";
	private static int fileNumber = 1;

	private static File newCardLocation = new File(baseLocation, "\\res\\raw\\");
	private static String newCardName = "hexcard";

	public static void generateImageAndCardJSONData(String hexLocation) {

		// File[] cardFiles = new
		// File("C:\\Program Files (x86)\\Hex\\Data\\Sets\\Set001\\CardDefinitions").listFiles();
		try {
			baseLocation.mkdir();
			newImageLocation.mkdir();
			newCardLocation.mkdir();
		} catch (Exception e) {
		}
		ArrayList<Card> allCards = new ArrayList<Card>();
		try {
			File[] cardFiles = new File(hexLocation,
					"\\Sets\\Set001\\CardDefinitions").listFiles();

			for (File cardFile : cardFiles) {
				String cardJSON = JSONSerializer.getJSONFromFiles(cardFile);
				allCards.add(JSONSerializer.deserializeJSONtoCard(cardJSON));
			}

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		for (AbstractCard card : allCards) {
			try {
				String cardImagePath = card.cardImagePath;
				File imageFile = new File(hexLocation, cardImagePath);
				File newImageFile = new File(newImageLocation , imageName
						+ String.format("%05d", fileNumber) + ".png");
				try {
					FileUtils.copyFile(imageFile, newImageFile);
				} catch (IOException e) {
					e.printStackTrace();
				}

				card.cardImagePath = newImageFile.getName();
				String newCardJSON = JSONSerializer.serializeCardToJSON(card);

				File newCardFile = new File(newCardLocation, newCardName
						+ String.format("%05d", fileNumber) + ".json");
				FileOutputStream newCardOutput = new FileOutputStream(
						newCardFile);
				for (byte b : newCardJSON.getBytes()) {
					try {
						newCardOutput.write(b);
					} catch (IOException e) {
						e.printStackTrace();
					}
				}

				fileNumber++;
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public static void main(String args[]) {
		generateImageAndCardJSONData("C:\\Program Files (x86)\\Hex\\Data");

	}

}
