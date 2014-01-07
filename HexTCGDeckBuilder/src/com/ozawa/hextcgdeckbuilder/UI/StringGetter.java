package com.ozawa.hextcgdeckbuilder.UI;

import java.lang.reflect.Field;

import com.ozawa.hextcgdeckbuilder.hexentities.AbstractCard;
import com.ozawa.hextcgdeckbuilder.hexentities.Card;
import com.ozawa.hextcgdeckbuilder.hexentities.ResourceCard;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.TextView;

public class StringGetter extends AsyncTask<AbstractCard, Void, String> {
	
	Context mContext;
	TextView textView;
	String fieldName;
	
	public StringGetter(Context mContext, TextView textView, String fieldName){
		this.mContext = mContext;
		this.textView = textView;
		this.fieldName = fieldName;
	}

	@Override
	protected String doInBackground(AbstractCard... params) {
		return getFieldValue(params[0]);
	}
	
	@Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);
        textView.setText(result);
    }
	
	private String getFieldValue(AbstractCard card){
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
