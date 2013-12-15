package enums;

import com.google.gson.annotations.SerializedName;

public enum Attribute {
	@SerializedName("Unknown")
	UNKNOWN("Unknown"),
	@SerializedName("Flight")
	FLIGHT("Flight"),
	@SerializedName("Defensive")
	DEFENSIVE("Defensive"),
	@SerializedName("Juggernaught")
	JUGGERNAUGHT("Juggernaught"),
	@SerializedName("ForceAttack")
	FORCEATTACK("Force Attack"),
	@SerializedName("CantReadyAutomatically")
	CANTREADYAUTOMATICALLY("Can't Ready Automatically"),
	@SerializedName("CantBlock")
	CANTBLOCK("Can't Block"),
	@SerializedName("SpiritDrain")
	SPIRITDRAIN("Spirit Drain"),
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
	FIRSTSTRIKE("First Strike"),
	@SerializedName("SpellShield")
	SPELLSHIELD("Spell Shield"),
	@SerializedName("Immortal")
	IMMORTAL("Immortal"),
	@SerializedName("AllowYardInspire")
	ALLOWYARDINSPIRE("Allow Yard Inspire"),
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
