package com.ozawa.hextcgdeckbuilder;

/**
 * Created by dkerr on 12/20/13.
 */
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;

public class FullImageActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.full_image);

        // get intent data
        Intent i = getIntent();

        // Selected image id
        int position = i.getExtras().getInt("id");
        boolean isMasterDeck = i.getExtras().getBoolean("isMaster");

        ImageView imageView = (ImageView) findViewById(R.id.full_image_view);
        if(isMasterDeck){
        	imageView.setImageBitmap(MasterDeckFragment.cardViewer.getFilteredCardList().get(position).getFullscreenCardBitmap(this));
        } else{
        	imageView.setImageBitmap(CustomDeckFragment.cardViewer.getFilteredCardList().get(position).getFullscreenCardBitmap(this));
        }
        
        imageView.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				finish();
			}
		});
    }
    
    @Override
    protected void onPause(){
    	super.onPause();
    	finish();
    }

}
