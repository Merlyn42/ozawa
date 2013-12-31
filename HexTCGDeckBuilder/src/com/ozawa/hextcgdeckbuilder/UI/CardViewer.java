package com.ozawa.hextcgdeckbuilder.UI;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;

import com.ozawa.hextcgdeckbuilder.ImageAdapter;
import com.ozawa.hextcgdeckbuilder.enums.Attribute;
import com.ozawa.hextcgdeckbuilder.enums.CardEnum;
import com.ozawa.hextcgdeckbuilder.enums.CardType;
import com.ozawa.hextcgdeckbuilder.enums.ColorFlag;
import com.ozawa.hextcgdeckbuilder.filter.Filter;
import com.ozawa.hextcgdeckbuilder.hexentities.AbstractCard;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Laurence on 19/12/13.
 */
public class CardViewer implements TextWatcher {
    private Filter filter;
    private List<AbstractCard> cards;
    private ImageAdapter adapter;

    public CardViewer(Context context,List<AbstractCard> abstractCards){
        filter = new Filter();
        cards= new ArrayList<AbstractCard>(abstractCards);
        adapter = new ImageAdapter(context,filter.filter(cards));
    }
    
    public List<AbstractCard> getFilteredCardList(){
    	return filter.filter(cards);
    }
    
    public List<AbstractCard> getUnFilteredCardList(){
    	return new ArrayList<AbstractCard>(cards);
    }
    
    public void setCardList(List<AbstractCard> abstractCards){
    	cards= new ArrayList<AbstractCard>(abstractCards);
    }

    public void toggleAttribute(Attribute attribute) {
        if(filter.isActive(attribute)){
            filter.removeAttribute(attribute);
        }else{
            filter.addAttribute(attribute);
        }
        adapter.updateDeck(filter.filter(cards));
    }

    public void toggleColor(ColorFlag color) {
        if(filter.isActive(color)){
            filter.removeColor(color);
        }else{
            filter.addColor(color);
        }
        adapter.updateDeck(filter.filter(cards));
    }

    public void toggleFilter(CardEnum e){
        if(filter.isActive(e)){
            filter.removeFilter(e);
        }else{
            filter.addFilter(e);
        }
        adapter.updateDeck(filter.filter(cards));
    }

    public void toggleType(CardType type) {
        if(filter.isActive(type)){
            filter.removeType(type);
        }else{
            filter.addType(type);
        }
        adapter.updateDeck(filter.filter(cards));
    }

    public boolean isActive(CardEnum e) {
        return filter.isActive(e);
    }

    public ImageAdapter getAdapter() {
        return adapter;
    }
    
    public void setAdapter(ImageAdapter adapter){
    	this.adapter = adapter;
    }

	@Override
	public void afterTextChanged(Editable arg0) {
		filter.setFilterString(arg0.toString());
		adapter.updateDeck(filter.filter(cards));
		
	}

	@Override
	public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
			int arg3) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
		// TODO Auto-generated method stub
		
	}
}
