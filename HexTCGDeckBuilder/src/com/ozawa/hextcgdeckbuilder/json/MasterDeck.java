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

import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.res.Resources;

import com.ozawa.hextcgdeckbuilder.R;
import com.ozawa.hextcgdeckbuilder.hexentities.AbstractCard;
import com.ozawa.hextcgdeckbuilder.hexentities.Card;
import com.ozawa.hextcgdeckbuilder.hexentities.LinkedCards;

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
				masterDeck = jsonReader.deserializeJSONInputStreamsToCard(getJson(context.getResources()));
				ArrayList<String> names = getNames(masterDeck);
				int max = 0;
				for (AbstractCard card : masterDeck) {
					if (card instanceof Card) {
						parseForLinks((Card) card, masterDeck, names);
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

	private static ArrayList<String> getNames(List<AbstractCard> allCards) {
		ArrayList<String> names = new ArrayList<String>();
		for (AbstractCard card : masterDeck) {
			if (card instanceof Card) {
				names.add(card.name);
			}
		}
		return names;
	}

	private static void parseForLinks(Card card, List<AbstractCard> allCards, ArrayList<String> names) {
		if (card.gameText.contains("<b>")) {
			String delims = "[<>]";
			String[] words = card.gameText.split(delims);
			for (int i = 0; i < words.length; i++) {
				if (words[i].equals("b")) {
					if (checkFullName(words[i + 1], names)) {
						mergeLinkedCards(card, getFullMatchedCard(words[i + 1], allCards));
					} else if(checkPartialName(words[i + 1], names)){
						mergeLinkedCards(card, getPartialMatchedCard(words[i + 1], allCards));
					}
				}
			}
		}
	}

	private static boolean checkFullName(String name, ArrayList<String> names) {
		for (String cardName : names) {
			if (cardName.equals(name)) {
				return true;
			}
		}
		return false;
	}

	private static boolean checkPartialName(String name, ArrayList<String> names) {
		for (String cardName : names) {
			if(cardName.contains(name)){
				return true;
			}
		}
		return false;
	}
	
	private static void mergeLinkedCards(Card card, ArrayList<Card> otherCards) {
		for (Card otherCard : otherCards) {
			if (!card.linkedCards.adjacenyList.contains(otherCard) && !card.name.equals(otherCard.name)) {
				card.linkedCards.adjacenyList.add(otherCard.linkedCards);
			}
			if (!otherCard.linkedCards.adjacenyList.contains(card) && !otherCard.name.equals(card.name)) {
				otherCard.linkedCards.adjacenyList.add(card.linkedCards);
			}
		}
	}

	private static ArrayList<Card> getPartialMatchedCard(String name, List<AbstractCard> allCards) {
		ArrayList<Card> matchedCards = new ArrayList<Card>();
		for (AbstractCard card : allCards) {
			if (card instanceof Card) {
				if (card.name.contains(name)) {
					matchedCards.add((Card) card);
				}
			}
		}
		return matchedCards;
	}
	
	private static ArrayList<Card> getFullMatchedCard(String name, List<AbstractCard> allCards) {
		ArrayList<Card> matchedCards = new ArrayList<Card>();
		for (AbstractCard card : allCards) {
			if (card instanceof Card) {
				if (card.name.equals(name)) {
					matchedCards.add((Card) card);
				}
			}
		}
		return matchedCards;
	}

	private static ArrayList<InputStream> getJson(Resources res) throws IllegalAccessException {
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
