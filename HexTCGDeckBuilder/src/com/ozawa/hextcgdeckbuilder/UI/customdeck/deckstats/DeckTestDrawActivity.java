package com.ozawa.hextcgdeckbuilder.UI.customdeck.deckstats;

import com.ozawa.hextcgdeckbuilder.FullImageActivity;
import com.ozawa.hextcgdeckbuilder.ImageAdapter;
import com.ozawa.hextcgdeckbuilder.R;
import com.ozawa.hextcgdeckbuilder.UI.CardsViewer;
import com.ozawa.hextcgdeckbuilder.UI.customdeck.Deck;
import com.ozawa.hextcgdeckbuilder.enums.DeckType;
import com.ozawa.hextcgdeckbuilder.programstate.HexApplication;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;

/**
 * Activity for test drawing hands on a custom deck
 */
public class DeckTestDrawActivity extends Activity {
	
	private ImageAdapter 		imAdapter;
	private CardsViewer			cardViewer;
	private GridView			gridView;
	private Deck				currentHand;
	private DeckTestDraw 		deckTestDraw;
	
	@Override
    protected void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.activity_test_draw);
	    HexApplication hexApp = (HexApplication) this.getApplication();
	    
	    deckTestDraw = new DeckTestDraw(hexApp.getCustomDeck());
	    currentHand = hexApp.getTestDrawDeck();
	    
	    if(cardViewer == null){
	    	cardViewer = hexApp.getTestDrawDeckViewer();
	    }
	    
	    setUpGridView();
	    setUpButtons();
	    
	    TextView title = (TextView) findViewById(R.id.tvTestDrawCurrentDeck);
	    if(hexApp.getCustomDeck().getCurrentDeck() != null){
	    	title.setText("Current Deck: " + hexApp.getCustomDeck().getCurrentDeck().name);
	    }else{
	    	title.setText("Deck not saved yet.");
	    }
	    
	}
	
	/**
	 * Set up the buttons for the test draw functionality
	 */
	private void setUpButtons() {
		Button newHand = (Button) findViewById(R.id.buttonNewHand);
		final Button mulligan = (Button) findViewById(R.id.buttonMulligan);
		final Button drawCard = (Button) findViewById(R.id.buttonDrawCard);		
		Button close = (Button) findViewById(R.id.buttonCloseDeckTestDraw);
		
		// Draw a new hand
		newHand.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				currentHand.setDeckCardList(deckTestDraw.drawNewHand());
				cardViewer.updateDeckAndView();				
				mulligan.setEnabled(true);
				// If the deck has 7 or less cards disable draw cards as we draw all cards at start
				if(deckTestDraw.getFullDeck().size() <= 7){
					drawCard.setEnabled(false);
				}else{
					drawCard.setEnabled(true);
				}
			}
		});
		
		// Mulligan current hand
		mulligan.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				currentHand.setDeckCardList(deckTestDraw.mulliganHand());
				cardViewer.updateDeckAndView();
				if(currentHand.getDeckCardList().size() == 0){
					mulligan.setEnabled(false);
				}
				drawCard.setEnabled(true); // Ensure draw card is enabled if mulligan occurs
			}
		});
		
		drawCard.setEnabled(false); // Disable draw card until a new hand is drawn
		// Draw a card
		drawCard.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				currentHand.setDeckCardList(deckTestDraw.drawNextCard());	
				cardViewer.updateDeckAndView();
				mulligan.setEnabled(false);
				if(currentHand.getDeckCardList().size() == deckTestDraw.getFullDeck().size()){
					drawCard.setEnabled(false);
				}
			}
		});
		
		// Close the test draw activity
		close.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		
	}
	
	/**
	 * Set up the grid view for displaying the cards
	 */
	private void setUpGridView() {
		if (imAdapter == null) {
			imAdapter = new ImageAdapter(this, cardViewer);
		}
		cardViewer.setAdapter(imAdapter);
		gridView = (GridView) findViewById(R.id.gridViewTestDraw);
		gridView.setAdapter(imAdapter);
		gridView.setHapticFeedbackEnabled(true);
		final Context context = this;
		
		// Open up full screen view when a card is clicked
		gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
				Intent i = new Intent(context, FullImageActivity.class);
				i.putExtra("id", position);
				i.putExtra("deckType", DeckType.TESTDRAW);
				startActivity(i);
			}
		});
	}
}
