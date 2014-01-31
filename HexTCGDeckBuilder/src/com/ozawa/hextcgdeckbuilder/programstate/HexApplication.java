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

import java.util.ArrayList;
import java.util.HashMap;

import com.ozawa.hextcgdeckbuilder.UI.CardsViewer;
import com.ozawa.hextcgdeckbuilder.UI.customdeck.CustomDeck;
import com.ozawa.hextcgdeckbuilder.hexentities.AbstractCard;
import com.ozawa.hextcgdeckbuilder.json.MasterDeck;

import android.app.Application;

public class HexApplication extends Application {
	
	CardsViewer customDeckViewer;
	CardsViewer cardLibraryViewer;
	CustomDeck customDeck;
	
	public CustomDeck getCustomDeck(){
		if(customDeck == null){
			customDeck = new CustomDeck(this);
		}
		
		return customDeck;
	}

	public CardsViewer getCustomDeckViewer() {
		if(customDeckViewer==null){
			customDeckViewer = new CardsViewer(getApplicationContext(), new ArrayList<AbstractCard>(), new HashMap<AbstractCard, Integer>());
		}
		return customDeckViewer;
	}
	
	public CardsViewer getCardLibraryViewer() {
		if(cardLibraryViewer==null){
			cardLibraryViewer = new CardsViewer(getApplicationContext(), MasterDeck.getMasterDeck(getApplicationContext()), null);
		}
		return cardLibraryViewer;
	}

}
