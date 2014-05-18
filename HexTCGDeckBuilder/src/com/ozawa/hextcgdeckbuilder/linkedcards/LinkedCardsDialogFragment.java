package com.ozawa.hextcgdeckbuilder.linkedcards;

import java.util.ArrayList;

import com.ozawa.hextcgdeckbuilder.FullImageActivity;
import com.ozawa.hextcgdeckbuilder.R;
import com.ozawa.hextcgdeckbuilder.enums.DeckType;
import com.ozawa.hextcgdeckbuilder.hexentities.AbstractCard;
import com.ozawa.hextcgdeckbuilder.hexentities.Card;
import com.ozawa.hextcgdeckbuilder.programstate.HexApplication;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

/**
 * Dialog to show the user all of the linked cards related to the current card
 */

public class LinkedCardsDialogFragment extends DialogFragment {

	private String			cardId;
	private HexApplication	hexApplication;
	private ListView		listView;

	public static LinkedCardsDialogFragment newInstance(String cardId) {
		LinkedCardsDialogFragment dialog = new LinkedCardsDialogFragment();
		dialog.cardId = cardId;

		return dialog;
	}

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		final Dialog dialog = new Dialog(getActivity());
		hexApplication = (HexApplication) getActivity().getApplication();

		dialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		dialog.setContentView(R.layout.popup_linked_cards);

		dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0xaa000000));
		dialog.setTitle("Related Cards");

		final ArrayList<Card> linkedCards = getCardLinkedList(cardId);
		LinkedCardAdapter adapter = new LinkedCardAdapter(getActivity(), linkedCards);

		listView = (ListView) dialog.findViewById(R.id.lvLinkedCards);
		listView.setAdapter(adapter);

		listView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				goToFullImagePage(linkedCards.get(position).getID());
			}
		});

		return dialog;
	}

	/**
	 * Get the linked card list for the given card
	 * 
	 * @param cardId
	 * @return the linked card list for the given card
	 */
	private ArrayList<Card> getCardLinkedList(String cardId) {
		AbstractCard card = hexApplication.getCardLibrary().getCardById(cardId);

		if (card instanceof Card) {
			return ((Card) card).relatedCards;
		}
		return null;
	}

	/**
	 * Open a full image activity for the given card
	 * 
	 * @param cardId
	 */
	private void goToFullImagePage(String cardId) {
		Intent i = new Intent(getActivity(), FullImageActivity.class);
		i.putExtra("cardId", cardId);
		i.putExtra("deckType", DeckType.LINKEDCARD);
		startActivity(i);
	}

}
