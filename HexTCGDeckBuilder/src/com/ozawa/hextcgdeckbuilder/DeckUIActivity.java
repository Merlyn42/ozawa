package com.ozawa.hextcgdeckbuilder;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;

import com.ozawa.hextcgdeckbuilder.LoadDeckDialogFragment.LoadDeckListener;
import com.ozawa.hextcgdeckbuilder.NewDeckDialogFragment.NewDeckListener;
import com.ozawa.hextcgdeckbuilder.R.drawable;
import com.ozawa.hextcgdeckbuilder.UI.CardViewer;
import com.ozawa.hextcgdeckbuilder.UI.CustomViewPager;
import com.ozawa.hextcgdeckbuilder.UI.TabPagerAdapter;
import com.ozawa.hextcgdeckbuilder.database.DatabaseHandler;
import com.ozawa.hextcgdeckbuilder.hexentities.AbstractCard;
import com.ozawa.hextcgdeckbuilder.hexentities.Deck;
import com.ozawa.hextcgdeckbuilder.hexentities.DeckResource;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.FragmentTransaction;

public class DeckUIActivity extends ActionBarActivity implements ActionBar.TabListener, NavigationDrawerFragment.NavigationDrawerCallbacks, NewDeckListener, LoadDeckListener{
	
    public static CardViewer cardViewer;
    
	private CustomViewPager viewPager;
    private TabPagerAdapter mAdapter;
    public static ActionBar actionBar;
    // Tab titles
    private String[] tabs = {"Custom Deck", "Master Deck"};
    
    // Current Custom Deck
    public ArrayList<AbstractCard> customDeckCardList;
    public HashMap<AbstractCard, Integer> customDeck;
    public Deck currentCustomDeck;
    public boolean deckChanged = false;
    
    // Database
    public DatabaseHandler dbHandler;
 
