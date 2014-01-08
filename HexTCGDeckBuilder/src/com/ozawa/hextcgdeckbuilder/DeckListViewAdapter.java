package com.ozawa.hextcgdeckbuilder;

import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ozawa.hextcgdeckbuilder.UI.ImageGetter;
import com.ozawa.hextcgdeckbuilder.UI.StringGetter;
import com.ozawa.hextcgdeckbuilder.enums.CardType;
import com.ozawa.hextcgdeckbuilder.enums.ImageGetterType;
import com.ozawa.hextcgdeckbuilder.hexentities.AbstractCard;
import com.ozawa.hextcgdeckbuilder.hexentities.ResourceCard;
import com.ozawa.hextcgdeckbuilder.util.HexUtil;

/**
 * Created by ckinsella on 19/12/13.
 */

public class DeckListViewAdapter extends ImageAdapter{

    private Context mContext;
    private static LayoutInflater inflater=null;
    private Bitmap back;

    public DeckListViewAdapter(Context c, List<AbstractCard> deck) {
        mContext = c;
        inflater = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.masterDeck = deck;
        back= BitmapFactory.decodeResource(c.getResources(), R.drawable.back);
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

        TextView cardName = (TextView)vi.findViewById(R.id.card_name);
        TextView gameText = (TextView)vi.findViewById(R.id.gametext);
        TextView cardAttack = (TextView)vi.findViewById(R.id.tvCardAttack);
        TextView cardDefense = (TextView)vi.findViewById(R.id.tvCardDefense);
        ImageView cardThreshold = (ImageView) vi.findViewById(R.id.cardthreshold);
        ImageView thumb_image = (ImageView)vi.findViewById(R.id.list_image);
        ImageView imCardAttack = (ImageView)vi.findViewById(R.id.imCardAttack);
        ImageView imCardDefense = (ImageView)vi.findViewById(R.id.imCardDefense);
        
        AbstractCard card = masterDeck.get(position);

		if (card.colorFlags.length > 0&&card.colorFlags[0]!=null) {
			switch (card.colorFlags[0]) {
				case COLORLESS: {
					vi.setBackgroundResource(R.drawable.list_selector_colorless);
					break;
				}
				case BLOOD: {
					vi.setBackgroundResource(R.drawable.list_selector_blood);
					break;
				}
				case DIAMOND: {
					vi.setBackgroundResource(R.drawable.list_selector_diamond);
					break;
				}
				case RUBY: {
					vi.setBackgroundResource(R.drawable.list_selector_ruby);
					break;
				}
				case SAPPHIRE: {
					vi.setBackgroundResource(R.drawable.list_selector_sapphire);
					break;
				}
				case WILD: {
					vi.setBackgroundResource(R.drawable.list_selector_wild);
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
        buildCardTextView(card, cardName, "name");
        buildCardTextView(card, gameText, "gameText");
        buildCardTextView(card, cardAttack, "baseAttackValue");
        buildCardTextView(card, cardDefense, "baseHealthValue");
        buildCardThreshold(card, cardThreshold);
        buildCardImage(card, thumb_image);
        return vi;
    }


    protected void buildCardImage(AbstractCard card,ImageView imageView){
        if(imageView.getTag() != null) {
            ((ImageGetter) imageView.getTag()).cancel(true);
        }
        imageView.setImageBitmap(back);
        ImageGetter task = new ImageGetter(imageView,mContext, ImageGetterType.CARDPORTRAIT) ;
        task.execute(card);
        imageView.setTag(task);
    }
    
    private void buildCardThreshold(AbstractCard card,ImageView imageView){
    	if(imageView.getTag() != null) {
            ((ImageGetter) imageView.getTag()).cancel(true);
        }
    	
        ImageGetter task = new ImageGetter(imageView,mContext, ImageGetterType.CARDTHRESHOLD) ;
        task.execute(card);
        imageView.setTag(task);
    }
    
    private void buildCardTextView(AbstractCard card, TextView textView, String fieldName){
    	if(textView.getTag() != null){
    		((StringGetter) textView.getTag()).cancel(true);
    	}
    	
    	StringGetter task = new StringGetter(mContext, textView, fieldName);
    	task.execute(card);
    	textView.setTag(task);
    }
}