/*******************************************************************************
 * Hex TCG Card Generator
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
package enums;

import com.google.gson.annotations.SerializedName;

/**
 * The rarity of each card.
 * 
 * @author Chad Kinsella
 */
public enum CardRarity {
	
	@SerializedName("Common")
	COMMON("Common"),
	@SerializedName("Uncommon")
	UNCOMMON("Uncommon"),
	@SerializedName("Rare")
	RARE("Rare"),
	@SerializedName("Legendary")
	LEGENDARY("Legendary");
	
	private String rarity;
	
	private CardRarity(String rarity){
		this.rarity = rarity;
	}
	
	public String getCardRarity(){
		return rarity;
	}

}
