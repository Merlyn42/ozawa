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
package com.ozawa.hextcgdeckbuilder.filter;

import java.util.Comparator;

import com.ozawa.hextcgdeckbuilder.hexentities.AbstractCard;
import com.ozawa.hextcgdeckbuilder.hexentities.Card;
import com.ozawa.hextcgdeckbuilder.hexentities.ResourceCard;

public class CardComparatorSubtype implements Comparator<AbstractCard>{

	@Override
	public int compare(AbstractCard card1, AbstractCard card2) {
		int result;
		if(card1 instanceof ResourceCard){
			result = -100;
		}else if(card2 instanceof ResourceCard){
			result=100;
		}else{
			result= ((Card)card1).cardSubtype.compareTo(((Card)card2).cardSubtype);
		}
		return result;
	}

}
