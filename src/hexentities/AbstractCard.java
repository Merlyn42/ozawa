package hexentities;

import com.google.gson.annotations.SerializedName;

import enums.CardRarity;
import enums.CardType;
import enums.ColorFlag;

/**
 * An Abstract Card used to define common fields for all Hex Cards
 * 
 * @author Chad Kinsella
 */
public abstract class AbstractCard {
	
	@SerializedName("m_Id")
	public GlobalIdentifier id;
	@SerializedName("m_SetId")
	public GlobalIdentifier setID;
	@SerializedName("m_Name")
	public String name;
	@SerializedName("m_CardNumber")
	public int cardNumber;
	@SerializedName("m_ColorFlags")
	public ColorFlag[] colorFlags;
	@SerializedName("m_CardImagePath")
	public String cardImagePath;
	@SerializedName("m_CardType")
	public CardType[] cardType;
	@SerializedName("m_CardSubtype")
	public String cardSubtype;
	@SerializedName("m_CardRarity")
	public CardRarity cardRarity;
	@SerializedName("m_ArtistName")
	public String artistName;
	@SerializedName("m_GameText")
	public String gameText;
	@SerializedName("m_DesignNotes")
	public String designNotes;
	@SerializedName("m_Unlimited")
	public Boolean unlimited;
	@SerializedName("m_Tradeable")
	public Boolean tradeable;
	@SerializedName("m_HasExtendedLayout")
	public Boolean hasExtendedLayout;
	@SerializedName("m_DefaultLayout")
	public CardLayout defaultLayout;
	@SerializedName("m_ExtendedLayout")
	public CardLayout extendedLayout;

}
