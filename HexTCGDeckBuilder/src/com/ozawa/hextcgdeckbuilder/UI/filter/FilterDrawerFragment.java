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

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.ozawa.hextcgdeckbuilder.R;
import com.ozawa.hextcgdeckbuilder.UI.CardsViewer;
import com.ozawa.hextcgdeckbuilder.UI.multiplecarddialogs.AbstractMultipleCardsDialogFragment;
import com.ozawa.hextcgdeckbuilder.UI.multiplecarddialogs.RemoveMultipleCardsDialogFragment;
import com.ozawa.hextcgdeckbuilder.UI.multiplecarddialogs.RemoveMultipleCardsDialogFragmentGinger;
import com.ozawa.hextcgdeckbuilder.enums.CardType;
import com.ozawa.hextcgdeckbuilder.enums.ColorFlag;
import com.ozawa.hextcgdeckbuilder.hexentities.AbstractCard;

/**
 * Fragment used for managing interactions for and presentation of a navigation
 * drawer. See the <a href=
 * "https://developer.android.com/design/patterns/navigation-drawer.html#Interaction"
 * > design guidelines</a> for a complete explanation of the behaviors
 * implemented here.
 */
public class FilterDrawerFragment extends Fragment implements TextWatcher {

	/**
	 * Remember the position of the selected item.
	 */
	private static final String			STATE_SELECTED_POSITION		= "selected_navigation_drawer_position";

	/**
	 * Per the design guidelines, you should show the drawer on launch until the
	 * user manually expands it. This shared preference tracks this.
	 */
	private static final String			PREF_USER_LEARNED_DRAWER	= "navigation_drawer_learned";

	/**
	 * A pointer to the current callbacks instance (the Activity).
	 */
	private NavigationDrawerCallbacks	mCallbacks;

	/**
	 * Helper component that ties the action bar to the navigation drawer.
	 */
	private ActionBarDrawerToggle		mDrawerToggle;

	private DrawerLayout				mDrawerLayout;
	// private ListView mDrawerListView;
	private ScrollView					scrollView;
	private View						mFragmentContainerView;
	CardsViewer							cardViewer;
	private Context						context;
	private List<FilterButton>			buttons = new ArrayList<FilterButton>();
	private EditText 					searchText;
	private int							mCurrentSelectedPosition	= 0;
	private boolean						mFromSavedInstanceState;
	private boolean						mUserLearnedDrawer;

	private boolean isCustomDeck;
	public FilterDrawerFragment() {

	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// Read in the flag indicating whether or not the user has demonstrated
		// awareness of the
		// drawer. See PREF_USER_LEARNED_DRAWER for details.
		SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getActivity());
		mUserLearnedDrawer = sp.getBoolean(PREF_USER_LEARNED_DRAWER, false);

		if (savedInstanceState != null) {
			mCurrentSelectedPosition = savedInstanceState.getInt(STATE_SELECTED_POSITION);
			mFromSavedInstanceState = true;
		}

		// Select either the default item (0) or the last selected item.
		selectItem(mCurrentSelectedPosition);

	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		// Indicate that this fragment would like to influence the set of
		// actions in the action bar.
		setHasOptionsMenu(true);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		scrollView = (ScrollView) inflater.inflate(R.layout.filter_layout, container, false);

