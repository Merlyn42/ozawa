package com.ozawa.android.hexentities;

import java.util.Comparator;

public class CardComparatorCost implements Comparator<Card> {

	@Override
	public int compare(Card card1, Card card2) {
		return card1.resourceCost - card2.resourceCost;
	}

}
