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
package com.ozawa.hextcgdeckbuilder;

import java.math.BigDecimal;
import java.util.ArrayList;

import com.espian.showcaseview.ShowcaseView;
import com.espian.showcaseview.ShowcaseView.ConfigOptions;
import com.ozawa.hextcgdeckbuilder.UI.CardTemplate;
import com.ozawa.hextcgdeckbuilder.UI.TutorialEventListener;
import com.ozawa.hextcgdeckbuilder.UI.customdeck.sockets.SocketCardDialogFragment;
import com.ozawa.hextcgdeckbuilder.enums.DeckType;
import com.ozawa.hextcgdeckbuilder.enums.TutorialType;
import com.ozawa.hextcgdeckbuilder.hexentities.AbstractCard;
import com.ozawa.hextcgdeckbuilder.hexentities.Card;
import com.ozawa.hextcgdeckbuilder.hexentities.Gem;
import com.ozawa.hextcgdeckbuilder.linkedcards.LinkedCardAdapter;
import com.ozawa.hextcgdeckbuilder.programstate.HexApplication;
import com.ozawa.hextcgdeckbuilder.util.HexUtil;

import android.content.Intent;
import android.content.SharedPreferences;
import android.gesture.Gesture;
import android.gesture.GestureLibraries;
import android.gesture.GestureLibrary;
import android.gesture.GestureOverlayView;
import android.gesture.Prediction;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.view.HapticFeedbackConstants;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

public class FullImageActivity extends ActionBarActivity implements GestureOverlayView.OnGesturePerformedListener {

	private GestureLibrary			gesLibrary;
	private int						position;
	private ImageView				imageView;
	public ImageButton				socketGem;
	private int						cardCount;
	private DeckType				deckType;

	// Tutorial
	public static final String		PREFS_NAME	= "FirstLaunchPrefFullscreen";
	private SharedPreferences		mPreferences;

