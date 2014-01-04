import hexentities.AbstractCard;
import hexentities.Card;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.ArrayList;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.PosixParser;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;

import json.JSONSerializer;

public class CardImagerMapperUtil {
	private static String imageName = "hex";
	private static int fileNumber = 1;

	private static String newCardName = "hexcard";
	public final static String VERSION = "v1.0";

	public static void generateImageAndCardJSONData(File hexLocation,File target) {
		File newImageLocation = new File(target,
				"\\res\\drawable-nodpi\\");
		File newCardLocation = new File(target, "\\res\\raw\\");
		// File[] cardFiles = new
		// File("C:\\Program Files (x86)\\Hex\\Data\\Sets\\Set001\\CardDefinitions").listFiles();
		try {
			target.mkdir();
			newCardLocation.getParentFile().mkdir();
			newCardLocation.mkdir();
			newImageLocation.mkdir();
		} catch (Exception e) {
		}
		if (!newCardLocation.exists() || !newImageLocation.exists()) {
			throw new RuntimeException("Location not found");
		}
		ArrayList<Card> allCards = new ArrayList<Card>();
		try {
			FilenameFilter filter = new FilenameFilter() {
		        public boolean accept(File directory, String fileName) {
		            return !fileName.endsWith("~");
		        }
		    };
			File[] cardFiles = new File(hexLocation,
					"Sets\\Set001\\CardDefinitions").listFiles(filter);

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
				File newImageFile = new File(newImageLocation, imageName
						+ String.format("%05d", fileNumber) + ".png");
				try {
					FileUtils.copyFile(imageFile, newImageFile);
				} catch (IOException e) {
					System.out.println("Skipping file as image not found");
				}
				card.cardImagePath = FilenameUtils.removeExtension(newImageFile
						.getName());
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

	public static void main(String args[]) throws ParseException {
		Options options = new Options();
		options.addOption("source", true,
				"Root directory of the Hex installation");
		options.addOption("target", true, "Target directory to write to");
		options.addOption("version", false,
				"Print the version information and exit");
		options.addOption("help", false, "Print this message");
		CommandLineParser parser = new PosixParser();
		CommandLine cmd = parser.parse(options, args);

		if (cmd.hasOption("version")) {
			System.out.println("Ozawa Tool " + VERSION);
			System.out.println("Written by havocx42");
			return;
		}

		if (((!cmd.hasOption("source") || !cmd
				.hasOption("target"))) || cmd.hasOption("help")) {
			HelpFormatter formatter = new HelpFormatter();
			formatter.printHelp("Ozawa Tool", options);
			return;
		}

		final String target;
		final String source;

		source = cmd.getOptionValue("source");
		target = cmd.getOptionValue("target");
		File targetFile = new File(target);
        File sourceFile = new File(source,"\\Data\\");

		generateImageAndCardJSONData(sourceFile,targetFile);

	}

}
