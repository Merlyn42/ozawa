package com.ozawa.hextcgdeckbuilder.UI.customdeck.sockets;

import java.util.ArrayList;
import java.util.List;

import com.ozawa.hextcgdeckbuilder.R;
import com.ozawa.hextcgdeckbuilder.UI.SelectChampionArrayAdapter;
import com.ozawa.hextcgdeckbuilder.UI.customdeck.CustomDeckFragment;
import com.ozawa.hextcgdeckbuilder.UI.customdeck.Deck;
import com.ozawa.hextcgdeckbuilder.database.DatabaseHandler;
import com.ozawa.hextcgdeckbuilder.hexentities.Champion;
import com.ozawa.hextcgdeckbuilder.hexentities.Gem;
import com.ozawa.hextcgdeckbuilder.hexentities.GemResource;
import com.ozawa.hextcgdeckbuilder.hexentities.GlobalIdentifier;
import com.ozawa.hextcgdeckbuilder.hexentities.HexDeck;
import com.ozawa.hextcgdeckbuilder.programstate.HexApplication;
import com.ozawa.hextcgdeckbuilder.util.HexUtil;

import android.app.Dialog;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.HapticFeedbackConstants;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class SocketCardDialogFragment extends DialogFragment {
	
	private ListView listView;
	private RelativeLayout selectedGemLayout;
	private LinearLayout linearLayout;
	private Deck currentCustomDeck;
	private List<GemResource> currentGemResources;
	private List<Gem> allGems;
	private Gem selectedGemOne;
	private Gem selectedGemTwo;
	private Gem selectedGemThree;
	private Gem selectedGemFour;
	private int selectedSocket;
	
	private GlobalIdentifier cardId;
	
	public static SocketCardDialogFragment newInstance(GlobalIdentifier cardId){
		SocketCardDialogFragment dialog = new SocketCardDialogFragment();		
		dialog.cardId = cardId;
		
		return dialog;
	}
	
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState){
		final Dialog dialog = new Dialog(getActivity());
		DatabaseHandler dbHandler = new DatabaseHandler(getActivity());		
		allGems = dbHandler.allGems;
		currentCustomDeck = ((HexApplication)getActivity().getApplication()).getCustomDeck();
		currentGemResources = ((HexApplication)getActivity().getApplication()).getCustomDeck().getGemResources();
		
		if(!currentGemResources.isEmpty()){
			List<GemResource> currentGemSockets = getGemResourcesForCurrentCard();
			if(!currentGemSockets.isEmpty() && currentGemSockets.size() == 1){
				for(int i = 0; i < currentGemSockets.get(0).gemCount; i++){
					switch(i){
						case 0:{
							selectedGemOne = getGemFromAllGems(currentGemSockets.get(i), allGems);
							break;
						}
						case 1:{
							selectedGemTwo = getGemFromAllGems(currentGemSockets.get(i), allGems);
							break;
						}
						case 2:{
							selectedGemThree = getGemFromAllGems(currentGemSockets.get(i), allGems);
							break;
						}
						case 3:{
							selectedGemFour = getGemFromAllGems(currentGemSockets.get(i), allGems);
							break;
						}
						default:{
							break;
						}
					}
				}
			}
		}else{
		}
		
		dialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		dialog.setContentView(R.layout.popup_socket_card);
		
		dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0xaa000000));
		dialog.setTitle("Socket Card");
		
		SocketCardArrayAdapter adapter = new SocketCardArrayAdapter(getActivity(), R.layout.popup_socket_card, allGems);
		
		selectedGemLayout = (RelativeLayout) dialog.findViewById(R.id.relLayoutSelectedGem);
		selectedSocket = 0;
		final TextView selectedGemName = (TextView) selectedGemLayout.findViewById(R.id.selected_gem_name);
		final TextView selectedGemText = (TextView) selectedGemLayout.findViewById(R.id.selected_gem_text);
		if(selectedGemOne != null){
			selectedGemName.setText(selectedGemOne.name);		
			HexUtil.populateTextViewWithHexHtml(selectedGemText, selectedGemOne.description);
		}
		
		listView = (ListView) dialog.findViewById(R.id.listviewGems);
		listView.setAdapter(adapter);
		
		listView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position,
					long id) {
				Gem gem = (Gem) listView.getItemAtPosition(position);
				if(gem != null){
					selectedGemName.setText(gem.name);
					HexUtil.populateTextViewWithHexHtml(selectedGemText, gem.description);
					
					setGemSocket(selectedSocket, position);
				}
			}

       }); 
		
		linearLayout = (LinearLayout) dialog.findViewById(R.id.linLayoutSelectGemButtons);
		
		
		Button cancel = (Button) linearLayout.findViewById(R.id.buttonCancelSocketGem);
		cancel.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				v.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);
				dialog.dismiss();
			}
		});
		
		Button saveGems = (Button) linearLayout.findViewById(R.id.buttonSaveSelectedGems);
		
		saveGems.setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View v) {
				v.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);
				 if(createGemResourceFromSelectedGems() != null){
					 Toast.makeText(getActivity().getApplicationContext(), "Gems socketed." , Toast.LENGTH_SHORT).show();
					 dialog.dismiss();
				 }else{
					 Toast.makeText(getActivity().getApplicationContext(), "Failed to socket Cards. Please try again." , Toast.LENGTH_SHORT).show();
				 }				
			}
		});
		
		setSocketButtons(dialog, 4);
	
		return dialog;
	}

	protected List<GemResource> createGemResourceFromSelectedGems() {
		currentCustomDeck.removePreviousGemResourcesForCard(cardId);
		
		if(selectedGemOne != null){
			currentCustomDeck.createGemResource(selectedGemOne, cardId);
		}
		
		if(selectedGemTwo != null){
			currentCustomDeck.createGemResource(selectedGemTwo, cardId);
		}
		
		if(selectedGemThree != null){
			currentCustomDeck.createGemResource(selectedGemThree, cardId);
		}
		
		if(selectedGemFour != null){
			currentCustomDeck.createGemResource(selectedGemFour, cardId);
		}
		
		currentGemResources = currentCustomDeck.getGemResources();
		
		return currentGemResources;
	}

	protected void setGemSocket(int selectedSocket, int position) {
		switch(selectedSocket){
			case 1:{
				selectedGemOne = allGems.get(position);
				break;
			}
			case 2:{
				selectedGemTwo = allGems.get(position);
				break;
			}
			case 3:{
				selectedGemThree = allGems.get(position);
				break;
			}
			case 4:{
				selectedGemFour = allGems.get(position);
				break;
			}
			default:{
				break;
			}
		}
		
	}

	private Gem getGemFromAllGems(GemResource gemResource, List<Gem> allGems) {
		for(Gem gem : allGems){
			if(gem.id == gemResource.gemId){
				return gem;
			}
		}
		
		return null;
	}

	private List<GemResource> getGemResourcesForCurrentCard() {
		ArrayList<GemResource> currentResources = new ArrayList<GemResource>();
		
		for(GemResource resource : currentGemResources){
			if(resource.cardId == cardId){
				currentResources.add(resource);
			}
		}
		
		return currentGemResources;
	}
	
	private void setSocketButtons(Dialog dialog, int count){
		ImageButton socketOne = (ImageButton) dialog.findViewById(R.id.button_socket_one);
		socketOne.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				selectedSocket = 1;
			}
		});
		
		ImageButton socketTwo = (ImageButton) dialog.findViewById(R.id.button_socket_two);
		socketTwo.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				selectedSocket = 2;
			}
		});

		ImageButton socketThree = (ImageButton) dialog.findViewById(R.id.button_socket_three);
		socketThree.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				selectedSocket = 3;
			}
		});
		
		ImageButton socketFour = (ImageButton) dialog.findViewById(R.id.button_socket_four);
		socketFour.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				selectedSocket = 4;
			}
		});
	}

}
