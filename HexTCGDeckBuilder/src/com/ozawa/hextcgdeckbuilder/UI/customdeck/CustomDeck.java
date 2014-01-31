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
import com.ozawa.hextcgdeckbuilder.hexentities.Deck;
import com.ozawa.hextcgdeckbuilder.hexentities.DeckResource;
import com.ozawa.hextcgdeckbuilder.json.MasterDeck;

public class CustomDeck {
	
	private List<AbstractCard> 				customDeckCardList;	// List of all the cards in the custom deck
    private HashMap<AbstractCard, Integer> 	customDeckData; // Custom deck data of cards, and number of cards
    private Deck 							currentCustomDeck; // The current custom deck
    private boolean 						deckChanged;
    private DatabaseHandler 				dbHandler; // Database handler to save, load, and delete decks
	
	public CustomDeck(Context mContext){
		customDeckData = new HashMap<AbstractCard, Integer>();
		setCustomDeckCardList(new ArrayList<AbstractCard>());
		dbHandler = new DatabaseHandler(mContext);
		deckChanged = false;
	}
	
	/**
	 * Add a card to the custom deck
	 * 
	 * @param card
	 * @param value
	 */
	public void addCardToCustomDeck(AbstractCard card, int value){
		if(customDeckCardList.contains(card)){
			customDeckData.put(card, customDeckData.get(card) + value);
		}else{
			customDeckData.put(card, value);
			customDeckCardList.add(card);
		}
		deckChanged = true;
	}
	
	/**
	 * Remove a card from the custom deck
	 * 
	 * @param card
	 * @return true if the card has been complete removed from custom deck, 
	 * other false if that card type is still in the deck
	 */
	public boolean removeCardFromCustomDeck(AbstractCard card, int value) {
		if (customDeckData.get(card) != null) {
			int cardCount = customDeckData.get(card);
			if (cardCount > value) {
				customDeckData.put(card, customDeckData.get(card) - value);
			} else {
				customDeckData.remove(card);
				customDeckCardList.remove(card);
				return true;
			}
		}
		deckChanged = true;
		return false;
	}
	
	/**
	 * Get the custom deck card list
	 * 
	 * @return the list of all cards in the current custom deck
	 */
	public List<AbstractCard> getCustomDeckCardList(){
		return this.customDeckCardList;
	}
	
	/**
	 * Set the custom deck card list
	 * 
	 * @param deck
	 */
	public void setCustomDeckCardList(List<AbstractCard> deck){
		this.customDeckCardList = deck;
	}
	
	/**
	 * Get the custom deck data
	 * 
	 * @return the data of all cards in the current custom deck and their counts
	 */
	public HashMap<AbstractCard, Integer> getCustomDeckData(){
		return this.customDeckData;
	}
	
	/**
	 * Get the custom deck data
	 * 
	 * @return the data of all cards in the current custom deck and their counts
	 */
	public Deck getCurrentCustomDeck(){
		return this.currentCustomDeck;
	}
	
	/**
	 * Check if the deck has been changed
	 * 
	 * @return true if the deck has been changed since its last save, otherwise false
	 */
	public boolean isDeckChanged(){
		return this.deckChanged;
	}
	
	/**
	 * Clear the Custom Deck Data
	 */
	public void clearCustomDeckData(){
		customDeckData.clear();
	}
	
	/**
	 * Save a new Deck
	 * 
	 * @param deckName
	 * @return The newly saved Deck
	 */
	public Deck saveNewDeck(String deckName) {
		Deck newDeck = new Deck();
		newDeck.name = deckName;

		long newDeckID = dbHandler.addDeck(newDeck);
		if (newDeckID == -1) {
			return null;
		}
		currentCustomDeck = dbHandler.getDeck(String.valueOf(newDeckID));
		deckChanged = false;
		return currentCustomDeck;
	}

	/**
	 * Load a Deck from the database using the given ID
	 * 
	 * @param deckID
	 * @param masterDeck 
	 * @return true if the deck loaded successfully
	 */
	public boolean loadDeck(String deckID, List<AbstractCard> masterDeck) {
		currentCustomDeck = dbHandler.getDeck(deckID);
		updateCustomDeck(currentCustomDeck, masterDeck);
		deckChanged = currentCustomDeck.getID().equalsIgnoreCase(deckID);
		return deckChanged;
	}

	/**
	 * Save the current Deck
	 * 
	 * @return true if the Deck saved successfully, otherwise false
	 */
	public boolean saveDeck() {
		if (currentCustomDeck != null && dbHandler.updateDeck(currentCustomDeck) && dbHandler.updateDeckResources(currentCustomDeck, customDeckData)) {
			deckChanged = false;
			return true;
		}

		return false;
	}

	/**
	 * Delete the current Deck from the database
	 */
	public boolean deleteDeck() {
		if (currentCustomDeck != null && dbHandler.deleteDeck(currentCustomDeck)) {
			resetCustomDeck();
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
		if (saveNewDeck(deckName) != null) {
			return saveDeck();
		}
		return false;
	}
	
	/**
	 * Check if the current deck is saved or not
	 * 
	 * @return true if the current deck has not been saved
	 */
	public boolean isUnsavedDeck(){
		return (currentCustomDeck == null && !customDeckData.isEmpty());
	}
	
	/**
	 * Reset the custom deck
	 * 
	 * Clears the custom deck data, the custom deck card list, and current custom deck
	 */
	private void resetCustomDeck(){
		clearCustomDeckData();
	    setCustomDeckCardList(new ArrayList<AbstractCard>(customDeckData.keySet()));
	    currentCustomDeck = null;
	    deckChanged = false;
	}
	
	/**
	 * Update the custom deck
	 * 
	 * @param deck - the deck that should be the current custom deck
	 * @param mContext 
	 */
	private void updateCustomDeck(Deck deck, List<AbstractCard> masterDeck ){
		clearCustomDeckData();
		
		if(deck.deckResources != null){
			for(DeckResource card : deck.deckResources){
				for(AbstractCard masterCard : masterDeck){
					if(masterCard.getID().contentEquals(card.cardID.gUID)){
						customDeckData.put(masterCard, card.cardCount);
						break;
					}
				}
			}
		}
		
		setCustomDeckCardList(new ArrayList<AbstractCard>(customDeckData.keySet()));
	}
}
