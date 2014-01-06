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
import com.ozawa.hextcgdeckbuilder.enums.ColorFlag;
import com.ozawa.hextcgdeckbuilder.hexentities.AbstractCard;

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

        TextView cardName = (TextView)vi.findViewById(R.id.card_name); // title
        TextView gameText = (TextView)vi.findViewById(R.id.gametext); // artist name
        TextView duration = (TextView)vi.findViewById(R.id.duration); // duration
        ImageView thumb_image=(ImageView)vi.findViewById(R.id.list_image); // thumb image

        AbstractCard card = masterDeck.get(position);

		if (card.colorFlags.length > 0) {
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
        cardName.setText(card.name);
        gameText.setText(card.gameText);
        duration.setText(card.artistName);
        buildCardImage(card, thumb_image);
        return vi;
    }


    protected void buildCardImage(AbstractCard card,ImageView imageView){
        if(imageView.getTag() != null) {
            ((ImageGetter) imageView.getTag()).cancel(true);
        }
        imageView.setImageBitmap(back);
        ImageGetter task = new ImageGetter(imageView,mContext) ;
        task.execute(card);
        imageView.setTag(task);
    }
}