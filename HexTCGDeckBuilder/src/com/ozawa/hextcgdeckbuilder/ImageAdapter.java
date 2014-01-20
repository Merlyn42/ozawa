package com.ozawa.hextcgdeckbuilder;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.text.BoringLayout;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.ozawa.hextcgdeckbuilder.UI.CardViewer;
import com.ozawa.hextcgdeckbuilder.UI.ImageGetter;
import com.ozawa.hextcgdeckbuilder.enums.ColorFlag;
import com.ozawa.hextcgdeckbuilder.enums.ImageGetterType;
import com.ozawa.hextcgdeckbuilder.hexentities.AbstractCard;
import com.ozawa.hextcgdeckbuilder.hexentities.Card;

import java.util.List;
import java.util.Map;

/**
 * Created by dkerr on 12/16/13.
 */
public class ImageAdapter extends BaseAdapter {
    protected Context mContext;
    public List<AbstractCard> masterDeck;
    private static int cardWidth = 200;
    protected Bitmap back;
    private static int numberOfColumns = 3;
    private static int differenceInHeight = 26;
    Map<AbstractCard, Integer> customDeck;

    public boolean isListView = false;
    private static LayoutInflater inflater=null;
    
    public ImageAdapter(){}
    public ImageAdapter(Context c, List<AbstractCard> deck, Map<AbstractCard, Integer> customDeck ) {
        mContext = c;
        masterDeck = deck;
        back= BitmapFactory.decodeResource(c.getResources(), R.drawable.back);
        this.customDeck=customDeck;
        inflater = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public int getCount() {
        return masterDeck.size();
    }

    public Object getItem(int position) {
        return null;
    }

    public long getItemId(int position) {
        return 0;
    }

    // create a new ImageView for each item referenced by the Adapter
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView imageView;
        if (convertView == null) {  // if it's not recycled, initialize some attributes
            // Find the width and height of the screen and set the card dimensions
            WindowManager wm = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
            DisplayMetrics metrics = new DisplayMetrics();
            wm.getDefaultDisplay().getMetrics(metrics);
            int cardWidth = (metrics.widthPixels / numberOfColumns) - (metrics.widthPixels / 20);
            int cardHeight = cardWidth + differenceInHeight;

            imageView = new ImageView(mContext);
            imageView.setLayoutParams(new GridView.LayoutParams(cardWidth, cardHeight));
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageView.setPadding(8, 8, 8, 8);
        } else {
            imageView = (ImageView) convertView;
        }

        buildCardImage(masterDeck.get(position), imageView);
        return imageView;
    }

    protected void buildCardImage(AbstractCard card,ImageView imageView){
        if(imageView.getTag() != null) {
            ((ImageGetter) imageView.getTag()).cancel(true);
        }
        imageView.setImageBitmap(back);
        ImageGetter task = new ImageGetter(imageView,mContext, ImageGetterType.CARDTHUMBNAIL,customDeck) ;
        task.execute(card);
        imageView.setTag(task);
    }

    public int getDPIFromPixels(Context context, float pixels){
        float density = context.getResources().getDisplayMetrics().density;

        return (int)(pixels / density);
    }

    public void updateDeck(List<AbstractCard> cards) {
        masterDeck=cards;
        notifyDataSetChanged();
    }
    
    public void updateDeckAndCardViewDeck(List<AbstractCard> cards, CardViewer cardViewer) {        
        cardViewer.setCardList(cards); // Update CardViewer Deck
        masterDeck=cardViewer.getFilteredCardList();
        notifyDataSetChanged();
    }
}