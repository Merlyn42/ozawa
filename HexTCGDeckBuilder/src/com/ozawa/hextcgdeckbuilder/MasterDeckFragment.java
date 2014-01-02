package com.ozawa.hextcgdeckbuilder;

import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.ozawa.hextcgdeckbuilder.MasterDeckActivity.PlaceholderFragment;
import com.ozawa.hextcgdeckbuilder.UI.CardViewer;
import com.ozawa.hextcgdeckbuilder.UI.CustomViewPager;
import com.ozawa.hextcgdeckbuilder.hexentities.AbstractCard;
import com.ozawa.hextcgdeckbuilder.json.JsonReader;

import android.content.Intent;
import android.content.res.Resources;
import android.gesture.Gesture;
import android.gesture.GestureLibraries;
import android.gesture.GestureLibrary;
import android.gesture.GestureOverlayView;
import android.gesture.Prediction;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.TextView;
import android.widget.Toast;

public class MasterDeckFragment extends Fragment implements NavigationDrawerFragment.NavigationDrawerCallbacks, GestureOverlayView.OnGesturePerformedListener{
	
	private FragmentActivity mainActivity;
	private DrawerLayout uiLayout;
	
	ListView listView;
	ImageAdapter imAdapter;
    DeckListViewAdapter lvAdapter;
    private static List<AbstractCard> deck;
    private JsonReader jsonReader;
    public boolean isGridView;
    
    /**
     * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
     */
    private NavigationDrawerFragment mNavigationDrawerFragment;

