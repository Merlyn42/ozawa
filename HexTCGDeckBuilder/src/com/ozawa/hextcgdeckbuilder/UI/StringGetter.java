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
package com.ozawa.hextcgdeckbuilder.UI;

import java.lang.reflect.Field;

import com.ozawa.hextcgdeckbuilder.hexentities.AbstractCard;
import com.ozawa.hextcgdeckbuilder.hexentities.Card;
import com.ozawa.hextcgdeckbuilder.hexentities.ResourceCard;
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

	
	@SuppressWarnings("unused")
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
