
package hexentities;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Generated;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import enums.CardType;
import enums.ColorFlag;

@Generated("com.googlecode.jsonschema2pojo")
public class Card {

    @Expose
    private List<hexentities._v> _v = new ArrayList<hexentities._v>();
    @Expose
    private String _t;
    @Expose
    private M_Id m_Id;
    @Expose
    private M_SetId m_SetId;
    @Expose
    private String m_DesignerCardId;
    @Expose
    private String m_Name;
    @Expose
    private Integer m_CardNumber;
    @Expose
    private Integer m_Tradeable;
    @Expose
    private String m_Faction;
    @Expose
    private Integer m_SocketCount;
    @Expose
    private String m_AttributeFlags;
   /* @Expose
    private String m_ColorFlags;*/
    @Expose
    private Integer m_ResourceCost;
    @Expose
    private ResourceThreshold[] m_Threshold;
    @Expose
    private String m_CardImagePath;
    /*@Expose
    private String m_CardType;*/
    @Expose
    private String m_CardSubtype;
    @Expose
    private Integer m_BaseAttackValue;
    @Expose
    private Integer m_BaseHealthValue;
    @Expose
    private String m_FlavorText;
    @Expose
    private String m_CardRarity;
    @Expose
    private M_LineageId m_LineageId;
    @Expose
    private String m_ResourceSymbolImagePath;
    @Expose
    private List<M_CardAbility> m_CardAbilities = new ArrayList<M_CardAbility>();
    @Expose
    private Integer m_VariableCost;
    @Expose
    private Integer m_VariableAttack;
    @Expose
    private Integer m_VariableHealth;
    @Expose
    private M_CardLayoutId m_CardLayoutId;
    @Expose
    private Integer m_Unlimited;
    @Expose
    private Integer m_Unique;
    @Expose
    private String m_ArtistName;
    @Expose
    private String m_GameText;
    @Expose
    private String m_SetIconImagePath;
    @Expose
    private String m_ArtistNotes;
    @Expose
    private List<M_EquipmantSlots> m_EquipmentSlots = new ArrayList<M_EquipmantSlots>();
    @Expose
    private String m_DesignNotes;
    @Expose
    private Object m_OverrideStateEffects;
    @Expose
    private Object m_OverrideEventEffects;
    @Expose
    private Object m_ResourceThresholdGranted;
    @Expose
    private Integer m_CurrentResourcesGranted;
    @Expose
    private Integer m_MaxResourcesGranted;
    @Expose
    private String m_ImplNotes;
    @Expose
    private M_SacrificeTarget m_SacrificeTarget;
    @Expose
    private Integer m_LifeCost;
    @Expose
    private String m_ArtStatus;
    @Expose
    private Integer m_RageValue;
    @Expose
    private M_ExhaustTarget m_ExhaustTarget;
    @Expose
    private M_DefaultLayout m_DefaultLayout;
    @Expose
    private Integer m_HasExtendedLayout;
    @Expose
    private Object m_ExtendedLayout;
    @Expose
    private Integer m_EntersPlayExhausted;
    @Expose
    private String m_ImplStage;
    @Expose
    private M_VoidTarget m_VoidTarget;
    @Expose
    private M_DiscardTarget m_DiscardTarget;
    @Expose
    private M_CounterCostType m_CounterCostType;
    @Expose
    private Integer m_CounterCostAmount;
    @Expose
    private Integer m_CombatDamageMultiplier;
    @Expose
    private Integer m_DesignerNumber;
    @Expose
    private M_ShuffleIntoDeckTarget m_ShuffleIntoDeckTarget;
    
    @SerializedName("m_ColorFlags")
	public ColorFlag[]			colorFlags;
    @SerializedName("m_CardType")
	public CardType[]			cardType;

    public List<hexentities._v> get_v() {
        return _v;
    }

    public void set_v(List<hexentities._v> _v) {
        this._v = _v;
    }

    public String get_t() {
        return _t;
    }

    public void set_t(String _t) {
        this._t = _t;
    }

    public M_Id getM_Id() {
        return m_Id;
    }

    public void setM_Id(M_Id m_Id) {
        this.m_Id = m_Id;
    }

