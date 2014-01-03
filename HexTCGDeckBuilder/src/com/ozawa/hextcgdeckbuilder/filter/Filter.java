package com.ozawa.hextcgdeckbuilder.filter;

import com.ozawa.hextcgdeckbuilder.enums.CardEnum;
import com.ozawa.hextcgdeckbuilder.hexentities.AbstractCard;
import com.ozawa.hextcgdeckbuilder.hexentities.Card;
import com.ozawa.hextcgdeckbuilder.hexentities.ResourceCard;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.List;

import com.ozawa.hextcgdeckbuilder.enums.Attribute;
import com.ozawa.hextcgdeckbuilder.enums.CardType;
import com.ozawa.hextcgdeckbuilder.enums.ColorFlag;

public class Filter {
	private String						filterString;
	private final EnumSet<Attribute>	attributes;
	private final EnumSet<ColorFlag>	colors;
	private final EnumSet<CardType>		cardTypes;
	private int							minCost;
	private int							maxCost;
	
	private final static int NUMBEROFCARDTYPES = EnumSet.allOf(CardType.class).size();
	private final static int NUMBEROFCOLORS = EnumSet.allOf(ColorFlag.class).size();

	public Filter() {
		attributes = EnumSet.noneOf(Attribute.class);
		colors = EnumSet.allOf(ColorFlag.class);
		cardTypes = EnumSet.allOf(CardType.class);
        cardTypes.remove(CardType.CHAMPION);
		minCost = -1;
		maxCost = -1;
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

	public List<AbstractCard> filter(List<? extends AbstractCard> cards) {
		ArrayList<AbstractCard> result = new ArrayList<AbstractCard>();
		for (AbstractCard abstractCard : cards) {
			if (filterCard(abstractCard)) {
				result.add(abstractCard);
			}

		}
		return result;
	}

	private boolean filterCard(AbstractCard abstractCard) {
		if (filterString!=null&&filterString.length()!=0){
			if(!(abstractCard.gameText.toLowerCase().contains(filterString.toLowerCase())||abstractCard.name.toLowerCase().contains(filterString.toLowerCase())))return false;
		}
		if (colors.size() != NUMBEROFCOLORS) {
			if(!match(abstractCard.colorFlags,colors))return false;
		}
		if (cardTypes.size() != NUMBEROFCARDTYPES) {
			if(!match(abstractCard.cardType,cardTypes))return false;
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
