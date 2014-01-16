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
public class RemoveMultipleCardsDialogFragment extends DialogFragment {
	Button removeCards;
	Button cancel;
	DeckUIActivity mainActivity;
	NumberPicker picker;

	AbstractCard card;
	public CustomDeckFragment fragment;

	public RemoveMultipleCardsDialogFragment() {
	}

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		final Dialog dialog = new Dialog(getActivity());

		dialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		dialog.setContentView(R.layout.add_multiple_cards_popup);

		dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0xaa000000));

		removeCards = (Button) dialog.findViewById(R.id.buttonAddCards);
		removeCards.setText("Remove Cards");
		cancel = (Button) dialog.findViewById(R.id.buttonCancelAddCards);
		picker = (NumberPicker) dialog.findViewById(R.id.addCardsNumberPicker);

		dialog.setTitle("Add Multiple Cards");

		picker.setMaxValue(40);
		picker.setMinValue(1);
		picker.setValue(4);

		removeCards.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				v.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);
				removeCardsFromCustomDeck();
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
	private void removeCardsFromCustomDeck() {
		HashMap<AbstractCard, Integer> customDeck = mainActivity.customDeck;
		if (customDeck.get(card) != null) {
			int cardCount = customDeck.get(card);
			if (cardCount > picker.getValue()) {
				customDeck.put(card, customDeck.get(card) - picker.getValue());
			} else {
				customDeck.remove(card);
				mainActivity.customDeckCardList.remove(card);
				fragment.reloadCustomDeckView();
			}
		}
		Toast.makeText(mainActivity.getApplicationContext(),picker.getValue() +" "+ card.name+(picker.getValue()>1?"s":"") + " removed from custom deck.", Toast.LENGTH_SHORT).show();
	}

}
