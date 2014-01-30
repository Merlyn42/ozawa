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

import com.ozawa.hextcgdeckbuilder.DeckUIActivity;
import com.ozawa.hextcgdeckbuilder.R;
import android.app.Dialog;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.HapticFeedbackConstants;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class NewDeckDialogFragment extends DialogFragment {

	Button saveNewDeck;
	Button cancel;
	EditText newDeckName;
	String text = "";
	DeckUIActivity mainActivity;

	public interface NewDeckListener {
		boolean saveNewDeck(String deckName, boolean resetCustomDeck);
	}

	public NewDeckDialogFragment() {
	}

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		final Dialog dialog = new Dialog(getActivity());

		dialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		dialog.setContentView(R.layout.new_deck_popup);

		dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0xaa000000));

		saveNewDeck = (Button) dialog.findViewById(R.id.buttonSaveNewDeck);
		saveNewDeck.setEnabled(false);
		cancel = (Button) dialog.findViewById(R.id.buttonCancelSaveNewDeck);
		newDeckName = (EditText) dialog.findViewById(R.id.etNewDeckName);

		dialog.setTitle("Save New Deck");
		saveNewDeck.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				v.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);
				String deckName = newDeckName.getText().toString();				
				NewDeckListener activity = (NewDeckListener) getActivity();
				
				if (activity.saveNewDeck(deckName, true)) {
					Toast.makeText(getActivity().getApplicationContext(),
							"Deck successfully saved.", Toast.LENGTH_SHORT).show();
					dialog.dismiss();
				} else {
					Toast.makeText(mainActivity.getApplicationContext(), "Failed to save deck. Please try again.", Toast.LENGTH_SHORT).show();
				}				
			}
		});

		cancel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				v.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);
				dialog.dismiss();
			}
		});
		
		newDeckName.addTextChangedListener(getTextWatcher());	

		return dialog;
	}
	
	/**
	 * Add a new TextWatcher to see if the save deck button should be enabled
	 * 
	 * @return a new TextWatcher
	 */
	protected TextWatcher getTextWatcher(){
		return new TextWatcher() {
			@Override
   		    public void onTextChanged(CharSequence cs, int start, int before, int count) {
				if(!newDeckName.getText().toString().trim().isEmpty()){
					saveNewDeck.setEnabled(true);
				}else{
					saveNewDeck.setEnabled(false);
				}
			}

			@Override
			public void afterTextChanged(Editable edit) {				
			}

			@Override
			public void beforeTextChanged(CharSequence cs, int start,
					int before, int count) {				
			}
		};
	}
}
