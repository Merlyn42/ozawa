package com.ozawa.hextcgdeckbuilder.UI.exporter;

import android.app.Dialog;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.HapticFeedbackConstants;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.NumberPicker;
import android.widget.Spinner;

import com.ozawa.hextcgdeckbuilder.MasterDeckFragment;
import com.ozawa.hextcgdeckbuilder.R;
import com.ozawa.hextcgdeckbuilder.UI.multiplecarddialogs.AbstractMultipleCardsDialogFragment;
import com.ozawa.hextcgdeckbuilder.util.HexUtil;

public class ExportDeckDialogFragment extends DialogFragment {
	private Button			copyButton;
	private Button			emailButton;
	private Spinner			formatSpinner;

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		final Dialog dialog = new Dialog(getActivity());

		dialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		dialog.setContentView(R.layout.export_deck_popup);

		dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0xaa000000));

		copyButton = (Button) dialog.findViewById(R.id.buttonCopy);
		emailButton = (Button) dialog.findViewById(R.id.buttonEmail);
		View text = dialog.findViewById(R.id.editText1);
		dialog.setTitle("Export deck");

/*		copyButton.setOnClickListener(new OnClickListener() {

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

		emailButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				v.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);
				dialog.dismiss();
			}
		});*/

		return dialog;
	}
}
