package com.ozawa.android;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.ozawa.android.UI.ImageGetter;
import com.ozawa.android.enums.ColorFlag;
import com.ozawa.android.hexentities.Card;

import java.util.List;

/**
 * Created by dkerr on 12/16/13.
 */
public class ImageAdapter extends BaseAdapter {
    private Context mContext;
    private List<Card> masterDeck;
    private static int cardWidth = 200;
    private Bitmap back;

    public ImageAdapter(Context c, List<Card> deck ) {
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
            imageView = new ImageView(mContext);
            imageView.setLayoutParams(new GridView.LayoutParams(200, 240));
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageView.setPadding(8, 8, 8, 8);
        } else {
            imageView = (ImageView) convertView;
        }


        buildCardImage(masterDeck.get(position), imageView);
        //imageView.setImageBitmap(buildCardImage(masterDeck.get(position), imageView));
        //imageView.setImageBitmap(combineImages(R.drawable.diamond_troop_cardtemplate,mThumbIds[position]));
        return imageView;
    }

    private void buildCardImage(Card card,ImageView imageView){
        Resources resources = mContext.getResources();
        final int resourceId = resources.getIdentifier(card.cardImagePath.split("\\.")[0], "drawable",
                mContext.getPackageName());
        if(imageView.getTag() != null) {
            ((ImageGetter) imageView.getTag()).cancel(true);
        }
        imageView.setImageBitmap(back);
        ImageGetter task = new ImageGetter(imageView) ;
        task.execute(resources,resourceId);
        imageView.setTag(task);
    }

    private Bitmap buildCardImage2(Card card,ImageView imageView){

        Resources resources = mContext.getResources();
        final int resourceId = resources.getIdentifier(card.cardImagePath.split("\\.")[0], "drawable",
                mContext.getPackageName());

        Bitmap fg = BitmapFactory.decodeResource(resources, R.drawable.diamond_action_cardtemplate);
        Bitmap bg = BitmapFactory.decodeResource(resources, resourceId);

        if(imageView.getTag() != null) {
            ((ImageGetter) imageView.getTag()).cancel(true);
        }
        ImageGetter task = new ImageGetter(imageView) ;
        task.execute(resources,resourceId);
        imageView.setTag(task);

        fg = Bitmap.createScaledBitmap(fg,200,240,false);
        bg = Bitmap.createScaledBitmap(bg,200,200,false);


        Bitmap cardImage;

        cardImage = Bitmap.createBitmap(fg.getWidth(), fg.getHeight(), Bitmap.Config.ARGB_8888);

        Canvas combine = new Canvas(cardImage);

        combine.drawBitmap(bg, 0f, 10f, null);
        combine.drawBitmap(fg, 0f, 0f,null);

        return cardImage;
    }

    private Bitmap combineImages(int card, int portrait){

        Resources resources = mContext.getResources();
        final int resourceId = resources.getIdentifier("hex000034", "drawable",
                mContext.getPackageName());


        Bitmap bg = BitmapFactory.decodeResource(resources,
                card);
        Bitmap fg = BitmapFactory.decodeResource(resources,
                resourceId);
        fg = Bitmap.createScaledBitmap(fg,380,300,false);

        Bitmap cardImage;

        cardImage = Bitmap.createBitmap(bg.getWidth(), bg.getHeight(), Bitmap.Config.ARGB_8888);

        Canvas combine = new Canvas(cardImage);

        combine.drawBitmap (fg, 30f, 10f, null);
        combine.drawBitmap(bg, 0f, 0f,null);

        return cardImage;
    }
}