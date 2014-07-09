package com.ozawa.hextcgdeckbuilder.exporter;

import java.util.Collection;

import com.ozawa.hextcgdeckbuilder.UI.customdeck.Deck;

public interface IDeckFormat {
	
	public String formatDeck(Deck deck);
	
	public String getName();

}