	private String[] 				noCards = {"No linked cards"};
	private AbstractCard			card;
	private HexApplication			hexApplication;    
    private ListView 				mLinkedCardList;
    private DrawerLayout 			mDrawerLayout;    
    private ActionBarDrawerToggle 	mDrawerToggle;


	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.full_image);
		hexApplication = (HexApplication) getApplication();
		// get intent data
		Intent i = getIntent();
        mDrawerLayout = (DrawerLayout) findViewById(R.id.linkedcards_drawer_layout);
		gesLibrary = GestureLibraries.fromRawResource(this, R.raw.gestures);
		if (!gesLibrary.load()) {
			this.finish();
		}
		position = i.getExtras().getInt("id");
		deckType = (DeckType) i.getExtras().getSerializable("deckType");
		if (deckType == DeckType.CARDLIBRARY) {
			card = ((HexApplication) getApplication()).getCardLibraryViewer().getFilteredCardList().get(position);
		}else if (deckType == DeckType.CUSTOMDECK) {
			card = ((HexApplication) getApplication()).getCustomDeckViewer().getFilteredCardList().get(position);	
		} else if(deckType == DeckType.TESTDRAW){
			card = ((HexApplication) getApplication()).getTestDrawDeckViewer().getFilteredCardList().get(position);
		} else if(deckType == DeckType.LINKEDCARD){
			DeckType subDeckType = (DeckType)i.getExtras().getSerializable("subDeckType");
			int subPosition = i.getExtras().getInt("subPosition");
			if (subDeckType == DeckType.CARDLIBRARY) {
				card = ((Card)(((HexApplication) getApplication()).getCardLibraryViewer().getFilteredCardList().get(subPosition))).linkedCards.adjacenyList.get(position).card;				
			}else if (subDeckType == DeckType.CUSTOMDECK) {
				card = ((Card)(((HexApplication) getApplication()).getCustomDeckViewer().getFilteredCardList().get(subPosition))).linkedCards.adjacenyList.get(position).card;
			} else if(subDeckType == DeckType.TESTDRAW){
				card = ((Card)(((HexApplication) getApplication()).getTestDrawDeckViewer().getFilteredCardList().get(subPosition))).linkedCards.adjacenyList.get(position).card;
			}
		}
		
        // ActionBarDrawerToggle ties together the the proper interactions
        // between the sliding drawer and the action bar app icon
        mDrawerToggle = new ActionBarDrawerToggle(
                this,                  /* host Activity */
                mDrawerLayout,         /* DrawerLayout object */
                R.drawable.ic_drawer,  /* nav drawer image to replace 'Up' caret */
                R.string.drawer_open,  /* "open drawer" description for accessibility */
                R.string.drawer_close  /* "close drawer" description for accessibility */
                ) {
            public void onDrawerClosed(View view) {                
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }

            public void onDrawerOpened(View drawerView) {
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }
        };
        mDrawerLayout.setDrawerListener(mDrawerToggle);
		
		mLinkedCardList = (ListView) findViewById(R.id.left_drawer);
		if (card instanceof Card && ((Card) card).linkedCards.adjacenyList.size() > 0){		 		 
		 mLinkedCardList.setAdapter(new LinkedCardAdapter(this,((Card)card).linkedCards.adjacenyList));		 
		} else {
			mLinkedCardList.setAdapter(new ArrayAdapter<String>(this,R.layout.linked_card_list_item,noCards));
		}
		mLinkedCardList.setTextFilterEnabled(true);
		mLinkedCardList.setOnItemClickListener(new OnItemClickListener() {
	        @Override
	        public void onItemClick(AdapterView<?> parent, View view, int listPosition, long id) {
	            if(card instanceof Card){
					// Sending image id to FullScreenActivity
	            	goToFullImagePage(listPosition);
	            }
	        }
		});
		
	
		
		imageView = (ImageView) findViewById(R.id.full_image_view);
		socketGem = (ImageButton) findViewById(R.id.buttonSocketGem);
		setImage();

		imageView.setOnLongClickListener(new OnLongClickListener() {

			@Override
			public boolean onLongClick(View v) {
				v.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);
				finish();
				return true;
			}
		});

		socketGem.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				v.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);
				if (deckType == DeckType.CUSTOMDECK) {
					showSocketCardPopup((Card) card);
				} else {
					Toast.makeText(getApplicationContext(), "You must be in the custom deck to socket cards.", Toast.LENGTH_SHORT).show();
				}
			}
		});

		GestureOverlayView gestureOverlayView = (GestureOverlayView) findViewById(R.id.fullImageGestureOverlayView);
		gestureOverlayView.addOnGesturePerformedListener(this);
		gestureOverlayView.setGestureVisible(false);

		// Tutorial
		mPreferences = getSharedPreferences(PREFS_NAME, 0);
		boolean firstTime = mPreferences.getBoolean("firstTime", true);
		if (firstTime) {
			showTutorial();
		}
	}
	
	private void goToFullImagePage(int listPosition){
		Intent i = new Intent(this, FullImageActivity.class);
		android.util.Log.e("***HEX", "****GOT HERE****");
		i.putExtra("id", listPosition);
		i.putExtra("deckType", DeckType.LINKEDCARD);
		i.putExtra("subPosition", position);
		i.putExtra("subDeckType", DeckType.CARDLIBRARY);								
		startActivity(i);
	}

	/**
	 * Show the app's tutorial
	 */
	@SuppressWarnings("deprecation")
	public void showTutorial() {
		ConfigOptions co = new ShowcaseView.ConfigOptions();
		co.shotType = ShowcaseView.TYPE_ONE_SHOT;
		co.centerText = true;
		co.hideOnClickOutside = true;
		ShowcaseView showcaseView = ShowcaseView.insertShowcaseView(HexUtil.getScreenWidth(this) / 2,
				(int) (HexUtil.getScreenHeight(this) / 15), this, "Fullscreen View",
				"Check out the hi-res on these bad boys. Here is a close up of the card and all it's interesting data.", co);
		showcaseView.setShowcase(ShowcaseView.NONE);
		showcaseView.setOnShowcaseEventListener(new TutorialEventListener(this, co, TutorialType.FULLSCREEN));
		showcaseView.show();
		SharedPreferences.Editor editor = mPreferences.edit();
		editor.putBoolean("firstTime", false);
		editor.commit();
	}

	@Override
	protected void onPause() {
		super.onPause();
		finish();
	}

	@Override
	public void onGesturePerformed(GestureOverlayView gestureOverlayView, Gesture gesture) {
		ArrayList<Prediction> predictions = gesLibrary.recognize(gesture);
		if(deckType != DeckType.LINKEDCARD){
			if (predictions.size() > 0) {
				for (Prediction prediction : predictions) {
					if (prediction.score > 1.0) {
						if (prediction.name.equalsIgnoreCase("swipe right")) {
							if (position > 0) {
								position--;
								setImage();
							} else {
								Toast.makeText(this, "No more cards, Jumping to the end", Toast.LENGTH_SHORT).show();
								position = cardCount - 1;
								setImage();
							}
						} else if (prediction.name.equalsIgnoreCase("swipe left")) {
							if (position < cardCount - 1) {
								position++;
								setImage();
							} else {
								position = 0;
								Toast.makeText(this, "No more cards, Jumping to the start", Toast.LENGTH_SHORT).show();
								setImage();
							}
						} else if (prediction.name.equalsIgnoreCase("anti clockwise") || prediction.name.equalsIgnoreCase("clockwise")) {
							finish();
						}
					}
				}
			}
		}
	}

	private void setImage() {
		if (deckType == DeckType.CARDLIBRARY) {
			card = ((HexApplication) getApplication()).getCardLibraryViewer().getFilteredCardList().get(position);
			imageView.setImageBitmap(card.getFullscreenCardBitmap(this));
			cardCount = ((HexApplication) getApplication()).getCardLibraryViewer().getFilteredCardList().size();
		} else if (deckType == DeckType.CUSTOMDECK) {
			card = ((HexApplication) getApplication()).getCustomDeckViewer().getFilteredCardList().get(position);
			imageView.setImageBitmap(card.getFullscreenCardBitmap(this));
			cardCount = ((HexApplication) getApplication()).getCustomDeckViewer().getFilteredCardList().size();
		} else if(deckType == DeckType.TESTDRAW) {
			card = ((HexApplication) getApplication()).getTestDrawDeckViewer().getFilteredCardList().get(position);
			imageView.setImageBitmap(card.getFullscreenCardBitmap(this));
			cardCount = ((HexApplication) getApplication()).getTestDrawDeckViewer().getFilteredCardList().size();
		} else if(deckType == DeckType.LINKEDCARD){
			
		}
		if (card instanceof Card && ((Card) card).socketCount > 0) {
			setSocketButton(card);
			socketGem.setVisibility(View.VISIBLE);
		} else {
			socketGem.setVisibility(View.INVISIBLE);
		}
		if(card instanceof Card && ((Card)card).linkedCards.adjacenyList.size() > 0){				 
			mLinkedCardList.setAdapter(new LinkedCardAdapter(this,((Card)card).linkedCards.adjacenyList));
		} else{
			mLinkedCardList.setAdapter(new ArrayAdapter<String>(this,R.layout.linked_card_list_item,noCards));
		}
	}

	/**
	 * Add the socketgem button to the fullscreen image
	 * 
	 * @param card
	 */
	private void setSocketButton(AbstractCard card) {
		CardTemplate template = CardTemplate.findCardTemplate(card, true, CardTemplate.getAllTemplates(this));

		float aspectRatio = (float) HexUtil.getScreenWidth(this) / HexUtil.getScreenHeight(this);
		int width = HexUtil.getScreenWidth(this);
		int height = (int) (width * (1 / HexUtil.round(aspectRatio, 2, BigDecimal.ROUND_HALF_UP)));
		RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams((int) (width * template.socketRatio),
				(int) (width * template.socketRatio));
		Bitmap socketImage = Gem.getGemSocketedImage(card, hexApplication, null);
		socketImage = Bitmap.createScaledBitmap(socketImage, (int) (width * template.socketRatio), (int) (width * template.socketRatio),
				true);
		socketGem.setImageBitmap(socketImage);
		lp.leftMargin = (int) (width - (width / 8.4f));
		lp.topMargin = (int) (height / 3.3f) - ((HexUtil.getScreenHeight(this) - height) / 2);
		socketGem.setLayoutParams(lp);
	}

	/**
	 * Display the Load Deck Dialog
	 */
	private void showSocketCardPopup(Card card) {
		SocketCardDialogFragment socketCardDialog = SocketCardDialogFragment.newInstance(card.id);
		socketCardDialog.show(getSupportFragmentManager(), "Socket Card Popup");
	}

	/*
	 * private void setSocketButton(AbstractCard card){ CardTemplate template =
	 * CardTemplate.findCardTemplate(card, true,
	 * CardTemplate.getAllTemplates(this));
	 * 
	 * float aspectRatio = (float) HexUtil.getScreenWidth(this) /
	 * HexUtil.getScreenHeight(this); int width = HexUtil.getScreenWidth(this);
	 * int height = (int) (width * (1 / HexUtil.round(aspectRatio, 2,
	 * BigDecimal.ROUND_HALF_UP))); RelativeLayout.LayoutParams lp = new
	 * RelativeLayout.LayoutParams((int)(width*template.socketRatio),
	 * (int)(width*template.socketRatio)); Bitmap socketImage =
	 * BitmapFactory.decodeResource(getResources(),R.drawable.gem_socket);
	 * socketImage = Bitmap.createScaledBitmap(socketImage,
	 * (int)(width*template.socketRatio), (int)(width*template.socketRatio),
	 * true); socketGem.setImageBitmap(socketImage); lp.leftMargin = (int)
	 * (width - (width / 4.5f)); lp.topMargin = (int) (height / 2.2f) -
	 * ((HexUtil.getScreenHeight(this) - height) / 2);
	 * socketGem.setLayoutParams(lp); }
	 */
}
