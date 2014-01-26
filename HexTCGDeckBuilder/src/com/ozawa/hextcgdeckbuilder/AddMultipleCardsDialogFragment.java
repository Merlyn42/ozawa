package com.ozawa.hextcgdeckbuilder;

import com.ozawa.hextcgdeckbuilder.util.HexUtil;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.HapticFeedbackConstants;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.NumberPicker;

@SuppressLint("NewApi")
public class AddMultipleCardsDialogFragment extends AbstractMultipleCardsDialogFragment {
	NumberPicker	picker;

	public AddMultipleCardsDialogFragment() {
	}

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		final Dialog dialog = new Dialog(getActivity());

		dialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		dialog.setContentView(R.layout.add_multiple_cards_popup);

		dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0xaa000000));

		affirmButton = (Button) dialog.findViewById(R.id.buttonAddCards);
		cancelButton = (Button) dialog.findViewById(R.id.buttonCancelAddCards);
		picker = (NumberPicker) dialog.findViewById(R.id.addCardsNumberPicker);
		dialog.setTitle("Add Multiple Cards");

		picker.setMaxValue(40);
		picker.setMinValue(1);
		picker.setValue(4);

		affirmButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				v.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);
				if (((MasterDeckFragment) fragment).addCardToCustomDeck(position, picker.getValue())) {
					animationArg.repeatCount = picker.getValue() - 1;
					HexUtil.moveImageAnimation(animationArg);
				}
				dialog.dismiss();

			}
		});

		cancelButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				v.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);
				dialog.dismiss();
			}
		});

		return dialog;
	}

}
