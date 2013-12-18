package hexentities;

import com.google.gson.annotations.SerializedName;

/**
 * A Deck entity which holds an array of DeckResources that correspond to Cards.
 * 
 * @author Chad Kinsella
 */
public class Deck {
	
	@SerializedName("m_MaximumDuplicates")
	public int maximumDuplicates;
	@SerializedName("m_MaximumTotalCards")
	public int maximumTotalCards;
	@SerializedName("m_Id")
	public GlobalIdentifier id;
	@SerializedName("m_DeckName")
	public String name;
	@SerializedName("m_SetId")
	public GlobalIdentifier setID;
	/*@SerializedName("m_DeckResources")
	public DeckResource[] deckResources;*/
	@SerializedName("m_ChampionId")
	public GlobalIdentifier championID;
	@SerializedName("m_DeckResources")
	public AbstractCard[] cards;
	
}