    public M_SetId getM_SetId() {
        return m_SetId;
    }

    public void setM_SetId(M_SetId m_SetId) {
        this.m_SetId = m_SetId;
    }

    public String getM_DesignerCardId() {
        return m_DesignerCardId;
    }

    public void setM_DesignerCardId(String m_DesignerCardId) {
        this.m_DesignerCardId = m_DesignerCardId;
    }

    public String getM_Name() {
        return m_Name;
    }

    public void setM_Name(String m_Name) {
        this.m_Name = m_Name;
    }

    public Integer getM_CardNumber() {
        return m_CardNumber;
    }

    public void setM_CardNumber(Integer m_CardNumber) {
        this.m_CardNumber = m_CardNumber;
    }

    public Integer getM_Tradeable() {
        return m_Tradeable;
    }

    public void setM_Tradeable(Integer m_Tradeable) {
        this.m_Tradeable = m_Tradeable;
    }

    public String getM_Faction() {
        return m_Faction;
    }

    public void setM_Faction(String m_Faction) {
        this.m_Faction = m_Faction;
    }

    public Integer getM_SocketCount() {
        return m_SocketCount;
    }

    public void setM_SocketCount(Integer m_SocketCount) {
        this.m_SocketCount = m_SocketCount;
    }

    public String getM_AttributeFlags() {
        return m_AttributeFlags;
    }

    public void setM_AttributeFlags(String m_AttributeFlags) {
        this.m_AttributeFlags = m_AttributeFlags;
    }

    /*public String getM_ColorFlags() {
        return m_ColorFlags;
    }

    public void setM_ColorFlags(String m_ColorFlags) {
        this.m_ColorFlags = m_ColorFlags;
    }*/

    public Integer getM_ResourceCost() {
        return m_ResourceCost;
    }

    public void setM_ResourceCost(Integer m_ResourceCost) {
        this.m_ResourceCost = m_ResourceCost;
    }

    public ResourceThreshold[] getM_Threshold() {
        return m_Threshold;
    }

    public void setM_Threshold(ResourceThreshold[] m_Threshold) {
        this.m_Threshold = m_Threshold;
    }

    public String getM_CardImagePath() {
        return m_CardImagePath;
    }

    public void setM_CardImagePath(String m_CardImagePath) {
        this.m_CardImagePath = m_CardImagePath;
    }

    /*public String getM_CardType() {
        return m_CardType;
    }

    public void setM_CardType(String m_CardType) {
        this.m_CardType = m_CardType;
    }*/

    public String getM_CardSubtype() {
        return m_CardSubtype;
    }

    public void setM_CardSubtype(String m_CardSubtype) {
        this.m_CardSubtype = m_CardSubtype;
    }

    public Integer getM_BaseAttackValue() {
        return m_BaseAttackValue;
    }

    public void setM_BaseAttackValue(Integer m_BaseAttackValue) {
        this.m_BaseAttackValue = m_BaseAttackValue;
    }

    public Integer getM_BaseHealthValue() {
        return m_BaseHealthValue;
    }

    public void setM_BaseHealthValue(Integer m_BaseHealthValue) {
        this.m_BaseHealthValue = m_BaseHealthValue;
    }

    public String getM_FlavorText() {
        return m_FlavorText;
    }

    public void setM_FlavorText(String m_FlavorText) {
        this.m_FlavorText = m_FlavorText;
    }

    public String getM_CardRarity() {
        return m_CardRarity;
    }

    public void setM_CardRarity(String m_CardRarity) {
        this.m_CardRarity = m_CardRarity;
    }

    public M_LineageId getM_LineageId() {
        return m_LineageId;
    }

    public void setM_LineageId(M_LineageId m_LineageId) {
        this.m_LineageId = m_LineageId;
    }

    public String getM_ResourceSymbolImagePath() {
        return m_ResourceSymbolImagePath;
    }

    public void setM_ResourceSymbolImagePath(String m_ResourceSymbolImagePath) {
        this.m_ResourceSymbolImagePath = m_ResourceSymbolImagePath;
    }

    public List<M_CardAbility> getM_CardAbilities() {
        return m_CardAbilities;
    }

