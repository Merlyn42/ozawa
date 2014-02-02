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

import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;

import com.espian.showcaseview.ShowcaseView;
import com.espian.showcaseview.ShowcaseView.ConfigOptions;
import com.ozawa.hextcgdeckbuilder.FullImageActivity;
import com.ozawa.hextcgdeckbuilder.ImageAdapter;
import com.ozawa.hextcgdeckbuilder.R;
import com.ozawa.hextcgdeckbuilder.UI.CardsViewer;
import com.ozawa.hextcgdeckbuilder.UI.CustomViewPager;
import com.ozawa.hextcgdeckbuilder.UI.PlaceholderFragment;
import com.ozawa.hextcgdeckbuilder.UI.TutorialEventListener;
import com.ozawa.hextcgdeckbuilder.UI.customdeck.champions.SelectChampionDialogFragment;
import com.ozawa.hextcgdeckbuilder.UI.filter.FilterDrawerFragment;
import com.ozawa.hextcgdeckbuilder.UI.listview.DeckListViewAdapter;
import com.ozawa.hextcgdeckbuilder.UI.multiplecarddialogs.AbstractMultipleCardsDialogFragment;
import com.ozawa.hextcgdeckbuilder.UI.multiplecarddialogs.RemoveMultipleCardsDialogFragment;
import com.ozawa.hextcgdeckbuilder.UI.multiplecarddialogs.RemoveMultipleCardsDialogFragmentGinger;
import com.ozawa.hextcgdeckbuilder.enums.TutorialType;
import com.ozawa.hextcgdeckbuilder.hexentities.AbstractCard;
import com.ozawa.hextcgdeckbuilder.hexentities.HexDeck;
import com.ozawa.hextcgdeckbuilder.json.MasterDeck;
import com.ozawa.hextcgdeckbuilder.programstate.HexApplication;
import com.ozawa.hextcgdeckbuilder.util.HexUtil;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.gesture.Gesture;
import android.gesture.GestureLibraries;
import android.gesture.GestureLibrary;
import android.gesture.GestureOverlayView;
import android.gesture.Prediction;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.HapticFeedbackConstants;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;

