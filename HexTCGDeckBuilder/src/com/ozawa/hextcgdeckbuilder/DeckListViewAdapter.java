package com.ozawa.hextcgdeckbuilder;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.PorterDuff.Mode;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ozawa.hextcgdeckbuilder.UI.ImageGetter;
import com.ozawa.hextcgdeckbuilder.enums.ColorFlag;
import com.ozawa.hextcgdeckbuilder.hexentities.AbstractCard;
import com.ozawa.hextcgdeckbuilder.hexentities.Card;
import com.ozawa.hextcgdeckbuilder.hexentities.CardThreshold;

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
        ImageView cardThreshold = (ImageView) vi.findViewById(R.id.cardthreshold);
        ImageView thumb_image=(ImageView)vi.findViewById(R.id.list_image);
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
		
		if(card instanceof Card && ((Card) card).threshold != null){
			ArrayList<Bitmap> thresholds = new ArrayList<Bitmap>();
			for(CardThreshold threshold : ((Card) card).threshold){
				if (threshold.colorFlags != null) {
					switch (threshold.colorFlags) {
						case COLORLESS:{
							cardThreshold.setImageBitmap(null);
							break;
						}
						case BLOOD: {
							addCardThresholdBitmapToList(thresholds, "cardthresholdblood", threshold.thresholdColorRequirement);
							break;
						}
						case DIAMOND: {
							addCardThresholdBitmapToList(thresholds, "cardthresholddiamond", threshold.thresholdColorRequirement);
							break;
						}
						case RUBY: {
							addCardThresholdBitmapToList(thresholds, "cardthresholdruby", threshold.thresholdColorRequirement);
							break;
						}
						case SAPPHIRE: {
							addCardThresholdBitmapToList(thresholds, "cardthresholdsapphire", threshold.thresholdColorRequirement);
							break;
						}
						case WILD: {
							addCardThresholdBitmapToList(thresholds, "cardthresholdwild", threshold.thresholdColorRequirement);
							break;
						}
						default: {
							break;
						}
					}
				}
			}
			
			if(!thresholds.isEmpty()){
				Bitmap allThresholds = Bitmap.createBitmap(180, 22, Bitmap.Config.ARGB_8888);
				
				Canvas canvas = new Canvas(allThresholds);
				canvas.drawColor(0, Mode.CLEAR);
				int left = 128;
				int top = 0;
				for(Bitmap image : thresholds){
					canvas.drawBitmap(image, left, top, null);
					left-=image.getWidth();
				}
				cardThreshold.setImageBitmap(allThresholds);
			}
		}else{
			cardThreshold.setImageBitmap(null);
		}
        // Setting all values in listview
        cardName.setText(card.name);
        gameText.setText(card.gameText);
        buildCardImage(card, thumb_image);
        return vi;
    }


    protected void buildCardImage(AbstractCard card,ImageView imageView){
        if(imageView.getTag() != null) {
            ((ImageGetter) imageView.getTag()).cancel(true);
        }
        imageView.setImageBitmap(back);
        ImageGetter task = new ImageGetter(imageView,mContext, true) ;
        task.execute(card);
        imageView.setTag(task);
    }
    
    private void addCardThresholdBitmapToList(List<Bitmap> thresholds, String resourcesName, int thresholdCount){
    	for(int i = 0; i < thresholdCount; i++){
			Bitmap thresh = BitmapFactory.decodeResource(mContext.getResources(), ((DeckUIActivity) mContext).getResourceID(resourcesName, R.drawable.class), null);
			thresholds.add(thresh);
		}
    }
}