package com.ozawa.hextcgdeckbuilder.hexentities;

import com.google.gson.annotations.SerializedName;

import com.ozawa.hextcgdeckbuilder.enums.ColorFlag;

/**
 * Threshold data for a card.
 * 
 * @author Chad Kinsella
 */
public class CardThreshold {
	
	@SerializedName("m_ColorFlags")
	ColorFlag colorFlags;
	@SerializedName("m_ThresholdColorRequirement")
	int thresholdColorRequirement;

}