    public static CardViewer cardViewer;
    public final static String GETDECK = "GETDECK";
    private GestureLibrary gesLibrary;
	private GridView gridView;
	
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
		mainActivity = super.getActivity();
		jsonReader = new JsonReader(container.getContext());
        try {
            deck = jsonReader.deserializeJSONInputStreamsToCard(getJson());
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        
        gesLibrary = GestureLibraries.fromRawResource(mainActivity, R.raw.gestures);
        if (!gesLibrary.load()) {
        	mainActivity.finish();
        }        

        cardViewer = new CardViewer(mainActivity, deck);
        imAdapter = cardViewer.getAdapter();
        uiLayout = (DrawerLayout) inflater.inflate(R.layout.fragment_master_deck, container, false);
        
        mNavigationDrawerFragment = (NavigationDrawerFragment) mainActivity.getSupportFragmentManager().findFragmentById(R.id.master_deck_navigation_drawer);
        //Set up the drawer.
        mNavigationDrawerFragment.setUp(uiLayout, cardViewer,mainActivity,
                R.id.master_deck_navigation_drawer,
                (DrawerLayout) uiLayout.findViewById(R.id.master_deck_drawer_layout));
        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
        transaction.add(R.id.master_deck_navigation_drawer, mNavigationDrawerFragment).commit();
        
        GestureOverlayView gestureOverlayView = (GestureOverlayView) uiLayout.findViewById(R.id.masterDeckGestureOverlayView);
        gestureOverlayView.addOnGesturePerformedListener(this);

        setUpGridView(); // Set up the card grid view
        
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
                if(name.contains("hexcard")){
                    InputStream inputStream = res.openRawResource(rid);
                    if(inputStream != null){
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
        fragmentManager.beginTransaction()
                .replace(R.id.container, PlaceholderFragment.newInstance(position + 1))
                .commit();
	}

	@Override
	public void onGesturePerformed(GestureOverlayView gestureOverlayView, Gesture gesture) {
		ArrayList<Prediction> predictions = gesLibrary.recognize(gesture);
        if(predictions.size() > 0){
        	for(Prediction prediction : predictions){
        		if(prediction.score > 1.0){
                    if(prediction.name.equalsIgnoreCase("swipe left")){                    	
                        int x = (int)gesture.getStrokes().get(0).points[0];
                        int y = (int)gesture.getStrokes().get(0).points[1];
                        AbstractCard card;
                        int position;
                        if(isGridView){
                        	GridView gridView = (GridView) uiLayout.findViewById(R.id.master_deck_grid_view);
                        	position = gridView.pointToPosition(x,y);
                        	card = imAdapter.masterDeck.get(position);
                        } else {
                        	ListView listView = (ListView) uiLayout.findViewById(R.id.master_deck_deck_list);
                        	position = listView.pointToPosition(x,y);
                        	card = lvAdapter.masterDeck.get(position);
                        }
                                                                               				
        				addCardToCustomDeck(card);
        				Toast.makeText(mainActivity.getApplicationContext(), card.name + " added to custom deck.", Toast.LENGTH_SHORT).show();
                    }else if(prediction.name.equalsIgnoreCase("swipe right")){
                    	CustomViewPager pager = (CustomViewPager) mainActivity.findViewById(R.id.pager);
                    	pager.setCurrentItem(pager.getCurrentItem()-1); // ******* TEMPORARY FIX FOR SLIDING BETWEEN PAGES
                    }
                }
        	}
        }		
	}
	
	public void changeToListView(){
		if(listView != null){
			listView.setVisibility(View.VISIBLE);
			gridView.setVisibility(View.INVISIBLE);
			cardViewer.setAdapter(lvAdapter);
			lvAdapter.updateDeck(imAdapter.masterDeck);
			setIsGridView(false);
		}else{
			setUpListView();
		}
	}
	
	public void changeToGridView(){
		if(gridView != null){
			cardViewer.setAdapter(imAdapter);
			listView.setVisibility(View.INVISIBLE);
			gridView.setVisibility(View.VISIBLE);
			imAdapter.updateDeck(lvAdapter.masterDeck);
			setIsGridView(true);
		}else{
	        setUpGridView();
		}
	}
	
	private void setUpGridView(){
		gridView = (GridView) uiLayout.findViewById(R.id.master_deck_grid_view);

        gridView.setAdapter(cardViewer.getAdapter());

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {

                // Sending image id to FullScreenActivity
                Intent i = new Intent(mainActivity.getApplicationContext(), FullImageActivity.class);
                // passing array index
                i.putExtra("id",position);
                startActivity(i);
            }
        });
        
        gridView.setOnItemLongClickListener(new OnItemLongClickListener(){

			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view,
                    int position, long id) {
				// Add card to custom deck
				AbstractCard card = imAdapter.masterDeck.get(position);
				addCardToCustomDeck(card);
				return true;
			}
		});
        setIsGridView(true);
	}
	
	private void setUpListView(){
		listView = (ListView) uiLayout.findViewById(R.id.master_deck_deck_list);
		        
        // Getting adapter by passing xml data ArrayList
        lvAdapter=new DeckListViewAdapter(mainActivity, cardViewer.getAdapter().masterDeck);
		cardViewer.setAdapter(lvAdapter);
		listView.setAdapter(cardViewer.getAdapter());
        // Click event for single list row
		listView.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
            	
            }
        });
		
		listView.setOnItemLongClickListener(new OnItemLongClickListener(){

			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view,
                    int position, long id) {
				// Add card to custom deck
				AbstractCard card = lvAdapter.masterDeck.get(position);
				addCardToCustomDeck(card);
				return true;
			}
			
		});
		
        
        setIsGridView(false);
	}
	
	public void setIsGridView(boolean isGridView){
		this.isGridView = isGridView;
	}
	
	
	/**
	 * Add a card to the custom deck
	 * 
	 * @param card
	 */
	private void addCardToCustomDeck(AbstractCard card) {
		HashMap<AbstractCard, Integer> customDeck = ((DeckUIActivity) mainActivity).customDeck;
		if(customDeck.get(card) == null){
			customDeck.put(card, 1);
			((DeckUIActivity) mainActivity).customDeckCardList.add(card);
		}else{
			customDeck.put(card, customDeck.get(card) + 1);
		}		
		Toast.makeText(mainActivity.getApplicationContext(), card.name + " added to custom deck.", Toast.LENGTH_SHORT).show();
	}
}
