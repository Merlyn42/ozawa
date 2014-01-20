package hexentities;

import com.google.gson.annotations.SerializedName;

import enums.ColorFlag;

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
