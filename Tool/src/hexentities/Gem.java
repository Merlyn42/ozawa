package hexentities;

import com.google.gson.annotations.SerializedName;
import enums.GemType;

/**
 * Gem class used to store gem data for gems socketing cards
 */
public class Gem extends Item {

	@SerializedName("m_BasePrice")
	public int		basePrice;
	@SerializedName("m_GemType")
	public GemType	gemType;

}
