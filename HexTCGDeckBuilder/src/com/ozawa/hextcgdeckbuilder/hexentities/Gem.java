package com.ozawa.hextcgdeckbuilder.hexentities;

import java.util.List;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.google.gson.annotations.SerializedName;
import com.ozawa.hextcgdeckbuilder.R;
import com.ozawa.hextcgdeckbuilder.enums.GemType;
import com.ozawa.hextcgdeckbuilder.programstate.HexApplication;

/**
 * Gem class used to store gem data for gems socketing cards
 */
public class Gem extends Item {

	@SerializedName("m_BasePrice")
	public int		basePrice;
	@SerializedName("m_GemType")
	public GemType	gemType;

	@SuppressLint("DefaultLocale")
	public static Bitmap getGemSocketedImage(AbstractCard card, HexApplication hexApplication, Gem gem) {
		int resourceId = R.drawable.gem_socket_new;
		
		if(gem == null){
			gem = getGemFromCard(card, hexApplication);
		}
		
		if (gem != null) {
			String gemColour = gem.gemType.toString().toLowerCase();

			if (gemColour.contains("blood")) {
				resourceId = R.drawable.gem_socket_blood_new;
			} else if (gemColour.contains("diamond")) {
				resourceId = R.drawable.gem_socket_diamond_new;
			} else if (gemColour.contains("ruby")) {
				resourceId = R.drawable.gem_socket_ruby_new;
			} else if (gemColour.contains("sapphire")) {
				resourceId = R.drawable.gem_socket_sapphire_new;
			} else if (gemColour.contains("wild")) {
				resourceId = R.drawable.gem_socket_wild_new;
			}
		}

		return BitmapFactory.decodeResource(hexApplication.getResources(), resourceId);
	}

	private static Gem getGemFromCard(AbstractCard card, HexApplication hexApplication) {
		List<GemResource> gemResources = hexApplication.getCustomDeck().getGemResources();
		if (!gemResources.isEmpty()) {
			for (GemResource gemResource : gemResources) {
				if (gemResource.cardId.gUID.equalsIgnoreCase(card.id.gUID)) {
					return hexApplication.getDatabaseHandler().getGem(gemResource.gemId.gUID);
				}
			}
		}

		return null;
	}

}
