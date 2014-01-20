package hexentities;

import com.google.gson.annotations.SerializedName;

/**
 * A Resource Card.
 * 
 * @author Chad Kinsella
 */
public class ResourceCard extends AbstractCard {
	
	@SerializedName("m_CurrentResourcesGranted")
	public int currentResourcesGranted;
	@SerializedName("m_MaxResourcesGranted")
	public int maxResourcesGranted;

}
