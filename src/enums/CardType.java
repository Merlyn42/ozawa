package enums;

import com.google.gson.annotations.SerializedName;

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
	@SerializedName("Mercenary")
	MERCENARY("Mercenary"),
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
