import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.NoSuchAlgorithmException;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.plugins.jpeg.JPEGImageWriteParam;
import javax.imageio.spi.IIORegistry;
import javax.imageio.spi.ImageWriterSpi;
import javax.imageio.spi.ServiceRegistry;
import javax.imageio.stream.FileImageOutputStream;
import javax.imageio.stream.MemoryCacheImageOutputStream;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.PosixParser;

public class CardImagerMapperUtil {

	public final static String	VERSION	= "v1.0";
	public static float			quality	= 0.6f;

	public static void generateImageAndCardJSONData(File hexLocation, File target, File previous, boolean previousIsMain)
			throws NoSuchAlgorithmException {

		ZipOutputStream out;
		try {
			out = new ZipOutputStream(new FileOutputStream(target));
		} catch (FileNotFoundException e1) {
			System.err.println("Unable to create target Zip");
			e1.printStackTrace();
			return;
		}

		CardImageMapper mapper = new CardImageMapper(hexLocation, out, quality);

		InputStream hashes = null;
		ZipFile originalZip;
		try {
			if (previous != null) {
				originalZip = new ZipFile(previous);
				hashes = originalZip.getInputStream(originalZip.getEntry("hashes.json"));
				mapper.loadOldHashData(hashes);
			}
		} catch (ZipException e2) {
			e2.printStackTrace();
			return;
		} catch (IOException e2) {
			e2.printStackTrace();
			return;
		}

		FilenameFilter filter = new FilenameFilter() {
			@Override
			public boolean accept(File directory, String fileName) {
				return !fileName.endsWith("~");
			}
		};
		File[] cardFiles = new File(hexLocation, "Sets\\Set001\\CardDefinitions").listFiles(filter);

		for (File cardFile : cardFiles) {
			try {
				mapper.transcribeCardFile(cardFile);
			} catch (Exception e) {
				System.err.println("Unable to copy card file:" + cardFile.getName());
				e.printStackTrace();
			}
		}

		File[] championFiles = new File(hexLocation, "Champions\\Templates").listFiles();

		for (File championFile : championFiles) {
			try {
				mapper.transcribeChampionFile(championFile);
			} catch (IOException e) {
				System.err.println("Unable to copy champion file:" + championFile.getName());
				e.printStackTrace();
			}
		}

		// Generate Gem JSON

		File[] gemFiles = new File(hexLocation, "Items\\Gems").listFiles();
		for (File gemFile : gemFiles) {
			try {
				mapper.transcribeGemFile(gemFile);
			} catch (IOException e) {
				System.err.println("Unable to copy gem file:" + gemFile.getName());
				e.printStackTrace();
			}
		}
		
		List<String> entries = mapper.zipEntries;
		
		ZipEntry newHashes = new ZipEntry("hashes.json");
		try {
			out.putNextEntry(newHashes);
			out.write(mapper.getNewHashData());
			entries.add(newHashes.getName());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		if (!previousIsMain) {
			if (previous != null) {
				try {
					originalZip = new ZipFile(previous);
					patch(originalZip,out,entries);
				} catch (ZipException e) {
					e.printStackTrace();
					return;
				} catch (IOException e) {
					e.printStackTrace();
					return;
				}
			}
		}


		
		try {
			out.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	// copy input to output stream
	private static void copyEntry(InputStream input, OutputStream output) throws IOException {
		byte[] BUFFER = new byte[4096 * 1024];
		int bytesRead;
		while ((bytesRead = input.read(BUFFER)) != -1) {
			output.write(BUFFER, 0, bytesRead);
		}
	}

	private static void patch(ZipFile originalZip, ZipOutputStream output, List<String> currentEntryList) throws IOException {

		// copy contents from original zip to the modded zip
		Enumeration<? extends ZipEntry> entries = originalZip.entries();
		while (entries.hasMoreElements()) {
			ZipEntry e = entries.nextElement();

			String name = e.getName();
			if (!currentEntryList.contains(name)) {
				output.putNextEntry(e);
				if (!e.isDirectory()) {
					copyEntry(originalZip.getInputStream(e), output);
				}
				output.closeEntry();
			}
		}
	}

	public static BufferedImage openImage(InputStream is) throws IOException {

		BufferedImage bufferedImage;

		bufferedImage = ImageIO.read(is);
		BufferedImage newBufferedImage = new BufferedImage(bufferedImage.getWidth(), bufferedImage.getHeight(), BufferedImage.TYPE_INT_RGB);
		newBufferedImage.createGraphics().drawImage(bufferedImage, 0, 0, Color.BLACK, null);

		return newBufferedImage;
	}

	public static void writeJpeg(File f, BufferedImage image, float quality) throws IOException {
		JPEGImageWriteParam jpegParams = new JPEGImageWriteParam(null);
		jpegParams.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
		jpegParams.setCompressionQuality(quality);

		ImageWriter writer = getJpegWriter();
		writer.setOutput(new FileImageOutputStream(f));
		writer.write(null, new IIOImage(image, null, null), jpegParams);
	}

	public static void writeJpegToOutputStream(OutputStream output, BufferedImage image, float quality) throws IOException {
		JPEGImageWriteParam jpegParams = new JPEGImageWriteParam(null);
		jpegParams.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
		jpegParams.setCompressionQuality(quality);

		ImageWriter writer = getJpegWriter();
		writer.setOutput(new MemoryCacheImageOutputStream(output));
		writer.write(null, new IIOImage(image, null, null), jpegParams);
	}

	public static ImageWriter getJpegWriter() throws IOException {
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

	public static void main(String args[]) throws ParseException, NoSuchAlgorithmException {
		Options options = new Options();
		options.addOption("source", true, "Root directory of the Hex installation");
		options.addOption("target", true, "Target directory to write to");
		options.addOption("quality", true, "The quality to save the images as [1-100]");
		options.addOption("patch", true, "The previous patch file");
		options.addOption("main", true, "The main file to base this patch on");
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

		if (cmd.hasOption("main") && cmd.hasOption("patch")) {
			System.err.println("can't have both a main file and a patch file specified!");
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

		boolean main = true;
		File previousFile = null;
		if (cmd.hasOption("main")) {
			final String previous;
			previous = cmd.getOptionValue("main");
			previousFile = new File(previous);
			main = true;
		}

		if (cmd.hasOption("patch")) {
			final String previous;
			previous = cmd.getOptionValue("patch");
			previousFile = new File(previous);
			main = false;
		}

		// cleanTarget(targetFile);

		generateImageAndCardJSONData(sourceFile, targetFile, previousFile, main);

	}

	public String getGuid() {
		return null;

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

}
