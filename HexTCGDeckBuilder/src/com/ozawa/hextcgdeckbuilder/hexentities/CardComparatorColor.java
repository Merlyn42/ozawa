package com.ozawa.hextcgdeckbuilder.hexentities;

import java.util.Comparator;

public class CardComparatorColor implements Comparator<Card> {

	@Override
	public int compare(Card o1, Card o2) {
		o1.colorFlags[0].compareTo(o2.colorFlags[0]);
		return 0;
	}

}
