/*******************************************************************************
 * Hex TCG Deck Builder
 *     Copyright ( C ) 2014  Chad Kinsella, Dave Kerr and Laurence Reading
 * 
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 * 
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 * 
 *     You should have received a copy of the GNU General Public License
 *     along with this program.  If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/
package com.ozawa.hextcgdeckbuilder.UI.customdeck;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.content.Context;

import com.ozawa.hextcgdeckbuilder.database.DatabaseHandler;
import com.ozawa.hextcgdeckbuilder.hexentities.AbstractCard;
import com.ozawa.hextcgdeckbuilder.hexentities.Gem;
import com.ozawa.hextcgdeckbuilder.hexentities.GemResource;
import com.ozawa.hextcgdeckbuilder.hexentities.GlobalIdentifier;
import com.ozawa.hextcgdeckbuilder.hexentities.HexDeck;
import com.ozawa.hextcgdeckbuilder.hexentities.DeckResource;

public class Deck {

	private List<AbstractCard>				deckCardList;	// List of all the
															// cards in the
															// deck
	private HashMap<AbstractCard, Integer>	deckData;		// deck data of
															// cards, and number
															// of
															// cards
	private HexDeck							currentDeck;	// The current deck
	private boolean							deckChanged;
	private DatabaseHandler					dbHandler;		// Database handler
															// to save, load,
															// and
															// delete decks

	private List<GemResource>				gemResources;

	public Deck(Context mContext) {
		deckData = new HashMap<AbstractCard, Integer>();
		setDeckCardList(new ArrayList<AbstractCard>());
		setGemResources(new ArrayList<GemResource>());
		dbHandler = new DatabaseHandler(mContext);
		deckChanged = false;
	}

	/**
	 * Add a card to the deck
	 * 
	 * @param card
	 * @param value
	 */
	public void addCardToDeck(AbstractCard card, int value) {
		if (deckCardList.contains(card)) {
			deckData.put(card, deckData.get(card) + value);
		} else {
			deckData.put(card, value);
			deckCardList.add(card);
		}
		deckChanged = true;
	}

	/**
	 * Remove a card from the deck
	 * 
	 * @param card
	 * @return true if the card has been complete removed from deck, other false
	 *         if that card type is still in the deck
	 */
	public boolean removeCardFromDeck(AbstractCard card, int value) {
		if (deckData.get(card) != null) {
			int cardCount = deckData.get(card);
			if (cardCount > value) {
				deckData.put(card, deckData.get(card) - value);
			} else {
				deckData.remove(card);
				deckCardList.remove(card);
				return true;
			}
		}
		deckChanged = true;
		return false;
	}

	/**
	 * Get the deck card list
	 * 
	 * @return the list of all cards in the current deck
	 */
	public List<AbstractCard> getDeckCardList() {
		return this.deckCardList;
	}

	/**
	 * Set the deck card list
	 * 
	 * @param deck
	 */
	public void setDeckCardList(List<AbstractCard> deck) {
		this.deckCardList = deck;
	}

	/**
	 * Get the deck data
	 * 
	 * @return the data of all cards in the current deck and their counts
	 */
	public HashMap<AbstractCard, Integer> getDeckData() {
		return this.deckData;
	}

	/**
	 * Get the deck data
	 * 
	 * @return the data of all cards in the current deck and their counts
	 */
	public HexDeck getCurrentDeck() {
		return this.currentDeck;
	}

	/**
	 * Check if the deck has been changed
	 * 
	 * @return true if the deck has been changed since its last save, otherwise
	 *         false
	 */
	public boolean isDeckChanged() {
		return this.deckChanged;
	}

	/**
	 * Clear the Deck Data
	 */
	public void clearDeckData() {
		deckData.clear();
	}

	/**
	 * Save a new Deck
	 * 
	 * @param deckName
	 * @return The newly saved Deck
	 */
	public HexDeck saveNewDeck(String deckName, boolean resetDeckData) {
		HexDeck newDeck = new HexDeck();
		newDeck.name = deckName;

		long newDeckID = dbHandler.addDeck(newDeck);
		if (newDeckID == -1) {
			return null;
		}
		if (resetDeckData) {
			resetDeck();
		}
		currentDeck = dbHandler.getDeck(String.valueOf(newDeckID));
		deckChanged = false;
		return currentDeck;
	}

	/**
	 * Load a Deck from the database using the given ID
	 * 
	 * @param deckID
	 * @param masterDeck
	 * @return true if the deck loaded successfully
	 */
	public boolean loadDeck(String deckID, List<AbstractCard> masterDeck) {
		currentDeck = dbHandler.getDeck(deckID);
		updateDeck(currentDeck, masterDeck);
		deckChanged = !currentDeck.getID().equalsIgnoreCase(deckID);
		return !deckChanged;
	}

	/**
	 * Save the current Deck
	 * 
	 * @return true if the Deck saved successfully, otherwise false
	 */
	public boolean saveDeck() {
		if (currentDeck != null && dbHandler.updateDeck(currentDeck) && dbHandler.updateDeckResources(currentDeck, deckData)
				&& dbHandler.updateGemResources(gemResources)) {
			deckChanged = false;
			return true;
		}

		return false;
	}

	/**
	 * Delete the current Deck from the database
	 */
	public boolean deleteDeck() {
		if (currentDeck != null && dbHandler.deleteDeck(currentDeck)) {
			resetDeck();
			return true;
		}

		return false;
	}

	/**
	 * Save a new Deck that has not previously been saved
	 * 
	 * @param deckName
	 * @return true if the Deck saved successfully, otherwise false
	 */
	public boolean saveUnsavedDeck(String deckName) {
		if (saveNewDeck(deckName, false) != null) {
			return saveDeck();
		}
		return false;
	}

	/**
	 * Check if the current deck is saved or not
	 * 
	 * @return true if the current deck has not been saved
	 */
	public boolean isUnsavedDeck() {
		return (currentDeck == null && !deckData.isEmpty());
	}

	/**
	 * Get the size of the deck
	 * 
	 * @return the number of cards in the deck.
	 */
	public int getDeckSize() {
		int deckSize = 0;
		for (int value : this.deckData.values()) {
			deckSize += value;
		}

		return deckSize;
	}

	/**
	 * Reset the deck
	 * 
	 * Clears the deck data, the deck card list, and current deck
	 */
	private void resetDeck() {
		clearDeckData();
		setDeckCardList(new ArrayList<AbstractCard>(deckData.keySet()));
		currentDeck = null;
		deckChanged = false;
	}

	/**
	 * Update the deck
	 * 
	 * @param deck
	 *            - the deck that should be the current deck
	 * @param mContext
	 */
	private void updateDeck(HexDeck deck, List<AbstractCard> masterDeck) {
		clearDeckData();

		if (deck.deckResources != null) {
			for (DeckResource card : deck.deckResources) {
				for (AbstractCard masterCard : masterDeck) {
					if (masterCard.getID().contentEquals(card.cardID.gUID)) {
						deckData.put(masterCard, card.cardCount);
						break;
					}
				}
			}
		}

		setDeckCardList(new ArrayList<AbstractCard>(deckData.keySet()));
	}

	/**
	 * Get the GemResources for the current deck
	 * 
	 * @return The GemResources for the current deck
	 */
	public List<GemResource> getGemResources() {
		return this.gemResources;
	}

	/**
	 * Set the GemResources for the current deck
	 * 
	 * @param gemResources
	 */
	public void setGemResources(List<GemResource> gemResources) {
		this.gemResources = gemResources;
	}

	/**
	 * Add a GemResource to the current deck
	 * 
	 * @param gemResource
	 * @return true if the GemResource was added, otherwise false
	 */
	public boolean addGemResource(GemResource gemResource) {
		return this.gemResources.add(gemResource);
	}
	
	public void removePreviousGemResourcesForCard(GlobalIdentifier cardId) {
		for(GemResource gemResource : gemResources){
			if(gemResource.cardId == cardId){
				gemResources.remove(gemResource);
			}
		}
	}

	public void createGemResource(Gem selectedGem, GlobalIdentifier cardId) {
		GemResource existingResource = getGemResourceForCardAndGem(cardId, selectedGem.id);
		if(existingResource != null){
			existingResource.gemCount++;
		}else{
			GemResource gemResource = new GemResource();
			gemResource.gemId = selectedGem.id;
			gemResource.deckId = getCurrentDeck().id;
			gemResource.cardId = cardId;
			gemResource.gemCount = 1;
			
			gemResources.add(gemResource);
		}
	}

	private GemResource getGemResourceForCardAndGem(GlobalIdentifier cardId, GlobalIdentifier gemId) {
		for(GemResource gemResource : gemResources){
			if(gemResource.cardId == cardId && gemResource.gemId == gemId){
				return gemResource;
			}
		}
		
		return null;
	}
}
