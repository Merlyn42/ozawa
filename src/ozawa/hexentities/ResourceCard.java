package hexentities;

import com.google.gson.annotations.SerializedName;

public class ResourceCard extends AbstractCard {
	
	@SerializedName("m_ResourceThresholdGranted")
	public String resourceThresholdGranted;
	@SerializedName("m_CurrentResourcesGranted")
	public int currentResourcesGranted;
	@SerializedName("m_MaxResourcesGranted")
	public int maxResourcesGranted;

}