    public void setM_CardAbilities(List<M_CardAbility> m_CardAbilities) {
        this.m_CardAbilities = m_CardAbilities;
    }

    public Integer getM_VariableCost() {
        return m_VariableCost;
    }

    public void setM_VariableCost(Integer m_VariableCost) {
        this.m_VariableCost = m_VariableCost;
    }

    public Integer getM_VariableAttack() {
        return m_VariableAttack;
    }

    public void setM_VariableAttack(Integer m_VariableAttack) {
        this.m_VariableAttack = m_VariableAttack;
    }

    public Integer getM_VariableHealth() {
        return m_VariableHealth;
    }

    public void setM_VariableHealth(Integer m_VariableHealth) {
        this.m_VariableHealth = m_VariableHealth;
    }

    public M_CardLayoutId getM_CardLayoutId() {
        return m_CardLayoutId;
    }

    public void setM_CardLayoutId(M_CardLayoutId m_CardLayoutId) {
        this.m_CardLayoutId = m_CardLayoutId;
    }

    public Integer getM_Unlimited() {
        return m_Unlimited;
    }

    public void setM_Unlimited(Integer m_Unlimited) {
        this.m_Unlimited = m_Unlimited;
    }

    public Integer getM_Unique() {
        return m_Unique;
    }

    public void setM_Unique(Integer m_Unique) {
        this.m_Unique = m_Unique;
    }

    public String getM_ArtistName() {
        return m_ArtistName;
    }

    public void setM_ArtistName(String m_ArtistName) {
        this.m_ArtistName = m_ArtistName;
    }

    public String getM_GameText() {
        return m_GameText;
    }

    public void setM_GameText(String m_GameText) {
        this.m_GameText = m_GameText;
    }

    public String getM_SetIconImagePath() {
        return m_SetIconImagePath;
    }

    public void setM_SetIconImagePath(String m_SetIconImagePath) {
        this.m_SetIconImagePath = m_SetIconImagePath;
    }

    public String getM_ArtistNotes() {
        return m_ArtistNotes;
    }

    public void setM_ArtistNotes(String m_ArtistNotes) {
        this.m_ArtistNotes = m_ArtistNotes;
    }

    public List<M_EquipmantSlots> getM_EquipmentSlots() {
        return m_EquipmentSlots;
    }

    public void setM_EquipmentSlots(List<M_EquipmantSlots> m_EquipmentSlots) {
        this.m_EquipmentSlots = m_EquipmentSlots;
    }

    public String getM_DesignNotes() {
        return m_DesignNotes;
    }

    public void setM_DesignNotes(String m_DesignNotes) {
        this.m_DesignNotes = m_DesignNotes;
    }

    public Object getM_OverrideStateEffects() {
        return m_OverrideStateEffects;
    }

    public void setM_OverrideStateEffects(Object m_OverrideStateEffects) {
        this.m_OverrideStateEffects = m_OverrideStateEffects;
    }

    public Object getM_OverrideEventEffects() {
        return m_OverrideEventEffects;
    }

    public void setM_OverrideEventEffects(Object m_OverrideEventEffects) {
        this.m_OverrideEventEffects = m_OverrideEventEffects;
    }

    public Object getM_ResourceThresholdGranted() {
        return m_ResourceThresholdGranted;
    }

    public void setM_ResourceThresholdGranted(Object m_ResourceThresholdGranted) {
        this.m_ResourceThresholdGranted = m_ResourceThresholdGranted;
    }

    public Integer getM_CurrentResourcesGranted() {
        return m_CurrentResourcesGranted;
    }

    public void setM_CurrentResourcesGranted(Integer m_CurrentResourcesGranted) {
        this.m_CurrentResourcesGranted = m_CurrentResourcesGranted;
    }

    public Integer getM_MaxResourcesGranted() {
        return m_MaxResourcesGranted;
    }

    public void setM_MaxResourcesGranted(Integer m_MaxResourcesGranted) {
        this.m_MaxResourcesGranted = m_MaxResourcesGranted;
    }

    public String getM_ImplNotes() {
        return m_ImplNotes;
    }

    public void setM_ImplNotes(String m_ImplNotes) {
        this.m_ImplNotes = m_ImplNotes;
    }

