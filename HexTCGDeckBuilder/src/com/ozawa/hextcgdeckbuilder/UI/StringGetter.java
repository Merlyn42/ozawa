package com.ozawa.hextcgdeckbuilder.UI;

import com.ozawa.hextcgdeckbuilder.hexentities.AbstractCard;
import com.ozawa.hextcgdeckbuilder.util.HexUtil;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.TextView;

public class StringGetter extends AsyncTask<AbstractCard, Void, String> {
	
	Context mContext;
	TextView textView;
	String fieldName;
	String prefix;
	String suffix;
	
	public StringGetter(Context mContext, TextView textView, String fieldName, String prefix, String suffix){
		this.mContext = mContext;
		this.textView = textView;
		this.fieldName = fieldName;
		this.prefix = prefix;
		this.suffix = suffix;
	}

	@Override
	protected String doInBackground(AbstractCard... params) {
		return HexUtil.getCardFieldValueAsString(params[0], fieldName);
	}
	
	@Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);
        if(result != null){
	        if(prefix != null){
	        	result = prefix.concat(result);
	        }
	        
	        if(suffix != null){
	        	result.concat(suffix);
	        }
	        textView.setText(result);
        }
    }
}
