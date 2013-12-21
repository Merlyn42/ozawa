package com.ozawa.android;


import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.ozawa.android.hexentities.AbstractCard;
import com.ozawa.android.json.JsonReader;

/**
 * Created by ckinsella on 19/12/13.
 */

public class DeckListViewActivity extends ActionBarActivity implements NavigationDrawerFragment.NavigationDrawerCallbacks {

    ListView list;
    DeckListViewAdapter adapter;
    private static List<AbstractCard> deck;
    private JsonReader jsonReader;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.deck_list_layout);
        Intent intent = getIntent();
        // If the deck didn't come from the MasterDeckActivityScreen get the deck data.
        if(intent.getBooleanExtra(MasterDeckActivity.GETDECK, true)){
            jsonReader = new JsonReader();
            try {
                deck = jsonReader.deserializeJSONInputStreamsToCard(getJson());
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }else{
            deck = MasterDeckActivity.cardViewer.cards;
        }

        list=(ListView)findViewById(R.id.deck_list);

        // Getting adapter by passing xml data ArrayList
        adapter=new DeckListViewAdapter(this, deck);
        list.setAdapter(adapter);

        // Click event for single list row
        list.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.action_bar_menu, menu);
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
                Intent intent = new Intent(this, MasterDeckActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                intent.putExtra(MasterDeckActivity.GETDECK, false);
                startActivity(intent);
                return true;
            case R.id.action_list_view:
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

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
    public void onResume(){
        super.onResume();
        deck = MasterDeckActivity.cardViewer.cards;
    }

    @Override
    public void onNavigationDrawerItemSelected(int position) {}
}
