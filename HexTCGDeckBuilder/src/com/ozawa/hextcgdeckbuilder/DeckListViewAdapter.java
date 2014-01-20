package com.ozawa.hextcgdeckbuilder;

import java.util.List;
import java.util.Map;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ozawa.hextcgdeckbuilder.UI.ImageGetter;
import com.ozawa.hextcgdeckbuilder.UI.StringGetter;
import com.ozawa.hextcgdeckbuilder.enums.ImageGetterType;
import com.ozawa.hextcgdeckbuilder.hexentities.AbstractCard;
import com.ozawa.hextcgdeckbuilder.util.HexUtil;

/**
 * Created by ckinsella on 19/12/13.
 */

public class DeckListViewAdapter extends ImageAdapter{

    private static LayoutInflater inflater=null;

    public DeckListViewAdapter(Context c, List<AbstractCard> deck, Map<AbstractCard, Integer> customDeck) {
        super(c,deck,customDeck);
        inflater = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public Object getItem(int position) {
        return position;
    }
    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View vi=convertView;
        
        if(convertView==null){
            vi = inflater.inflate(R.layout.deck_list_row, null);
        }
        
        LinearLayout portaitBorder = (LinearLayout) vi.findViewById(R.id.portrait_border);
        TextView cardName = (TextView)vi.findViewById(R.id.card_name);
        TextView gameText = (TextView)vi.findViewById(R.id.gametext);
        TextView cardCost = (TextView)vi.findViewById(R.id.tvCardCost);
        TextView cardAttack = (TextView)vi.findViewById(R.id.tvCardAttack);
        TextView cardDefense = (TextView)vi.findViewById(R.id.tvCardDefense);
        TextView cardCount = (TextView)vi.findViewById(R.id.tvCardCount);
        ImageView cardThreshold = (ImageView) vi.findViewById(R.id.cardthreshold);
        ImageView thumb_image = (ImageView)vi.findViewById(R.id.list_image);
        ImageView imCardAttack = (ImageView)vi.findViewById(R.id.imCardAttack);
        ImageView imCardDefense = (ImageView)vi.findViewById(R.id.imCardDefense);
        
        AbstractCard card = masterDeck.get(position);
        
		if (card.colorFlags.length > 0&&card.colorFlags[0]!=null) {
			switch (card.colorFlags[0]) {
				case COLORLESS: {
					vi.setBackgroundResource(R.drawable.list_selector_colorless);
					portaitBorder.setBackgroundResource(R.drawable.portrait_border_colorless);
					portaitBorder.setPadding(5, 5, 5, 5);
					break;
				}
				case BLOOD: {
					vi.setBackgroundResource(R.drawable.list_selector_blood);
					portaitBorder.setBackgroundResource(R.drawable.portrait_border_blood);
					portaitBorder.setPadding(5, 5, 5, 5);
					break;
				}
				case DIAMOND: {
					vi.setBackgroundResource(R.drawable.list_selector_diamond);
					portaitBorder.setBackgroundResource(R.drawable.portrait_border_diamond);
					portaitBorder.setPadding(5, 5, 5, 5);
					break;
				}
				case RUBY: {
					vi.setBackgroundResource(R.drawable.list_selector_ruby);
					portaitBorder.setBackgroundResource(R.drawable.portrait_border_ruby);
					portaitBorder.setPadding(5, 5, 5, 5);
					break;
				}
				case SAPPHIRE: {
					vi.setBackgroundResource(R.drawable.list_selector_sapphire);
					portaitBorder.setBackgroundResource(R.drawable.portrait_border_sapphire);
					portaitBorder.setPadding(5, 5, 5, 5);
					break;
				}
				case WILD: {
					vi.setBackgroundResource(R.drawable.list_selector_wild);
					portaitBorder.setBackgroundResource(R.drawable.portrait_border_wild);
					portaitBorder.setPadding(5, 5, 5, 5);
					break;
				}
				default: {
					break;
				}
			}
		} else {
			vi.setBackgroundResource(R.drawable.list_selector_colorless);
		}
		
        // Setting all values in listview
		int screenWidth = HexUtil.getScreenWidth(mContext);
        gameText.setWidth((screenWidth / 10) * 7);
        if(card.isTroop()){
        	imCardAttack.setImageResource(R.drawable.gametext_attack);
        	imCardDefense.setImageResource(R.drawable.gametext_defense);        	
        }else{
        	imCardAttack.setImageBitmap(null);
        	imCardDefense.setImageBitmap(null);
        }
        buildCardTextView(card, cardName, "name", null, null);
        buildCardTextView(card, gameText, "gameText", null, null);
        buildCardTextView(card, cardCost, "resourceCost", "Cost: ", null);
        buildCardTextView(card, cardAttack, "baseAttackValue", null, null);
        buildCardTextView(card, cardDefense, "baseHealthValue", null, null);
        buildCardThreshold(card, cardThreshold);
        buildCardImage(card, thumb_image);
        if(customDeck!=null&&customDeck.get(card)!=null){
        	cardCount.setText("Count:"+customDeck.get(card));
        }
        return vi;
    }


    protected void buildCardImage(AbstractCard card,ImageView imageView){
        if(imageView.getTag() != null) {
            ((ImageGetter) imageView.getTag()).cancel(true);
        }
        imageView.setImageBitmap(back);
        ImageGetter task = new ImageGetter(imageView,mContext, ImageGetterType.CARDPORTRAIT,null) ;
        task.execute(card);
        imageView.setTag(task);
    }
    
    private void buildCardThreshold(AbstractCard card,ImageView imageView){
    	if(imageView.getTag() != null) {
            ((ImageGetter) imageView.getTag()).cancel(true);
        }
    	
        ImageGetter task = new ImageGetter(imageView,mContext, ImageGetterType.CARDTHRESHOLD,null) ;
        task.execute(card);
        imageView.setTag(task);
    }
    
    private void buildCardTextView(AbstractCard card, TextView textView, String fieldName, String prefix, String suffix){
    	if(textView.getTag() != null){
    		((StringGetter) textView.getTag()).cancel(true);
    	}
    	
    	if(prefix != null || suffix != null){
    		StringGetter task = new StringGetter(mContext, textView, fieldName, prefix, suffix);
        	task.execute(card);
        	textView.setTag(task);
    	}else{
	    	StringGetter task = new StringGetter(mContext, textView, fieldName);
	    	task.execute(card);
	    	textView.setTag(task);
    	}
    }
}