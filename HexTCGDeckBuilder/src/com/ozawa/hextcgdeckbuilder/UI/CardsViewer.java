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
package com.ozawa.hextcgdeckbuilder.UI;

import android.content.Context;
import com.ozawa.hextcgdeckbuilder.ImageAdapter;
import com.ozawa.hextcgdeckbuilder.UI.customdeck.Deck;
import com.ozawa.hextcgdeckbuilder.enums.CardEnum;
import com.ozawa.hextcgdeckbuilder.filter.Filter;
import com.ozawa.hextcgdeckbuilder.hexentities.AbstractCard;
import com.ozawa.hextcgdeckbuilder.hexentities.CardComparatorColor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

public class CardsViewer {
	private Filter						filter;
	private Deck						deck;
	private ImageAdapter				adapter;
	private Comparator<AbstractCard>	comparator	= new CardComparatorColor();

	public CardsViewer(Context context, Deck deck) {
		filter = new Filter();
		this.deck = deck;
		Collections.sort(deck.getDeckCardList(), comparator);
		adapter = new ImageAdapter(context, this);
	}

	public void clearFilter() {
		filter = new Filter();
		adapter.notifyDataSetChanged();
	}

	public List<AbstractCard> getFilteredCardList() {
		return filter.filter(deck.getDeckCardList());
	}

	public List<AbstractCard> getUnFilteredCardList() {
		return new ArrayList<AbstractCard>(deck.getDeckCardList());
	}

	public String getFilterString() {
		return filter.getFilterString();
	}

	public void setFilterString(String filterString) {
		filter.setFilterString(filterString);
		adapter.notifyDataSetChanged();
	}

	public boolean toggleFilter(CardEnum e) {
		boolean result;
		if (filter.isActive(e)) {
			filter.removeFilter(e);
			result = false;
		} else {
			filter.addFilter(e);
			result = true;
		}
		adapter.notifyDataSetChanged();
		return result;
	}

	public boolean isActive(CardEnum e) {
		return filter.isActive(e);
	}

	public ImageAdapter getAdapter() {
		return adapter;
	}

	public void setAdapter(ImageAdapter adapter) {
		this.adapter = adapter;
		adapter.cardViewer = this;
		adapter.notifyDataSetChanged();
	}

	public void updateDeckAndView() {
		adapter.notifyDataSetChanged();
	}

	public Map<AbstractCard, Integer> getDeckData() {
		return deck.getDeckData();
	}
}
