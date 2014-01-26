package com.ozawa.hextcgdeckbuilder.UI;

import android.app.Activity;
import android.widget.Toast;

import com.espian.showcaseview.OnShowcaseEventListener;
import com.espian.showcaseview.ShowcaseView;
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
		case 0: //Filter menu tutorial 
			showcaseView = ShowcaseView.insertShowcaseView(HexUtil.getScreenWidth(activity), (int)(HexUtil.getScreenHeigth(activity) / 2), activity, "Gestures", "Swipe left from the edge to bring out the filter menu", co);
	        showcaseView.setOnShowcaseEventListener(this);
			showcaseView.animateGesture(HexUtil.getScreenWidth(activity), HexUtil.getScreenHeigth(activity) / 2, HexUtil.getScreenWidth(activity) / 2, HexUtil.getScreenHeigth(activity) / 2, true);
	        showcaseView.show();
	        showcaseView.animate();
	        tutCount++;
	        break;
		case 1: //Go to custom deck
			showcaseView = ShowcaseView.insertShowcaseView(HexUtil.getScreenWidth(activity)/2, (int)(HexUtil.getScreenHeigth(activity) / 2), activity, "Gestures", "Swipe right to go to the custom deck", co);
	        showcaseView.setOnShowcaseEventListener(this);
			showcaseView.animateGesture(HexUtil.getScreenWidth(activity) / 2, HexUtil.getScreenHeigth(activity) / 2, HexUtil.getScreenWidth(activity), HexUtil.getScreenHeigth(activity) / 2, true);
	        showcaseView.show();
	        showcaseView.animate();
	        tutCount++;
	        break;
		case 2: //change to list view tutorial
			showcaseView = ShowcaseView.insertShowcaseView(HexUtil.getScreenWidth(activity)  - (HexUtil.getScreenWidth(activity) / 17), HexUtil.getScreenHeigth(activity) / 15, activity, "ListView", "Click here to change view to a listview", co);
			showcaseView.setScaleMultiplier(.5f);
	        showcaseView.setOnShowcaseEventListener(this);
			showcaseView.show();			
			tutCount++;
			break;
		case 3: //click for fullscreen
			showcaseView = ShowcaseView.insertShowcaseView(HexUtil.getScreenWidth(activity) / 2, (int)(HexUtil.getScreenHeigth(activity) / 1.45), activity, "Fullscreen", "Click on a card to go fullscreen", co);        
			showcaseView.setOnShowcaseEventListener(this);        
	        showcaseView.show();
	        tutCount++;
	        break;
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
