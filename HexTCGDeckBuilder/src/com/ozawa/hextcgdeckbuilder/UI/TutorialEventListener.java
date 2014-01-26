package com.ozawa.hextcgdeckbuilder.UI;

import android.app.Activity;
import android.widget.Toast;

import com.espian.showcaseview.OnShowcaseEventListener;
import com.espian.showcaseview.ShowcaseView;
import com.ozawa.hextcgdeckbuilder.R;
import com.ozawa.hextcgdeckbuilder.util.HexUtil;

public class TutorialEventListener implements OnShowcaseEventListener {
	
	private int tutCount;
	private Activity activity;
	private ShowcaseView.ConfigOptions co;
	
	public TutorialEventListener(Activity activity,ShowcaseView.ConfigOptions co){
		this.tutCount = 0;
		this.activity = activity;
		this.co = co;
	}
	
	@Override
	public void onShowcaseViewHide(ShowcaseView showcaseView) {
		// TODO Auto-generated method stub
		
	}

	@SuppressWarnings("deprecation")
	@Override
	public void onShowcaseViewDidHide(ShowcaseView showcaseView) {
		switch(tutCount){
		case 0:{ // Card Library introduction
			showcaseView = ShowcaseView.insertShowcaseView(HexUtil.getScreenWidth(activity) / 2, (int)(HexUtil.getScreenHeight(activity) / 15) ,activity,"Card Library", "This is the Card Library. Here is where you will find every card currently in Hex. You can use the Card Library as a reference and also to add cards to custom decks.", co);        
			showcaseView.setShowcase(ShowcaseView.NONE);
			showcaseView.setOnShowcaseEventListener(this);	                
	        showcaseView.show();
	        tutCount++;
	        break;
		}
		case 1:{ // Full Screen
			showcaseView = ShowcaseView.insertShowcaseView(HexUtil.getScreenWidth(activity) / 2, (int)(HexUtil.getScreenHeight(activity) / 2.15), activity, 
					"Fullscreen View", "Click on a card to go fullscreen. Click on that card again to exit fullscreen.", co);   			
			showcaseView.setOnShowcaseEventListener(this);        
	        showcaseView.show();
	        tutCount++;
	        break;
		}		
		case 2:{ //Filter menu tutorial 
			showcaseView = ShowcaseView.insertShowcaseView(HexUtil.getScreenWidth(activity), (int)(HexUtil.getScreenHeight(activity) / 2), activity, "Filter Options", "Swipe left from the edge to bring out the filter menu", co);
	        showcaseView.setOnShowcaseEventListener(this);
			showcaseView.animateGesture(HexUtil.getScreenWidth(activity), HexUtil.getScreenHeight(activity) / 2, HexUtil.getScreenWidth(activity) / 2, HexUtil.getScreenHeight(activity) / 2, true);
	        showcaseView.show();
	        tutCount++;
	        break;
		}		
		case 3:{ //change to list view tutorial
			//showcaseView = ShowcaseView.insertShowcaseView(HexUtil.getScreenWidth(activity)  - (HexUtil.getScreenWidth(activity) / 17), HexUtil.getScreenHeight(activity) / 15, activity, "ListView", "Click here to change view to a listview", co);
			showcaseView = ShowcaseView.insertShowcaseView(activity.findViewById(R.id.action_list_view),activity,"List View", "Click here to change view to a listview", co);
			showcaseView.setScaleMultiplier(.5f);
	        showcaseView.setOnShowcaseEventListener(this);
			showcaseView.show();			
			tutCount++;
			break;
		}
		case 4:{ // change to deck view
			showcaseView = ShowcaseView.insertShowcaseView(activity.findViewById(R.id.action_deck_view),activity,"Deck View", "Click here to change view to a deck view.", co);
			showcaseView.setScaleMultiplier(.5f);
	        showcaseView.setOnShowcaseEventListener(this);
			showcaseView.show();			
			tutCount++;
			break;
		}
		case 5:{ // Add card to custom deck
			showcaseView = ShowcaseView.insertShowcaseView(HexUtil.getScreenWidth(activity) / 2, (int)(HexUtil.getScreenHeight(activity) / 2.15), activity, "Add Cards to Custom Deck", "Swipe left to add a card to your custom deck.", co);        
			showcaseView.setOnShowcaseEventListener(this);
	        showcaseView.animateGesture(HexUtil.getScreenWidth(activity) / 2, (int)(HexUtil.getScreenHeight(activity) / 2.1), (int)(HexUtil.getScreenWidth(activity) / 4), (int)(HexUtil.getScreenHeight(activity) / 2.1), true);        
	        showcaseView.show();
	        tutCount++;
	        break;
		}
		case 6:{ // Add multiple cards to custom deck
			showcaseView = ShowcaseView.insertShowcaseView(HexUtil.getScreenWidth(activity) / 2, (int)(HexUtil.getScreenHeight(activity) / 2.15), activity, "Add Multiple Cards at Once", "Hold down to add multiple cards to your custom deck.", co);        
			showcaseView.setOnShowcaseEventListener(this);
	        showcaseView.animateGesture(HexUtil.getScreenWidth(activity) / 2, (int)(HexUtil.getScreenHeight(activity) / 1.5), (int)(HexUtil.getScreenWidth(activity) / 2), (int)(HexUtil.getScreenHeight(activity) / 2.1), true);        
	        showcaseView.show();
	        tutCount++;
	        break;
		}
		case 7:{ //Go to custom deck
			showcaseView = ShowcaseView.insertShowcaseView(HexUtil.getScreenWidth(activity)/2, (int)(HexUtil.getScreenHeight(activity) / 2), activity, "Custom Deck Screen", "Swipe right to view the custom deck.", co);
	        showcaseView.setOnShowcaseEventListener(this);
			showcaseView.animateGesture(HexUtil.getScreenWidth(activity) / 2, HexUtil.getScreenHeight(activity) / 2, HexUtil.getScreenWidth(activity), HexUtil.getScreenHeight(activity) / 2, true);
	        showcaseView.show();
	        tutCount++;
	        break;
		}
		case 8:{ // Custom Deck Options
			showcaseView = ShowcaseView.insertShowcaseView(0, (int)(HexUtil.getScreenHeight(activity) / 2), activity, "Custom Deck Options", "Swipe right from the edge to bring out the filter menu and deck options.", co);
	        showcaseView.setOnShowcaseEventListener(this);
			showcaseView.animateGesture(0, HexUtil.getScreenHeight(activity) / 2, HexUtil.getScreenWidth(activity) / 2, HexUtil.getScreenHeight(activity) / 2, true);
	        showcaseView.show();
	        tutCount++;
	        break;
		}
		case 9:{ // Remove card from custom deck
			showcaseView = ShowcaseView.insertShowcaseView(HexUtil.getScreenWidth(activity)/7, (int)(HexUtil.getScreenHeight(activity) / 4), activity, "Remove Card from Custom Deck", "Swipe right or hold down to remove a card from your custom deck.", co);        
			showcaseView.setOnShowcaseEventListener(this);
	        showcaseView.animateGesture(0, (int)(HexUtil.getScreenHeight(activity) / 4), (int)HexUtil.getScreenWidth(activity) /2, (int)(HexUtil.getScreenHeight(activity) / 4), true);        
	        showcaseView.show();
	        tutCount++;
	        break;
		}
		case 10:{ // Finish
			showcaseView = ShowcaseView.insertShowcaseView((int)HexUtil.getScreenWidth(activity) - 20, (int)(HexUtil.getScreenHeight(activity) / 15), activity,"Your done!", 
					"That's it. The rest of the functionality should be pretty intuatuive. You can view this tutorial again, anytime, by click the settings button and choosing the \"View Tutorial\" option. Have fun and keep an eye out for more awesome updates coming to this app soon!", 
					co);
			showcaseView.setScaleMultiplier(.5f);
	        showcaseView.setOnShowcaseEventListener(this);
			showcaseView.show();			
			tutCount++;
			break;
		}
		default:
			//No more tutorials to show
			break;
		}
		
	}

	@Override
	public void onShowcaseViewShow(ShowcaseView showcaseView) {
		// TODO Auto-generated method stub
		
	}

}
