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
package utility;

import hexentities.Card;
import hexentities.CardTemplate;
import hexentities.ResourceThreshold;
import hexentities.SymbolTemplate;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FilenameFilter;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.plugins.jpeg.JPEGImageWriteParam;
import javax.imageio.spi.IIORegistry;
import javax.imageio.spi.ImageWriterSpi;
import javax.imageio.spi.ServiceRegistry;
import javax.imageio.stream.FileImageOutputStream;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.PosixParser;

import enums.CardType;
import json.JSONSerializer;

public class CardImagerMapperUtil {
	private static String		cardName				= "hexcard_";
	public final static String	VERSION					= "v1.0";
	public static float			quality					= 0.6f;
	public static float			line;
	
	public static boolean generateCardImages(File hexLocation, File target, Integer quality) throws FileNotFoundException{
		CardImagerMapperUtil.quality = quality / 100.0f;
		cleanTarget(target);

		try {
			generateImageAndCardJSONData(hexLocation, target);
		} catch (FileNotFoundException e) {
			throw new FileNotFoundException("Could not find card files. Check path is HEX root directory.");
		}
		
		return true;
	}
	
	public static void generateImageAndCardJSONData(File hexLocation, File target) throws FileNotFoundException {
		File newImageLocation = new File(target, "\\cards\\");
		
		try {
			target.mkdir();
			newImageLocation.getParentFile().mkdir();
			newImageLocation.mkdir();
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (!newImageLocation.exists()) {
			throw new RuntimeException("Location not found");
		}
		ArrayList<Card> allCards = new ArrayList<Card>();
		try {
			FilenameFilter filter = new FilenameFilter() {
				@Override
				public boolean accept(File directory, String fileName) {
					return !fileName.endsWith("~");
				}
			};
			File[] cardFiles = new File(hexLocation, "Sets\\Set001\\CardDefinitions").listFiles(filter);

			for (File cardFile : cardFiles) {
				String cardJSON = JSONSerializer.getJSONFromFiles(cardFile);
				try {
					allCards.add(JSONSerializer.deserializeJSONtoCard(cardJSON));

				} catch (Exception e) {
					System.err.println("Unable to parse file:" + cardFile.getName());
					throw e;
				}
			}

		} catch (FileNotFoundException e) {
			e.printStackTrace();
			throw new FileNotFoundException("Could not find card files. Check path is HEX root directory.");
		}

		for (Card card : allCards) {
			try {
				if(card.cardType[0] != CardType.RESOURCE){
					CardTemplate template = CardTemplate.findCardTemplate(card, true, CardTemplate.getAllTemplates(CardTemplate.templateJsonPath));
					//File templateImageFile = new File(null, template.templateId);
					File portraitImageFile = new File(hexLocation, card.getM_CardImagePath());
					
					BufferedImage fullCardImage = generateCardImage(card, template, null, portraitImageFile);
					File newImageFile = new File(newImageLocation, cardName + card.getM_Name().replaceAll("\\s", "") + "_" + card.getM_Id().getM_Guid() + ".png");
					
					writeJpeg(newImageFile, fullCardImage, quality);
				}
			} catch (FileNotFoundException e) {
				System.out.println("Skipping file as image not found" + card.getM_Name());
			} catch (IOException e) {
				System.out.println("Skipping file as error loading image" + card.getM_Name());
				e.printStackTrace();
			} catch (Exception e){
				e.printStackTrace();
			}
		}
	}
	
	
	private static BufferedImage openImage(File f, URL url) throws IOException {

		BufferedImage bufferedImage;
		if(f != null){
			bufferedImage = ImageIO.read(f);
		}else{
			bufferedImage = ImageIO.read(url);
		}
		BufferedImage newBufferedImage = new BufferedImage(bufferedImage.getWidth(), bufferedImage.getHeight(), BufferedImage.TYPE_INT_ARGB);
		newBufferedImage.createGraphics().drawImage(bufferedImage, 0, 0, null);

		return newBufferedImage;
	}
	
	private static BufferedImage generateCardImage(Card card, CardTemplate template, File templateImageFile, File portraitImageFile){
		BufferedImage canvas = null;
		
		try {
			BufferedImage portrait = openImage(portraitImageFile, null);
			BufferedImage cardTemplate = openImage(null, template.templateId);
			canvas = new BufferedImage(cardTemplate.getWidth(), cardTemplate.getHeight(), BufferedImage.TYPE_INT_RGB);
			Graphics2D graphics2D = canvas.createGraphics();
			graphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
			graphics2D.drawImage(canvas, 0, 0, Color.BLACK, null);
			int dstRectY1 = (int) (cardTemplate.getHeight() * template.top);
			int dstRectY2 = (int) (cardTemplate.getHeight() * template.bottom);
			int dstRectX2 = (int) (cardTemplate.getWidth() * template.right);
			int dstRectX1 = (int) (cardTemplate.getWidth() * template.left);
			int srcRectX1 = Double.valueOf(portrait.getWidth() * card.getM_DefaultLayout().getM_PortraitLeft()).intValue();
			int srcRectX2 = Double.valueOf(portrait.getWidth() * card.getM_DefaultLayout().getM_PortraitRight()).intValue();
			int srcRectY1 = Double.valueOf(portrait.getWidth() * card.getM_DefaultLayout().getM_PortraitTop()).intValue();
			int srcRectY2 = Double.valueOf(portrait.getWidth() * card.getM_DefaultLayout().getM_PortraitBottom()).intValue();
			
			graphics2D.drawImage(portrait, dstRectX1, dstRectY1, dstRectX2, dstRectY2, srcRectX1, srcRectY1, srcRectX2, srcRectY2, null);
			graphics2D.drawImage(cardTemplate, 0, 0, null);
			graphics2D.dispose();
			
			drawFullImageText(card, canvas, cardTemplate, template);
		} catch (IOException e) {
			e.printStackTrace();
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return canvas;
	}

	private static void writeJpeg(File f, BufferedImage image, float quality) throws IOException {
		JPEGImageWriteParam jpegParams = new JPEGImageWriteParam(null);
		jpegParams.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
		jpegParams.setCompressionQuality(quality);

		ImageWriter writer = getJpegWriter();
		writer.setOutput(new FileImageOutputStream(f));
		writer.write(null, new IIOImage(image, null, null), jpegParams);
		writer.dispose();
	}

	private static ImageWriter getJpegWriter() throws IOException {
		// use IIORegistry to get the available services
		IIORegistry registry = IIORegistry.getDefaultInstance();
		// return an iterator for the available ImageWriterSpi for jpeg images

		Iterator<ImageWriterSpi> services = registry.getServiceProviders(ImageWriterSpi.class, new ServiceRegistry.Filter() {
			@Override
			public boolean filter(Object provider) {
				if (!(provider instanceof ImageWriterSpi))
					return false;

				ImageWriterSpi writerSPI = (ImageWriterSpi) provider;
				String[] formatNames = writerSPI.getFormatNames();
				for (int i = 0; i < formatNames.length; i++) {
					if (formatNames[i].equalsIgnoreCase("JPEG")) {
						return true;
					}
				}

				return false;
			}
		}, true);
		// ...assuming that servies.hasNext() == true, I get the first available
		// service.
		ImageWriterSpi writerSpi = services.next();
		ImageWriter writer = writerSpi.createWriterInstance();
		return writer;

	}

	public static void main(String args[]) throws ParseException {
		Options options = new Options();
		options.addOption("source", true, "Root directory of the Hex installation");
		options.addOption("target", true, "Target directory to write to");
		options.addOption("quality", true, "The quality to save the images as [1-100]");
		options.addOption("version", false, "Print the version information and exit");
		options.addOption("help", false, "Print this message");
		CommandLineParser parser = new PosixParser();
		CommandLine cmd = parser.parse(options, args);

		if (cmd.hasOption("version")) {
			System.out.println("Ozawa Tool " + VERSION);
			System.out.println("Written by havocx42");
			return;
		}

		if (cmd.hasOption("quality")) {
			Boolean fail = false;
			Integer value = null;
			try {
				value = Integer.valueOf(cmd.getOptionValue("quality"));
			} catch (Exception e) {
				fail = true;
			}
			if (!fail && value != null && value > 0 && value <= 100) {
				quality = value / 100.0f;
				System.out.println("Setting quality to: " + quality);
			} else {
				System.out.println("quality must be between 1 and 100");
				HelpFormatter formatter = new HelpFormatter();
				formatter.printHelp("Ozawa Tool", options);
				return;
			}
		}

		if (((!cmd.hasOption("source") || !cmd.hasOption("target"))) || cmd.hasOption("help")) {
			HelpFormatter formatter = new HelpFormatter();
			formatter.printHelp("Ozawa Tool", options);
			return;
		}

		final String target;
		final String source;

		source = cmd.getOptionValue("source");
		target = cmd.getOptionValue("target");

		File targetFile = new File(target);
		File sourceFile = new File(source, "\\Data\\");

		cleanTarget(targetFile);

		try {
			generateImageAndCardJSONData(sourceFile, targetFile);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

	}

	private static void cleanTarget(File targetFile) {
		File res = new File(targetFile, "res");
		if (res.exists()) {
			File images = new File(res, "drawable-nodpi");
			if (images.exists()) {
				File[] files = images.listFiles(new FilenameFilter() {
					@Override
					public boolean accept(File dir, String name) {
						return (name.startsWith("hex0") && name.endsWith(".jpg") || (name.startsWith("championportait") && name
								.endsWith(".png")));
					}
				});
				for (File f : files) {
					f.delete();
				}
			}
			File raw = new File(res, "raw");
			if (raw.exists()) {
				File[] files = raw.listFiles(new FilenameFilter() {
					@Override
					public boolean accept(File dir, String name) {
						return ((name.startsWith("hexcard0") || name.startsWith("champion")) && name.endsWith(".json"));
					}
				});
				for (File f : files) {
					f.delete();
				}
			}
		}

	}
	private static void drawFullImageText(Card card, BufferedImage canvas, BufferedImage templateImage,	CardTemplate template) {
		float imageHeight = templateImage.getHeight();
		Graphics2D graphics2D = canvas.createGraphics();
		
		graphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);		
		graphics2D.setFont(new Font("Roboto", Font.BOLD, (int)(imageHeight * template.nameFontRatio)));
		graphics2D.drawString(card.getM_Name(), templateImage.getWidth() / template.nameWidth, templateImage.getHeight() / template.nameHeight);
		graphics2D.setFont(new Font("Roboto", Font.BOLD, (int)(imageHeight * template.numberRatio)));
		int resourceCost = card.getM_ResourceCost();
		
		if (resourceCost > 9) {
			graphics2D.drawString("" + resourceCost, templateImage.getWidth() / template.bigResourceWidth, templateImage.getHeight()
					/ template.bigResourceHeight);
		} else if (card.getM_VariableCost() == 1) {
			if (resourceCost == 0)
				graphics2D.drawString("X", templateImage.getWidth() / template.smallResourceWidth, templateImage.getHeight()
						/ template.smallResourceHeight);
			else {
				graphics2D.drawString(resourceCost + "X", templateImage.getWidth() / template.bigResourceWidth, templateImage.getHeight()
						/ template.bigResourceHeight);
			}
		} else {
			graphics2D.drawString("" + resourceCost, templateImage.getWidth() / template.smallResourceWidth, templateImage.getHeight()
					/ template.smallResourceHeight);
		}
		if (card.cardType[0].equals(CardType.TROOP)) {
			if (card.getM_VariableAttack() == 1) {
				graphics2D.drawString("X", templateImage.getWidth() / template.atkWidth, templateImage.getHeight()
						- (templateImage.getHeight() / template.atkHeight));
			} else {
				graphics2D.drawString(String.valueOf(card.getM_BaseAttackValue()), templateImage.getWidth() / template.atkWidth,
						templateImage.getHeight() - (templateImage.getHeight() / template.atkHeight));
			}
			if (card.getM_VariableHealth() == 1) {
				graphics2D.drawString("X", templateImage.getWidth() - (templateImage.getWidth() / template.defWidth), templateImage.getHeight()
						- (templateImage.getHeight() / template.defHeight));
			} else {
				graphics2D.drawString(String.valueOf(card.getM_BaseHealthValue()), templateImage.getWidth() - (templateImage.getWidth() / template.defWidth),
						templateImage.getHeight() - (templateImage.getHeight() / template.defHeight));
			}
		}
		graphics2D.setFont(new Font("Roboto", Font.BOLD, (int)(imageHeight * template.typeFontRatio)));
		String cardTypes = "";
		for (int i = 0; i < card.cardType.length; i++) {
			cardTypes += card.cardType[i].getCardType();
			if (i != card.cardType.length - 1)
				cardTypes += ", ";
		}
		if (!card.getM_CardSubtype().equals("")){
			cardTypes += " -- " + card.getM_CardSubtype();
		}

		graphics2D.drawString(cardTypes, templateImage.getWidth() / template.cardTypeWidth,
				templateImage.getHeight() - (templateImage.getHeight() / template.cardTypeHeight));
		if (card.getM_Unique() == 1) {
			graphics2D.drawString("Unique", (templateImage.getWidth() - (templateImage.getWidth() / template.uniqueWidth)) - templateImage.getWidth() / 10,
					templateImage.getHeight() - (templateImage.getHeight() / template.uniqueHeight));
		}
		if (!card.getM_Faction().equals("None")) {
			drawFaction(card, canvas, templateImage, template);
		}
		
		drawRarity(card, canvas, templateImage, template);
		graphics2D.setFont(new Font("Roboto", Font.PLAIN, (int)(imageHeight * template.typeFontRatio)));
		drawGameText(card.getM_GameText().trim(), 64, card, canvas, templateImage, template);
		BufferedImage threshold = getCardThresholdImage(card, template, canvas);
		graphics2D.drawImage(threshold, (int)(templateImage.getWidth() / template.thresholdWidth), 
				(int)(templateImage.getHeight() / template.thresholdHeight), null);
		graphics2D.dispose();
	}
	
	private static void drawFaction(Card card, BufferedImage canvas, BufferedImage templateImage, CardTemplate template) {
		BufferedImage factionImage = null;
		try{
		if (card.getM_Faction().equalsIgnoreCase("Aria")) {
			factionImage = openImage(null, card.getClass().getResource("/images/faction_ardent_new.png"));
		} else if (card.getM_Faction().equalsIgnoreCase("Underworld")) {
			factionImage = openImage(null, card.getClass().getResource("/images/faction_underworld_new.png"));
		}
		}catch (IOException e) {
			e.printStackTrace();
		}

		canvas.createGraphics().drawImage(factionImage, (int)(templateImage.getWidth() / template.factionWidth),
				(int)(templateImage.getHeight() - (templateImage.getHeight() / template.factionHeight)), null);
	}
	
	private static void drawRarity(Card card, BufferedImage canvas, BufferedImage templateImage, CardTemplate template) {
		String resourceId;
		String imagePath = "/images/";
		switch (card.getM_CardRarity().toUpperCase()) {
		case "COMMON":
			resourceId = "card_rarity_common_new.png";
			break;
		case "UNCOMMON":
			resourceId = "card_rarity_uncommon_new.png";
			break;
		case "RARE":
			resourceId = "card_rarity_rare_new.png";
			break;
		case "LEGENDARY":
			resourceId = "card_rarity_legendary_new.png";
			break;
		case "LAND":
			resourceId = "card_rarity_system_new.png";
			break;
		case "PROMO":
			resourceId = "card_rarity_common_new.png";
			break;
		default:
			resourceId = "card_rarity_common_new.png";
		}
		BufferedImage rarity;
		try {
			rarity = openImage(null, card.getClass().getResource(imagePath + resourceId));
			canvas.createGraphics().drawImage(rarity, (int)(templateImage.getWidth() / template.rarityWidth),
					(int)(templateImage.getHeight() - (templateImage.getHeight() / template.rarityHeight)), null);
		} catch (IOException e) {
			e.printStackTrace();
		}		
	}
	
	private static void drawGameText(String gameText, int length, Card card, BufferedImage canvas, BufferedImage templateImage, CardTemplate template) {
		line = 0f;
		FontMetrics fMetrics = canvas.createGraphics().getFontMetrics(new Font("Roboto", Font.BOLD, (int)(templateImage.getHeight() * template.typeFontRatio)));
		
		if (fMetrics.stringWidth(gameText) < (templateImage.getWidth() * template.gameTextLength)) {
			if (gameText.contains("<p>")) {
				int pLocation = gameText.lastIndexOf("<p>") + 3;
				String paragraph = gameText.substring(0, pLocation);
				drawTextWithImages(paragraph, templateImage, canvas, template);
				drawTextWithImages(gameText.substring(pLocation, gameText.length()), templateImage, canvas,	template);
			} else {
				drawTextWithImages(gameText, templateImage, canvas, template);
			}
		} else {
			String displayText = "";
			String[] words = gameText.split(" ");
			for (String word : words) {
				if (word.contains("<p>")) {
					int pLocation = word.lastIndexOf("<p>") + 3;
					String paragraph = word.substring(0, pLocation);
					displayText += paragraph;
					drawTextWithImages(displayText, templateImage, canvas, template);
					displayText = "" + word.substring(pLocation, word.length()) + " ";
				} else {
					if (fMetrics.stringWidth(displayText + word) > (templateImage.getWidth() * template.gameTextLength)) {
						drawTextWithImages(displayText, templateImage, canvas, template);
						displayText = "";
						line += .05f;
					}
					displayText += word + " ";
				}
			}
			if (fMetrics.stringWidth(displayText) > (templateImage.getWidth() * 0.829)) {
				String stuff = displayText.substring(0, displayText.lastIndexOf(" "));
				drawTextWithImages(stuff, templateImage, canvas, template);
				line += .05f;
				stuff = displayText.substring(displayText.lastIndexOf(" "), displayText.length() - 1);
				drawTextWithImages(stuff, templateImage, canvas, template);
			} else {
				drawTextWithImages(displayText, templateImage, canvas, template);
			}
		}
	}

	private static void drawTextWithImages(String displayText, BufferedImage templateImage, BufferedImage canvas, CardTemplate template) {
		String delims = "[\\[\\]<>]";
		String[] stuff = displayText.split(delims);
		FontMetrics fMetrics = canvas.createGraphics().getFontMetrics(new Font("Roboto", Font.BOLD, (int)(templateImage.getHeight() * template.typeFontRatio)));
		float width = templateImage.getWidth() / template.gameTextWidth;
		int baseline = fMetrics.getAscent();
		int height = baseline + fMetrics.getDescent();//(int) templateImage.getHeight() / 20;
		for (int i = 0; i < stuff.length; i++) {
			if (i % 2 == 0) {
				if (stuff[i].equals(""))
					continue;
				Graphics2D graphics2D = canvas.createGraphics();
				
				graphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
				graphics2D.setFont(new Font("Roboto", Font.BOLD, (int)(templateImage.getHeight() * template.typeFontRatio)));
				graphics2D.drawString(stuff[i], width, (templateImage.getHeight() / (template.gameTextHeight - line)) + 20);
				width += (fMetrics.stringWidth(stuff[i]) + 0.5f);
				graphics2D.dispose();
			} else {
				if (stuff[i].equalsIgnoreCase("p")) {
					line += 0.04f;
					width = templateImage.getWidth() / 14;
				} else if (stuff[i].equalsIgnoreCase("b")) {
					// TODO: Make text bold.
				} else if (stuff[i].equalsIgnoreCase("/b")) {
					// TODO: Make text bold.
				} else if (stuff[i].equalsIgnoreCase("i")) {
					// TODO: Make text italic.
				} else if (stuff[i].equalsIgnoreCase("/i")) {
					// TODO: Make text italic.
				} else {
					BufferedImage symbolImage;
					if (stuff[i].equalsIgnoreCase("BASIC")) {
						symbolImage = getSymbolImage(stuff[i], height);
					} else if (stuff[i].startsWith("L") && i <= stuff.length - 2 && stuff[i + 2].startsWith("R")) {
						symbolImage = getSymbolImage(stuff[i],height);
						if (stuff[i + 1].equals("/"))
							stuff[i + 1] = "";
					} else if (stuff[i].startsWith("R") && i >= 2 && stuff[i - 2].startsWith("L")) {
						symbolImage = getSymbolImage(stuff[i], height);
					} else if (stuff[i].equalsIgnoreCase("ONE-SHOT")) {
						symbolImage = getSymbolImage(stuff[i], height);
					} else {
						symbolImage = getSymbolImage(stuff[i], height);
					}
					canvas.createGraphics().drawImage(symbolImage, (int)width, (int)(templateImage.getHeight() / (template.gameTextHeight - line)), null);
					width += symbolImage.getWidth();
				}
			}
		}
	}

	private static BufferedImage getSymbolImage(String symbol, int height) {
		SymbolTemplate symTemp = SymbolTemplate.findSymbolTemplate(symbol, SymbolTemplate.getAllTemplates(SymbolTemplate.symbolJson));
		if (symTemp != null) {
			try {
				BufferedImage bufferedImage = ImageIO.read(symTemp.templateId);
				BufferedImage symbolImage = new BufferedImage((int) (height * symTemp.sizeRatio), height, BufferedImage.TYPE_INT_ARGB);
				symbolImage.createGraphics().drawImage(bufferedImage, 0, 0, (int) (height * symTemp.sizeRatio), height, null);
				return symbolImage;
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		BufferedImage blank = null;
		try {
			blank =  openImage(null, symTemp.getClass().getResource("/images/blank.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return blank;
	}
	
	public static BufferedImage getCardThresholdImage(Card card, CardTemplate template, BufferedImage canvas) {
		if (card.getM_Threshold() != null && card.getM_Threshold().length > 0) {
			ArrayList<BufferedImage> thresholds = new ArrayList<BufferedImage>();
			String imagePath = "/images/";
			for (ResourceThreshold threshold : card.getM_Threshold()) {
				if (threshold.colorFlags != null && threshold.colorFlags.length > 0 && threshold.colorFlags[0] != null) {
					String thresholdName = null;
					switch (threshold.colorFlags[0]) {
					case COLORLESS: {
						break;
					}
					case BLOOD: {
						thresholdName = "blood_threshold_new.png";
						break;
					}
					case DIAMOND: {
						thresholdName = "diamond_threshold_new.png";
						break;
					}
					case RUBY: {
						thresholdName = "ruby_threshold_new.png";
						break;
					}
					case SAPPHIRE: {
						thresholdName = "sapphire_threshold_new.png";
						break;
					}
					case WILD: {
						thresholdName = "wild_threshold_new.png";
						break;
					}
					default: {
						break;
					}
					}
					
					if (thresholdName != null) {
						addCardThresholdBitmapToList(thresholds, card.getClass().getResource(imagePath + thresholdName), threshold.thresholdColorRequirement);
					}
				}
			}

			BufferedImage allThresholds = null;
			if (!thresholds.isEmpty()) {
					int width = Math.round(canvas.getWidth() * template.thresholdWidthRatio);
					int height = Math.round(canvas.getHeight() * template.thresholdHeightRatio);
					allThresholds = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);

					Graphics2D graphics2D = allThresholds.createGraphics();
					graphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
					int left = 0;
					int top = 0;
					int padding = Math.round(canvas.getHeight() * template.thresholdPaddingRatio);
					for (BufferedImage image : thresholds) {
						graphics2D.drawImage(image, left, top, null);
						top += image.getHeight() + padding;
					}
					graphics2D.dispose();
			}

			return allThresholds;
		}

		return null;
	}

	private static void addCardThresholdBitmapToList(List<BufferedImage> thresholds, URL resourcesName, int thresholdCount) {
		for (int i = 0; i < thresholdCount; i++) {
			BufferedImage thresh;
			try {
				thresh = openImage(null, resourcesName);
				thresholds.add(thresh);
			} catch (IOException e) {
				e.printStackTrace();
			}			
		}
	}

}
