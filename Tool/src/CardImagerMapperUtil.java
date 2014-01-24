import hexentities.Card;
import hexentities.Champion;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

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
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;

import sun.java2d.loops.ProcessPath.EndSubPathHandler;

import com.google.gson.Gson;

import json.JSONSerializer;

public class CardImagerMapperUtil {
	private static String		imageName				= "hex";
	private static String		championPortrait		= "championportait";
	private static String		championPortraitSmall	= "championportaitsmall";
	private static int			fileNumber				= 1;

	private static String		newCardName				= "hexcard";
	private static String		newChampionName			= "champion";
	public final static String	VERSION					= "v1.0";

	public static void generateImageAndCardJSONData(File hexLocation, File target) {
		File newImageLocation = new File(target, "\\res\\drawable-nodpi\\");
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
			File[] cardFiles = new File(hexLocation, "Sets\\Set001\\CardDefinitions").listFiles(filter);

			for (File cardFile : cardFiles) {
				String cardJSON = JSONSerializer.getJSONFromFiles(cardFile);
				try{
				allCards.add(JSONSerializer.deserializeJSONtoCard(cardJSON));
				
				}catch (Exception e){
					System.err.println("Unable to parse file:"+cardFile.getName() );
					throw e;
				}
			}

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		for (Card card : allCards) {
			try {
				String cardImagePath = card.getM_CardImagePath();
				File imageFile = new File(hexLocation, cardImagePath);
				File newImageFile = new File(newImageLocation, imageName + String.format("%05d", fileNumber) + ".jpg");

				BufferedImage image = openImage(imageFile);
				writeJpeg(newImageFile, image, 0.6f);
				// FileUtils.copyFile(imageFile, newImageFile);
				card.setM_CardImagePath(FilenameUtils.removeExtension(newImageFile.getName()));
				String newCardJSON = JSONSerializer.serializeCardToJSON(card);

				File newCardFile = new File(newCardLocation, newCardName + String.format("%05d", fileNumber) + ".json");
				FileOutputStream newCardOutput = new FileOutputStream(newCardFile);
				for (byte b : newCardJSON.getBytes()) {
					try {
						newCardOutput.write(b);
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
				newCardOutput.close();
				fileNumber++;
			} catch (FileNotFoundException e) {
				System.out.println("Skipping file as image not found" + card.getM_Name());
			} catch (IOException e) {
				System.out.println("Skipping file as error loading image" + card.getM_Name());
				e.printStackTrace();
			}
		}

		/**
		 * Generate Champion JSON and Images
		 */
		fileNumber = 1;
		Gson gson = new Gson();
		ArrayList<Champion> allChampions = new ArrayList<Champion>();
		try {
			File[] championFiles = new File(hexLocation, "Champions\\Templates").listFiles();

			for (File championFile : championFiles) {
				String championJSON = JSONSerializer.getJSONFromFiles(championFile);
				allChampions.add(gson.fromJson(championJSON, Champion.class));
			}

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		for (Champion champ : allChampions) {
			try {
				if (champ.gameText != null && !champ.gameText.trim().contentEquals("")) {
					if (champ.hudPortraitSmall != null) {
						String championImagePath = champ.hudPortraitSmall;
						File imageFile = new File(hexLocation, championImagePath);
						File newImageFile = new File(newImageLocation, championPortraitSmall + String.format("%05d", fileNumber) + ".png");
						try {
							FileUtils.copyFile(imageFile, newImageFile);
						} catch (IOException e) {
							System.out.println("Skipping champion file as image not found");
						}

						champ.hudPortraitSmall = FilenameUtils.removeExtension(newImageFile.getName());
					}

					if (champ.hudPortrait != null) {
						String championImagePath = champ.hudPortrait;
						File imageFile = new File(hexLocation, championImagePath);
						File newImageFile = new File(newImageLocation, championPortrait + String.format("%05d", fileNumber) + ".png");
						try {
							FileUtils.copyFile(imageFile, newImageFile);
						} catch (IOException e) {
							System.out.println("Skipping champion file as image not found");
						}

						champ.hudPortrait = FilenameUtils.removeExtension(newImageFile.getName());
					}
					String newChampionJSON = gson.toJson(champ);

					File newChampionFile = new File(newCardLocation, newChampionName + String.format("%05d", fileNumber) + ".json");
					FileOutputStream newChampionOutput = new FileOutputStream(newChampionFile);
					for (byte b : newChampionJSON.getBytes()) {
						try {
							newChampionOutput.write(b);
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
					newChampionOutput.close();
					fileNumber++;
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}

	private static BufferedImage openImage(File f) throws IOException {

		BufferedImage bufferedImage;

		bufferedImage = ImageIO.read(f);
		BufferedImage newBufferedImage = new BufferedImage(bufferedImage.getWidth(), bufferedImage.getHeight(), BufferedImage.TYPE_INT_RGB);
		newBufferedImage.createGraphics().drawImage(bufferedImage, 0, 0, Color.BLACK, null);

		return newBufferedImage;
	}

	private static void writeJpeg(File f, BufferedImage image, float quality) throws IOException {
		JPEGImageWriteParam jpegParams = new JPEGImageWriteParam(null);
		jpegParams.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
		jpegParams.setCompressionQuality(quality);

		ImageWriter writer = getJpegWriter();
		writer.setOutput(new FileImageOutputStream(f));
		writer.write(null, new IIOImage(image, null, null), jpegParams);
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
		options.addOption("version", false, "Print the version information and exit");
		options.addOption("help", false, "Print this message");
		CommandLineParser parser = new PosixParser();
		CommandLine cmd = parser.parse(options, args);

		if (cmd.hasOption("version")) {
			System.out.println("Ozawa Tool " + VERSION);
			System.out.println("Written by havocx42");
			return;
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

		generateImageAndCardJSONData(sourceFile, targetFile);

	}

	private static void cleanTarget(File targetFile) {
		File res =new File(targetFile,"res") ;
		if(res.exists()){
			File images =new File(res,"drawable-nodpi") ;
			if(images.exists()){
				File[] files = images.listFiles(new FilenameFilter(){
					@Override
					public boolean accept(File dir, String name) {
						return (name.startsWith("hex0")&&name.endsWith(".jpg")||(name.startsWith("championportait")&&name.endsWith(".png")));
					}
				});
				for (File f :files){
					f.delete();
				}
			}
			File raw =new File(res,"raw") ;
			if(raw.exists()){
				File[] files = raw.listFiles(new FilenameFilter(){
					@Override
					public boolean accept(File dir, String name) {
						return ((name.startsWith("hexcard0")||name.startsWith("champion"))&&name.endsWith(".json"));
					}
				});
				for (File f :files){
					f.delete();
				}
			}
		}
		
		
	}

}
