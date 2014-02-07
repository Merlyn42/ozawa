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

import java.io.IOException;

import com.android.vending.expansion.zipfile.APKExpansionSupport;
import com.android.vending.expansion.zipfile.ZipResourceFile;
import com.ozawa.hextcgdeckbuilder.UI.CardsViewer;
import com.ozawa.hextcgdeckbuilder.UI.customdeck.Deck;
import com.ozawa.hextcgdeckbuilder.json.MasterDeck;

import android.app.Application;

public class HexApplication extends Application {
	
	CardsViewer customDeckViewer;
	CardsViewer cardLibraryViewer;
	Deck cardLibrary;
	Deck customDeck;
	ZipResourceFile expansionFile;
	
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

	public CardsViewer getCustomDeckViewer() {
		if(customDeckViewer==null){
			customDeckViewer = new CardsViewer(getApplicationContext(), getCustomDeck());
		}
		return customDeckViewer;
	}
	
	public CardsViewer getCardLibraryViewer() {
		if(cardLibraryViewer==null){
			cardLibraryViewer = new CardsViewer(getApplicationContext(), getCardLibrary());
		}
		return cardLibraryViewer;
	}
	
	public ZipResourceFile getExpansionFile(){
		if(expansionFile == null){
			try {
				expansionFile = APKExpansionSupport.getAPKExpansionZipFile(this, 1, 0);
			} catch (IOException e) {
				
			}
		}
		
		return expansionFile;
	}

}
