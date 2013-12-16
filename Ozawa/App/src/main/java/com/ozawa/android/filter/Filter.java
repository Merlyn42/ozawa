package com.ozawa.android.filter;

import com.ozawa.android.hexentities.AbstractCard;
import com.ozawa.android.hexentities.Card;
import com.ozawa.android.hexentities.ResourceCard;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.List;

import com.ozawa.android.enums.Attribute;
import com.ozawa.android.enums.CardType;
import com.ozawa.android.enums.ColorFlag;

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
		minCost = -1;
		maxCost = -1;
	}

	public String getFilterString() {
		return filterString;
	}

	public void setFilterString(String filterString) {
		this.filterString = filterString;
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

	public boolean isActive(Attribute attribute) {
		return attributes.contains(attribute);
	}

	public boolean isActive(CardType type) {
		return cardTypes.contains(type);
	}

	public boolean isActive(ColorFlag color) {
		return colors.contains(color);
	}

	public ArrayList<AbstractCard> filter(AbstractCard[] cards) {
		return (filter(Arrays.asList(cards)));
	}

	public ArrayList<AbstractCard> filter(List<? extends AbstractCard> cards) {
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
