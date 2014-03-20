package com.ozawa.hextcgdeckbuilder.linkedcards;

import java.util.ArrayList;

import com.ozawa.hextcgdeckbuilder.hexentities.Card;

public class LinkedCards {

	public Card card;
	public ArrayList<LinkedCards> adjacenyList;
	
	public LinkedCards(Card card){
		this.card = card;
		this.adjacenyList = new ArrayList<LinkedCards>();
	}
}
