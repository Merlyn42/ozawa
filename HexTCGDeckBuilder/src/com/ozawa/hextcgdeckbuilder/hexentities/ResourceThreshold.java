package com.ozawa.hextcgdeckbuilder.hexentities;

import com.google.gson.annotations.SerializedName;

import com.ozawa.hextcgdeckbuilder.enums.ColorFlag;

/**
 * Threshold data for a card.
 * 
 * @author Chad Kinsella
 */
public class ResourceThreshold {
	
	@SerializedName("m_ColorFlags")
	public ColorFlag colorFlags;
	@SerializedName("m_ThresholdColorRequirement")
	public int thresholdColorRequirement;

}
