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

public class CardComparatorColor implements Comparator<AbstractCard> {

	@Override
	public int compare(AbstractCard o1, AbstractCard o2) {
		if(o1.colorFlags[0]==null){
			if(o2.colorFlags[0]==null){
				return 0;
			}
			return 1;
		}
		if(o2.colorFlags[0]==null){
			return -1;
		}
		
		return o1.colorFlags[0].compareTo(o2.colorFlags[0]);
		
	}

}
