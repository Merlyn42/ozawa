package com.ozawa.hextcgdeckbuilder;

/**
 * Created by dkerr on 12/20/13.
 */
import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.gesture.Gesture;
import android.gesture.GestureLibraries;
import android.gesture.GestureLibrary;
import android.gesture.GestureOverlayView;
import android.gesture.Prediction;
import android.os.Bundle;
import android.view.HapticFeedbackConstants;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.Toast;

public class FullImageActivity extends Activity implements GestureOverlayView.OnGesturePerformedListener {

	private GestureLibrary gesLibrary;
	private int position;
	private ImageView imageView;
	private int cardCount;
	private boolean isMaster;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.full_image);

        // get intent data
        Intent i = getIntent();
        
		gesLibrary = GestureLibraries.fromRawResource(this, R.raw.gestures);
		if (!gesLibrary.load()) {
			this.finish();
		}

        // Selected image id
        position = i.getExtras().getInt("id");
        isMaster = i.getExtras().getBoolean("isMaster");

        imageView = (ImageView) findViewById(R.id.full_image_view);
        
        setImage();
        
        imageView.setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View v) {
				v.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);
				finish();
			}
		});
        
		GestureOverlayView gestureOverlayView = (GestureOverlayView) findViewById(R.id.fullImageGestureOverlayView);
		gestureOverlayView.addOnGesturePerformedListener(this);		
    }
    
    @Override
    protected void onPause(){
    	super.onPause();
    	finish();
    }

	@Override
	public void onGesturePerformed(GestureOverlayView gestureOverlayView, Gesture gesture) {
		ArrayList<Prediction> predictions = gesLibrary.recognize(gesture);
		if (predictions.size() > 0) {
			for (Prediction prediction : predictions) {
				if (prediction.score > 1.0) {
					if (prediction.name.equalsIgnoreCase("swipe right")) {
						if(position > 0){
							position --;
							setImage();
						} else{
							Toast.makeText(this, "No more cards, Jumping to the end", Toast.LENGTH_SHORT).show();
							position = cardCount - 1;
							setImage();
						}
					} else if (prediction.name.equalsIgnoreCase("swipe left")) {
						if(position < cardCount - 1){
							position ++;
							setImage();
						} else{
							position = 0;
							Toast.makeText(this, "No more cards, Jumping to the start", Toast.LENGTH_SHORT).show();
							setImage();
						}						
					}else if(prediction.name.equalsIgnoreCase("clear")){
                    	finish();
                    }
				}
			}
		}
	}
	
	private void setImage(){
		if(isMaster){
        	imageView.setImageBitmap(MasterDeckFragment.cardViewer.getFilteredCardList().get(position).getFullscreenCardBitmap(this));
        	cardCount = MasterDeckFragment.cardViewer.getFilteredCardList().size();
        } else{
        	imageView.setImageBitmap(CustomDeckFragment.cardViewer.getFilteredCardList().get(position).getFullscreenCardBitmap(this));
        	cardCount = CustomDeckFragment.cardViewer.getFilteredCardList().size();
        }
	}
}
