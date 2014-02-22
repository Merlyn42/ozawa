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

import com.ozawa.hextcgdeckbuilder.enums.CardEnum;
import com.ozawa.hextcgdeckbuilder.hexentities.AbstractCard;
import com.ozawa.hextcgdeckbuilder.hexentities.Card;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.EnumSet;
import java.util.List;
import java.util.Locale;

import com.ozawa.hextcgdeckbuilder.enums.Attribute;
import com.ozawa.hextcgdeckbuilder.enums.CardType;
import com.ozawa.hextcgdeckbuilder.enums.ColorFlag;

public class Filter {
	private String						filterString;
	private final EnumSet<Attribute>	attributes;
	private final EnumSet<ColorFlag>	colors;
	private final EnumSet<CardType>		cardTypes;
	private Comparator<AbstractCard>	comparator;
	
	private final static int NUMBEROFCARDTYPES = EnumSet.allOf(CardType.class).size();
	private final static int NUMBEROFCOLORS = EnumSet.allOf(ColorFlag.class).size();

	public Filter() {
		attributes = EnumSet.noneOf(Attribute.class);
		colors = EnumSet.allOf(ColorFlag.class);
		cardTypes = EnumSet.allOf(CardType.class);
        cardTypes.remove(CardType.CHAMPION);
        comparator = new DualComparator(new CardComparatorColor(), new CardComparatorCost());
	}

	public String getFilterString() {
		return filterString;
	}

	public void setFilterString(String filterString) {
		this.filterString = filterString;
	}

    public void addFilter(CardEnum e){
        if(e instanceof ColorFlag){
            colors.add((ColorFlag)e);
        }else if(e instanceof Attribute){
            attributes.add((Attribute)e);
        }else if(e instanceof CardType){
        	if(e==CardType.TROOP||e==CardType.CONSTANT){
        		cardTypes.add(CardType.ARTIFACT);
        	}
            cardTypes.add((CardType) e);
        }else{
            throw new RuntimeException("Unknown enum type");
        }
    }

    public void removeFilter(CardEnum e){
        if(e instanceof ColorFlag){
            colors.remove((ColorFlag) e);
        }else if(e instanceof Attribute){
            attributes.remove((Attribute) e);
        }else if(e instanceof CardType){
        	if(e==CardType.TROOP&&!isActive(CardType.CONSTANT)||e==CardType.CONSTANT&&!isActive(CardType.TROOP)){
        		cardTypes.remove(CardType.ARTIFACT);
        	}
            cardTypes.remove((CardType) e);
        }else{
            throw new RuntimeException("Unknown enum type");
        }
    }


	public void addAttribute(Attribute attribute) {
		attributes.add(attribute);
	}

	public void removeAttribute(Attribute attribute) {
		attributes.remove(attribute);
	}

	public void addColor(ColorFlag color) {
		colors.add(color);
	}

	public void removeColor(ColorFlag color) {
		colors.remove(color);
	}

	public void addType(CardType type) {
		cardTypes.add(type);
	}

	public void removeType(CardType type) {
		cardTypes.remove(type);
	}

	public boolean isActive(CardEnum e) {
        if(e instanceof ColorFlag){
            return colors.contains(e);
        }else if(e instanceof Attribute){
            return attributes.contains(e);
        }else if(e instanceof CardType){
            return cardTypes.contains(e);
        }else{
            throw new RuntimeException("Unknown enum type");
        }
	}

	public List<AbstractCard> filter(AbstractCard[] cards) {
		return (filter(Arrays.asList(cards)));
	}
	
	public void sort(List<? extends AbstractCard> cards) {
		Collections.sort(cards, comparator);
	}

	public List<AbstractCard> filter(List<? extends AbstractCard> cards) {
		ArrayList<AbstractCard> result = new ArrayList<AbstractCard>();
		for (AbstractCard abstractCard : cards) {
			if (filterCard(abstractCard)) {
				result.add(abstractCard);
			}
		}
		return result;
	}
	
	private boolean searchCardText(AbstractCard card,String searchString){
		String lowercase = searchString.toLowerCase();
		if(card.cardSubtype.toLowerCase().contains(lowercase)){
			return true;
		}
		if(card.gameText.toLowerCase().contains(lowercase)){
			return true;
		}
		if(card.name.toLowerCase().contains(lowercase)){
			return true;
		}
		return false;
	}

	private boolean filterCard(AbstractCard abstractCard) {
		if (filterString!=null&&filterString.length()!=0){
			if(!searchCardText(abstractCard,filterString)){
				return false;
			}
			
		}
		if (cardTypes.size() != NUMBEROFCARDTYPES) {
			if(!match(abstractCard.cardType,cardTypes))return false;
		}
		if (colors.size() != NUMBEROFCOLORS) {
			if(!match(abstractCard.colorFlags,colors)&&(abstractCard.resourceThresholdGranted==null?true:!match(abstractCard.resourceThresholdGranted[0].colorFlags,colors)))return false;
		}

		if (abstractCard instanceof Card) {
			Card card;
			card = (Card) abstractCard;
			if (!attributes.isEmpty()) {
				for (Attribute att : attributes) {
					if (!Arrays.asList(card.attributeFlags).contains(att)) {
						return false;
					}
				}
			}
		}
		return true;
	}
	
	private <T extends Enum<T>> boolean match(T[] cardValues,EnumSet<T> filterValues){
		if (cardValues != null) {
			for (T value : cardValues) {
				if (filterValues.contains(value)) {
					return true;
				}
			}
		}
		return false;
	}

}
