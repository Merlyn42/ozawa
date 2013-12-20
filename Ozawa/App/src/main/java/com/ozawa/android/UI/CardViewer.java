package com.ozawa.android.UI;

import android.content.Context;

import com.ozawa.android.ImageAdapter;
import com.ozawa.android.enums.Attribute;
import com.ozawa.android.enums.CardType;
import com.ozawa.android.enums.ColorFlag;
import com.ozawa.android.filter.Filter;
import com.ozawa.android.hexentities.AbstractCard;

import java.util.List;

/**
 * Created by Laurence on 19/12/13.
 */
public class CardViewer {
    private Filter filter;
    private static List<AbstractCard> cards;
    private ImageAdapter adapter;

    public CardViewer(Context context,List<AbstractCard> abstractCards){
        filter = new Filter();
        cards=abstractCards;
        adapter = new ImageAdapter(context,filter.filter(cards));
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

    public void toggleType(CardType type) {
        if(filter.isActive(type)){
            filter.removeType(type);
        }else{
            filter.addType(type);
        }
        adapter.updateDeck(filter.filter(cards));
    }

    public boolean isActive(Attribute attribute) {
        return isActive(attribute);
    }

    public boolean isActive(CardType type) {
        return isActive(type);
    }

    public boolean isActive(ColorFlag color) {
        return isActive(color);
    }

    public ImageAdapter getAdapter() {
        return adapter;
    }
}
