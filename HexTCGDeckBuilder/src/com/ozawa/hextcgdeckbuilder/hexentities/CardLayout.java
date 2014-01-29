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
package com.ozawa.hextcgdeckbuilder.hexentities;

import com.google.gson.annotations.SerializedName;

/**
 * A layout for a card which gives the relative positions of the card portrait.
 */
public class CardLayout {
	
	@SerializedName("m_Id")
	public GlobalIdentifier id;
	@SerializedName("m_Name")
	public String name;
	@SerializedName("m_PortraitRotation")
	public double portraitRotation;
	@SerializedName("m_PortraitLeft")
	public double portraitLeft;
	@SerializedName("m_PortraitBottom")
	public double portraitBottom;
	@SerializedName("m_PortraitRight")
	public double portraitRight;
	@SerializedName("m_PortraitTop")
	public double portraitTop;

}
