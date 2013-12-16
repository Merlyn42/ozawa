package com.ozawa.android.enums;
import com.google.gson.annotations.SerializedName;

/**
 * Each different card type.
 * 
 * @author Chad Kinsella
 */
public enum CardType {
	
	@SerializedName("Troop")
	TROOP("Troop"),
	@SerializedName("BasicAction")
	BASICACTION("Basic Action"),
	@SerializedName("QuickAction")
	QUICKACTION("Quick Action"),
	@SerializedName("Constant")
	CONSTANT("Constant"),
	@SerializedName("Artifact")
	ARTIFACT("Artifact"),
	@SerializedName("Champion")
	CHAMPION("Champion"),
	@SerializedName("Resource")
	RESOURCE("Resource");
	
	private String cardType;
	
	private CardType(String type){
		this.cardType = type;
	}
	
	public String getCardType(){
		return cardType;
	}

}
