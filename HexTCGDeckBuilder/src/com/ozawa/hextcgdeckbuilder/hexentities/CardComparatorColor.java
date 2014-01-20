package com.ozawa.hextcgdeckbuilder.hexentities;

import java.util.Comparator;

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
