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
 * Each card colour.
 * 
 * @author Chad Kinsella
 */
public enum ColorFlag {
	
	@SerializedName("Blood")
	BLOOD("Blood"),
	@SerializedName("Diamond")
	DIAMOND("Diamond"),
	@SerializedName("Ruby")
	RUBY("Ruby"),
	@SerializedName("Sapphire")
	SAPPHIRE("Sapphire"),
	@SerializedName("Wild")
	WILD("Wild"),
	@SerializedName("Colorless")
	COLORLESS("Colorless");
	
	private String color;
	
	private ColorFlag(String color){
		this.color = color;
	}
	
	public String getColorFlag(){
		return color;
	}
}
