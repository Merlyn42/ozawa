package com.ozawa.hextcgdeckbuilder;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.widget.Button;
import com.ozawa.hextcgdeckbuilder.hexentities.AbstractCard;
import com.ozawa.hextcgdeckbuilder.util.HexUtil;

public abstract class AbstractMultipleCardsDialogFragment extends DialogFragment {
	Button						affirmButton;
	Button						cancelButton;
	DeckUIActivity				mainActivity;
	public HexUtil.AnimationArg	animationArg;
	public int					position;

	AbstractCard				card;
	public Fragment				fragment;

	@Override
	public abstract Dialog onCreateDialog(Bundle savedInstanceState);

}