public class CustomDeckFragment extends Fragment implements FilterDrawerFragment.NavigationDrawerCallbacks,
		GestureOverlayView.OnGesturePerformedListener {

	// private DeckUIActivity mainActivity;
	private DrawerLayout		uiLayout;

	ListView					listView;
	ImageAdapter				imAdapter;
	DeckListViewAdapter			lvAdapter;
	// private static List<AbstractCard> deck;
	public boolean				isGridView;

	public ImageView			cardBack;
	private int					cardBackDimension;
	private int					screenWidth;

	/**
	 * Fragment managing the behaviors, interactions and presentation of the
	 * navigation drawer.
	 */
	public FilterDrawerFragment	mFilterDrawerFragment;

	private CardsViewer			cardViewer;
	public final static String	GETDECK		= "GETDECK";
	private GestureLibrary		gesLibrary;
	private GridView			gridView;

	// Database
	// DatabaseHandler dbHandler;

	Button						saveDeck;
	Button						deleteDeck;
	Button						selectChampion;

	// Tutorial
	public static final String	PREFS_NAME	= "FirstLaunchPrefCustomDeck";
	private SharedPreferences	mPreferences;

	Deck						customDeck;
	HexApplication				hexApplication;

	Context						mContext;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		hexApplication = (HexApplication) getActivity().getApplication();
		if (customDeck == null) {
			customDeck = hexApplication.getCustomDeck();
		}
		mContext = getActivity();
		screenWidth = HexUtil.getScreenWidth(getActivity());

		gesLibrary = GestureLibraries.fromRawResource(mContext, R.raw.gestures);
		gesLibrary.load();
		if (cardViewer == null) {
			cardViewer = hexApplication.getCustomDeckViewer();
		}

		uiLayout = (DrawerLayout) inflater.inflate(R.layout.fragment_custom_deck, container, false);

		setupNavigationDrawer();

		GestureOverlayView gestureOverlayView = (GestureOverlayView) uiLayout.findViewById(R.id.customDeckGestureOverlayView);
		gestureOverlayView.addOnGesturePerformedListener(this);
		gestureOverlayView.setGestureVisible(false);

		if (cardViewer.getAdapter() != null && cardViewer.getAdapter() instanceof DeckListViewAdapter) {
			setUpListView();
		} else {
			setUpGridView(); // Set up the card grid view
		}

		/**
		 * Card Animation
		 */
		RelativeLayout cardAnimationView = (RelativeLayout) uiLayout.findViewById(R.id.cardAnimationLayout);
		cardBackDimension = HexUtil.getScreenWidth(mContext) / 3;
		cardBack = (ImageView) cardAnimationView.findViewById(R.id.cardAnimation);
		cardBack.setImageResource(R.drawable.back);
		cardBack.setLayoutParams(new RelativeLayout.LayoutParams(cardBackDimension, cardBackDimension));
		cardBack.setVisibility(View.INVISIBLE);

		return uiLayout;
	}

	@Override
	public void onResume() {
		super.onResume();
		if (mFilterDrawerFragment == null) {
			setupNavigationDrawer();
		}
	}

	@Override
	public void onStart() {
		super.onStart();
		if (mFilterDrawerFragment == null) {
			setupNavigationDrawer();
		}
	}

	private void setupNavigationDrawer() {
		mFilterDrawerFragment = (FilterDrawerFragment) getActivity().getSupportFragmentManager().findFragmentById(
				R.id.custom_deck_navigation_drawer);
		// Set up the drawer.
		mFilterDrawerFragment.setUp(uiLayout, cardViewer, mContext, R.id.custom_deck_navigation_drawer,
				(DrawerLayout) uiLayout.findViewById(R.id.custom_deck_drawer_layout));
		mFilterDrawerFragment.setUpCustomDeckViews();
		setCustomDeckButtonListeners();
		FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
		transaction.add(R.id.custom_deck_navigation_drawer, mFilterDrawerFragment).commit();
	}

	public ArrayList<InputStream> getJson() throws IllegalAccessException {
		Field[] rawFields = R.raw.class.getFields();
		ArrayList<InputStream> jsonFiles = new ArrayList<InputStream>();

		for (int count = 0; count < rawFields.length; count++) {
			int rid = rawFields[count].getInt(rawFields[count]);
			try {
				Resources res = getResources();
				String name = res.getResourceName(rid);
				if (!name.contains("gestures")) {
					InputStream inputStream = res.openRawResource(rid);
					if (inputStream != null) {
						jsonFiles.add(inputStream);
					}
				}
			} catch (Exception e) {
			}
		}
		return jsonFiles;
	}

	@Override
	public void onNavigationDrawerItemSelected(int position) {
		// update the main content by replacing fragments
		FragmentManager fragmentManager = getChildFragmentManager();
		fragmentManager.beginTransaction().replace(R.id.container, PlaceholderFragment.newInstance(position + 1)).commit();
	}

	@Override
	public void onGesturePerformed(GestureOverlayView gestureOverlayView, Gesture gesture) {
		ArrayList<Prediction> predictions = gesLibrary.recognize(gesture);
		if (predictions.size() > 0) {
			for (Prediction prediction : predictions) {
				if (prediction.score > 1.0) {
					if (prediction.name.equalsIgnoreCase("swipe left")) {
						CustomViewPager pager = (CustomViewPager) getActivity().findViewById(R.id.pager);
						pager.setCurrentItem(pager.getCurrentItem() + 1); // Swipe
																			// between
																			// pages
					} else if (prediction.name.equalsIgnoreCase("swipe right")) {
						int x = (int) gesture.getStrokes().get(0).points[0];
						int y = (int) gesture.getStrokes().get(0).points[1];
						int position = -1;
						if (isGridView) {
							GridView gridView = (GridView) uiLayout.findViewById(R.id.custom_deck_grid_view);
							position = gridView.pointToPosition(x, y);
						} else {
							ListView listView = (ListView) uiLayout.findViewById(R.id.custom_deck_deck_list);
							position = listView.pointToPosition(x, y);
						}

						if (position >= 0) {
							removeCardFromCustomDeck(position);
							HexUtil.moveImageAnimation(createAnimationArg(x, y));
						}
					} else if (prediction.name.equalsIgnoreCase("anti clockwise") || prediction.name.equalsIgnoreCase("clockwise")) {
						cardViewer.clearFilter();
						mFilterDrawerFragment.updateFilterUI();
					}
				}
			}
		}
	}

	private HexUtil.AnimationArg createAnimationArg(int x, int y) {
		HexUtil.AnimationArg result = new HexUtil.AnimationArg(cardBack, x - (cardBackDimension / 2), screenWidth + cardBackDimension, y
				- (cardBackDimension / 2), y - (y / 3), 400, 0);
		return result;
	}

	public void changeToListView() {
		if (listView != null) {
			cardViewer.setAdapter(lvAdapter);
			listView.setAdapter(lvAdapter);
			setIsGridView(false);
		} else {
			setUpListView();
		}
		updateListViewHeight();
		listView.setVisibility(View.VISIBLE);
		gridView.setVisibility(View.INVISIBLE);
	}

	public void changeToGridView() {
		if (gridView != null) {
			cardViewer.setAdapter(imAdapter);
			gridView.setAdapter(imAdapter);
			setIsGridView(true);
		} else {
			setUpGridView();
		}
		listView.setVisibility(View.INVISIBLE);
		gridView.setVisibility(View.VISIBLE);
	}

	private void setUpGridView() {
		if (imAdapter == null)
			imAdapter = new ImageAdapter(mContext, cardViewer);

		gridView = (GridView) uiLayout.findViewById(R.id.custom_deck_grid_view);
		gridView.setAdapter(imAdapter);

		gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View v, int position, long id) {

				// Sending image id to FullScreenActivity
				Intent i = new Intent(mContext, FullImageActivity.class);
				// passing array index
				i.putExtra("id", position);
				i.putExtra("isMaster", false);
				startActivity(i);
			}
		});

		gridView.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
				// Remove cards from custom deck
				if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
					int[] values = new int[2];
					view.getLocationOnScreen(values);
					makeRemoveMultipleCardsDialog(position, values);
				}
				return true;
			}

		});

		setIsGridView(true);
	}

	private void setUpListView() {
		listView = (ListView) uiLayout.findViewById(R.id.custom_deck_deck_list);

		// Getting adapter by passing xml data ArrayList
		lvAdapter = new DeckListViewAdapter(mContext, cardViewer);
		cardViewer.setAdapter(lvAdapter);
		listView.setAdapter(lvAdapter);
		// Click event for single list row
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				// Sending image id to FullScreenActivity
				Intent i = new Intent(mContext, FullImageActivity.class);
				// passing array index
				i.putExtra("id", position);
				i.putExtra("isMaster", false);
				startActivity(i);
			}
		});

		listView.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
				// Remove cards from custom deck
				if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
					int[] values = new int[2];
					view.getLocationOnScreen(values);
					makeRemoveMultipleCardsDialog(position, values);
				}
				return true;
			}

		});

		setIsGridView(false);
	}

	private boolean makeRemoveMultipleCardsDialog(int position, int[] values) {
		if (position >= 0) {
			AbstractMultipleCardsDialogFragment removeMultipleCardsDialog;
			if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
				removeMultipleCardsDialog = new RemoveMultipleCardsDialogFragment();
			} else {
				removeMultipleCardsDialog = new RemoveMultipleCardsDialogFragmentGinger();
			}
			AbstractCard card = cardViewer.getFilteredCardList().get(position);
			removeMultipleCardsDialog.card = card;
			removeMultipleCardsDialog.position = position;
			removeMultipleCardsDialog.animationArg = createAnimationArg(values[0] + cardBackDimension / 2, values[1] - cardBackDimension
					/ 2);
			removeMultipleCardsDialog.fragment = this;
			removeMultipleCardsDialog.show(getActivity().getSupportFragmentManager(), "Remove Multiple Cards");
			return true;
		}
		return false;
	}

	public void setIsGridView(boolean isGridView) {
		this.isGridView = isGridView;
	}

	/**
	 * Check when the fragment is visible to the user
	 */
	@Override
	public void setUserVisibleHint(boolean isVisibleToUser) {
		super.setUserVisibleHint(isVisibleToUser);

		// Update the view so the user can see the newly added cards
		if (this.isVisible() && isVisibleToUser) {
			reloadCustomDeckView();
			mPreferences = getActivity().getSharedPreferences(PREFS_NAME, 0);
			boolean firstTime = mPreferences.getBoolean("firstTime", true);
			if (firstTime) {
				showTutorial();
			}
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
		ShowcaseView showcaseView = ShowcaseView.insertShowcaseView(HexUtil.getScreenWidth(getActivity()) / 2,
				HexUtil.getScreenHeight(getActivity()) / 15, getActivity(), "Custom Deck",
				"This is where the magic happens, and you'll create your custom decks.", co);
		showcaseView.setShowcase(ShowcaseView.NONE);
		showcaseView.setOnShowcaseEventListener(new TutorialEventListener(getActivity(), co, TutorialType.CUSTOMDECK));
		showcaseView.show();
		SharedPreferences.Editor editor = mPreferences.edit();
		editor.putBoolean("firstTime", false);
		editor.commit();
	}

	public void reloadCustomDeckView() {
		cardViewer.updateDeckAndView();
		updateCustomDeckData();
		// Set button availability
		setDeckButtonAvailablity();
	}

	public void updateCustomDeckData() {
		ImageView championPortrait = (ImageView) mFilterDrawerFragment.getView().findViewById(R.id.imageChampionPortrait);
		TextView championName = (TextView) mFilterDrawerFragment.getView().findViewById(R.id.tvChampionName);
		HexDeck currentCustomDeck = customDeck.getCurrentDeck();
		if (currentCustomDeck != null && currentCustomDeck.champion != null) {
			int portaitID = HexUtil.getResourceID(currentCustomDeck.champion.hudPortraitSmall, R.drawable.class);
			if (portaitID != -1) {
				championPortrait.setImageResource(portaitID);
			} else {
				championPortrait.setImageResource(R.drawable.championnoportaitsmall);
			}
			championName.setText(currentCustomDeck.champion.name);
		} else {
			championPortrait.setImageResource(R.drawable.championnoportaitsmall);
			championName.setText("No Champion Selected");
		}
		if (customDeck.getDeckData() != null) {
			TextView deckCardCount = (TextView) mFilterDrawerFragment.getView().findViewById(R.id.tvDeckCardCount);
			int cardCount = 0;
			for (int value : customDeck.getDeckData().values()) {
				cardCount += value;
			}
			deckCardCount.setText("Card Count: " + cardCount);
		}
	}

	public void reloadCustomDeckView(boolean reloadFullView, int position) {
		if (reloadFullView) {
			reloadCustomDeckView();
		} else {
			if (isGridView) {
				imAdapter.getView(position, gridView.getChildAt(position), null);
			} else {
				lvAdapter.getView(position, listView.getChildAt(position), null);
			}
			updateCustomDeckData();
			// Set button availability
			setDeckButtonAvailablity();
		}
	}

	private void setDeckButtonAvailablity() {
		if (customDeck.getCurrentDeck() != null) {
			deleteDeck.setEnabled(true);
		} else {
			deleteDeck.setEnabled(false);
		}
		if (!customDeck.getDeckCardList().isEmpty()) {
			saveDeck.setEnabled(true);
		} else {
			saveDeck.setEnabled(false);
		}
	}

	/**
	 * Remove a card from the custom deck
	 * 
	 * @param card
	 */
	public void removeCardFromCustomDeck(int position, int value) {
		if (position >= 0) {
			AbstractCard card = cardViewer.getFilteredCardList().get(position);
			if (customDeck.removeCardFromDeck(card, value)) {
				reloadCustomDeckView();
			} else {
				reloadCustomDeckView(false, position);
			}
		}
	}

	public void removeCardFromCustomDeck(int position) {
		removeCardFromCustomDeck(position, 1);
	}

	/**
	 * Set onClick functionality for Deck Buttons i.e. New Deck, Load Deck, Save
	 * Deck, etc.
	 */
	private void setCustomDeckButtonListeners() {
		final CustomDeckFragment fragment = this;
		Button newDeck = (Button) mFilterDrawerFragment.getView().findViewById(R.id.buttonNewDeck);
		newDeck.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				v.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);
				if (customDeck.isUnsavedDeck() || customDeck.isDeckChanged()) {
					buildSaveUnsavedDeckDialog("showNewDeckPopup", fragment);
				} else {
					showNewDeckPopup();
				}
			}

		});

		Button loadDeck = (Button) mFilterDrawerFragment.getView().findViewById(R.id.buttonLoadDeck);
		loadDeck.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				v.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);
				if (customDeck.isUnsavedDeck() || customDeck.isDeckChanged()) {
					buildSaveUnsavedDeckDialog("showLoadDeckPopup", fragment);
				} else {
					showLoadDeckPopup();
				}

			}

		});

		saveDeck = (Button) mFilterDrawerFragment.getView().findViewById(R.id.buttonSaveDeck);
		saveDeck.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				v.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);
				if (customDeck.isUnsavedDeck() || customDeck.isDeckChanged()) {
					buildSaveUnsavedDeckDialog(null, fragment);
				} else {
					saveDeck();
				}
			}

		});

		deleteDeck = (Button) mFilterDrawerFragment.getView().findViewById(R.id.buttonDeleteDeck);
		deleteDeck.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				v.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);
				if (customDeck.getCurrentDeck() != null) {
					buildDeleteDeckConfirmationDialog();
				} else {
					Toast.makeText(mContext, "Deck isn't saved, no need to delete.", Toast.LENGTH_SHORT).show();
				}

			}

		});

		selectChampion = (Button) mFilterDrawerFragment.getView().findViewById(R.id.buttonSelectChampion);
		selectChampion.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				v.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);
				showSelectChampionPopup();
			}

		});
	}

	/**
	 * Display the New Deck Dialog
	 */
	private void showNewDeckPopup() {
		NewDeckDialogFragment newDeckDialog = new NewDeckDialogFragment();
		newDeckDialog.setTargetFragment(this, 1);
		newDeckDialog.show(getActivity().getSupportFragmentManager(), "New Deck Popup");
	}

	/**
	 * Display the Load Deck Dialog
	 */
	private void showLoadDeckPopup() {
		LoadDeckDialogFragment loadDeckDialog = new LoadDeckDialogFragment();
		loadDeckDialog.setTargetFragment(this, 1);
		loadDeckDialog.show(getActivity().getSupportFragmentManager(), "Load Deck Popup");
	}

	/**
	 * Display the Load Deck Dialog
	 */
	private void showSelectChampionPopup() {
		SelectChampionDialogFragment selectChampionDialog = new SelectChampionDialogFragment();
		selectChampionDialog.setTargetFragment(this, 1);
		selectChampionDialog.show(getActivity().getSupportFragmentManager(), "Select Champion Popup");
	}

	/**
	 * Save a new Deck
	 * 
	 * @param deckName
	 * @return The newly saved Deck
	 */
	public boolean saveNewDeck(String deckName) {
		if (customDeck.saveNewDeck(deckName) != null) {
			reloadCustomDeck(deckName);
			return true;
		}
		return false;
	}

	/**
	 * Load a Deck from the database using the given ID
	 * 
	 * @param deckID
	 * @return The Deck retrieved from the database with the given ID
	 */
	public boolean loadDeck(String deckID) {
		if (customDeck.loadDeck(deckID, MasterDeck.getMasterDeck(mContext))) {
			reloadCustomDeck(customDeck.getCurrentDeck().name);
			return true;
		}

		return false;
	}

	/**
	 * Save the current Deck
	 * 
	 * @return true if the Deck saved successfully, otherwise false
	 */
	private boolean saveDeck() {
		if (customDeck.getCurrentDeck() != null) {
			if (customDeck.saveDeck()) {
				reloadCustomDeck(customDeck.getCurrentDeck().name);
				Toast.makeText(mContext, "Deck successfully saved.", Toast.LENGTH_SHORT).show();
				return true;
			} else {
				Toast.makeText(mContext, "Failed to save deck. Please try again.", Toast.LENGTH_SHORT).show();
			}
		} else {
			Toast.makeText(mContext, "No deck to save.", Toast.LENGTH_SHORT).show();
		}

		return false;
	}

	/**
	 * Delete the current Deck from the database
	 */
	private void deleteDeck() {
		if (customDeck.getCurrentDeck() != null) {
			if (customDeck.deleteDeck()) {
				reloadCustomDeck("Custom Deck");
				Toast.makeText(mContext, "Deck successfully deleted.", Toast.LENGTH_SHORT).show();
			} else {
				Toast.makeText(mContext, "Failed to delete deck. Please try again.", Toast.LENGTH_SHORT).show();
			}

		}
	}

	private void reloadCustomDeck(String deckName) {
		((ActionBarActivity) getActivity()).getSupportActionBar().getTabAt(0).setText(deckName);
		reloadCustomDeckView();
	}

	/**
	 * Save a new Deck that has not previously been saved
	 * 
	 * @param deckName
	 * @return true if the Deck saved successfully, otherwise false
	 */
	private boolean saveUnsavedDeck(String deckName) {
		if (customDeck.saveUnsavedDeck(deckName)) {
			reloadCustomDeck(deckName);
			return true;
		}

		return false;
	}

	/**
	 * Build the Alert Dialog to confirm that the user wishes to delete the
	 * current deck
	 */
	private void buildDeleteDeckConfirmationDialog() {
		AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
		builder.setTitle("Delete Deck");
		builder.setMessage("Are you sure you want to delete deck: " + customDeck.getCurrentDeck().name + "?");
		builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface arg0, int arg1) {
				deleteDeck();
			}
		});
		builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface arg0, int arg1) {

			}
		});

		builder.show();

	}

	/**
	 * Build the Alert Dialog to check if the user would like to save changes to
	 * the current deck
	 * 
	 * @param methodName
	 *            - the method to invoke from the CustomDeckFragment using
	 *            Reflection
	 * @param fragment
	 *            - the CustomDeckFragment to invoke the given method on
	 */
	private void buildSaveUnsavedDeckDialog(final String methodName, final CustomDeckFragment fragment) {
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		builder.setTitle("Save Deck?");
		final EditText input = new EditText(getActivity());

		if (customDeck.getCurrentDeck() == null) {
			builder.setMessage("This deck is not yet saved, would you like to save now?");
			builder.setView(input);
		} else {
			builder.setMessage("This deck has unsaved changes, would you like to save it before proceeding?");
		}

		builder.setPositiveButton("Save", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int position) {
				if (input.getText().toString().isEmpty()) {
					Toast.makeText(getActivity(), "You must enter a name before saving.", Toast.LENGTH_SHORT).show();
				} else if (customDeck.getCurrentDeck() != null) {
					saveDeck();
					invokeNoParamReflectiveMethod(methodName, fragment);
				} else if (!saveUnsavedDeck(input.getText().toString())) {
					Toast.makeText(getActivity(), "Failed to save deck. Please try again.", Toast.LENGTH_SHORT).show();
				} else {
					invokeNoParamReflectiveMethod(methodName, fragment);
				}
			}
		});
		builder.setNegativeButton("No Thanks!", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int position) {
				invokeNoParamReflectiveMethod(methodName, fragment);
			}
		});
		final AlertDialog dialog = builder.create();
		dialog.show(); // To show the AlertDialog
		dialog.getButton(DialogInterface.BUTTON_POSITIVE).setEnabled(false);
		input.addTextChangedListener(new TextWatcher() {

			@Override
			public void afterTextChanged(Editable edit) {
				if (!input.getText().toString().trim().isEmpty()) {
					dialog.getButton(DialogInterface.BUTTON_POSITIVE).setEnabled(true);
				} else {
					dialog.getButton(DialogInterface.BUTTON_POSITIVE).setEnabled(false);
				}
			}

			@Override
			public void beforeTextChanged(CharSequence cs, int start, int before, int count) {
			}

			@Override
			public void onTextChanged(CharSequence cs, int start, int before, int count) {

			}

		});
	}

	/**
	 * Invoke a class method using Reflection
	 * 
	 * @param methodName
	 *            - the method to invoke from the CustomDeckFragment using
	 *            Reflection
	 * @param fragment
	 *            - the CustomDeckFragment to invoke the given method on
	 */
	private void invokeNoParamReflectiveMethod(final String methodName, final CustomDeckFragment fragment) {
		if (methodName != null) {
			try {
				Method method = CustomDeckFragment.class.getDeclaredMethod(methodName);
				if (!method.isAccessible()) {
					method.setAccessible(true);
				}
				method.invoke(fragment, new Object[0]);
			} catch (NoSuchMethodException e) {
			} catch (Exception ex) {
			}
		}
	}

	/**
	 * Update the listview height
	 */
	private void updateListViewHeight() {
		Runnable fitsOnScreen = new Runnable() {
			@Override
			public void run() {
				if (listView.getChildCount() > 0) {
					int last = listView.getLastVisiblePosition();
					if (last == listView.getCount() - 1 && listView.getChildAt(last).getBottom() >= listView.getHeight()) {
						listView.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, listView.getHeight()
								+ listView.getChildAt(last).getBottom()));
					}
				}
			}
		};
		listView.post(fitsOnScreen);
	}
}
