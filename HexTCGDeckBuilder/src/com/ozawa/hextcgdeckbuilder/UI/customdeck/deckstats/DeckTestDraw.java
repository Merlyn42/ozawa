package com.ozawa.hextcgdeckbuilder.UI.customdeck.deckstats;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import com.ozawa.hextcgdeckbuilder.UI.customdeck.Deck;
import com.ozawa.hextcgdeckbuilder.hexentities.AbstractCard;

/**
 * Provides ability to shuffle a deck and simulate card draws 
 */

public class DeckTestDraw {
	
	private List<AbstractCard> fullDeck;
	private List<AbstractCard> currentHand;
	
	public DeckTestDraw(Deck deck){
		this.fullDeck = createAndShuffleDeck(deck);
		this.currentHand = new ArrayList<AbstractCard>();
	}
	
	/**
	 * Draw a new hand of 7 cards
	 * 
	 * @return a list of the first 7 cards of the newly shuffled deck
	 */
	public List<AbstractCard> drawNewHand(){
		currentHand = new ArrayList<AbstractCard>();
		shuffleDeck(fullDeck);
		
		for(int i = 0; i < 7; i++){
			currentHand.add(fullDeck.get(i));
		}		
		
		return currentHand;
	}
	
	/**
	 * Mulligan the current hand a draw a new one with one less card
	 * 
	 * @return a new list of cards that is one less than the previous list
	 */
	public List<AbstractCard> mulliganHand(){	
		int size = currentHand.size();
		currentHand.clear();
		
		if(size > 1){
			shuffleDeck(fullDeck);
			for(int i = 0; i < (size - 1); i++){
				currentHand.add(fullDeck.get(i));
			}
		}
		
		return currentHand;
	}
	
	/**
	 * Draw the next card from the deck
	 * 
	 * @return the current list of cards with the new card in the deck added
	 */
	public List<AbstractCard> drawNextCard(){
		if(currentHand.size() < fullDeck.size()){
			currentHand.add(fullDeck.get(currentHand.size()));
		}		
		
		return currentHand;
	}
	
	/**
	 * Get the full deck list
	 * 
	 * @return the full deck list for the test draw
	 */
	public List<AbstractCard> getFullDeck(){
		return this.fullDeck;
	}
	
	/**
	 * Create a new deck and shuffle it
	 * 
	 * @param deck - a map of each card in the deck and how many of that card there is in the deck
	 * @return a list of cards from the deck data, that has been shuffled
	 */
	private List<AbstractCard> createAndShuffleDeck(Deck deck) {
		HashMap<AbstractCard, Integer> cards = deck.getDeckData();
		ArrayList<AbstractCard> newDeck = new ArrayList<AbstractCard>();
		if(!cards.isEmpty()){
			for(AbstractCard card : deck.getDeckCardList()){
				if(cards.containsKey(card)){
					for(int i = 0; i < cards.get(card); i++){
						newDeck.add(card);
					}
				}
			}
		}
		
		if(!newDeck.isEmpty()){
			shuffleDeck(newDeck);
		}
		return newDeck;
	}
	
	/**
	 * Shuffles a list of cards
	 * 
	 * @param newDeck - the cards to be shuffled
	 */
	private void shuffleDeck(List<AbstractCard> newDeck) {
		Collections.shuffle(newDeck);
	}

}
