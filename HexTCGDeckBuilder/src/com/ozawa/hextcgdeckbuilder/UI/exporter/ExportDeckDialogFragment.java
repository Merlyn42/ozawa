package com.ozawa.hextcgdeckbuilder.UI.exporter;

import org.mockito.cglib.transform.TransformingClassGenerator;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.HapticFeedbackConstants;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.Spinner;

import com.ozawa.hextcgdeckbuilder.MasterDeckFragment;
import com.ozawa.hextcgdeckbuilder.R;
import com.ozawa.hextcgdeckbuilder.UI.customdeck.CustomDeckFragment;
import com.ozawa.hextcgdeckbuilder.UI.multiplecarddialogs.AbstractMultipleCardsDialogFragment;
import com.ozawa.hextcgdeckbuilder.database.DatabaseHandler;
import com.ozawa.hextcgdeckbuilder.exporter.IDeckFormat;
import com.ozawa.hextcgdeckbuilder.exporter.RedditDeckFormat;
import com.ozawa.hextcgdeckbuilder.exporter.TCGBrowserFormat;
import com.ozawa.hextcgdeckbuilder.programstate.HexApplication;
import com.ozawa.hextcgdeckbuilder.util.HexUtil;

public class ExportDeckDialogFragment extends DialogFragment {
	private Button		copyButton;
	private Button		emailButton;
	private Spinner		formatSpinner;
	private EditText	text;
	IDeckFormat[]		array;

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		final Dialog dialog = new Dialog(getActivity());
		dialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		dialog.setContentView(R.layout.export_deck_popup);
		dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0xaa000000));
		dialog.setTitle("Export deck");

		copyButton = (Button) dialog.findViewById(R.id.buttonCopy);
		emailButton = (Button) dialog.findViewById(R.id.buttonEmail);
		text = (EditText) dialog.findViewById(R.id.editText1);
		formatSpinner = (Spinner) dialog.findViewById(R.id.spinnerFormat);

		CustomDeckFragment fragment = (CustomDeckFragment) getTargetFragment();
		Context mContext = fragment.mContext;
		DatabaseHandler dbHandler = (mContext instanceof HexApplication) ? ((HexApplication) mContext).getDatabaseHandler()
				: new DatabaseHandler(mContext);

		array = new IDeckFormat[]{ new TCGBrowserFormat(dbHandler,fragment.customDeck), new RedditDeckFormat(dbHandler,fragment.customDeck) };

		ArrayAdapter<IDeckFormat> adapter = new ArrayAdapter<IDeckFormat>(mContext, android.R.layout.simple_list_item_1, array);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		formatSpinner.setAdapter(adapter);
		formatSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
				text.setText(array[position].formatDeck());
			}

			@Override
			public void onNothingSelected(AdapterView<?> parentView) {
				// your code here
			}

		});

		
		 copyButton.setOnClickListener(new OnClickListener() {
		 
		 @Override public void onClick(View v) {
			 Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND); 
			    sharingIntent.setType("text/plain");
			    String shareBody = text.getText().toString();
			    sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Subject Here");
			    sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
			startActivity(Intent.createChooser(sharingIntent, "Share via"));
		 
		 } });
		 /* 
		 * emailButton.setOnClickListener(new OnClickListener() {
		 * 
		 * @Override public void onClick(View v) {
		 * v.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);
		 * dialog.dismiss(); } });
		 */

		return dialog;
	}
}
