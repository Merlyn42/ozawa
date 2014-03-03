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
package com.ozawa.hextcgdeckbuilder.UI.filter;

import java.util.Comparator;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.NumberPicker;
import android.widget.RadioGroup;
import android.widget.Spinner;

import com.ozawa.hextcgdeckbuilder.DeckUIActivity;
import com.ozawa.hextcgdeckbuilder.R;
import com.ozawa.hextcgdeckbuilder.UI.CardsViewer;
import com.ozawa.hextcgdeckbuilder.UI.customdeck.CustomDeckFragment;
import com.ozawa.hextcgdeckbuilder.hexentities.AbstractCard;
import com.ozawa.hextcgdeckbuilder.filter.CardComparatorColor;
import com.ozawa.hextcgdeckbuilder.filter.CardComparatorName;
import com.ozawa.hextcgdeckbuilder.filter.CardComparatorCost;
import com.ozawa.hextcgdeckbuilder.filter.CardComparatorSubtype;
import com.ozawa.hextcgdeckbuilder.filter.CardComparatorThreshold;
import com.ozawa.hextcgdeckbuilder.filter.CardComparatorType;
import com.ozawa.hextcgdeckbuilder.filter.DualComparator;
import com.ozawa.hextcgdeckbuilder.json.MasterDeck;
import com.ozawa.hextcgdeckbuilder.programstate.HexApplication;

@SuppressLint("NewApi")
public class AdvancedFilterDialogFragment extends DialogFragment {
	private NumberPicker	minCost;
	private NumberPicker	maxCost;
	private Button			acceptButton;
	private CardsViewer		cardsViewer;
	private Spinner			primarySpinner;
	private Spinner			secondarySpinner;
	public Boolean			isCustomDeck=null;			// possible values "library" or "custom"

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		final Dialog dialog = new Dialog(getActivity());
		dialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		dialog.setContentView(R.layout.advanced_filter_popup);
		if(isCustomDeck==null){
			isCustomDeck= savedInstanceState.getBoolean("isCustomDeck");
		}
		dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0xaa000000));
		if (isCustomDeck) {
			cardsViewer = ((HexApplication) getActivity().getApplication()).getCustomDeckViewer();
		} else if (!isCustomDeck) {
			cardsViewer = ((HexApplication) getActivity().getApplication()).getCardLibraryViewer();
		}else{
			dialog.dismiss();
			return dialog;
		}

		primarySpinner = (Spinner) dialog.findViewById(R.id.spinnerPrimary);
		secondarySpinner = (Spinner) dialog.findViewById(R.id.spinnerSecondary);

		ArrayAdapter<SortingType> primaryAdapter = new ArrayAdapter<SortingType>(getActivity(), android.R.layout.simple_spinner_item,
				SortingType.values());
		// Specify the layout to use when the list of choices appears
		primaryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		// Apply the adapter to the spinner
		primarySpinner.setAdapter(primaryAdapter);
		primarySpinner.setSelection(0);

		ArrayAdapter<SortingType> secondaryAdapter = new ArrayAdapter<SortingType>(getActivity(), android.R.layout.simple_spinner_item,
				SortingType.values());
		// Specify the layout to use when the list of choices appears
		secondaryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		// Apply the adapter to the spinner
		secondarySpinner.setAdapter(secondaryAdapter);
		secondarySpinner.setSelection(0);

		minCost = (NumberPicker) dialog.findViewById(R.id.numberPickerMinCost);
		maxCost = (NumberPicker) dialog.findViewById(R.id.numberPickerMaxCost);
		Integer max;
		if ((max = MasterDeck.getHighestCardCost()) != null) {
			maxCost.setMaxValue(max);
			minCost.setMaxValue(max);
		}
		maxCost.setValue(cardsViewer.getMaxCost());
		minCost.setValue(cardsViewer.getMinCost());
		maxCost.setWrapSelectorWheel(false);
		minCost.setWrapSelectorWheel(false);

		acceptButton = (Button) dialog.findViewById(R.id.buttonAccept);
		acceptButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				cardsViewer.setMaxCost(maxCost.getValue());
				cardsViewer.setMinCost(minCost.getValue());
				cardsViewer.updateDeckAndView();
				Comparator<AbstractCard> comparator = getComparator();
				if (comparator != null)
					cardsViewer.setComparator(comparator);
				dialog.dismiss();
			}
		});

		return dialog;
	}

	@Override
	public void onSaveInstanceState(Bundle arg0) {
		super.onSaveInstanceState(arg0);
		arg0.putBoolean("deckName", isCustomDeck);
	}

	private Comparator<AbstractCard> getComparator() {
		Comparator<AbstractCard> result;
		Comparator<AbstractCard> primary = getSingleComparator(primarySpinner);
		Comparator<AbstractCard> secondary = getSingleComparator(secondarySpinner);
		if (primary == null && secondary == null) {
			result = null;
		} else if (secondary == null) {
			result = primary;
		} else if (primary == null) {
			result = secondary;
		} else {
			result = new DualComparator(primary, secondary);
		}
		return result;

	}

	private Comparator<AbstractCard> getSingleComparator(Spinner spinner) {
		Comparator<AbstractCard> comparator = null;
		SortingType selected = (SortingType) spinner.getSelectedItem();
		switch (selected) {
		case None:
			break;
		case Colour:
			comparator = new CardComparatorColor();
			break;
		case Cost:
			comparator = new CardComparatorCost();
			break;
		case Name:
			comparator = new CardComparatorName();
			break;
		case Type:
			comparator = new CardComparatorType();
			break;
		case Subtype:
			comparator = new CardComparatorSubtype();
			break;
		case Threshold:
			comparator = new CardComparatorThreshold();
			break;
		default:
			comparator = null;
			break;
		}
		return comparator;

	}

}