    public M_SacrificeTarget getM_SacrificeTarget() {
        return m_SacrificeTarget;
    }

    public void setM_SacrificeTarget(M_SacrificeTarget m_SacrificeTarget) {
        this.m_SacrificeTarget = m_SacrificeTarget;
    }

    public Integer getM_LifeCost() {
        return m_LifeCost;
    }

    public void setM_LifeCost(Integer m_LifeCost) {
        this.m_LifeCost = m_LifeCost;
    }

    public String getM_ArtStatus() {
        return m_ArtStatus;
    }

    public void setM_ArtStatus(String m_ArtStatus) {
        this.m_ArtStatus = m_ArtStatus;
    }

    public Integer getM_RageValue() {
        return m_RageValue;
    }

    public void setM_RageValue(Integer m_RageValue) {
        this.m_RageValue = m_RageValue;
    }

    public M_ExhaustTarget getM_ExhaustTarget() {
        return m_ExhaustTarget;
    }

    public void setM_ExhaustTarget(M_ExhaustTarget m_ExhaustTarget) {
        this.m_ExhaustTarget = m_ExhaustTarget;
    }

    public M_DefaultLayout getM_DefaultLayout() {
        return m_DefaultLayout;
    }

    public void setM_DefaultLayout(M_DefaultLayout m_DefaultLayout) {
        this.m_DefaultLayout = m_DefaultLayout;
    }

    public Integer getM_HasExtendedLayout() {
        return m_HasExtendedLayout;
    }

    public void setM_HasExtendedLayout(Integer m_HasExtendedLayout) {
        this.m_HasExtendedLayout = m_HasExtendedLayout;
    }

    public Object getM_ExtendedLayout() {
        return m_ExtendedLayout;
    }

    public void setM_ExtendedLayout(Object m_ExtendedLayout) {
        this.m_ExtendedLayout = m_ExtendedLayout;
    }

    public Integer getM_EntersPlayExhausted() {
        return m_EntersPlayExhausted;
    }

    public void setM_EntersPlayExhausted(Integer m_EntersPlayExhausted) {
        this.m_EntersPlayExhausted = m_EntersPlayExhausted;
    }

    public String getM_ImplStage() {
        return m_ImplStage;
    }

    public void setM_ImplStage(String m_ImplStage) {
        this.m_ImplStage = m_ImplStage;
    }

    public M_VoidTarget getM_VoidTarget() {
        return m_VoidTarget;
    }

    public void setM_VoidTarget(M_VoidTarget m_VoidTarget) {
        this.m_VoidTarget = m_VoidTarget;
    }

    public M_DiscardTarget getM_DiscardTarget() {
        return m_DiscardTarget;
    }

    public void setM_DiscardTarget(M_DiscardTarget m_DiscardTarget) {
        this.m_DiscardTarget = m_DiscardTarget;
    }

    public M_CounterCostType getM_CounterCostType() {
        return m_CounterCostType;
    }

    public void setM_CounterCostType(M_CounterCostType m_CounterCostType) {
        this.m_CounterCostType = m_CounterCostType;
    }

    public Integer getM_CounterCostAmount() {
        return m_CounterCostAmount;
    }

    public void setM_CounterCostAmount(Integer m_CounterCostAmount) {
        this.m_CounterCostAmount = m_CounterCostAmount;
    }

    public Integer getM_CombatDamageMultiplier() {
        return m_CombatDamageMultiplier;
    }

    public void setM_CombatDamageMultiplier(Integer m_CombatDamageMultiplier) {
        this.m_CombatDamageMultiplier = m_CombatDamageMultiplier;
    }

    public Integer getM_DesignerNumber() {
        return m_DesignerNumber;
    }

    public void setM_DesignerNumber(Integer m_DesignerNumber) {
        this.m_DesignerNumber = m_DesignerNumber;
    }

    public M_ShuffleIntoDeckTarget getM_ShuffleIntoDeckTarget() {
        return m_ShuffleIntoDeckTarget;
    }

    public void setM_ShuffleIntoDeckTarget(M_ShuffleIntoDeckTarget m_ShuffleIntoDeckTarget) {
        this.m_ShuffleIntoDeckTarget = m_ShuffleIntoDeckTarget;
    }

}
