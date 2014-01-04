package com.ozawa.hextcgdeckbuilder;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
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
	
	public interface NewDeckListener{
		boolean saveNewDeck(String deckName, boolean resetCustomDeck);
	}
	public NewDeckDialogFragment(){}
	
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState){
		final Dialog dialog = new Dialog(getActivity());
		
		dialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		dialog.setContentView(R.layout.new_deck_popup);
		
		dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
		
		saveNewDeck = (Button) dialog.findViewById(R.id.buttonSaveNewDeck);
		cancel = (Button) dialog.findViewById(R.id.buttonCancelSaveNewDeck);
		newDeckName = (EditText) dialog.findViewById(R.id.etNewDeckName);
		
		dialog.setTitle("Save New Deck");
		saveNewDeck.setOnClickListener(new OnClickListener() {  
			  
			   @Override  
			   public void onClick(View v) {  
					String deckName = newDeckName.getText().toString();
					if(deckName == null || deckName.trim().contentEquals("")){
						Toast.makeText(mainActivity.getApplicationContext(), "Deck Name must not be empty!", Toast.LENGTH_SHORT).show();
					}else{
						NewDeckListener activity = (NewDeckListener) getActivity();
						if(activity.saveNewDeck(deckName, true)){
							Toast.makeText(getActivity().getApplicationContext(), "Deck successfully saved.", Toast.LENGTH_SHORT).show();
							dialog.dismiss();
						}else{
							Toast.makeText(mainActivity.getApplicationContext(), "Failed to save deck. Please try again.", Toast.LENGTH_SHORT).show();
						}
					}
			   }  
			  });
		
		cancel.setOnClickListener(new OnClickListener() {  
			  
			   @Override  
			   public void onClick(View v) {  
				   dialog.dismiss();  
			   }  
			  });
		
		return dialog;
	}
}
