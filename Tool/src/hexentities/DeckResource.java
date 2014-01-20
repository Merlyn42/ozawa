package hexentities;

import com.google.gson.annotations.SerializedName;

/**
 * DeckResouces hold Card data for a Deck. The Card's GUID and the number of that Card in the Deck.
 * 
 * @author Chad Kinsella
 */
public class DeckResource {
	
	@SerializedName("m_idTemplate")
	GlobalIdentifier cardID;
	@SerializedName("m_Count")
	int cardCount;

}
