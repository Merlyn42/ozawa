package com.ozawa.hextcgdeckbuilder.hexentities;

import com.google.gson.annotations.SerializedName;

/**
 * DeckResouces hold Card data for a Deck. The Card's GUID and the number of that Card in the Deck.
 * 
 * @author Chad Kinsella
 */
public class DeckResource {
	
	@SerializedName("m_idTemplate")
	public GlobalIdentifier cardID;
	@SerializedName("m_Count")
	public int cardCount;

}
