package com.ozawa.hextcgdeckbuilder.UI;

import java.util.List;

import com.ozawa.hextcgdeckbuilder.R;
import com.ozawa.hextcgdeckbuilder.hexentities.Deck;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class LoadDeckArrayAdapter extends ArrayAdapter<Deck> {
	
	private List<Deck> decks;
	
	public LoadDeckArrayAdapter(Context context, int resource, List<Deck> decks) {
		super(context, resource, decks);

		this.decks = decks;
	}
	
	public View getView(int position, View convertView, ViewGroup parent){
		View view = convertView;
		
		if (view == null) {
			LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			view = inflater.inflate(R.layout.load_deck_list_row, null);
		}
		
		Deck deck = decks.get(position);
		
		if(deck != null){
			TextView deckName = (TextView) view.findViewById(R.id.deck_name);
			deckName.setText(deck.name);
		}
		
		return view;
	}
}
