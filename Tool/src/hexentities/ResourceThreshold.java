package hexentities;

import com.google.gson.annotations.SerializedName;

import enums.ColorFlag;

/**
 * Threshold data for a card.
 * 
 * @author Chad Kinsella
 */
public class ResourceThreshold {
	
	@SerializedName("m_ColorFlags")
	public ColorFlag[] colorFlags;
	@SerializedName("m_ThresholdColorRequirement")
	public int thresholdColorRequirement;

}
