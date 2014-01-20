package com.ozawa.android.hexentities;

import com.google.gson.annotations.SerializedName;

import com.ozawa.android.enums.ColorFlag;

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
