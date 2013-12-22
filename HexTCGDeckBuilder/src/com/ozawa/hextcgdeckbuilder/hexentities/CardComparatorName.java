package com.ozawa.hextcgdeckbuilder.hexentities;

import java.util.Comparator;

public class CardComparatorName implements Comparator<Card>{

	@Override
	public int compare(Card o1, Card o2) {
		return o1.name.compareTo(o2.name);
	}

}
