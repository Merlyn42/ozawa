import hexentities.Card;
import hexentities.Champion;
import hexentities.Gem;
import hexentities.M_CardAbility;
import hexentities.M_RelatedCard;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.security.DigestInputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import json.JSONSerializer;

public class CardImageMapper {
	public final List<String>	zipEntries				= new ArrayList<String>();
	private Map<String, byte[]>	oldHashData				= new HashMap<String, byte[]>();
	private Map<String, byte[]>	newHashData				= new HashMap<String, byte[]>();
	private File				hexLocation;
	private static String		newCardName				= "cards/hexcard";
	private static String		imageName				= "images/hexcardportrait";
	private static String		championPortrait		= "images/championportait";
	private static String		championPortraitSmall	= "images/championportaitsmall";
	private static String		newChampionName			= "champions/champion";
	private static String		newGemName				= "gems/gemdata";
	private float				quality					= 0.7f;
	private ZipOutputStream		target;
	private Map<String, String>	newCardMap				= new HashMap<String, String>();
	private boolean				compress				= true;

	CardImageMapper(File _hexLocation, ZipOutputStream _target) {
		hexLocation = _hexLocation;
		target = _target;
	}

	CardImageMapper(File _hexLocation, ZipOutputStream _target,boolean compress, float quality) {
		this.compress=compress;
		hexLocation = _hexLocation;
		target = _target;
		this.quality = quality;
	}

	public void loadOldHashData(InputStream oldHashFile) {
		Gson gson = new Gson();
		Type typeOfHashMap = new TypeToken<Map<String, byte[]>>() {
		}.getType();
		oldHashData = gson.fromJson(new InputStreamReader(oldHashFile), typeOfHashMap);
	}

