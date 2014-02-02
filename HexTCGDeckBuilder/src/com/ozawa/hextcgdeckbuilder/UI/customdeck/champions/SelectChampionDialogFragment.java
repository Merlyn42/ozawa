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
package com.ozawa.hextcgdeckbuilder.UI.customdeck.champions;

import java.lang.reflect.Field;
import java.util.List;

import com.ozawa.hextcgdeckbuilder.R;
import com.ozawa.hextcgdeckbuilder.R.drawable;
import com.ozawa.hextcgdeckbuilder.UI.SelectChampionArrayAdapter;
import com.ozawa.hextcgdeckbuilder.UI.customdeck.CustomDeckFragment;
import com.ozawa.hextcgdeckbuilder.database.DatabaseHandler;
import com.ozawa.hextcgdeckbuilder.hexentities.Champion;
import com.ozawa.hextcgdeckbuilder.hexentities.HexDeck;
import com.ozawa.hextcgdeckbuilder.programstate.HexApplication;
import com.ozawa.hextcgdeckbuilder.util.HexUtil;

import android.app.Dialog;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.HapticFeedbackConstants;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class SelectChampionDialogFragment extends DialogFragment {
	
	private ListView listView;
	private RelativeLayout relativeLayout;
	private LinearLayout linearLayout;
	private HexDeck currentCustomDeck;
	private Champion selectedChampion;
	
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState){
		final Dialog dialog = new Dialog(getActivity());
		DatabaseHandler dbHandler = new DatabaseHandler(getActivity());
		final List<Champion> allChampions = dbHandler.allChampions;
		currentCustomDeck = ((HexApplication)getActivity().getApplication()).getCustomDeck().getCurrentDeck();
		
		if(currentCustomDeck != null && currentCustomDeck.champion != null){
			selectedChampion = currentCustomDeck.champion;
		}else{
			selectedChampion = allChampions.get(0);
		}
		
		dialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		dialog.setContentView(R.layout.select_champion_popup);
		
		dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0xaa000000));
		dialog.setTitle("Select Champion");
		
		SelectChampionArrayAdapter adapter = new SelectChampionArrayAdapter(getActivity(), R.layout.select_champion_popup, allChampions);
		
		relativeLayout = (RelativeLayout) dialog.findViewById(R.id.relLayoutChampionSelected);
		
		final TextView selectedChampionName = (TextView) relativeLayout.findViewById(R.id.tvChampionName);
		selectedChampionName.setText(selectedChampion.name);
		final TextView selectedChampionGameText = (TextView) relativeLayout.findViewById(R.id.tvChampionGameText);
		HexUtil.populateTextViewWithHexHtml(selectedChampionGameText,selectedChampion.gameText);
		final ImageView selectedChampionPortrait = (ImageView) relativeLayout.findViewById(R.id.imageChampionPortrait);		
		if(selectedChampion.hudPortrait != null){
			selectedChampionPortrait.setImageResource(getResourceID(selectedChampion.hudPortrait));						
		}else if(selectedChampion.hudPortraitSmall != null){
			selectedChampionPortrait.setImageResource(getResourceID(selectedChampion.hudPortraitSmall));
		}
		
		int portraitDimensions = HexUtil.getScreenWidth(getActivity())/3;
		selectedChampionPortrait.getLayoutParams().width = portraitDimensions;
		selectedChampionPortrait.getLayoutParams().height = (int) (portraitDimensions * 1.22);
		
		listView = (ListView) dialog.findViewById(R.id.championList);
		listView.setAdapter(adapter);
		listView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position,
					long id) {
				Champion champion = (Champion) listView.getItemAtPosition(position);
				if(champion != null){
					selectedChampionName.setText(champion.name);
					HexUtil.populateTextViewWithHexHtml(selectedChampionGameText,champion.gameText);
					
					int championPortraitID = -1;
					if(champion.hudPortrait != null){
						championPortraitID = getResourceID(champion.hudPortrait);						
					}else if(champion.hudPortraitSmall != null){
						championPortraitID = getResourceID(champion.hudPortraitSmall);
					}
					
					if(championPortraitID != -1){
						selectedChampionPortrait.setImageResource(championPortraitID);
					}else{
						selectedChampionPortrait.setImageResource(R.drawable.championnoportait);
					}
					selectedChampion = allChampions.get(position);
				}
			}

       }); 
		
		linearLayout = (LinearLayout) dialog.findViewById(R.id.linLayoutSelectChampButtons);
		
		
		Button cancel = (Button) linearLayout.findViewById(R.id.buttonCancelSelectChampion);
		cancel.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				v.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);
				dialog.dismiss();
			}
		});
		
		Button selectChampion = (Button) linearLayout.findViewById(R.id.buttonSaveSelectedChampion);
		if(currentCustomDeck != null){
		selectChampion.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				v.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);
				currentCustomDeck.champion = selectedChampion;
				 if(currentCustomDeck.champion == selectedChampion){
					 Toast.makeText(getActivity().getApplicationContext(), "Champion selected." , Toast.LENGTH_SHORT).show();
					 ((CustomDeckFragment) getTargetFragment()).updateCustomDeckMenuData();
					 dialog.dismiss();
				 }else{
					 Toast.makeText(getActivity().getApplicationContext(), "Failed to select champion. Please try again." , Toast.LENGTH_SHORT).show();
				 }				
				}
			});
		}else{
			selectChampion.setEnabled(false);
		}
		
		
		
		
		
	
		return dialog;
	}
	
	private int getResourceID(String resourceName){
		int id = -1;
		try {
		    Class<drawable> res = R.drawable.class;
		    Field field = res.getField(resourceName);
		    id = field.getInt(null);
		}
		catch (Exception e) {
			Toast.makeText(getActivity().getApplicationContext(), "Could not find champion portrait." , Toast.LENGTH_SHORT).show();
		}
		
		return id;
	}
}
