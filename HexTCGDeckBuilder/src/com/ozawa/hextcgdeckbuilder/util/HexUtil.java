package com.ozawa.hextcgdeckbuilder.util;

import java.lang.reflect.Field;

import com.ozawa.hextcgdeckbuilder.HtmlImageGetter;
import com.ozawa.hextcgdeckbuilder.hexentities.AbstractCard;
import com.ozawa.hextcgdeckbuilder.hexentities.Card;
import com.ozawa.hextcgdeckbuilder.hexentities.ResourceCard;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Point;
import android.os.Build;
import android.text.Html;
import android.text.Spanned;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.view.animation.Animation.AnimationListener;
import android.widget.ImageView;
import android.widget.TextView;

public class HexUtil {
	
	private HexUtil(){}
	
	@SuppressWarnings("deprecation")
	@SuppressLint("NewApi")
    public static int getScreenWidth(Context context){
    	int measuredWidth = 0;
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Point size = new Point();
        //different methods based on SDK version
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            wm.getDefaultDisplay().getSize(size);
            measuredWidth = size.x;
        } else {
            Display d = wm.getDefaultDisplay();
            measuredWidth = d.getWidth();
        }
        return measuredWidth;
    }
	
	@SuppressWarnings("deprecation")
	@SuppressLint("NewApi")
    public static int getScreenHeigth(Context context){
    	int measuredHeigth = 0;
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Point size = new Point();
        //different methods based on SDK version
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            wm.getDefaultDisplay().getSize(size);
            measuredHeigth = size.y;
        } else {
            Display d = wm.getDefaultDisplay();
            measuredHeigth = d.getHeight();
        }
        return measuredHeigth;
    }
	
	/**
	 * Get the resource's ID
	 * 
	 * @param resourceName - the name of the resource
	 * @param mClass - the class of the resource e.g. R.drawable.class
	 * @return the ID for the desired resource
	 */
	@SuppressWarnings("rawtypes")
	public static int getResourceID(String resourceName, Class mClass){
		int id = -1;
		try {
		    Class res = mClass;
		    Field field = res.getField(resourceName);
		    id = field.getInt(null);
		}
		catch (Exception e) {
			
		}
		
		return id;
	}
	
	/**
	 * Return the value of a field on a Card or ResourceCard object
	 * 
	 * @param card - the card who's field value is needed
	 * @param fieldName - the name of the field
	 * @return the value of the card's field
	 */
	public static String getCardFieldValueAsString(AbstractCard card, String fieldName){
		try {
			Field field;
			if(card instanceof Card){
				if((fieldName.contentEquals("baseAttackValue") || fieldName.contentEquals("baseHealthValue")) && !card.isTroop()){
					return null;
				}
				field = Card.class.getField(fieldName);
			}else{
				field = ResourceCard.class.getField(fieldName);
			}
			return String.valueOf(field.get(card));			
		} catch (NoSuchFieldException e) {
			
		} catch (IllegalAccessException e) {
			
		} catch (IllegalArgumentException e) {
			
		}
		return null;
	}
	
	public static int convertDensityPixelsToPixels(Context mContext, int dp) {
	    DisplayMetrics displayMetrics = mContext.getResources().getDisplayMetrics();
	    int px = Math.round(dp * (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));       
	    return px;
	}
	
	public static Spanned parseStringAsHexHtml(String text,Context context,int height){
		text=text.replaceAll("\\[([0-9])\\]", "{$1}");
		text=text.replace("[", "<img src=\"");
		text=text.replace("]", "\"/>");
		text=text.replaceAll("\\{([0-9])\\}", "[$1]");
		return Html.fromHtml(text,new HtmlImageGetter(context,height),null);
	}
	
	public static void populateTextViewWithHexHtml(TextView view,String text){
		view.setText(parseStringAsHexHtml(text, view.getContext(), Float.valueOf(view.getTextSize()).intValue()));
	}
	
	/**
	 * Move an ImageView from one location to anoter via animation
	 * 
	 * @param image
	 * @param fromX
	 * @param toX
	 * @param fromY
	 * @param toY
	 * @param duration - the length of the animation in milliseconds
	 * @param repeatCount - the number of times to repeat the animation i.e to play 4 times, the repeat count should be 3
	 */
	public static void moveImageAnimation(final ImageView image, int fromX, int toX, int fromY, int toY, int duration, int repeatCount){
		TranslateAnimation moveCard = new TranslateAnimation(fromX, toX, fromY, toY);
		moveCard.setDuration(400);
		moveCard.setFillAfter(true);
		moveCard.setAnimationListener(new AnimationListener() {    
			@Override
			public void onAnimationEnd(Animation animation) {
				image.setVisibility(View.INVISIBLE);
			}
			@Override
			public void onAnimationRepeat(Animation animation) {
				
			}
			@Override
			public void onAnimationStart(Animation animation) {
				image.setVisibility(View.VISIBLE);
			}
	    });
		image.startAnimation(moveCard);
	}
	
	public static void moveImageAnimation(AnimationArg arg){
		moveImageAnimation(arg.image, arg.fromX, arg.toX, arg.fromY, arg.toY, arg.duration, arg.repeatCount);
	}
	
	public static class AnimationArg{
		
		public AnimationArg(ImageView image, int fromX, int toX, int fromY, int toY, int duration, int repeatCount) {
			super();
			this.image = image;
			this.fromX = fromX;
			this.toX = toX;
			this.fromY = fromY;
			this.toY = toY;
			this.duration = duration;
			this.repeatCount = repeatCount;
		}
		public ImageView image;
		public int fromX;
		public int toX;
		public int fromY;
		public int toY;
		public int duration;
		public int repeatCount;	
	}

}
