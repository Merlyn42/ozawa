package hexentities;

import com.google.gson.annotations.SerializedName;

import enums.ColorFlag;

public class CardThreshold {
	
	@SerializedName("m_ColorFlags")
	ColorFlag colorFlags;
	@SerializedName("m_ThresholdColorRequirement")
	int thresholdColorRequirement;

}
