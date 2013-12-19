package com.ozawa.android;

import android.app.Activity;
import android.content.res.Resources;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.support.v4.widget.DrawerLayout;
import android.widget.GridView;
import android.widget.TextView;


import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.ozawa.android.enums.CardType;
import com.ozawa.android.enums.ColorFlag;
import com.ozawa.android.filter.Filter;
import com.ozawa.android.hexentities.AbstractCard;
import com.ozawa.android.hexentities.Card;
import com.ozawa.android.json.JsonReader;

import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class MasterDeckActivity extends ActionBarActivity
        implements NavigationDrawerFragment.NavigationDrawerCallbacks {

    protected ImageLoader imageLoader = ImageLoader.getInstance();

    /**
     * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
     */
    private NavigationDrawerFragment mNavigationDrawerFragment;

    /**
     * Used to store the last screen title. For use in {@link #restoreActionBar()}.
     */
    private CharSequence mTitle;
    private static List<AbstractCard> masterDeck;
    private JsonReader jsonReader;
    private static Filter filter = new Filter();
    private ImageAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        filter.removeColor(ColorFlag.COLORLESS);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_master_deck);
        GridView gridView = (GridView) findViewById(R.id.grid_view);
        jsonReader = new JsonReader();
        try {
            masterDeck = jsonReader.deserializeJSONInputStreamsToCard(getJson());
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        adapter = new ImageAdapter(this,filter.filter(masterDeck));
        gridView.setAdapter(adapter);
        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getSupportFragmentManager().findFragmentById(R.id.navigation_drawer);
        mTitle = getTitle();

        // Set up the drawer.
        mNavigationDrawerFragment.setUp(
                R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout));

    }

    public InputStream[] getJson() throws IllegalAccessException {
        Field[] rawFields = R.raw.class.getFields();
        InputStream [] jsonFiles = new InputStream[rawFields.length];

        for(int count=0; count < rawFields.length; count++){
            int rid = rawFields[count].getInt(rawFields[count]);
            try {
                Resources res = getResources();
                InputStream inputStream = res.openRawResource(rid);
                jsonFiles[count] = inputStream;
            } catch (Exception e) {
            }
        }
        return  jsonFiles;
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
                filter.removeColor(ColorFlag.COLORLESS);
                filter.addType(CardType.BASICACTION);
                filter.addType(CardType.TROOP);
                adapter.masterDeck=filter.filter(masterDeck);
                adapter.notifyDataSetChanged();
                break;
            case 2:
                mTitle = getString(R.string.title_section2);
                filter.addColor(ColorFlag.COLORLESS);
                filter.removeType(CardType.TROOP);
                filter.addType(CardType.BASICACTION);
                adapter.masterDeck=filter.filter(masterDeck);
                adapter.notifyDataSetChanged();
                break;
            case 3:
                mTitle = getString(R.string.title_section3);
                filter.addType(CardType.TROOP);
                filter.addColor(ColorFlag.COLORLESS);
                filter.removeType(CardType.BASICACTION);
                adapter.masterDeck=filter.filter(masterDeck);
                adapter.notifyDataSetChanged();
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
        if (!mNavigationDrawerFragment.isDrawerOpen()) {
            // Only show items in the action bar relevant to this screen
            // if the drawer is not showing. Otherwise, let the drawer
            // decide what to show in the action bar.
            getMenuInflater().inflate(R.menu.master_deck, menu);
            restoreActionBar();
            return true;
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
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
            ((MasterDeckActivity) activity).onSectionAttached(
                    getArguments().getInt(ARG_SECTION_NUMBER));
        }
    }

}
