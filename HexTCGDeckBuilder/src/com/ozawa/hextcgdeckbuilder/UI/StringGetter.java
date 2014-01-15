package com.ozawa.hextcgdeckbuilder.UI;

import java.lang.reflect.Field;

import com.ozawa.hextcgdeckbuilder.HtmlImageGetter;
import com.ozawa.hextcgdeckbuilder.hexentities.AbstractCard;
import com.ozawa.hextcgdeckbuilder.hexentities.Card;
import com.ozawa.hextcgdeckbuilder.hexentities.ResourceCard;
import com.ozawa.hextcgdeckbuilder.util.HexUtil;

import android.content.Context;
import android.os.AsyncTask;
import android.text.Html;
import android.widget.TextView;

public class StringGetter extends AsyncTask<AbstractCard, Void, String> {
	
	Context mContext;
	TextView textView;
	String fieldName;
	String prefix;
	String suffix;
	
	public StringGetter(Context mContext, TextView textView, String fieldName){
		this.mContext = mContext;
		this.textView = textView;
		this.fieldName = fieldName;
	}
	
	public StringGetter(Context mContext, TextView textView, String fieldName, String prefix, String suffix){
		this.mContext = mContext;
		this.textView = textView;
		this.fieldName = fieldName;
		this.prefix = prefix;
		this.suffix = suffix;
	}

	@Override
	protected String doInBackground(AbstractCard... params) {
		return HexUtil.getCardFieldValueAsString(params[0], this.fieldName);
	}
	
	@Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);
        textView.setText(result);
        if(result != null){
	        if(prefix != null){
	        	result = prefix.concat(result);
	        }
	        
	        if(suffix != null){
	        	result.concat(suffix);
	        }
	        HexUtil.populateTextViewWithHexHtml(textView, result);
        }
    }

	
	private String getFieldValue(AbstractCard card){
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
}
