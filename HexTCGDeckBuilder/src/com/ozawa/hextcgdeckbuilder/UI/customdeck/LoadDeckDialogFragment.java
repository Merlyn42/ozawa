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
import java.util.List;

import com.ozawa.hextcgdeckbuilder.R;
import com.ozawa.hextcgdeckbuilder.UI.LoadDeckArrayAdapter;
import com.ozawa.hextcgdeckbuilder.database.DatabaseHandler;
import com.ozawa.hextcgdeckbuilder.hexentities.HexDeck;
import com.ozawa.hextcgdeckbuilder.json.MasterDeck;
import com.ozawa.hextcgdeckbuilder.programstate.HexApplication;

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
		List<HexDeck> allDecks = dbHandler.getAllDecks();
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
				HexDeck  deck = (HexDeck) listView.getItemAtPosition(position);
				if(deck != null){
					Deck customDeck = ((HexApplication)getActivity().getApplication()).getCustomDeck();
					if(customDeck.loadDeck(deck.getID(), MasterDeck.getMasterDeck(getActivity()))){
						Toast.makeText(getActivity(), "Loaded Deck successfully." , Toast.LENGTH_LONG).show();
						dialog.dismiss();
					}else{
						Toast.makeText(getActivity(), "Failed to load deck. Please try again." , Toast.LENGTH_LONG).show();
					}
				}
			}

       }); 
	
		return dialog;
	}
}
