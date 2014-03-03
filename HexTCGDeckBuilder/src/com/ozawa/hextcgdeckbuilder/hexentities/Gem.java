package com.ozawa.hextcgdeckbuilder.hexentities;

import com.google.gson.annotations.SerializedName;
import com.ozawa.hextcgdeckbuilder.enums.GemType;

/**
 * Gem class used to store gem data for gems socketing cards
 */
public class Gem extends Item {

	@SerializedName("m_BasePrice")
	public int		basePrice;
	@SerializedName("m_GemType")
	public GemType	gemType;

}
