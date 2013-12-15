package ozawa;

import java.util.List;

import com.google.gson.annotations.SerializedName;

public class Card {
	@SerializedName("m_Name")
	public String name;
	@SerializedName("m_CardNumber")
	public int cardNumber;
	@SerializedName("m_Faction")
	public String faction;
	@SerializedName("m_SocketCount")
	public int socketCount;
	@SerializedName("m_AttributeFlags")
	public List<Attribute> attributeFlags;
	@SerializedName("m_ColorFlags")
	public String colorFlags;
	@SerializedName("m_ResourceCost")
	public int resourceCost;
	@SerializedName("m_Threshold")
	public ThresholdData[] threshold;
	@SerializedName("m_CardImagePath")
	public String cardImagePath;
	@SerializedName("m_CardType")
	public String cardType;
	@SerializedName("m_BaseAttackValue")
	public String baseAttackValue;
	@SerializedName("m_BaseHealthValue")
	public String baseHealthValue;
	@SerializedName("m_FlavorText")
	public String flavorText;
	@SerializedName("m_CardRarity")
	public String cardRarity;
	@SerializedName("m_ResourceSymbolImagePath")
	public String resourceSymbolImagePath;
	@SerializedName("m_ArtistName")
	public String artistName;
	@SerializedName("m_GameText")
	public String gameText;
	@SerializedName("m_DesignNotes")
	public String designNotes;
	
	public enum Attribute{
		@SerializedName("Unknown")
		UNKNOWN,
		@SerializedName("Flight")
		FLIGHT,
		@SerializedName("Defensive")
		DEFENSIVE,
		@SerializedName("Juggernaught")
		JUGGERNAUGHT,
		@SerializedName("ForceAttack")
		FORCEATTACK,
		@SerializedName("CantReadyAutomatically")
		CANTREADYAUTOMATICALLY,
		@SerializedName("CantBlock")
		CANTBLOCK,
		@SerializedName("SpiritDrain")
		SPIRITDRAIN,
		@SerializedName("Escalation")
		ESCALATION,
		@SerializedName("CantAttack")
		CANTATTACK,
		@SerializedName("Speed")
		SPEED,
		@SerializedName("Steadfast")
		STEADFAST,
		@SerializedName("Inspire")
		INSPIRE,
		@SerializedName("FirstStrike")
		FIRSTSTRIKE,
		@SerializedName("SpellShield")
		SPELLSHIELD,
		@SerializedName("Immortal")
		IMMORTAL,
		@SerializedName("AllowYardInspire")
		ALLOWYARDINSPIRE,
		@SerializedName("Rage")
		RAGE,
		@SerializedName("PreventCombatDamage")
		PREVENTCOMBATDAMAGE,
		@SerializedName("CantBeBlocked")
		CANTBEBLOCKED      
	}
}