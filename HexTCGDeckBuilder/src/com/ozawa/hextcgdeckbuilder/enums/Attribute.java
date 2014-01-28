/*******************************************************************************
 * Hex TCG Deck Builder
 *     Copyright ( C ) 2014  Chad Kinsella, Dave Kerr and Laurence Reading
 * 
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 * 
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 * 
 *     You should have received a copy of the GNU General Public License
 *     along with this program.  If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/
package com.ozawa.hextcgdeckbuilder.enums;

import com.google.gson.annotations.SerializedName;

/**
 * Each Attribute for a non-resource card.
 */
public enum Attribute implements CardEnum {
	
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
