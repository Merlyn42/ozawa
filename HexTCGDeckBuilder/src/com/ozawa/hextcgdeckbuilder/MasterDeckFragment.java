package com.ozawa.hextcgdeckbuilder;

import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.ArrayList;
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
import android.widget.GridView;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

public class MasterDeckFragment extends Fragment implements NavigationDrawerFragment.NavigationDrawerCallbacks, GestureOverlayView.OnGesturePerformedListener{
	
	private FragmentActivity mainActivity;
	private DrawerLayout uiLayout;
	
	ListView listView;
	ImageAdapter imAdapter;
    DeckListViewAdapter lvAdapter;
    private static List<AbstractCard> deck;
    private JsonReader jsonReader;
    
    /**
     * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
     */
    private NavigationDrawerFragment mNavigationDrawerFragment;

    public static CardViewer cardViewer;
    public final static String GETDECK = "GETDECK";
    private GestureLibrary gesLibrary;
	private GridView gridView;
    
/*	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
		mainActivity = super.getActivity();
		Intent intent = mainActivity.getIntent();
        //View rootView = inflater.inflate(R.layout.activity_main, container, false);
        listLayout = (DrawerLayout) inflater.inflate(R.layout.deck_list_layout, container, false); 
        
        jsonReader = new JsonReader();
        try {
            deck = jsonReader.deserializeJSONInputStreamsToCard(getJson());
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        
        list=(ListView) listLayout.findViewById(R.id.deck_list);
        
        // Getting adapter by passing xml data ArrayList
        adapter=new DeckListViewAdapter(mainActivity, deck);
        list.setAdapter(adapter);

        // Click event for single list row
        list.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

            }
        });
        
        return listLayout;
    }*/
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
		mainActivity = super.getActivity();
		jsonReader = new JsonReader();
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
        uiLayout = (DrawerLayout) inflater.inflate(R.layout.master_deck_fragment, container, false);
        //setContentView(R.layout.activity_master_deck);


        gridView = (GridView) uiLayout.findViewById(R.id.grid_view);

        gridView.setAdapter(cardViewer.getAdapter());
        mNavigationDrawerFragment = (NavigationDrawerFragment) mainActivity.getSupportFragmentManager().findFragmentById(R.id.navigation_drawer);
        this.getResources();
        //Set up the drawer.
        mNavigationDrawerFragment.setUp(uiLayout, cardViewer,mainActivity,
                R.id.navigation_drawer,
                (DrawerLayout) uiLayout.findViewById(R.id.drawer_layout));
        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
        transaction.add(R.id.navigation_drawer, mNavigationDrawerFragment).commit();
        GestureOverlayView gestureOverlayView = (GestureOverlayView) uiLayout.findViewById(R.id.masterDeckGestureOverlayView);
        

        gestureOverlayView.addOnGesturePerformedListener(this);

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
        
        return uiLayout;
    }
	
	public ArrayList<InputStream> getJson() throws IllegalAccessException {
        Field[] rawFields = R.raw.class.getFields();
        ArrayList<InputStream> jsonFiles = new ArrayList<InputStream>();
        //InputStream[] jsonFiles = new InputStream[rawFields.length];

        for (int count = 0; count < rawFields.length; count++) {
            int rid = rawFields[count].getInt(rawFields[count]);
            try {
                Resources res = getResources();
                String name = res.getResourceName(rid);
                if(!name.contains("gestures")){
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
		// TODO Auto-generated method stub
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
        			System.out.println("********** " + prediction.name + " ***********");
                    if(prediction.name.equalsIgnoreCase("swipe left")){
                        /*GridView gridView = (GridView) uiLayout.findViewById(R.id.grid_view);
                        int x = (int)gesture.getStrokes().get(0).points[0];
                        int y = (int)gesture.getStrokes().get(0).points[1];
                        int test = gridView.pointToPosition(x,y);
                        AbstractCard card = cardViewer.getFilteredCardList().get(test);
                        Intent i = new Intent(mainActivity.getApplicationContext(), FullImageActivity.class);
                        // passing array index
                        i.putExtra("id",test);
                        startActivity(i);*/
                    	CustomViewPager pager = (CustomViewPager) mainActivity.findViewById(R.id.pager);
                    	pager.setPagingEnabled(true);
                    	System.out.println("********** SWIPED LEFT ***********");
                    }else if(prediction.name.equalsIgnoreCase("swipe right")){
                    	CustomViewPager pager = (CustomViewPager) mainActivity.findViewById(R.id.pager);
                    	pager.setPagingEnabled(true);
                    	System.out.println("********** SWIPED RIGHT ***********");
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
		}else{
			//ViewGroup vg = (ViewGroup)(gridView.getParent());
			//vg.removeView(gridView);
			//cardViewer = new CardViewer(mainActivity, cardViewer.getFilteredCardList(), true);
			listView = (ListView) uiLayout.findViewById(R.id.deck_list);
			        
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
	        
	        GestureOverlayView gestureOverlayView = (GestureOverlayView) uiLayout.findViewById(R.id.masterDeckGestureOverlayView);
	        
	        gestureOverlayView.addOnGesturePerformedListener(this);
		}
	}
	
	public void changeToGridView(){
		if(gridView != null){
			cardViewer.setAdapter(imAdapter);
			listView.setVisibility(View.INVISIBLE);
			gridView.setVisibility(View.VISIBLE);
			imAdapter.updateDeck(lvAdapter.masterDeck);
		}else{
			System.out.println("******** Where's my GridView? ********");
		}
		//ViewGroup vg = (ViewGroup)(listView.getParent());
		//vg.removeView(listView);
		
	}
}
