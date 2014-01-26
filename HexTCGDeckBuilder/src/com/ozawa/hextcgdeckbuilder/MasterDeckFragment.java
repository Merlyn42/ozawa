package com.ozawa.hextcgdeckbuilder;

import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.ozawa.hextcgdeckbuilder.UI.CardViewer;
import com.ozawa.hextcgdeckbuilder.UI.CustomViewPager;
import com.ozawa.hextcgdeckbuilder.UI.PlaceholderFragment;
import com.ozawa.hextcgdeckbuilder.UI.TutorialEventListener;
import com.ozawa.hextcgdeckbuilder.hexentities.AbstractCard;
import com.ozawa.hextcgdeckbuilder.hexentities.Card;
import com.ozawa.hextcgdeckbuilder.json.JsonReader;
import com.ozawa.hextcgdeckbuilder.util.HexUtil;

import com.espian.showcaseview.ShowcaseViews;
import com.espian.showcaseview.ShowcaseView;

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
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.RelativeLayout;

public class MasterDeckFragment extends Fragment implements NavigationDrawerFragment.NavigationDrawerCallbacks,
		GestureOverlayView.OnGesturePerformedListener {

	private FragmentActivity			mainActivity;
	private DrawerLayout				uiLayout;
	private ShowcaseView				showcaseView; 
	ListView							listView;
	ImageAdapter						imAdapter;
	DeckListViewAdapter					lvAdapter;
	public List<AbstractCard>			masterDeck;
	private JsonReader					jsonReader;
	public boolean						isGridView;

	public ImageView					cardBack;
	private int							cardBackDimension;
	private int							tutCount;
	/**
	 * Fragment managing the behaviors, interactions and presentation of the
	 * navigation drawer.
	 */
	private NavigationDrawerFragment	mNavigationDrawerFragment;
	private ShowcaseView.ConfigOptions 	co;
	ShowcaseViews 						mViews;
	public static CardViewer			cardViewer;
	public final static String			GETDECK	= "GETDECK";
	private static final String PREFS_NAME = "FirstLaunchPref";
	private GestureLibrary				gesLibrary;
	private GridView					gridView;
	
	private SharedPreferences mPreferences;
	

	@SuppressWarnings("deprecation")
	@Override
	public void onActivityCreated(Bundle savedInstanceState){
		super.onActivityCreated(savedInstanceState);	
		mPreferences = getActivity().getSharedPreferences(PREFS_NAME, 0);
		boolean firstTime = mPreferences.getBoolean("firstTime", true);
		if (firstTime) { 
		    SharedPreferences.Editor editor = mPreferences.edit();
		    editor.putBoolean("firstTime", false);
		    editor.commit();
		
			tutCount = 0;
			co = new ShowcaseView.ConfigOptions();
			co.shotType = ShowcaseView.TYPE_ONE_SHOT;
			co.centerText = true;
			co.hideOnClickOutside = true;
	        showcaseView = ShowcaseView.insertShowcaseView(HexUtil.getScreenWidth(getActivity()) / 2, (int)(HexUtil.getScreenHeight(getActivity()) / 2.15), getActivity(), "Gestures", "Swipe left to add the card to your custom deck", co);        
			showcaseView.setOnShowcaseEventListener(new TutorialEventListener(getActivity(),co));
	        showcaseView.animateGesture(HexUtil.getScreenWidth(getActivity()) / 2, (int)(HexUtil.getScreenHeight(getActivity()) / 2.1), (int)(HexUtil.getScreenWidth(getActivity()) / 4), (int)(HexUtil.getScreenHeight(getActivity()) / 2.1), true);        
	        showcaseView.show();
	        tutCount++;
		}
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		mainActivity = super.getActivity();
		jsonReader = new JsonReader(container.getContext());
		try {
			masterDeck = jsonReader.deserializeJSONInputStreamsToCard(getJson());
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}

		gesLibrary = GestureLibraries.fromRawResource(mainActivity, R.raw.gestures);
		if (!gesLibrary.load()) {
			mainActivity.finish();
		}
        ShowcaseView.ConfigOptions co = new ShowcaseView.ConfigOptions();
        co.hideOnClickOutside = true;       
		
		cardViewer = new CardViewer(mainActivity, masterDeck, null);
		imAdapter = cardViewer.getAdapter();
		uiLayout = (DrawerLayout) inflater.inflate(R.layout.fragment_master_deck, container, false);

		mNavigationDrawerFragment = (NavigationDrawerFragment) mainActivity.getSupportFragmentManager().findFragmentById(
				R.id.master_deck_navigation_drawer);
		// Set up the drawer.
		mNavigationDrawerFragment.setUp(uiLayout, cardViewer, mainActivity, R.id.master_deck_navigation_drawer,
				(DrawerLayout) uiLayout.findViewById(R.id.master_deck_drawer_layout));
		FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
		transaction.add(R.id.master_deck_navigation_drawer, mNavigationDrawerFragment).commit();

		GestureOverlayView gestureOverlayView = (GestureOverlayView) uiLayout.findViewById(R.id.masterDeckGestureOverlayView);
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
				if (name.contains("hexcard")) {
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
						int x = (int) gesture.getStrokes().get(0).points[0];
						int y = (int) gesture.getStrokes().get(0).points[1];
						int position;
						if (isGridView) {
							GridView gridView = (GridView) uiLayout.findViewById(R.id.master_deck_grid_view);
							position = gridView.pointToPosition(x, y);
						} else {
							ListView listView = (ListView) uiLayout.findViewById(R.id.master_deck_deck_list);
							position = listView.pointToPosition(x, y);
						}

						if (position >= 0) {
							if (addCardToCustomDeck(position)) {
								HexUtil.moveImageAnimation(createAnimationArg(x, y));
							}
						}
					} else if (prediction.name.equalsIgnoreCase("swipe right")) {
						CustomViewPager pager = (CustomViewPager) mainActivity.findViewById(R.id.pager);
						pager.setCurrentItem(pager.getCurrentItem() - 1); // *******
																			// TEMPORARY
																			// FIX
																			// FOR
																			// SLIDING
																			// BETWEEN
																			// PAGES
					} else if (prediction.name.equalsIgnoreCase("anti clockwise") || prediction.name.equalsIgnoreCase("clockwise")) {
						cardViewer.clearFilter();
					}
				}
			}
		}
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
		gridView.setVisibility(View.INVISIBLE);
		listView.setVisibility(View.VISIBLE);
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
		gridView = (GridView) uiLayout.findViewById(R.id.master_deck_grid_view);
		gridView.setHapticFeedbackEnabled(true);
		gridView.setAdapter(cardViewer.getAdapter());

		gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View v, int position, long id) {

				// Sending image id to FullScreenActivity
				Intent i = new Intent(mainActivity.getApplicationContext(), FullImageActivity.class);
				// passing array index
				i.putExtra("id", position);
				i.putExtra("isMaster", true);
				startActivity(i);
			}
		});

		gridView.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
				int[] values = new int[2];
				view.getLocationOnScreen(values);
				return makeAddMultipleCardsDialog(position, values);
			}
		});
		setIsGridView(true);
	}

	private void setUpListView() {
		listView = (ListView) uiLayout.findViewById(R.id.master_deck_deck_list);
		listView.setHapticFeedbackEnabled(true);
		// Getting adapter by passing xml data ArrayList
		lvAdapter = new DeckListViewAdapter(mainActivity, cardViewer.getAdapter().masterDeck, null);
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
				i.putExtra("isMaster", true);
				startActivity(i);
			}
		});

		listView.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
				int[] values = new int[2];
				view.getLocationOnScreen(values);
				return makeAddMultipleCardsDialog(position, values);
			}

		});

		setIsGridView(false);
	}

	public void setIsGridView(boolean isGridView) {
		this.isGridView = isGridView;
	}

	private HexUtil.AnimationArg createAnimationArg(int x, int y) {
		HexUtil.AnimationArg result = new HexUtil.AnimationArg(cardBack, x - (cardBackDimension / 2), -cardBack.getLayoutParams().width, y
				- (cardBackDimension / 2), (int) y - (y / 3), 400, 0);
		return result;
	}

	private boolean makeAddMultipleCardsDialog(int position, int[] values) {
		AbstractMultipleCardsDialogFragment addMultipleCardsDialog;
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			addMultipleCardsDialog = new AddMultipleCardsDialogFragmentGinger();
		} else {
			addMultipleCardsDialog = new AddMultipleCardsDialogFragmentGinger();
		}
		if (position >= 0) {
			AbstractCard card = isGridView == true ? imAdapter.masterDeck.get(position) : lvAdapter.masterDeck.get(position);

			addMultipleCardsDialog.card = card;
			addMultipleCardsDialog.position = position;
			addMultipleCardsDialog.animationArg = createAnimationArg(values[0] + cardBackDimension / 2, values[1] - cardBackDimension / 2);
			addMultipleCardsDialog.mainActivity = ((DeckUIActivity) mainActivity);
			addMultipleCardsDialog.fragment = this;
			addMultipleCardsDialog.show(mainActivity.getSupportFragmentManager(), "Add Multiple Cards");
			return true;
		}
		return false;
	}

	/**
	 * Add a card to the custom deck
	 * 
	 * @param card
	 */
	public boolean addCardToCustomDeck(int position, int value) {
		AbstractCard card = isGridView == true ? imAdapter.masterDeck.get(position) : lvAdapter.masterDeck.get(position);
		if (card instanceof Card && ((Card) card).cardNumber == 0) {
			Toast.makeText(mainActivity.getApplicationContext(), card.name + " cannot be added directly to decks.", Toast.LENGTH_SHORT)
					.show();
			return false;
		}
		HashMap<AbstractCard, Integer> customDeck = ((DeckUIActivity) mainActivity).customDeck;
		if (customDeck.get(card) == null) {
			customDeck.put(card, value);
			((DeckUIActivity) mainActivity).customDeckCardList.add(card);
		} else {
			customDeck.put(card, customDeck.get(card) + value);
		}
		((DeckUIActivity) mainActivity).deckChanged = true;
		return true;
	}

	public boolean addCardToCustomDeck(int position) {
		return addCardToCustomDeck(position, 1);
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
