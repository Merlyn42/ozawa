package com.ozawa.android;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.gesture.Gesture;
import android.gesture.GestureLibraries;
import android.gesture.GestureLibrary;
import android.gesture.GestureOverlayView;
import android.gesture.Prediction;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;


import com.nostra13.universalimageloader.core.ImageLoader;
import com.ozawa.android.UI.CardViewer;
import com.ozawa.android.filter.Filter;
import com.ozawa.android.hexentities.AbstractCard;
import com.ozawa.android.hexentities.Card;
import com.ozawa.android.json.JsonReader;

import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class MasterDeckActivity extends ActionBarActivity
        implements NavigationDrawerFragment.NavigationDrawerCallbacks, GestureOverlayView.OnGesturePerformedListener {

    protected ImageLoader imageLoader = ImageLoader.getInstance();

    /**
     * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
     */
    private NavigationDrawerFragment mNavigationDrawerFragment;

    /**
     * Used to store the last screen title. For use in {@link #restoreActionBar()}.
     */
    private CharSequence mTitle;
    private List<AbstractCard> masterDeck;
    private JsonReader jsonReader;
    private static Filter filter = new Filter();
    private ImageAdapter adapter;
    public static CardViewer cardViewer;
    public final static String GETDECK = "GETDECK";
    private GestureLibrary gesLibrary;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        jsonReader = new JsonReader();
        try {
            masterDeck = jsonReader.deserializeJSONInputStreamsToCard(getJson());
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        gesLibrary = GestureLibraries.fromRawResource(this, R.raw.gestures);
        if (!gesLibrary.load()) {
            finish();
        }

        cardViewer = new CardViewer(this, masterDeck);
        setContentView(R.layout.activity_master_deck);


        GridView gridView = (GridView) findViewById(R.id.grid_view);

        gridView.setAdapter(cardViewer.getAdapter());
        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getSupportFragmentManager().findFragmentById(R.id.navigation_drawer);
        mTitle = getTitle();
        this.getResources();
        //Set up the drawer.
        mNavigationDrawerFragment.setUp(cardViewer,this,
                R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout));

        GestureOverlayView gestureOverlayView = (GestureOverlayView) findViewById(R.id.gestureOverlayView);

        gestureOverlayView.addOnGesturePerformedListener(this);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {

                // Sending image id to FullScreenActivity
                Intent i = new Intent(getApplicationContext(), FullImageActivity.class);
                // passing array index
                i.putExtra("id",position);
                startActivity(i);
            }
        });
    }

    @Override
    public void onGesturePerformed(GestureOverlayView gestureOverlayView, Gesture gesture) {

        ArrayList<Prediction> predictions = gesLibrary.recognize(gesture);
        if(predictions.size() > 0){
            Prediction prediction = predictions.get(0);
            if(prediction.score > 1.0){
                if(prediction.name.equalsIgnoreCase("swipe left")){
                    GridView gridView = (GridView)findViewById(R.id.grid_view);
                    int x = (int)gesture.getStrokes().get(0).points[0];
                    int y = (int)gesture.getStrokes().get(0).points[1];
                    int test = gridView.pointToPosition(x,y);
                    AbstractCard card = cardViewer.cards.get(test);
                    Intent i = new Intent(getApplicationContext(), FullImageActivity.class);
                    // passing array index
                    i.putExtra("id",test);
                    startActivity(i);

                }
            }
        }
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
                    else{
                        String string = "String";
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
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.container, PlaceholderFragment.newInstance(position + 1))
                .commit();
    }

    public void onSectionAttached(int number) {
        switch (number) {
            case 1:
                mTitle = getString(R.string.title_section1);
                break;
            case 2:
                mTitle = getString(R.string.title_section2);
                break;
            case 3:
                mTitle = getString(R.string.title_section3);
                break;
        }
    }

    public void restoreActionBar() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle(mTitle);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        /*if (!mNavigationDrawerFragment.isDrawerOpen()) {
            // Only show items in the action bar relevant to this screen
            // if the drawer is not showing. Otherwise, let the drawer
            // decide what to show in the action bar.
            getMenuInflater().inflate(R.menu.action_bar_menu, menu);
            restoreActionBar();
            return true;
        }*/
        getMenuInflater().inflate(R.menu.action_bar_menu, menu);
        //restoreActionBar();
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        // Open the list view for the deck
        switch (item.getItemId()) {
            case R.id.action_deck_view:
                return true;
            case R.id.action_list_view:
                Intent intent = new Intent(this, DeckListViewActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                intent.putExtra(GETDECK, false);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_master_deck, container, false);
            return rootView;
        }

        @Override
        public void onAttach(Activity activity) {
            super.onAttach(activity);
            /*((MasterDeckActivity) activity).onSectionAttached(
                    getArguments().getInt(ARG_SECTION_NUMBER));*/
        }
    }
}