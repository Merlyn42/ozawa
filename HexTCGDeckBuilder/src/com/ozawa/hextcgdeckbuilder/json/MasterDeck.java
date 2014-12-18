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
package com.ozawa.hextcgdeckbuilder.json;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.BitmapFactory;

import com.android.vending.expansion.zipfile.ZipResourceFile;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.ozawa.hextcgdeckbuilder.R;
import com.ozawa.hextcgdeckbuilder.hexentities.AbstractCard;
import com.ozawa.hextcgdeckbuilder.hexentities.Card;
import com.ozawa.hextcgdeckbuilder.linkedcards.LinkedCards;
import com.ozawa.hextcgdeckbuilder.util.HexUtil;

public class MasterDeck {
	private static List<AbstractCard>	masterDeck;
	private static Integer				highestCardCost;

	public static Integer getHighestCardCost() {
		return highestCardCost;
	}

	public static List<AbstractCard> getMasterDeck(Context context) {
		if (masterDeck == null) {
			JsonReader jsonReader = new JsonReader(context);
			try {
				masterDeck = jsonReader.deserializeJSONInputStreamsToCard(JsonReader.getJson(context,"cards/hexcard"));				
				int max = 0;
				for (AbstractCard card : masterDeck) {
					if (card instanceof Card) {
						parseForLinks((Card) card, masterDeck);
						if (((Card) card).resourceCost > max) {
							max = ((Card) card).resourceCost;
						}
					}
				}
				highestCardCost = max;
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
		}
		return masterDeck;
	}

	private static Card getCard(String guid) {		
		for (AbstractCard card : masterDeck) {
			if (card instanceof Card && card.getID().equals(guid)) {
				return (Card) card;
			}
		}
		return null;
	}

	private static void parseForLinks(Card card, List<AbstractCard> allCards) {
		ArrayList<Card> relatedCards = new ArrayList<Card>();
		for(String guid : card.relatedCardIDs){
			Card relatedCard = getCard(guid); 
			if(relatedCard != null){
				relatedCards.add(relatedCard);
			}
		}
		card.relatedCards = relatedCards;
	}

	private static ArrayList<InputStream> getJson2(Resources res) throws IllegalAccessException {
		Field[] rawFields = R.raw.class.getFields();
		ArrayList<InputStream> jsonFiles = new ArrayList<InputStream>();

		for (int count = 0; count < rawFields.length; count++) {
			int rid = rawFields[count].getInt(rawFields[count]);
			try {
				String name = res.getResourceName(rid);
				if (name.contains("hexcard")) {
					InputStream inputStream = res.openRawResource(rid);
					if (inputStream != null) {
						jsonFiles.add(inputStream);
					}
				}
			} catch (Exception e) {
			}
		}
		return jsonFiles;
	}
}