package com.ozawa.hextcgdeckbuilder.filter;

import java.util.Comparator;

import com.ozawa.hextcgdeckbuilder.hexentities.AbstractCard;
import com.ozawa.hextcgdeckbuilder.hexentities.Card;

public class DualComparator implements Comparator<AbstractCard> {
	private Comparator<AbstractCard> primary;
	private Comparator<AbstractCard> secondary;
	

	public DualComparator(Comparator<AbstractCard> primary, Comparator<AbstractCard> secondary) {
		super();
		this.primary = primary;
		this.secondary = secondary;
	}

	@Override
	public int compare(AbstractCard arg0, AbstractCard arg1) {
		int result = primary.compare(arg0, arg1);
		if(result==0){
			result= secondary.compare(arg0, arg1);
		}
		return result;
	}

}
