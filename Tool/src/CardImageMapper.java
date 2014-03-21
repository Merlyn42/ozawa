import hexentities.Card;
import hexentities.Champion;
import hexentities.Gem;

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
	private float				quality					= 0.6f;
	private ZipOutputStream		target;

	CardImageMapper(File _hexLocation, ZipOutputStream _target) {
		hexLocation = _hexLocation;
		target = _target;
	}

	CardImageMapper(File _hexLocation, ZipOutputStream _target, float quality) {
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
		FileInputStream gemStream = new FileInputStream(gemFile);
		DigestInputStream gemDigestStream = new DigestInputStream(gemStream, md);
		Gem gem = JSONSerializer.deserializeJSONtoGem(gemDigestStream);
		gemDigestStream.close();
		byte[] gemDigest = md.digest();
		String newGemJSON = JSONSerializer.serializeGemToJSON(gem);
		String guid = newGemName + gem.id.getM_Guid().replace("-", "_");
		newHashData.put(guid, gemDigest);
		if (!compareDigests(gemDigest, oldHashData.get(guid))) {
			ZipEntry newGemEntry = new ZipEntry(guid + ".json");
			target.putNextEntry(newGemEntry);
			zipEntries.add(newGemEntry.getName());
			target.write(newGemJSON.getBytes());
			target.closeEntry();
		}
	}

	public void transcribeChampionFile(File championFile) throws NoSuchAlgorithmException, IOException {
		Gson gson = new Gson();
		MessageDigest champMD = MessageDigest.getInstance("MD5");
		FileInputStream champStream = new FileInputStream(championFile);
		DigestInputStream champDigestStream = new DigestInputStream(champStream, champMD);
		Champion champ = gson.fromJson(new InputStreamReader(champDigestStream), Champion.class);
		champDigestStream.close();
		byte[] champDigest = champMD.digest();
		if (champ.gameText != null && !champ.gameText.trim().contentEquals("")) {
			if (champ.hudPortraitSmall != null) {
				String championImagePath = champ.hudPortraitSmall;
				File imageFile = new File(hexLocation, championImagePath);
				String guid = championPortraitSmall + champ.id.getM_Guid().replace("-", "_");

				ZipEntry smallPortEntry = new ZipEntry(guid + ".png");

				MessageDigest md = MessageDigest.getInstance("MD5");
				FileInputStream stream = new FileInputStream(imageFile);
				DigestInputStream digestStream = new DigestInputStream(stream, md);

				byte[] buf = new byte[1024];
				int len = 0;
				ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
				while ((len = digestStream.read(buf)) > 0) {
					outputStream.write(buf, 0, len);
				}
				digestStream.close();
				byte[] digest = md.digest();
				newHashData.put(guid, digest);
				if (!compareDigests(digest, oldHashData.get(guid))) {
					target.putNextEntry(smallPortEntry);
					zipEntries.add(smallPortEntry.getName());
					target.write(outputStream.toByteArray());
					target.closeEntry();
					champ.hudPortraitSmall = smallPortEntry.getName();
				}
			}

			if (champ.hudPortrait != null) {
				MessageDigest md = MessageDigest.getInstance("MD5");
				String championImagePath = champ.hudPortrait;
				File imageFile = new File(hexLocation, championImagePath);
				String guid = championPortrait + champ.id.getM_Guid().replace("-", "_");

				FileInputStream stream = new FileInputStream(imageFile);
				DigestInputStream dis = new DigestInputStream(stream, md);
				BufferedImage image = CardImagerMapperUtil.openImage(dis);
				dis.close();
				byte[] portraitDigest = md.digest();
				newHashData.put(guid, portraitDigest);
				if (!compareDigests(portraitDigest, oldHashData.get(guid))) {
					ZipEntry newImageEntry = new ZipEntry(guid + ".jpg");
					target.putNextEntry(newImageEntry);
					zipEntries.add(newImageEntry.getName());
					CardImagerMapperUtil.writeJpegToOutputStream(target, image, quality);
					target.closeEntry();
					champ.hudPortrait = newImageEntry.getName();
				}

			}
			String guid = newChampionName + champ.id.getM_Guid().replace("-", "_");
			newHashData.put(guid, champDigest);
			if (!compareDigests(champDigest, oldHashData.get(guid))) {
				String newChampionJSON = gson.toJson(champ);
				ZipEntry newChampionEntry = new ZipEntry(guid + ".json");
				target.putNextEntry(newChampionEntry);
				zipEntries.add(newChampionEntry.getName());
				target.write(newChampionJSON.getBytes());
				target.closeEntry();
			}
		}

	}

	public void transcribeCardFile(File cardFile) throws NoSuchAlgorithmException, IOException {
		MessageDigest cardMD = MessageDigest.getInstance("MD5");
		MessageDigest imageMD = MessageDigest.getInstance("MD5");
		FileInputStream cardStream = new FileInputStream(cardFile);
		DigestInputStream cardDigestStream = new DigestInputStream(cardStream, cardMD);
		Card card = JSONSerializer.deserializeJSONtoCard(cardDigestStream);
		cardDigestStream.close();
		String imageGuid = imageName + card.getM_Id().getM_Guid().replace("-", "_");
		String cardGuid = newCardName + card.getM_Id().getM_Guid().replace("-", "_");
		String cardImagePath = card.getM_CardImagePath();
		File imageFile = new File(hexLocation, cardImagePath);
		FileInputStream stream = new FileInputStream(imageFile);
		DigestInputStream dis = new DigestInputStream(stream, imageMD);
		BufferedImage image = CardImagerMapperUtil.openImage(dis);
		dis.close();
		byte[] newImageDigest = imageMD.digest();
		newHashData.put(imageGuid, newImageDigest);
		if (!compareDigests(newImageDigest, oldHashData.get(imageGuid))) {
			ZipEntry newImageEntry = new ZipEntry(imageGuid + ".jpg");
			target.putNextEntry(newImageEntry);
			zipEntries.add(newImageEntry.getName());
			CardImagerMapperUtil.writeJpegToOutputStream(target, image, quality);
			target.closeEntry();
			card.setM_CardImagePath(newImageEntry.getName());
		}

		byte[] newCardDigest = cardMD.digest();
		newHashData.put(cardGuid, newCardDigest);
		if (!compareDigests(newCardDigest, oldHashData.get(cardGuid))) {
			String newCardJSON = JSONSerializer.serializeCardToJSON(card);
			ZipEntry newCardEntry = new ZipEntry(cardGuid + ".json");
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

	public byte[] getNewHashData() {
		Gson gson = new Gson();
		return gson.toJson(newHashData).getBytes();
	}

}
