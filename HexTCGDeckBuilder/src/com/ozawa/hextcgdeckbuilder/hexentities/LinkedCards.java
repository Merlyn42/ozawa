package com.ozawa.hextcgdeckbuilder.hexentities;

import java.util.ArrayList;

public class LinkedCards {

	public Card card;
	public ArrayList<LinkedCards> adjacenyList;
	
	public LinkedCards(Card card){
		this.card = card;
		this.adjacenyList = new ArrayList<LinkedCards>();
	}
}
