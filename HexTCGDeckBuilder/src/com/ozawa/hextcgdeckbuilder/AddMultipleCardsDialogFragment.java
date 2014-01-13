package com.ozawa.hextcgdeckbuilder;
import java.util.ArrayList;
import java.util.HashMap;

import com.ozawa.hextcgdeckbuilder.NewDeckDialogFragment.NewDeckListener;
import com.ozawa.hextcgdeckbuilder.hexentities.AbstractCard;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.view.HapticFeedbackConstants;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.Toast;


@SuppressLint("NewApi")
public class AddMultipleCardsDialogFragment extends DialogFragment {
	Button addCards;
	Button cancel;
	DeckUIActivity mainActivity;
	NumberPicker picker;
	
	AbstractCard card;
	
	public AddMultipleCardsDialogFragment(){
	}

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		final Dialog dialog = new Dialog(getActivity());

		dialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		dialog.setContentView(R.layout.add_multiple_cards_popup);

		dialog.getWindow().setBackgroundDrawable(
				new ColorDrawable(Color.TRANSPARENT));

		addCards = (Button) dialog.findViewById(R.id.buttonAddCards);
		cancel = (Button) dialog.findViewById(R.id.buttonCancelAddCards);
		picker = (NumberPicker) dialog.findViewById(R.id.addCardsNumberPicker);

		dialog.setTitle("Add Multiple Cards");
		
		picker.setMaxValue(40);
		picker.setMinValue(1);
		picker.setValue(4);
		
		addCards.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				v.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);
				addCardsToCustomDeck();
				dialog.dismiss();
				
			}
		});

		cancel.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				v.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);
				dialog.dismiss();
			}
		});

		return dialog;
	}
	
	/**
	 * Add cards to the custom deck
	 * 
	 * @param card
	 */
	private void addCardsToCustomDeck() {
		{
			if(mainActivity.customDeck.get(card) == null){
				mainActivity.customDeck.put(card, picker.getValue());
				mainActivity.customDeckCardList.add(card);
			}else{
				mainActivity.customDeck.put(card, mainActivity.customDeck.get(card) + picker.getValue());
			}		
			mainActivity.deckChanged = true;
			Toast.makeText(mainActivity.getApplicationContext(), card.name + " added to custom deck.", Toast.LENGTH_SHORT).show();
		}
	}
	

}