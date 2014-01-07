package com.ozawa.hextcgdeckbuilder.util;

import java.lang.reflect.Field;

import com.ozawa.hextcgdeckbuilder.hexentities.AbstractCard;
import com.ozawa.hextcgdeckbuilder.hexentities.Card;
import com.ozawa.hextcgdeckbuilder.hexentities.ResourceCard;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Point;
import android.os.Build;
import android.view.Display;
import android.view.WindowManager;

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
	public static String getCardStringFieldValue(AbstractCard card, String fieldName){
		try {
			Field field;
			if(card instanceof Card){
				field = Card.class.getField(fieldName);
			}else{
				field = ResourceCard.class.getField(fieldName);
			}
			return (String) field.get(card);			
		} catch (NoSuchFieldException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		}
		return null;
	}

}