    @Override
    protected void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.deck_ui_activity);
        
	    // Initilization
	    viewPager = (CustomViewPager) findViewById(R.id.pager);
	    actionBar = getActionBar();
	    mAdapter = new TabPagerAdapter(getSupportFragmentManager());
	    customDeck = new HashMap<AbstractCard, Integer>();
	    customDeckCardList = new ArrayList<AbstractCard>(customDeck.keySet());
	    dbHandler = new DatabaseHandler(this);
	    
	 
	    viewPager.setAdapter(mAdapter);
	    //actionBar.setHomeButtonEnabled(false);
	    actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);       
	 
	        // Adding Tabs
	    for (String tab_name : tabs) {
	        actionBar.addTab(actionBar.newTab().setText(tab_name)
	                 .setTabListener(this));
	    }
	    
	    /**
         * on swiping the viewpager make respective tab selected
         * */
        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
 
            @Override
            public void onPageSelected(int position) {
                // on changing the page
                // make respected tab selected
                actionBar.setSelectedNavigationItem(position);
                /*if(position == 1){
                	viewPager.setPagingEnabled(false);
                }*/
            }
 
            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {
            }
 
            @Override
            public void onPageScrollStateChanged(int arg0) {
            }
        });
        
        viewPager.setCurrentItem(1); // Start app on MasterDeck view
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.action_bar_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Open the list view for the deck
    	MasterDeckFragment masterDeckFragment = mAdapter.masterDeckFragment;
    	CustomDeckFragment customDeckFragment = mAdapter.customDeckFragment;
        switch (item.getItemId()) {
            case R.id.action_deck_view:            	
            	if(masterDeckFragment != null && !masterDeckFragment.isGridView){
	            	masterDeckFragment.changeToGridView();	                
            	}
            	if(customDeckFragment != null && !customDeckFragment.isGridView){
            		customDeckFragment.changeToGridView();	                
            	}
            	return true;
            case R.id.action_list_view:
            	if(masterDeckFragment != null && masterDeckFragment.isGridView){
	            	masterDeckFragment.changeToListView();
            	}
            	if(customDeckFragment != null && customDeckFragment.isGridView){
            		customDeckFragment.changeToListView();
            	}
            	return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }
        
	@Override
	public void onTabReselected(Tab arg0, FragmentTransaction arg1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onTabSelected(Tab tab, FragmentTransaction fragTransaction) {
		viewPager.setCurrentItem(tab.getPosition()); // Change to the tab when clicked
	}

	@Override
	public void onTabUnselected(Tab arg0, FragmentTransaction arg1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onNavigationDrawerItemSelected(int position) {
		// TODO Auto-generated method stub
	}

	@Override
	public boolean saveNewDeck(String deckName, boolean resetCustomDeck) {
		if(mAdapter.customDeckFragment != null){
			Deck savedDeck = mAdapter.customDeckFragment.saveNewDeck(deckName);			
			if(savedDeck != null && savedDeck.name.contentEquals(String.valueOf(deckName))){				
				if(resetCustomDeck){
					resetCustomDeck();
				}
					
				currentCustomDeck = savedDeck;
				mAdapter.customDeckFragment.reloadCustomDeckView();
				deckChanged = false;
				updateCustomDeckData();
				actionBar.getTabAt(0).setText("Custom Deck - " + deckName);
				return true;				
			}
		}
		return false;
	}
	
	@Override
	public boolean loadDeck(String deckID) {
		if(mAdapter.customDeckFragment != null){
			Deck loadedDeck = mAdapter.customDeckFragment.loadDeck(deckID);
			if(loadedDeck.getID().contentEquals(deckID)){
				updateCustomDeck(loadedDeck);
				actionBar.getTabAt(0).setText("Custom Deck - " + loadedDeck.name);
				mAdapter.customDeckFragment.reloadCustomDeckView();
				updateCustomDeckData();
				return true;
			}			
		}
		return false;
	}
	
	public void deleteDeck(){
		resetCustomDeck();
	}
	
	public boolean isUnsavedDeck(){
		return (currentCustomDeck == null && !customDeck.isEmpty());
	}
	
	public void updateCustomDeckData(){
		if(mAdapter.customDeckFragment != null){
			ImageView championPortrait = (ImageView) mAdapter.customDeckFragment.mNavigationDrawerFragment.getView().findViewById(R.id.imageChampionPortrait);
			TextView championName = (TextView) mAdapter.customDeckFragment.mNavigationDrawerFragment.getView().findViewById(R.id.tvChampionName);
			if(currentCustomDeck != null && currentCustomDeck.champion != null){		
				int portaitID = getResourceID(currentCustomDeck.champion.hudPortraitSmall, R.drawable.class);
				if(portaitID != -1){
					championPortrait.setImageResource(portaitID);
				}else{
					championPortrait.setImageResource(R.drawable.championnoportaitsmall);
				}
				championName.setText(currentCustomDeck.champion.name);
			}else{
				championPortrait.setImageResource(R.drawable.championnoportaitsmall);				
				championName.setText("No Champion Selected");
			}
			if(customDeck != null){
				TextView deckCardCount = (TextView) mAdapter.customDeckFragment.mNavigationDrawerFragment.getView().findViewById(R.id.tvDeckCardCount);
				int cardCount = 0;
				for(int value : customDeck.values()){
					cardCount += value;
				}
				deckCardCount.setText("Card Count: " + cardCount);
			}
		}
	}
	
	private void resetCustomDeck(){
		customDeck = new HashMap<AbstractCard, Integer>();
	    customDeckCardList = new ArrayList<AbstractCard>(customDeck.keySet());
	    currentCustomDeck = null;
	    actionBar.getTabAt(0).setText("Custom Deck");
	    updateCustomDeckData();
	}
	
	private void updateCustomDeck(Deck deck){
		customDeck = new HashMap<AbstractCard, Integer>();
		
		if(deck.deckResources != null){
			for(DeckResource card : deck.deckResources){
				for(AbstractCard masterCard : mAdapter.masterDeckFragment.masterDeck){
					if(masterCard.getID().contentEquals(card.cardID.gUID)){
						customDeck.put(masterCard, card.cardCount);
						break;
					}
				}
			}
		}
		
	    customDeckCardList = new ArrayList<AbstractCard>(customDeck.keySet());
	    updateCustomDeckData();
	}
	
	public int getResourceID(String resourceName, Class mClass){
		int id = -1;
		try {
		    Class res = mClass;
		    Field field = res.getField(resourceName);
		    id = field.getInt(null);
		}
		catch (Exception e) {
			
		}
		
		return id;
	}

}
