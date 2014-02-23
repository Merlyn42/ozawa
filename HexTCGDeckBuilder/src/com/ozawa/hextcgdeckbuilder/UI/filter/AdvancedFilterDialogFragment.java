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
import android.widget.Button;
import android.widget.NumberPicker;
import android.widget.RadioGroup;

import com.ozawa.hextcgdeckbuilder.R;
import com.ozawa.hextcgdeckbuilder.UI.CardsViewer;
import com.ozawa.hextcgdeckbuilder.hexentities.AbstractCard;
import com.ozawa.hextcgdeckbuilder.filter.CardComparatorColor;
import com.ozawa.hextcgdeckbuilder.filter.CardComparatorName;
import com.ozawa.hextcgdeckbuilder.filter.CardComparatorCost;
import com.ozawa.hextcgdeckbuilder.filter.CardComparatorSubtype;
import com.ozawa.hextcgdeckbuilder.filter.CardComparatorType;
import com.ozawa.hextcgdeckbuilder.filter.DualComparator;
import com.ozawa.hextcgdeckbuilder.json.MasterDeck;

@SuppressLint("NewApi")
public class AdvancedFilterDialogFragment extends DialogFragment {
	NumberPicker		minCost;
	NumberPicker		maxCost;
	Button				acceptButton;
	public CardsViewer	cardsViewer;
	RadioGroup			primaryRadioGroup;
	RadioGroup			secondaryRadioGroup;

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		final Dialog dialog = new Dialog(getActivity());
		dialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		dialog.setContentView(R.layout.advanced_filter_popup);

		dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0xaa000000));

		primaryRadioGroup = (RadioGroup) dialog.findViewById(R.id.radioGroupPrimary);
		secondaryRadioGroup = (RadioGroup) dialog.findViewById(R.id.radioGroupSecondary);

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
				if(comparator!=null)cardsViewer.setComparator(comparator);
				dialog.dismiss();
			}
		});

		return dialog;
	}

	private Comparator<AbstractCard> getComparator() {
		Comparator<AbstractCard> result;
		Comparator<AbstractCard> primary = getSingleComparator(primaryRadioGroup);
		Comparator<AbstractCard> secondary = getSingleComparator(secondaryRadioGroup);
		if(primary==null&&secondary==null){
			result = null;
		}else if(secondary==null){
			result=primary;
		}else if(primary==null){
			result=secondary;
		}else{
			result = new DualComparator(primary,secondary);
		}
		return result;
		
	}

	private Comparator<AbstractCard> getSingleComparator(RadioGroup group) {
		Comparator<AbstractCard> comparator = null;
		int id = group.getCheckedRadioButtonId();
		switch (id) {
		case R.id.radioButtonNonePrimary:
		case R.id.radioButtonNoneSecondary:
			break;
		case R.id.radioButtonColourPrimary:
		case R.id.radioButtonColourSecondary:
			comparator = new CardComparatorColor();
			break;
		case R.id.radioButtonCostPrimary:
		case R.id.radioButtonCostSecondary:
			comparator = new CardComparatorCost();
			break;
		case R.id.radioButtonNamePrimary:
		case R.id.radioButtonNameSecondary:
			comparator = new CardComparatorName();
			break;
		case R.id.radioButtonTypePrimary:
		case R.id.radioButtonTypeSecondary:
			comparator = new CardComparatorType();
			break;
		case R.id.radioButtonSubtypePrimary:
		case R.id.radioButtonSubtypeSecondary:
			comparator = new CardComparatorSubtype();
			break;
		default:
			comparator= null;
			break;
		}
		return comparator;

	}

}
