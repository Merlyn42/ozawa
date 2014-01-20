package com.ozawa.android;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.text.BoringLayout;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.ozawa.android.UI.ImageGetter;
import com.ozawa.android.enums.ColorFlag;
import com.ozawa.android.hexentities.AbstractCard;
import com.ozawa.android.hexentities.Card;

import java.util.List;

/**
 * Created by dkerr on 12/16/13.
 */
public class ImageAdapter extends BaseAdapter {
    private Context mContext;
    public List<AbstractCard> masterDeck;
    private static int cardWidth = 200;
    private Bitmap back;
    private static int numberOfColumns = 3;
    private static int differenceInHeight = 26;

    public ImageAdapter(){}
    public ImageAdapter(Context c, List<AbstractCard> deck ) {
        mContext = c;
        masterDeck = deck;
        back= BitmapFactory.decodeResource(c.getResources(), R.drawable.back);
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

    private void buildCardImage(AbstractCard card,ImageView imageView){
        if(imageView.getTag() != null) {
            ((ImageGetter) imageView.getTag()).cancel(true);
        }
        imageView.setImageBitmap(back);
        ImageGetter task = new ImageGetter(imageView,mContext) ;
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
}