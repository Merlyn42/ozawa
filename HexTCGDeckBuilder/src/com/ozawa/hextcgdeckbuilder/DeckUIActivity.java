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

import java.util.ArrayList;
import java.util.HashMap;

import com.ozawa.hextcgdeckbuilder.UI.CardsViewer;
import com.ozawa.hextcgdeckbuilder.UI.CustomViewPager;
import com.ozawa.hextcgdeckbuilder.UI.TabPagerAdapter;
import com.ozawa.hextcgdeckbuilder.UI.customdeck.CustomDeckFragment;
import com.ozawa.hextcgdeckbuilder.UI.customdeck.LoadDeckDialogFragment.LoadDeckListener;
import com.ozawa.hextcgdeckbuilder.UI.customdeck.NewDeckDialogFragment.NewDeckListener;
import com.ozawa.hextcgdeckbuilder.UI.filter.FilterDrawerFragment;
import com.ozawa.hextcgdeckbuilder.database.DatabaseHandler;
import com.ozawa.hextcgdeckbuilder.hexentities.AbstractCard;
import com.ozawa.hextcgdeckbuilder.hexentities.Deck;
import com.ozawa.hextcgdeckbuilder.hexentities.DeckResource;
import com.ozawa.hextcgdeckbuilder.json.MasterDeck;
import com.ozawa.hextcgdeckbuilder.util.HexUtil;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBar.Tab;

public class DeckUIActivity extends ActionBarActivity implements ActionBar.TabListener, FilterDrawerFragment.NavigationDrawerCallbacks{
	
    public static CardsViewer cardViewer;
    
	private CustomViewPager viewPager;
    private TabPagerAdapter mAdapter;
    public static ActionBar actionBar;
    // Tab titles
    private String[] tabs = {"Custom Deck", "Card Library"};
 
    @Override
    protected void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.deck_ui_activity);
        
	    // Initilization
	    viewPager = (CustomViewPager) findViewById(R.id.pager);
	    actionBar = getSupportActionBar();
	    mAdapter = new TabPagerAdapter(getSupportFragmentManager());
	    
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
            case R.id.view_tutorial:{
            	viewPager.setCurrentItem(1); // Change to Card Library
            	masterDeckFragment.showTutorial();
            	return true;
            }
            default:
                return super.onOptionsItemSelected(item);
        }

    }

	@Override
	public void onNavigationDrawerItemSelected(int position) {
	}
	
	@Override
	public void onStart(){
		super.onStart();
		if(mAdapter == null || mAdapter.masterDeckFragment == null || mAdapter.customDeckFragment == null){
			mAdapter = new TabPagerAdapter(getSupportFragmentManager());
		}
	}
	
	@Override
	public void onResume(){
		super.onResume();
		if(mAdapter == null || mAdapter.masterDeckFragment == null || mAdapter.customDeckFragment == null){
			mAdapter = new TabPagerAdapter(getSupportFragmentManager());
		}
	}

	@Override
	public void onTabReselected(Tab arg0,
			android.support.v4.app.FragmentTransaction arg1) {		
	}

	@Override
	public void onTabSelected(Tab tab,
			android.support.v4.app.FragmentTransaction fragTransaction) {
		viewPager.setCurrentItem(tab.getPosition()); // Change to the tab when clicked
		
	}

	@Override
	public void onTabUnselected(Tab arg0,
			android.support.v4.app.FragmentTransaction arg1) {		
	}

}
