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
import java.util.List;

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
import com.ozawa.hextcgdeckbuilder.linkedcards.LinkedCardsDialogFragment;
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
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.HapticFeedbackConstants;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

public class FullImageActivity extends ActionBarActivity implements GestureOverlayView.OnGesturePerformedListener {

	private GestureLibrary		gesLibrary;
	private int					position;
	private ImageView			imageView;
	public ImageButton			socketGem;
	private int					cardCount;
	private DeckType			deckType;

	// Tutorial
	public static final String	PREFS_NAME	= "FirstLaunchPrefFullscreen";
	private SharedPreferences	mPreferences;

	private AbstractCard		card;
	private Gem					currentGem;
	private HexApplication		hexApplication;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.full_image);
		hexApplication = (HexApplication) getApplication();

		gesLibrary = GestureLibraries.fromRawResource(this, R.raw.gestures);
		if (!gesLibrary.load()) {
			this.finish();
		}

		// get intent data
		Intent i = getIntent();
		position = i.getExtras().getInt("id");
		deckType = (DeckType) i.getExtras().getSerializable("deckType");
		String cardId = i.getExtras().getString("cardId");

		if (cardId != null) {
			card = hexApplication.getCardLibrary().getCardById(cardId);
		} else if (deckType == DeckType.CARDLIBRARY) {
			card = hexApplication.getCardLibraryViewer().getFilteredCardList().get(position);
		} else if (deckType == DeckType.CUSTOMDECK) {
			card = hexApplication.getCustomDeckViewer().getFilteredCardList().get(position);
		} else if (deckType == DeckType.TESTDRAW) {
			card = hexApplication.getTestDrawDeckViewer().getFilteredCardList().get(position);
		}

		if (card instanceof Card && ((Card) card).isSocketable()) {
			setSocketButton(card);
		}

		setCardImage();

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
		if (deckType == DeckType.LINKEDCARD) {
			finish();
		}
	}

	@Override
	public void onGesturePerformed(GestureOverlayView gestureOverlayView, Gesture gesture) {
		ArrayList<Prediction> predictions = gesLibrary.recognize(gesture);
		if (deckType != DeckType.LINKEDCARD) {
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

	/**
	 * Set the card image for the activity
	 */
	private void setCardImage() {
		imageView = (ImageView) findViewById(R.id.full_image_view);
		setImage();
		imageView.setOnLongClickListener(new OnLongClickListener() {

			@Override
			public boolean onLongClick(View v) {
				v.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);
				finish();
				return true;
			}
		});
		if (card instanceof Card) {
			imageView.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					showLinkedCardsPopup(card);
				}
			});
		}
	}

	/**
	 * Set the card image for the imageview
	 */
	private void setImage() {
		// Default to Card Library
		List<AbstractCard> filteredList = hexApplication.getCardLibraryViewer().getFilteredCardList();

		if (deckType == DeckType.CUSTOMDECK) {
			filteredList = hexApplication.getCustomDeckViewer().getFilteredCardList();
			cardCount = hexApplication.getCustomDeckViewer().getFilteredCardList().size();
		} else if (deckType == DeckType.TESTDRAW) {
			filteredList = hexApplication.getTestDrawDeckViewer().getFilteredCardList();
			cardCount = hexApplication.getTestDrawDeckViewer().getFilteredCardList().size();
		}
		// If not viewing a linked card, update the card and count
		if (deckType != DeckType.LINKEDCARD) {
			card = filteredList.get(position);
			cardCount = filteredList.size();
		}

		imageView.setImageBitmap(card.getFullscreenCardBitmap(this));

		if (card instanceof Card && ((Card) card).isSocketable()) {
			setSocketButton(card);
			socketGem.setVisibility(View.VISIBLE);
		} else if (socketGem != null) {
			socketGem.setVisibility(View.INVISIBLE);
		}
	}

	/**
	 * Add the socketgem button to the fullscreen image
	 * 
	 * @param card
	 */
	public void setSocketButton(AbstractCard card) {
		socketGem = (ImageButton) findViewById(R.id.buttonSocketGem);

		CardTemplate template = CardTemplate.findCardTemplate(card, true, CardTemplate.getAllTemplates(this));

		float aspectRatio = (float) HexUtil.getScreenWidth(this) / HexUtil.getScreenHeight(this);
		int width = HexUtil.getScreenWidth(this);
		int height = (int) (width * (1 / HexUtil.round(aspectRatio, 2, BigDecimal.ROUND_HALF_UP)));
		RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams((int) (width * template.socketRatio),
				(int) (width * template.socketRatio));
		Bitmap socketImage = getGemSocketedImage(card, hexApplication);
		socketImage = Bitmap.createScaledBitmap(socketImage, (int) (width * template.socketRatio), (int) (width * template.socketRatio),
				true);
		socketGem.setImageBitmap(socketImage);
		lp.leftMargin = (int) (width - (width / 8.4f));
		lp.topMargin = (int) (height / 3.3f) - ((HexUtil.getScreenHeight(this) - height) / 2);

		socketGem.setLayoutParams(lp);

		setSocketListener();
	}

	/**
	 * Get the socket image for the card
	 * 
	 * @param card
	 * @param hexApplication
	 * @return the socket image for the card
	 */
	private Bitmap getGemSocketedImage(AbstractCard card, HexApplication hexApplication) {
		if (deckType == DeckType.TESTDRAW && !hexApplication.getCustomDeck().getSocketCards().isEmpty()) {
			currentGem = hexApplication.getCustomDeck().getSocketedGemForCard(position, card);
			if (currentGem == null) {
				return BitmapFactory.decodeResource(hexApplication.getResources(), R.drawable.gem_socket_new);
			}
		}

		return Gem.getGemSocketedImage(card, hexApplication, currentGem);
	}

	/**
	 * Set the socket listener
	 */
	private void setSocketListener() {
		socketGem.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				v.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);
				if (deckType == DeckType.CUSTOMDECK) {
					if (hexApplication.getCustomDeck().isUnsavedDeck()) {
						Toast.makeText(getApplicationContext(), "Deck must be saved before socketing cards.", Toast.LENGTH_SHORT).show();
					} else {
						showSocketCardPopup((Card) card);
					}
				} else if (deckType == DeckType.CARDLIBRARY || deckType == DeckType.LINKEDCARD) {
					Toast.makeText(getApplicationContext(), "You must be in the custom deck to socket cards.", Toast.LENGTH_SHORT).show();
				} else if (deckType == DeckType.TESTDRAW) {
					if (currentGem != null) {
						Toast.makeText(getApplicationContext(),
								HexUtil.parseStringAsHexHtml(currentGem.description, getApplicationContext(), 0), Toast.LENGTH_SHORT)
								.show();
					} else {
						Toast.makeText(getApplicationContext(), "No gem socketed.", Toast.LENGTH_SHORT).show();
					}
				}
			}
		});
	}

	/**
	 * Display the Load Deck Dialog
	 */
	private void showSocketCardPopup(Card card) {
		SocketCardDialogFragment socketCardDialog = SocketCardDialogFragment.newInstance(card.id);
		socketCardDialog.show(getSupportFragmentManager(), "Socket Card Popup");
	}

	/**
	 * Display the Linked Cards Dialog
	 * 
	 * @param card
	 */
	private void showLinkedCardsPopup(AbstractCard card) {
		LinkedCardsDialogFragment linkedCardDialog = LinkedCardsDialogFragment.newInstance(card.getID());
		linkedCardDialog.show(getSupportFragmentManager(), "Related Cards");
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
