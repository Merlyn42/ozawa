package com.ozawa.android.enums;

import com.google.gson.annotations.SerializedName;

/**
 * The rarity of each card.
 * 
 * @author Chad Kinsella
 */
public enum CardRarity implements CardEnum {
	
	@SerializedName("Common")
	COMMON("Common"),
	@SerializedName("Uncommon")
	UNCOMMON("Uncommon"),
	@SerializedName("Rare")
	RARE("Rare"),
	@SerializedName("Legendary")
	LEGENDARY("Legendary");
	
	private String rarity;
	
	private CardRarity(String rarity){
		this.rarity = rarity;
	}
	
	public String getCardRarity(){
		return rarity;
	}

}
