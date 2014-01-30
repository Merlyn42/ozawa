/*******************************************************************************
 * Hex TCG Deck Builder
 *     Copyright ( C ) 2014  Chad Kinsella, Dave Kerr and Laurence Reading
 * 
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 * 
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 * 
 *     You should have received a copy of the GNU General Public License
 *     along with this program.  If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/
package com.ozawa.hextcgdeckbuilder.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.math.BigDecimal;

import com.ozawa.hextcgdeckbuilder.UI.listview.HtmlImageGetter;
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

/**
 * Utility class that holds reusable methods for the application
 */
public class HexUtil {
	
	private HexUtil(){}
	
	/**
	 * Get the width of the device's screen
	 * 
	 * @param context
	 * @return the pixel width of the screen
	 */
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
	
	/**
	 * Get the height of the device's screen
	 * 
	 * @param context
	 * @return the pixel height of the screen
	 */
	@SuppressWarnings("deprecation")
	@SuppressLint("NewApi")
    public static int getScreenHeight(Context context){
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
	
	/**
	 * Convert a desired density pixel value into a screen pixels value
	 * 
	 * @param mContext
	 * @param dp
	 * @return the converted screen pixel value
	 */
	public static int convertDensityPixelsToPixels(Context mContext, int dp) {
	    DisplayMetrics displayMetrics = mContext.getResources().getDisplayMetrics();
	    int px = Math.round(dp * (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));       
	    return px;
	}
	
	/**
	 * Parse HTML from given a String
	 * 
	 * @param text
	 * @param context
	 * @param height
	 * @return displayable styled text from the provided HTML string
	 */
	public static Spanned parseStringAsHexHtml(String text,Context context,int height){
		text=text.replaceAll("\\[([0-9])\\]", "{$1}");
		text=text.replace("[", "<img src=\"");
		text=text.replace("]", "\"/>");
		text=text.replaceAll("\\{([0-9])\\}", "[$1]");
		return Html.fromHtml(text,new HtmlImageGetter(context,height),null);
	}
	
	/**
	 * Replace text with HTML
	 * 
	 * @param view
	 * @param text
	 */
	public static void populateTextViewWithHexHtml(TextView view,String text){
		view.setText(parseStringAsHexHtml(text, view.getContext(), Float.valueOf(view.getTextSize()).intValue()));
	}
	
	/**
	 * Move an ImageView from one location to another via animation
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
		TranslateAnimation imageAnimation = new TranslateAnimation(fromX, toX, fromY, toY);
		imageAnimation.setDuration(400/(1+repeatCount)+20);
		imageAnimation.setFillAfter(true);
		imageAnimation.setRepeatCount(repeatCount);
		imageAnimation.setAnimationListener(new AnimationListener() {    
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
		image.startAnimation(imageAnimation);
	}
	
	/**
	 * Move an image from the given arguments
	 * 
	 * @param arg
	 */
	public static void moveImageAnimation(AnimationArg arg){
		moveImageAnimation(arg.image, arg.fromX, arg.toX, arg.fromY, arg.toY, arg.duration, arg.repeatCount);
	}
	
	/**
	 * Used to hold an ImageView and all the required variable to animate
	 * the given ImageView
	 */
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
	
	/**
	 * Convert an inputstream to a byte[]
	 * 
	 * @param is
	 * @return the resulting byte[]
	 */
	public static byte[] toByteArray(InputStream is){
		ByteArrayOutputStream buffer = new ByteArrayOutputStream();

		int nRead;
		byte[] data = new byte[700000];

		try {
			while ((nRead = is.read(data, 0, data.length)) != -1) {
			  buffer.write(data, 0, nRead);
			}
			buffer.flush();
		} catch (IOException e) {
		}
		
		return buffer.toByteArray();
	}
	
	/**
	 * Round a double to the specified number of decimal places
	 * 
	 * @param unrounded
	 * @param precision
	 * @param roundingMode
	 * @return a rounded double
	 */
	public static double round(double unrounded, int precision, int roundingMode)
	{
	    BigDecimal bd = new BigDecimal(unrounded);
	    BigDecimal rounded = bd.setScale(precision, roundingMode);
	    return rounded.doubleValue();
	}
}
