package com.ozawa.hextcgdeckbuilder.enums;

import com.google.gson.annotations.SerializedName;

/**
 * Each card colour.
 * 
 * @author Chad Kinsella
 */
public enum ColorFlag implements CardEnum {
	
	@SerializedName("Colorless")
	COLORLESS("Colorless"),
	@SerializedName("Blood")
	BLOOD("Blood"),
	@SerializedName("Diamond")
	DIAMOND("Diamond"),
	@SerializedName("Ruby")
	RUBY("Ruby"),
	@SerializedName("Sapphire")
	SAPPHIRE("Sapphire"),
	@SerializedName("Wild")
	WILD("Wild");

	
	
	private String color;
	
	private ColorFlag(String color){
		this.color = color;
	}
	
	@Override
	public String toString(){
		return getColorFlag();
	}
	
	public String getColorFlag(){
		return color;
	}
}
