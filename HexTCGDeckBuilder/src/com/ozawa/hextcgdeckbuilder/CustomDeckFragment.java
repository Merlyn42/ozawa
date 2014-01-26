package com.ozawa.hextcgdeckbuilder;

import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.ozawa.hextcgdeckbuilder.UI.CardViewer;
import com.ozawa.hextcgdeckbuilder.UI.CustomViewPager;
import com.ozawa.hextcgdeckbuilder.UI.PlaceholderFragment;
import com.ozawa.hextcgdeckbuilder.database.DatabaseHandler;
import com.ozawa.hextcgdeckbuilder.hexentities.AbstractCard;
import com.ozawa.hextcgdeckbuilder.hexentities.Deck;
import com.ozawa.hextcgdeckbuilder.util.HexUtil;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
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
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Display;
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
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;

public class CustomDeckFragment extends Fragment implements NavigationDrawerFragment.NavigationDrawerCallbacks,
		GestureOverlayView.OnGesturePerformedListener {

	private DeckUIActivity				mainActivity;
	private DrawerLayout				uiLayout;

	ListView							listView;
	ImageAdapter						imAdapter;
	DeckListViewAdapter					lvAdapter;
	private static List<AbstractCard>	deck;
	public boolean						isGridView;

	public ImageView					cardBack;
	private int							cardBackDimension;
	private int							screenWidth;

	/**
	 * Fragment managing the behaviors, interactions and presentation of the
	 * navigation drawer.
	 */
	public NavigationDrawerFragment		mNavigationDrawerFragment;

	public static CardViewer			cardViewer;
	public final static String			GETDECK	= "GETDECK";
	private GestureLibrary				gesLibrary;
	private GridView					gridView;

	// Database
	DatabaseHandler						dbHandler;

	Button								saveDeck;
	Button								deleteDeck;
	Button								selectChampion;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		mainActivity = (DeckUIActivity) super.getActivity();
		deck = mainActivity.customDeckCardList;
		dbHandler = mainActivity.dbHandler;
		screenWidth = HexUtil.getScreenWidth(mainActivity);

		gesLibrary = GestureLibraries.fromRawResource(mainActivity, R.raw.gestures);
		if (!gesLibrary.load()) {
			mainActivity.finish();
		}

		cardViewer = new CardViewer(mainActivity, deck, mainActivity.customDeck);
		imAdapter = cardViewer.getAdapter();
		uiLayout = (DrawerLayout) inflater.inflate(R.layout.fragment_custom_deck, container, false);

		mNavigationDrawerFragment = (NavigationDrawerFragment) mainActivity.getSupportFragmentManager().findFragmentById(
				R.id.custom_deck_navigation_drawer);
		// Set up the drawer.
		mNavigationDrawerFragment.setUp(uiLayout, cardViewer, mainActivity, R.id.custom_deck_navigation_drawer,
				(DrawerLayout) uiLayout.findViewById(R.id.custom_deck_drawer_layout));
		mNavigationDrawerFragment.setUpCustomDeckViews();
		setCustomDeckButtonListeners();
		FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
		transaction.add(R.id.custom_deck_navigation_drawer, mNavigationDrawerFragment).commit();

		GestureOverlayView gestureOverlayView = (GestureOverlayView) uiLayout.findViewById(R.id.customDeckGestureOverlayView);
		gestureOverlayView.addOnGesturePerformedListener(this);
		gestureOverlayView.setGestureVisible(false);

		setUpGridView(); // Set up the card grid view

		/**
		 * Card Animation
		 */
		RelativeLayout cardAnimationView = (RelativeLayout) uiLayout.findViewById(R.id.cardAnimationLayout);
		cardBackDimension = HexUtil.getScreenWidth(mainActivity) / 3;
		cardBack = (ImageView) cardAnimationView.findViewById(R.id.cardAnimation);
		cardBack.setImageResource(R.drawable.back);
		cardBack.setLayoutParams(new RelativeLayout.LayoutParams(cardBackDimension, cardBackDimension));
		cardBack.setVisibility(View.INVISIBLE);

		return uiLayout;
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
						CustomViewPager pager = (CustomViewPager) mainActivity.findViewById(R.id.pager);
						pager.setCurrentItem(pager.getCurrentItem() + 1); // *******
																			// TEMPORARY
																			// FIX
																			// FOR
																			// SLIDING
																			// BETWEEN
																			// PAGES
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
					}
				}
			}
		}
	}

	private HexUtil.AnimationArg createAnimationArg(int x, int y) {
		HexUtil.AnimationArg result = new HexUtil.AnimationArg(cardBack, x - (cardBackDimension / 2), screenWidth + cardBackDimension, y
				- (cardBackDimension / 2), (int) y - (y / 3), 400, 0);
		return result;
	}

	public void changeToListView() {
		if (listView != null) {
			cardViewer.setAdapter(lvAdapter);
			lvAdapter.updateDeck(imAdapter.masterDeck);
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
			imAdapter.updateDeck(lvAdapter.masterDeck);
			setIsGridView(true);
		} else {
			setUpGridView();
		}
		listView.setVisibility(View.INVISIBLE);
		gridView.setVisibility(View.VISIBLE);
	}

	private void setUpGridView() {
		gridView = (GridView) uiLayout.findViewById(R.id.custom_deck_grid_view);

		gridView.setAdapter(cardViewer.getAdapter());

		gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View v, int position, long id) {

				// Sending image id to FullScreenActivity
				Intent i = new Intent(mainActivity.getApplicationContext(), FullImageActivity.class);
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
		lvAdapter = new DeckListViewAdapter(mainActivity, cardViewer.getAdapter().masterDeck, mainActivity.customDeck);
		cardViewer.setAdapter(lvAdapter);
		listView.setAdapter(cardViewer.getAdapter());
		// Click event for single list row
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				// Sending image id to FullScreenActivity
				Intent i = new Intent(mainActivity.getApplicationContext(), FullImageActivity.class);
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
			AbstractCard card = isGridView == true ? imAdapter.masterDeck.get(position) : lvAdapter.masterDeck.get(position);
			removeMultipleCardsDialog.card = card;
			removeMultipleCardsDialog.position = position;
			removeMultipleCardsDialog.animationArg = createAnimationArg(values[0] + cardBackDimension / 2, values[1] - cardBackDimension
					/ 2);
			removeMultipleCardsDialog.mainActivity = ((DeckUIActivity) mainActivity);
			removeMultipleCardsDialog.fragment = this;
			removeMultipleCardsDialog.show(mainActivity.getSupportFragmentManager(), "Remove Multiple Cards");
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
		}
	}

	public void reloadCustomDeckView() {
		deck = mainActivity.customDeckCardList;
		if (isGridView) {
			imAdapter.updateDeckAndCardViewDeck(deck, cardViewer);
		} else {
			lvAdapter.updateDeckAndCardViewDeck(deck, cardViewer);
		}

		mainActivity.updateCustomDeckData();
		// Set button availability
		setDeckButtonAvailablity();
	}

	public void reloadCustomDeckView(boolean reloadFullView, int position) {
		deck = mainActivity.customDeckCardList;
		if (reloadFullView) {
			reloadCustomDeckView();
		} else {
			if (isGridView) {
				imAdapter.getView(position, gridView.getChildAt(position), null);
			} else {
				lvAdapter.getView(position, listView.getChildAt(position), null);
			}
			mainActivity.updateCustomDeckData();
			// Set button availability
			setDeckButtonAvailablity();
		}
	}

	private void setDeckButtonAvailablity() {
		if (mainActivity.currentCustomDeck != null) {
			deleteDeck.setEnabled(true);
		} else {
			deleteDeck.setEnabled(false);
		}
		if (!deck.isEmpty()) {
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
			AbstractCard card = isGridView == true ? imAdapter.masterDeck.get(position) : lvAdapter.masterDeck.get(position);
			HashMap<AbstractCard, Integer> customDeck = mainActivity.customDeck;
			if (customDeck.get(card) != null) {
				int cardCount = customDeck.get(card);
				if (cardCount > value) {
					customDeck.put(card, customDeck.get(card) - value);
					reloadCustomDeckView(false, position);
				} else {
					customDeck.remove(card);
					mainActivity.customDeckCardList.remove(card);
					reloadCustomDeckView();
				}

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
		Button newDeck = (Button) mNavigationDrawerFragment.getView().findViewById(R.id.buttonNewDeck);
		newDeck.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				v.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);
				if (mainActivity.isUnsavedDeck() || mainActivity.deckChanged) {
					buildSaveUnsavedDeckDialog("showNewDeckPopup", fragment);
				} else {
					showNewDeckPopup();
				}
			}

		});

		Button loadDeck = (Button) mNavigationDrawerFragment.getView().findViewById(R.id.buttonLoadDeck);
		loadDeck.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				v.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);
				if (mainActivity.isUnsavedDeck() || mainActivity.deckChanged) {
					buildSaveUnsavedDeckDialog("showLoadDeckPopup", fragment);
				} else {
					showLoadDeckPopup();
				}

			}

		});

		saveDeck = (Button) mNavigationDrawerFragment.getView().findViewById(R.id.buttonSaveDeck);
		saveDeck.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				v.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);
				if (mainActivity.isUnsavedDeck() || mainActivity.deckChanged) {
					buildSaveUnsavedDeckDialog(null, fragment);
				} else {
					saveDeck();
				}
			}

		});

		deleteDeck = (Button) mNavigationDrawerFragment.getView().findViewById(R.id.buttonDeleteDeck);
		deleteDeck.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				v.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);
				if (mainActivity.currentCustomDeck != null) {
					buildDeleteDeckConfirmationDialog();
				} else {
					Toast.makeText(mainActivity.getApplicationContext(), "Deck isn't saved, no need to delete.", Toast.LENGTH_SHORT).show();
				}

			}

		});

		selectChampion = (Button) mNavigationDrawerFragment.getView().findViewById(R.id.buttonSelectChampion);
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
		newDeckDialog.show(mainActivity.getSupportFragmentManager(), "New Deck Popup");
	}

	/**
	 * Display the Load Deck Dialog
	 */
	private void showLoadDeckPopup() {
		LoadDeckDialogFragment loadDeckDialog = new LoadDeckDialogFragment();
		loadDeckDialog.show(mainActivity.getSupportFragmentManager(), "Load Deck Popup");
	}

	/**
	 * Display the Load Deck Dialog
	 */
	private void showSelectChampionPopup() {
		SelectChampionDialogFragment selectChampionDialog = new SelectChampionDialogFragment();
		selectChampionDialog.show(mainActivity.getSupportFragmentManager(), "Select Champion Popup");
	}

	/**
	 * Save a new Deck
	 * 
	 * @param deckName
	 * @return The newly saved Deck
	 */
	public Deck saveNewDeck(String deckName) {
		Deck newDeck = new Deck();
		newDeck.name = deckName;

		long newDeckID = dbHandler.addDeck(newDeck);
		if (newDeckID == -1) {
			return null;
		}

		return dbHandler.getDeck(String.valueOf(newDeckID));
	}

	/**
	 * Load a Deck from the database using the given ID
	 * 
	 * @param deckID
	 * @return The Deck retrieved from the database with the given ID
	 */
	public Deck loadDeck(String deckID) {
		mainActivity.currentCustomDeck = dbHandler.getDeck(deckID);

		return mainActivity.currentCustomDeck;
	}

	/**
	 * Save the current Deck
	 * 
	 * @return true if the Deck saved successfully, otherwise false
	 */
	private boolean saveDeck() {
		if (mainActivity.currentCustomDeck != null) {
			if (dbHandler.updateDeck(mainActivity.currentCustomDeck)
					&& dbHandler.updateDeckResources(mainActivity.currentCustomDeck, mainActivity.customDeck)) {
				Toast.makeText(mainActivity.getApplicationContext(), "Deck successfully saved.", Toast.LENGTH_SHORT).show();
				mainActivity.deckChanged = false;
				return true;
			} else {
				Toast.makeText(mainActivity.getApplicationContext(), "Failed to save deck. Please try again.", Toast.LENGTH_SHORT).show();
			}
		} else {
			Toast.makeText(mainActivity.getApplicationContext(), "No deck to save.", Toast.LENGTH_SHORT).show();
		}

		return false;
	}

	/**
	 * Delete the current Deck from the database
	 */
	private void deleteDeck() {
		if (mainActivity.currentCustomDeck != null) {
			if (dbHandler.deleteDeck(mainActivity.currentCustomDeck)) {
				mainActivity.deleteDeck();
				reloadCustomDeckView();
				Toast.makeText(mainActivity.getApplicationContext(), "Deck successfully deleted.", Toast.LENGTH_SHORT).show();
			} else {
				Toast.makeText(mainActivity.getApplicationContext(), "Failed to delete deck. Please try again.", Toast.LENGTH_SHORT).show();
			}

		}
	}

	/**
	 * Save a new Deck that has not previously been saved
	 * 
	 * @param deckName
	 * @return true if the Deck saved successfully, otherwise false
	 */
	private boolean saveUnsavedDeck(String deckName) {
		if (mainActivity.saveNewDeck(deckName, false)) {
			return saveDeck();
		}
		return false;
	}

	/**
	 * Build the Alert Dialog to confirm that the user wishes to delete the
	 * current deck
	 */
	private void buildDeleteDeckConfirmationDialog() {
		AlertDialog.Builder builder = new AlertDialog.Builder(mainActivity);
		builder.setTitle("Delete Deck");
		builder.setMessage("Are you sure you want to delete deck: " + mainActivity.currentCustomDeck.name + "?");
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
		AlertDialog.Builder builder = new AlertDialog.Builder(mainActivity);
		builder.setTitle("Save Deck?");
		final EditText input = new EditText(mainActivity);

		if (mainActivity.currentCustomDeck == null) {
			builder.setMessage("This deck is not yet saved, would you like to save now?");
			builder.setView(input);
		} else {
			builder.setMessage("This deck has unsaved changes, would you like to save it before proceeding?");
		}

		builder.setPositiveButton("Save", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int position) {
				if(input.getText().toString().isEmpty()){
					Toast.makeText(mainActivity.getApplicationContext(), "You must enter a name before saving.", Toast.LENGTH_SHORT).show();
				}else if (mainActivity.currentCustomDeck != null) {
					saveDeck();
					invokeNoParamReflectiveMethod(methodName, fragment);
				} else if (!saveUnsavedDeck(input.getText().toString())) {
					Toast.makeText(mainActivity.getApplicationContext(), "Failed to save deck. Please try again.", Toast.LENGTH_SHORT).show();
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
		dialog.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(false);
		input.addTextChangedListener(new TextWatcher(){

			@Override
			public void afterTextChanged(Editable edit) {
				if(!input.getText().toString().trim().isEmpty()){
					dialog.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(true);
				}else{
					dialog.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(false);
				}
			}

			@Override
			public void beforeTextChanged(CharSequence cs, int start,
					int before, int count) {				
			}

			@Override
			public void onTextChanged(CharSequence cs, int start, int before,
					int count) {
								
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
						Toast.makeText(mainActivity, "Updating Listview height", Toast.LENGTH_SHORT).show();
					}
				}
			}
		};
		listView.post(fitsOnScreen);
	}
}
