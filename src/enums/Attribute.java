package enums;

import com.google.gson.annotations.SerializedName;

/**
 * Each Attribute for a non-resource card.
 * 
 * @author Chad Kinsella
 */
public enum Attribute {
	
    @SerializedName("Unknown")
    UNKNOWN("Unknown"),
    @SerializedName("Flight")
    FLIGHT("Flight"),
    @SerializedName("Defensive")
    DEFENSIVE("Defensive"),
    @SerializedName("Juggernaught")
    CRUSH("Crush"),
    @SerializedName("ForceAttack")
    FORCEATTACK("Force Attack"),
    @SerializedName("CantReadyAutomatically")
    CANTREADYAUTOMATICALLY("Can't Ready Automatically"),
    @SerializedName("CantBlock")
    CANTBLOCK("Can't Block"),
    @SerializedName("SpiritDrain")
    LIFEDRAIN("Lifedrain"),
    @SerializedName("Escalation")
    ESCALATION("Escalation"),
    @SerializedName("CantAttack")
    CANTATTACK("Can't Attack"),
    @SerializedName("Speed")
    SPEED("Speed"),
    @SerializedName("Steadfast")
    STEADFAST("Steadfast"),
    @SerializedName("Inspire")
    INSPIRE("Inspire"),
    @SerializedName("FirstStrike")
    SWIFTSTRIKE("Swiftstrike"),
    @SerializedName("SpellShield")
    SPELLSHIELD("Spellshield"),
    @SerializedName("Immortal")
    IMMORTAL("Immortal"),
    @SerializedName("AllowYardInspire")
    ALLOWYARDINSPIRE("Allow Graveyard Inspire"),
    @SerializedName("Rage")
    RAGE("Rage"),
    @SerializedName("PreventCombatDamage")
    PREVENTCOMBATDAMAGE("Prevent Combat Damage"),
    @SerializedName("CantBeBlocked")
    CANTBEBLOCKED("Can't Be Blocked");
    
    private String attribute;
    
    private Attribute(String attribute){
            this.attribute = attribute;
    }
    
    public String getAttribute(){
            return attribute;
    }
}