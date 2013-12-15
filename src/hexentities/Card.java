package hexentities;

import com.google.gson.annotations.SerializedName;

import enums.Attribute;

/**
 * A non-resource card.
 * 
 * @author Chad Kinsella
 */
public class Card extends AbstractCard {
	
	@SerializedName("m_Faction")
	public String faction;
	@SerializedName("m_SocketCount")
	public int socketCount;
	@SerializedName("m_AttributeFlags")
	public Attribute[] attributeFlags;
	@SerializedName("m_ResourceCost")
	public int resourceCost;
	@SerializedName("m_Threshold")
	public CardThreshold[] threshold;
	@SerializedName("m_BaseAttackValue")
	public String baseAttackValue;
	@SerializedName("m_BaseHealthValue")
	public String baseHealthValue;
	@SerializedName("m_FlavorText")
	public String flavorText;
	@SerializedName("m_ResourceSymbolImagePath")
	public String resourceSymbolImagePath;
	@SerializedName("m_Unique")
	public Boolean unique;
	@SerializedName("m_EquipmentSlots")
	public GlobalIdentifier[] equipmentSlots;

}
