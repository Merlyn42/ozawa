package com.ozawa.hextcgdeckbuilder.hexentities;

import com.google.gson.annotations.SerializedName;

/**
 * A layout for a card which gives the relative positions of the card portrait.
 * 
 * @author Chad Kinsella
 */
public class CardLayout {
	
	@SerializedName("m_Id")
	public GlobalIdentifier id;
	@SerializedName("m_Name")
	public String name;
	@SerializedName("m_PortraitRotation")
	public double portraitRotation;
	@SerializedName("m_PortraitLeft")
	public double portraitLeft;
	@SerializedName("m_PortraitBottom")
	public double portraitBottom;
	@SerializedName("m_PortraitRight")
	public double portraitRight;
	@SerializedName("m_PortraitTop")
	public double portraitTop;

}
