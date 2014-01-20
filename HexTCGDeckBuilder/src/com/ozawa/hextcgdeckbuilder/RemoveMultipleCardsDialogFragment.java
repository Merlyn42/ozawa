package com.ozawa.hextcgdeckbuilder;

import java.util.ArrayList;
import java.util.HashMap;

import com.ozawa.hextcgdeckbuilder.NewDeckDialogFragment.NewDeckListener;
import com.ozawa.hextcgdeckbuilder.hexentities.AbstractCard;
import com.ozawa.hextcgdeckbuilder.util.HexUtil;

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
	public DeckUIActivity mainActivity;
	NumberPicker picker;
	public HexUtil.AnimationArg animationArg;
	public int position;

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
		HashMap<AbstractCard, Integer> customDeck = mainActivity.customDeck;
		picker.setMaxValue(customDeck.get(card));
		picker.setMinValue(1);
		picker.setValue(customDeck.get(card));

		removeCards.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				v.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);
				int count =picker.getValue();
				animationArg.repeatCount=count-1;
				HexUtil.moveImageAnimation(animationArg);
				dialog.dismiss();
				fragment.removeCardFromCustomDeck(position,count);
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

}
