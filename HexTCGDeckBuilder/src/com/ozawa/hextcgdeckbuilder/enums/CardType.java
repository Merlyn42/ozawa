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
 * Each different card type.
 */
public enum CardType implements CardEnum {
	
	@SerializedName("Troop")
	TROOP("Troop"),
	@SerializedName("BasicAction")
	BASICACTION("Basic Action"),
	@SerializedName("QuickAction")
	QUICKACTION("Quick Action"),
	@SerializedName("Constant")
	CONSTANT("Constant"),
	@SerializedName("Artifact")
	ARTIFACT("Artifact"),
	@SerializedName("Champion")
	CHAMPION("Champion"),
	@SerializedName("Resource")
	RESOURCE("Resource");
	
	private String cardType;
	
	private CardType(String type){
		this.cardType = type;
	}
	
	@Override
	public String toString(){
		return getCardType();
	}
	
	public String getCardType(){
		return cardType;
	}

}
