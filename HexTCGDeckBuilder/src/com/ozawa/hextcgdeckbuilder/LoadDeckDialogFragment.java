package com.ozawa.hextcgdeckbuilder;

import java.util.ArrayList;
import java.util.List;

import com.ozawa.hextcgdeckbuilder.UI.LoadDeckArrayAdapter;
import com.ozawa.hextcgdeckbuilder.database.DatabaseHandler;
import com.ozawa.hextcgdeckbuilder.hexentities.Deck;

import android.app.Dialog;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.Toast;

public class LoadDeckDialogFragment extends DialogFragment {
	
	private static final String DECK_NAMES = "DECKNAMES";
	private ListView listView;
	public interface LoadDeckListener{
		boolean loadDeck(String deckID);
	}
	
	public static LoadDeckDialogFragment newInstance(ArrayList<String> deckNames){
		LoadDeckDialogFragment dialog = new LoadDeckDialogFragment();
		
		Bundle args = new Bundle();
		args.putStringArrayList(DECK_NAMES, deckNames);
		
		dialog.setArguments(args);
		
		return dialog;
	}
	
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState){
		final Dialog dialog = new Dialog(getActivity());
		DatabaseHandler dbHandler = new DatabaseHandler(getActivity());
		List<Deck> allDecks = dbHandler.getAllDecks();
		dialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		dialog.setContentView(R.layout.load_deck_popup);
		
		dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0xaa000000));
		dialog.setTitle("Load Deck");
		
		LoadDeckArrayAdapter adapter = new LoadDeckArrayAdapter(getActivity(), R.layout.load_deck_popup, allDecks);
		
		listView = (ListView) dialog.findViewById(R.id.loadDeckList);
		listView.setAdapter(adapter);
		
		listView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position,
					long id) {
				Deck  deck = (Deck) listView.getItemAtPosition(position);
				if(deck != null){
					LoadDeckListener activity = (LoadDeckListener) getActivity();
					if(activity.loadDeck(deck.getID())){
						Toast.makeText(getActivity().getApplicationContext(), "Loaded Deck successfully." , Toast.LENGTH_LONG).show();
						dialog.dismiss();
					}else{
						Toast.makeText(getActivity().getApplicationContext(), "Failed to load deck. Please try again." , Toast.LENGTH_LONG).show();
					}
				}
			}

       }); 
	
		return dialog;
	}
}
