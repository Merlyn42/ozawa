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

public class CardComparatorType implements Comparator<AbstractCard>{

	@Override
	public int compare(AbstractCard o1, AbstractCard o2) {
		int i =0;
		while (true){
			
			if(o1.cardType.length<=i||o1.cardType[i]==null){
				if(o2.cardType.length<=i){
					return 0;
				}else if(o2.colorFlags[i]==null){
					return 0;
				}
				return 1;
			}
			if(o2.cardType[i]==null){
				return -1;
			}
			int value = o1.cardType[i].compareTo(o2.cardType[i]);
			if(value!=0){
				return value;
			}
			i=i+1;
		}
	}
}