		return scrollView;
	}

	public boolean isDrawerOpen() {
		return mDrawerLayout != null && mDrawerLayout.isDrawerOpen(mFragmentContainerView);
	}

	public void updateFilterUI() {
		for (FilterButton f : buttons) {
			f.updateImage();
		}
		if (searchText != null) {
			searchText.setText(cardViewer.getFilterString());
		}
	}

	private void setUpButtons() {
		Resources res = context.getResources();
		FilterButton button;

		button = (FilterButton) scrollView.findViewById(R.id.blood);
		buttons.add(button);
		button.setUp(BitmapFactory.decodeResource(res, R.drawable.blood_on), BitmapFactory.decodeResource(res, R.drawable.blood_off),
				ColorFlag.BLOOD, cardViewer);
		button = (FilterButton) scrollView.findViewById(R.id.wild);
		buttons.add(button);
		button.setUp(BitmapFactory.decodeResource(res, R.drawable.wild_on), BitmapFactory.decodeResource(res, R.drawable.wild_off),
				ColorFlag.WILD, cardViewer);
		button = (FilterButton) scrollView.findViewById(R.id.ruby);
		buttons.add(button);
		button.setUp(BitmapFactory.decodeResource(res, R.drawable.ruby_on), BitmapFactory.decodeResource(res, R.drawable.ruby_off),
				ColorFlag.RUBY, cardViewer);
		button = (FilterButton) scrollView.findViewById(R.id.sapphire);
		buttons.add(button);
		button.setUp(BitmapFactory.decodeResource(res, R.drawable.sapphire_on), BitmapFactory.decodeResource(res, R.drawable.sapphire_off),
				ColorFlag.SAPPHIRE, cardViewer);
		button = (FilterButton) scrollView.findViewById(R.id.diamond);
		buttons.add(button);
		button.setUp(BitmapFactory.decodeResource(res, R.drawable.diamond_on), BitmapFactory.decodeResource(res, R.drawable.diamond_off),
				ColorFlag.DIAMOND, cardViewer);
		button = (FilterButton) scrollView.findViewById(R.id.colorless);
		buttons.add(button);
		button.setUp(BitmapFactory.decodeResource(res, R.drawable.colorless_on),
				BitmapFactory.decodeResource(res, R.drawable.colorless_off), ColorFlag.COLORLESS, cardViewer);
		button = (FilterButton) scrollView.findViewById(R.id.troop);
		buttons.add(button);
		button.setUp(BitmapFactory.decodeResource(res, R.drawable.troop_on), BitmapFactory.decodeResource(res, R.drawable.troop_off),
				CardType.TROOP, cardViewer);
		button = (FilterButton) scrollView.findViewById(R.id.basicaction);
		buttons.add(button);
		button.setUp(BitmapFactory.decodeResource(res, R.drawable.basic_on), BitmapFactory.decodeResource(res, R.drawable.basic_off),
				CardType.BASICACTION, cardViewer);
		button = (FilterButton) scrollView.findViewById(R.id.quickaction);
		buttons.add(button);
		button.setUp(BitmapFactory.decodeResource(res, R.drawable.quick_on), BitmapFactory.decodeResource(res, R.drawable.quick_off),
				CardType.QUICKACTION, cardViewer);
		button = (FilterButton) scrollView.findViewById(R.id.constant);
		buttons.add(button);
		button.setUp(BitmapFactory.decodeResource(res, R.drawable.constant_on), BitmapFactory.decodeResource(res, R.drawable.constant_off),
				CardType.CONSTANT, cardViewer);
		button = (FilterButton) scrollView.findViewById(R.id.resource);
		buttons.add(button);
		button.setUp(BitmapFactory.decodeResource(res, R.drawable.resource_on), BitmapFactory.decodeResource(res, R.drawable.resource_off),
				CardType.RESOURCE, cardViewer);
		
		Button filterButton = (Button)scrollView.findViewById(R.id.buttonAdvFilter);
		filterButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
            	AdvancedFilterDialogFragment advancedFilterDialog;
    			if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
    				advancedFilterDialog = new AdvancedFilterDialogFragment();
    				advancedFilterDialog.isCustomDeck=isCustomDeck;
        			advancedFilterDialog.show(getActivity().getSupportFragmentManager(), "Remove Multiple Cards");
    			}else{
    				Toast.makeText(context, "Not available for this version of Android", Toast.LENGTH_LONG).show();
    			}
    			return;
            }
    });
		
		
		EditText text = (EditText) scrollView.findViewById(R.id.SearchTextField);
		text.addTextChangedListener(this);
		searchText = text;
		text.setOnFocusChangeListener(new OnFocusChangeListener() {
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if (!hasFocus) {
					InputMethodManager inputManager = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);

					if (inputManager.isActive()) {
						inputManager.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
					}
				}
			}
		});
		
		// Filter cards by card set
		Spinner selectCardSet = (Spinner) scrollView.findViewById(R.id.spinnerSelectCardSet);
		selectCardSet.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View aView, int position, long id) {
				cardViewer.setCardSet(parent.getItemAtPosition(position).toString());
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub				
			}
			
		});
	}

	public void setUpCustomDeckViews() {
		Button newDeck = (Button) scrollView.findViewById(R.id.buttonNewDeck);
		newDeck.setHapticFeedbackEnabled(true);
		newDeck.setVisibility(View.VISIBLE);

		Button loadDeck = (Button) scrollView.findViewById(R.id.buttonLoadDeck);
		loadDeck.setHapticFeedbackEnabled(true);
		loadDeck.setVisibility(View.VISIBLE);

		Button saveDeck = (Button) scrollView.findViewById(R.id.buttonSaveDeck);
		saveDeck.setHapticFeedbackEnabled(true);
		saveDeck.setVisibility(View.VISIBLE);
		
		/*Button exportDeck = (Button) scrollView.findViewById(R.id.buttonExportDeck);
		exportDeck.setHapticFeedbackEnabled(true);
		exportDeck.setVisibility(View.VISIBLE);*/

		Button deleteDeck = (Button) scrollView.findViewById(R.id.buttonDeleteDeck);
		deleteDeck.setHapticFeedbackEnabled(true);
		deleteDeck.setVisibility(View.VISIBLE);

		Button selectChampion = (Button) scrollView.findViewById(R.id.buttonSelectChampion);
		selectChampion.setHapticFeedbackEnabled(true);
		selectChampion.setVisibility(View.VISIBLE);
		
		Button deckTestDraw = (Button) scrollView.findViewById(R.id.buttonDeckTestDraw);
		deckTestDraw.setHapticFeedbackEnabled(true);
		deckTestDraw.setVisibility(View.VISIBLE);

		ImageView championPortrait = (ImageView) scrollView.findViewById(R.id.imageChampionPortrait);
		championPortrait.setVisibility(View.VISIBLE);

		TextView championName = (TextView) scrollView.findViewById(R.id.tvChampionName);
		championName.setVisibility(View.VISIBLE);

		TextView deckCardCount = (TextView) scrollView.findViewById(R.id.tvDeckCardCount);
		deckCardCount.setVisibility(View.VISIBLE);
	}

	/**
	 * Users of this fragment must call this method to set up the navigation
	 * drawer interactions.
	 * 
	 * @param fragmentId
	 *            The android:id of this fragment in its activity's layout.
	 * @param drawerLayout
	 *            The DrawerLayout containing this fragment's UI.
	 */
	public void setUp(CardsViewer iCardViewer, Context iContext, int fragmentId, DrawerLayout drawerLayout, boolean isCustomDeck) {
		context = iContext;
		cardViewer = iCardViewer;
		mFragmentContainerView = getActivity().findViewById(fragmentId);
		mDrawerLayout = drawerLayout;

		// set a custom shadow that overlays the main content when the drawer
		// opens
		mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);
		// set up the drawer's list view with items and click listener

		ActionBar actionBar = getActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);
		actionBar.setHomeButtonEnabled(true);

		// ActionBarDrawerToggle ties together the the proper interactions
		// between the navigation drawer and the action bar app icon.
		mDrawerToggle = new ActionBarDrawerToggle(getActivity(), mDrawerLayout, R.drawable.ic_drawer, R.string.navigation_drawer_open,
				R.string.navigation_drawer_close) {
			@Override
			public void onDrawerClosed(View drawerView) {
				super.onDrawerClosed(drawerView);
				if (!isAdded()) {
					return;
				}
			}

			@Override
			public void onDrawerOpened(View drawerView) {
				super.onDrawerOpened(drawerView);
				if (!isAdded()) {
					return;
				}

				if (!mUserLearnedDrawer) {
					// The user manually opened the drawer; store this flag to
					// prevent auto-showing
					// the navigation drawer automatically in the future.
					mUserLearnedDrawer = true;
					SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getActivity());
					sp.edit().putBoolean(PREF_USER_LEARNED_DRAWER, true).apply();
				}

				getActivity().supportInvalidateOptionsMenu(); // calls
																// onPrepareOptionsMenu()
			}
		};

		// If the user hasn't 'learned' about the drawer, open it to introduce
		// them to the drawer,
		// per the navigation drawer design guidelines.
		if (!mUserLearnedDrawer && !mFromSavedInstanceState) {
			mDrawerLayout.openDrawer(mFragmentContainerView);
		}

		// Defer code dependent on restoration of previous instance state.
		mDrawerLayout.post(new Runnable() {
			@Override
			public void run() {
				mDrawerToggle.syncState();
			}
		});

		mDrawerLayout.setDrawerListener(mDrawerToggle);
	}

	public void setUp(View frag, CardsViewer iCardViewer, Context iContext, int fragmentId, DrawerLayout drawerLayout,boolean isCustomDeck) {
		context = iContext;
		cardViewer = iCardViewer;
		mFragmentContainerView = frag.findViewById(fragmentId);
		mDrawerLayout = drawerLayout;
		this.isCustomDeck=isCustomDeck;
		// set a custom shadow that overlays the main content when the drawer
		// opens
		mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);

		setUpButtons();
	}

	private void selectItem(int position) {
		mCurrentSelectedPosition = position;
		// if (mDrawerListView != null) {
		// mDrawerListView.setItemChecked(position, true);
		// }
		if (mDrawerLayout != null) {
			mDrawerLayout.closeDrawer(mFragmentContainerView);
		}
		if (mCallbacks != null) {
			mCallbacks.onNavigationDrawerItemSelected(position);
		}
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		try {
			mCallbacks = (NavigationDrawerCallbacks) activity;
		} catch (ClassCastException e) {
			throw new ClassCastException("Activity must implement NavigationDrawerCallbacks.");
		}
	}

	@Override
	public void onDetach() {
		super.onDetach();
		mCallbacks = null;
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putInt(STATE_SELECTED_POSITION, mCurrentSelectedPosition);
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		// Forward the new configuration the drawer toggle component.
		mDrawerToggle.onConfigurationChanged(newConfig);
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		// If the drawer is open, show the global app actions in the action bar.
		// See also
		// showGlobalContextActionBar, which controls the top-left area of the
		// action bar.
		/*
		 * if (mDrawerLayout != null && isDrawerOpen()) {
		 * //inflater.inflate(R.menu.global, menu);
		 * inflater.inflate(R.menu.action_bar_menu, menu);
		 * showGlobalContextActionBar(); }
		 */
		super.onCreateOptionsMenu(menu, inflater);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (mDrawerToggle.onOptionsItemSelected(item)) {
			return true;
		}

		if (item.getItemId() == R.id.action_example) {
			Toast.makeText(getActivity(), "I said don\'t press.", Toast.LENGTH_SHORT).show();
			return true;
		}

		return super.onOptionsItemSelected(item);
	}

	/**
	 * Per the navigation drawer design guidelines, updates the action bar to
	 * show the global app 'context', rather than just what's in the current
	 * screen.
	 */
	@SuppressWarnings("unused")
	private void showGlobalContextActionBar() {
		ActionBar actionBar = getActionBar();
		actionBar.setDisplayShowTitleEnabled(true);
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
		actionBar.setTitle(R.string.app_name);
	}

	private ActionBar getActionBar() {
		return ((ActionBarActivity) getActivity()).getSupportActionBar();
	}

	/**
	 * Callbacks interface that all activities using this fragment must
	 * implement.
	 */
	public static interface NavigationDrawerCallbacks {
		/**
		 * Called when an item in the navigation drawer is selected.
		 */
		void onNavigationDrawerItemSelected(int position);
	}

	/*
	 * private void setPagingEnabled(boolean enabled){ CustomViewPager pager =
	 * (CustomViewPager) super.getActivity().findViewById(R.id.pager);
	 * pager.setPagingEnabled(enabled); }
	 */

	@Override
	public void afterTextChanged(Editable arg0) {
		cardViewer.setFilterString(arg0.toString());
	}

	@Override
	public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
			int arg3) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
		// TODO Auto-generated method stub
		
	}
}
