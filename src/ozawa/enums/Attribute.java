package ozawa.enums;

import com.google.gson.annotations.SerializedName;

public enum Attribute {
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