	public void transcribeGemFile(File gemFile) throws NoSuchAlgorithmException, IOException {
		MessageDigest md = MessageDigest.getInstance("MD5");
		DigestInputStream gemDigestStream = null;
		Gem gem = null;
		try {
			FileInputStream gemStream = new FileInputStream(gemFile);
			gemDigestStream = new DigestInputStream(gemStream, md);
			gem = JSONSerializer.deserializeJSONtoGem(gemDigestStream);
		} catch (IOException e) {
			System.err.println("Failed to read Gem file: " + gemFile.getAbsolutePath());
			System.err.println("Skipping...");
			return;
		} finally {
			if (gemDigestStream != null) {
				try {
					gemDigestStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		byte[] gemDigest = md.digest();
		String newGemJSON = JSONSerializer.serializeGemToJSON(gem);
		String guid = newGemName + gem.id.getM_Guid().replace("-", "_") + ".json";
		newHashData.put(guid, gemDigest);
		if (!compareDigests(gemDigest, oldHashData.get(guid))) {
			ZipEntry newGemEntry = new ZipEntry(guid);
			target.putNextEntry(newGemEntry);
			zipEntries.add(newGemEntry.getName());
			target.write(newGemJSON.getBytes());
			target.closeEntry();
		}
	}

	public void transcribeChampionFile(File championFile) throws NoSuchAlgorithmException, IOException {
		// Get Champion JSON
		Gson gson = new Gson();
		MessageDigest champMD = MessageDigest.getInstance("MD5");
		DigestInputStream champDigestStream = null;
		Champion champ = null;
		try {
			FileInputStream champStream = new FileInputStream(championFile);
			champDigestStream = new DigestInputStream(champStream, champMD);
			champ = gson.fromJson(new InputStreamReader(champDigestStream), Champion.class);
		} catch (IOException e) {
			System.err.println("Failed to read Champion file: " + championFile.getAbsolutePath());
			System.err.println("Skipping...");
			return;
		} finally {
			try {
				if (champDigestStream != null) {
					champDigestStream.close();
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		byte[] champDigest = champMD.digest();
		if (champ.gameText != null && !champ.gameText.trim().contentEquals("")) {
			ArrayList<PendingFile> pendingFiles = new ArrayList<PendingFile>();
			String champGuid = newChampionName + champ.id.getM_Guid().replace("-", "_") + ".json";

			if (!compareDigests(champDigest, oldHashData.get(champGuid))) {
				String newChampionJSON = gson.toJson(champ);
				ZipEntry newChampionEntry = new ZipEntry(champGuid);
				pendingFiles.add(new PendingFile(newChampionEntry, newChampionJSON.getBytes()));
			}

			// Get Small portrait
			if (champ.hudPortraitSmall != null) {
				String championImagePath = champ.hudPortraitSmall;
				File imageFile = new File(hexLocation, championImagePath);
				String guid = championPortraitSmall + champ.id.getM_Guid().replace("-", "_") + ".png";

				ZipEntry smallPortEntry = new ZipEntry(guid);

				MessageDigest md = MessageDigest.getInstance("MD5");
				DigestInputStream digestStream = null;
				byte[] outputBytes;
				try {
					FileInputStream stream = new FileInputStream(imageFile);
					digestStream = new DigestInputStream(stream, md);
					outputBytes = readInputStreamToByteArrayOutputStream(digestStream).toByteArray();
				} catch (IOException e) {
					System.err.println("Failed to read Champion Small Portrait file: " + imageFile.getAbsolutePath()
							+ " For champion file: " + championFile.getAbsolutePath());
					System.err.println("Skipping...");
					return;
				} finally {
					if (digestStream != null) {
						try {
							digestStream.close();
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}
				byte[] digest = md.digest();
				newHashData.put(guid, digest);
				if (!compareDigests(digest, oldHashData.get(guid))) {
					pendingFiles.add(new PendingFile(smallPortEntry, outputBytes));
					champ.hudPortraitSmall = smallPortEntry.getName();
				}
			}

			// get large portrait
			if (champ.hudPortrait != null) {
				MessageDigest md = MessageDigest.getInstance("MD5");
				String championImagePath = champ.hudPortrait;
				File imageFile = new File(hexLocation, championImagePath);
				String guid = championPortrait + champ.id.getM_Guid().replace("-", "_") + ".jpg";
				DigestInputStream dis = null;
				byte[] outputBytes = null;
				BufferedImage image = null;
				try {
					FileInputStream stream = new FileInputStream(imageFile);
					dis = new DigestInputStream(stream, md);
					if (compress) {
						image = CardImagerMapperUtil.openImage(dis);
					} else {
						outputBytes = readInputStreamToByteArrayOutputStream(dis).toByteArray();
					}
				} catch (IOException e) {
					System.err.println("Failed to read Champion Portrait file: " + imageFile.getAbsolutePath() + " For champion file: "
							+ championFile.getAbsolutePath());
					System.err.println("Skipping...");
					return;
				} finally {
					if (dis != null) {
						try {
							dis.close();
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}
				byte[] portraitDigest = md.digest();
				newHashData.put(guid, portraitDigest);
				if (!compareDigests(portraitDigest, oldHashData.get(guid))) {
					if (compress) {
						ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
						try {
							CardImagerMapperUtil.writeJpegToOutputStream(byteArrayOutputStream, image, quality);
							outputBytes = byteArrayOutputStream.toByteArray();
						} catch (IOException e) {
							System.err.println("Failed to compress champion portrait file: " + imageFile.getAbsolutePath());
							System.err.println("Skipping...");
							e.printStackTrace();
							return;
						}
					}
					ZipEntry newImageEntry = new ZipEntry(guid);
					pendingFiles.add(new PendingFile(newImageEntry, outputBytes));
					champ.hudPortrait = newImageEntry.getName();
				}

			}

			// write data to zip
			for (PendingFile pendingFile : pendingFiles) {
				target.putNextEntry(pendingFile.zipEntry);
				zipEntries.add(pendingFile.zipEntry.getName());
				target.write(pendingFile.bytes);
				target.closeEntry();
			}
			newHashData.put(champGuid, champDigest);
		}

	}

	public void transcribeCardFile(File cardFile, HashMap<String, List<M_RelatedCard>> relatedCards) throws NoSuchAlgorithmException, IOException {
		// read the card json
		MessageDigest cardMD = MessageDigest.getInstance("MD5");
		DigestInputStream cardDigestStream = null;
		Card card = null;
		try {
			FileInputStream cardStream = new FileInputStream(cardFile);
			cardDigestStream = new DigestInputStream(cardStream, cardMD);
			card = JSONSerializer.deserializeJSONtoCard(cardDigestStream);
			List<M_RelatedCard> relatedCardIDs = new ArrayList<M_RelatedCard>();
			for(M_CardAbility cardAbility : card.getM_CardAbilities()){				
				if(relatedCards.get(cardAbility.getM_CardAbilityId().getM_Guid())!= null)
					relatedCardIDs.addAll(relatedCards.get(cardAbility.getM_CardAbilityId().getM_Guid()));
			}
			card.setM_RelatedCards(relatedCardIDs);
		} catch (IOException e) {
			System.err.println("Failed to read card file: " + cardFile.getAbsolutePath());
			System.err.println("Skipping...");
			return; // if we fail to read the card json skip the whole card
		} finally {
			try {
				if (cardDigestStream != null) {
					cardDigestStream.close();
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		// read the portrait image

		String cardImagePath = card.getM_CardImagePath();
		MessageDigest imageMD = MessageDigest.getInstance("MD5");
		String imageGuid = newCardMap.get(cardImagePath);
		boolean portraitReferenced = false;
		if (imageGuid == null) {
			File imageFile = new File(hexLocation, cardImagePath);
			DigestInputStream dis = null;
			byte[] outputBytes = null;
			BufferedImage image = null;
			try {
				FileInputStream stream = new FileInputStream(imageFile);
				dis = new DigestInputStream(stream, imageMD);
				if (compress) {
					image = CardImagerMapperUtil.openImage(dis);
				} else {
					outputBytes = readInputStreamToByteArrayOutputStream(dis).toByteArray();
				}
			} catch (IOException e) {
				System.err.println("Failed to read card portrait file: " + imageFile.getAbsolutePath() + " For card file: "
						+ cardFile.getAbsolutePath());
				System.err.println("Skipping...");
				return;// if we fail to read the image skip the whole card
			} finally {
				if (dis != null) {
					try {
						dis.close();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}

			// write the image to the zip file if its modified
			imageGuid = imageName + card.getM_Id().getM_Guid().replace("-", "_") + ".jpg";
			byte[] newImageDigest = imageMD.digest();
			newHashData.put(imageGuid, newImageDigest);
			if (!compareDigests(newImageDigest, oldHashData.get(imageGuid))) {

				ZipEntry newImageEntry = new ZipEntry(imageGuid);
				target.putNextEntry(newImageEntry);
				zipEntries.add(newImageEntry.getName());
				if (compress) {
					try {
						CardImagerMapperUtil.writeJpegToOutputStream(target, image, quality);
					} catch (IOException e) {
						System.err.println("Failed to compress portrait file: " + imageFile.getAbsolutePath());
						System.err.println("Skipping...");
						e.printStackTrace();
						return;
					}
				} else {
					target.write(outputBytes);
				}

				target.closeEntry();
			}
			newCardMap.put(cardImagePath, imageGuid);
		} else {
			portraitReferenced = true;
		}
		card.setM_CardImagePath(imageGuid);

		// write the card to the zip if its modified or the image location has
		// changed.
		byte[] newCardDigest = cardMD.digest();
		String cardGuid = newCardName + card.getM_Id().getM_Guid().replace("-", "_") + ".json";
		newHashData.put(cardGuid, newCardDigest);
		if (!compareDigests(newCardDigest, oldHashData.get(cardGuid)) || portraitReferenced) {
			String newCardJSON = JSONSerializer.serializeCardToJSON(card);
			ZipEntry newCardEntry = new ZipEntry(cardGuid);
			target.putNextEntry(newCardEntry);
			zipEntries.add(newCardEntry.getName());
			target.write(newCardJSON.getBytes());
			target.closeEntry();
		}
	}

	private boolean compareDigests(byte[] first, byte[] second) {
		if (first == second) {
			return true;
		}
		if (first == null || second == null) {
			return false;
		}
		if (first.length != second.length) {
			return false;
		}
		for (int i = 0; i < first.length; i++) {
			if (first[i] != second[i]) {
				return false;
			}
		}
		return true;
	}

	private static class PendingFile {
		public ZipEntry	zipEntry;
		public byte[]	bytes;

		public PendingFile(ZipEntry zipEntry, byte[] bytes) {
			super();
			this.zipEntry = zipEntry;
			this.bytes = bytes;
		}

	}

	private ByteArrayOutputStream readInputStreamToByteArrayOutputStream(InputStream inputStream) throws IOException {

		byte[] buf = new byte[1024];
		int len = 0;
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		while ((len = inputStream.read(buf)) > 0) {
			outputStream.write(buf, 0, len);
		}
		return outputStream;
	}

	public byte[] getNewHashData() {
		Gson gson = new Gson();
		return gson.toJson(newHashData).getBytes();
	}

}
