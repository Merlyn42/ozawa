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
package com.ozawa.hextcgdeckbuilder.programstate;

import com.ozawa.hextcgdeckbuilder.UI.CardsViewer;
import com.ozawa.hextcgdeckbuilder.UI.customdeck.Deck;
import com.ozawa.hextcgdeckbuilder.filter.CardComparatorColor;
import com.ozawa.hextcgdeckbuilder.filter.CardComparatorCost;
import com.ozawa.hextcgdeckbuilder.filter.DualComparator;
import com.ozawa.hextcgdeckbuilder.json.MasterDeck;

import android.app.Application;

public class HexApplication extends Application {
	
	CardsViewer customDeckViewer;
	CardsViewer cardLibraryViewer;
	CardsViewer deckTestDrawViewer;
	Deck cardLibrary;
	Deck customDeck;
	Deck testDrawDeck;
	
	public Deck getCustomDeck(){
		if(customDeck == null){
			customDeck = new Deck(this);
		}
		
		return customDeck;
	}
	
	public Deck getCardLibrary(){
		if(cardLibrary == null){
			cardLibrary = new Deck(this);
			cardLibrary.setDeckCardList(MasterDeck.getMasterDeck(this));
		}
		
		return cardLibrary;
	} 
	
	public Deck getTestDrawDeck(){
		if(testDrawDeck == null){
			testDrawDeck = new Deck(this);
		}
		
		return testDrawDeck;
	}

	public CardsViewer getCustomDeckViewer() {
		if(customDeckViewer==null){
			customDeckViewer = new CardsViewer(getApplicationContext(), getCustomDeck());
			customDeckViewer.setComparator(new DualComparator(new CardComparatorColor(), new CardComparatorCost()));
		}
		return customDeckViewer;
	}
	
	public CardsViewer getCardLibraryViewer() {
		if(cardLibraryViewer==null){
			cardLibraryViewer = new CardsViewer(getApplicationContext(), getCardLibrary());
			cardLibraryViewer.setComparator(new DualComparator(new CardComparatorColor(), new CardComparatorCost()));
		}
		return cardLibraryViewer;
	}
	
	public CardsViewer getTestDrawDeckViewer(){
		if(deckTestDrawViewer==null){
			deckTestDrawViewer = new CardsViewer(getApplicationContext(), getTestDrawDeck());
		}
		return deckTestDrawViewer;
	}

}
