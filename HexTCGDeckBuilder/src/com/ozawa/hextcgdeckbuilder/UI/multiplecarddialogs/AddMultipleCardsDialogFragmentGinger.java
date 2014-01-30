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
package com.ozawa.hextcgdeckbuilder.UI.multiplecarddialogs;

import com.ozawa.hextcgdeckbuilder.MasterDeckFragment;
import com.ozawa.hextcgdeckbuilder.R;
import com.ozawa.hextcgdeckbuilder.util.HexUtil;

import android.app.Dialog;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.HapticFeedbackConstants;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class AddMultipleCardsDialogFragmentGinger extends AbstractMultipleCardsDialogFragment {

	EditText	text;

	public AddMultipleCardsDialogFragmentGinger() {
	}

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		final Dialog dialog = new Dialog(getActivity());

		dialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		dialog.setContentView(R.layout.add_multiple_cards_popup_ginger);

		dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0xaa000000));

		affirmButton = (Button) dialog.findViewById(R.id.buttonAddCards);
		cancelButton = (Button) dialog.findViewById(R.id.buttonCancelAddCards);
		text = (EditText) dialog.findViewById(R.id.editText1);

		dialog.setTitle("Add Multiple Cards");

		affirmButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				v.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);
				if (text != null && text.getText() != null && text.getText().toString() != null && text.getText().toString().length() > 0) {
					if (((MasterDeckFragment) fragment).addCardToCustomDeck(position, Integer.parseInt(text.getText().toString()))) {
						animationArg.repeatCount = Integer.parseInt(text.getText().toString()) - 1;
						HexUtil.moveImageAnimation(animationArg);
					}
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
