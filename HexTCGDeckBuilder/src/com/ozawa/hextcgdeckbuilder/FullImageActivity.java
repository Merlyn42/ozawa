package com.ozawa.hextcgdeckbuilder;

/**
 * Created by dkerr on 12/20/13.
 */
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;

import com.ozawa.hextcgdeckbuilder.hexentities.Card;

public class FullImageActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.full_image);

        // get intent data
        Intent i = getIntent();

        // Selected image id
        int position = i.getExtras().getInt("id");

        ImageView imageView = (ImageView) findViewById(R.id.full_image_view);
        imageView.setImageBitmap(MasterDeckFragment.cardViewer.getFilteredCardList().get(position).getFullscreenCardBitmap(this));
    }

}
