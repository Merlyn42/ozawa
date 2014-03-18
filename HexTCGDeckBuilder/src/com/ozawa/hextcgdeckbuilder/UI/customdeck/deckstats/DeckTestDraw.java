package com.ozawa.hextcgdeckbuilder.UI.customdeck.deckstats;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import android.app.Application;

import com.ozawa.hextcgdeckbuilder.UI.customdeck.Deck;
import com.ozawa.hextcgdeckbuilder.database.DatabaseHandler;
import com.ozawa.hextcgdeckbuilder.hexentities.AbstractCard;
import com.ozawa.hextcgdeckbuilder.hexentities.Gem;
import com.ozawa.hextcgdeckbuilder.hexentities.GemResource;
import com.ozawa.hextcgdeckbuilder.programstate.HexApplication;

/**
 * Provides ability to shuffle a deck and simulate card draws
 */

public class DeckTestDraw {

	private List<AbstractCard>	fullDeck;
	private List<AbstractCard>	currentHand;
	private List<SocketedCard>	socketedCards;
	private HexApplication		hexApplication;

	public DeckTestDraw(Application application) {
		this.hexApplication = (HexApplication) application;
		this.fullDeck = createAndShuffleDeck(hexApplication.getCustomDeck());
		this.currentHand = new ArrayList<AbstractCard>();

		if (hexApplication.getCustomDeck().getCurrentDeck() != null) {
			this.socketedCards = getSocketedCardsInDeck(hexApplication.getCustomDeck().getCurrentDeck().getID());
		}
	}

	/**
	 * Draw a new hand of cards
	 * 
	 * @return a list of the first cards of the newly shuffled deck
	 */
	public List<AbstractCard> drawNewHand() {
		currentHand = new ArrayList<AbstractCard>();
		shuffleDeck(fullDeck);
		// If testing a deck that has less than 7 cards.
		if (fullDeck.size() < 7) {
			for (int i = 0; i < fullDeck.size(); i++) {
				currentHand.add(fullDeck.get(i));
			}
		} else {
			for (int i = 0; i < 7; i++) {
				currentHand.add(fullDeck.get(i));
			}
		}
		
		resetSocketedCard(); // Reset the socketed cards
		
		return currentHand;
	}
	

	/**
	 * Mulligan the current hand a draw a new one with one less card
	 * 
	 * @return a new list of cards that is one less than the previous list
	 */
	public List<AbstractCard> mulliganHand() {
		int size = currentHand.size();
		currentHand.clear();

		if (size > 1) {
			shuffleDeck(fullDeck);
			for (int i = 0; i < (size - 1); i++) {
				currentHand.add(fullDeck.get(i));
			}
		}
		
		resetSocketedCard(); // Reset the socketed cards
		
		return currentHand;
	}

	/**
	 * Draw the next card from the deck
	 * 
	 * @return the current list of cards with the new card in the deck added
	 */
	public List<AbstractCard> drawNextCard() {
		if (currentHand.size() < fullDeck.size()) {
			currentHand.add(fullDeck.get(currentHand.size()));
		}

		return currentHand;
	}

	/**
	 * Get the full deck list
	 * 
	 * @return the full deck list for the test draw
	 */
	public List<AbstractCard> getFullDeck() {
		return this.fullDeck;
	}

	/**
	 * Create a new deck and shuffle it
	 * 
	 * @param deck
	 *            - a map of each card in the deck and how many of that card
	 *            there is in the deck
	 * @return a list of cards from the deck data, that has been shuffled
	 */
	private List<AbstractCard> createAndShuffleDeck(Deck deck) {
		HashMap<AbstractCard, Integer> cards = deck.getDeckData();
		ArrayList<AbstractCard> newDeck = new ArrayList<AbstractCard>();
		if (!cards.isEmpty()) {
			for (AbstractCard card : deck.getDeckCardList()) {
				if (cards.containsKey(card)) {
					for (int i = 0; i < cards.get(card); i++) {
						newDeck.add(card);
					}
				}
			}
		}

		if (!newDeck.isEmpty()) {
			shuffleDeck(newDeck);
		}
		return newDeck;
	}

	/**
	 * Shuffles a list of cards
	 * 
	 * @param newDeck
	 *            - the cards to be shuffled
	 */
	private void shuffleDeck(List<AbstractCard> newDeck) {
		Collections.shuffle(newDeck);
	}

	/**
	 * Populate the list of socketed cards for the current deck
	 * 
	 * @param deckId
	 * @return a list of socketed cards for the current deck
	 */
	private List<SocketedCard> getSocketedCardsInDeck(String deckId) {
		DatabaseHandler dbHandler = hexApplication.getDatabaseHandler();
		List<GemResource> gemResources = dbHandler.getAllGemResourcesForDeck(deckId);
		ArrayList<SocketedCard> socketedCards = new ArrayList<SocketedCard>();
		if (!gemResources.isEmpty()) {
			for (GemResource gemResource : gemResources) {
				for (AbstractCard card : fullDeck) {
					if (card.getID().equalsIgnoreCase(gemResource.cardId.gUID)) {
						socketedCards.add(new SocketedCard(card, dbHandler.getGem(gemResource.gemId.gUID)));
						break;
					}
				}
			}
		}
		return socketedCards;
	}

	/**
	 * Return the gem for the card in the given position
	 * 
	 * @param position
	 * @param card
	 * @return the gem for the card in the given position, if one exists,
	 *         otherwise returns null
	 */
	public Gem getSocketedGemForCard(int position, AbstractCard card) {
		SocketedCard socketedCard = getSocketedCard(position);

		if (socketedCard == null) {
			return updateSocketedCard(position, card).gem;
		}

		return socketedCard.gem;
	}

	/**
	 * Update a socketed card with the position if it does not have one
	 * 
	 * @param position
	 * @param card
	 * @return the socketed card with the new position, otherwise returns null
	 */
	private SocketedCard updateSocketedCard(int position, AbstractCard card) {
		for (SocketedCard socketedCard : socketedCards) {
			if (socketedCard.getPostision() == -1 && socketedCard.card.getID().equalsIgnoreCase(card.getID())) {
				socketedCard.setPosition(position);
				return socketedCard;
			}
		}
		return null;
	}

	/**
	 * Get the Socketed Card in the given position
	 * 
	 * @param position
	 * @return the Socketed Card in the given position
	 */
	private SocketedCard getSocketedCard(int position) {
		for (SocketedCard socketedCard : socketedCards) {
			if (socketedCard.getPostision() == position) {
				return socketedCard;
			}
		}

		return null;
	}
	
	/**
	 * Reset the list of socketed cards for the deck by changing the position to -1
	 */
	private void resetSocketedCard() {
		if(!socketedCards.isEmpty()){
			for(SocketedCard socketedCard : socketedCards){
				socketedCard.setPosition(-1);
			}
		}		
	}

	/**
	 * Private class to store socketed card data for cards in the test draw hand
	 */
	private class SocketedCard {

		private int			position;
		public AbstractCard	card;
		public Gem			gem;

		public SocketedCard(AbstractCard card, Gem gem) {
			this.card = card;
			this.gem = gem;
			this.position = -1;
		}

		public int getPostision() {
			return this.position;
		}

		public void setPosition(int position) {
			this.position = position;
		}
	}
}
