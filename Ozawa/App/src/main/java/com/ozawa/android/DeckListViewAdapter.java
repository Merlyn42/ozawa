package com.ozawa.android;

import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.ArrayList;
        import java.util.HashMap;
import java.util.List;

import android.app.Activity;
        import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
        import android.view.View;
        import android.view.ViewGroup;
        import android.widget.BaseAdapter;
        import android.widget.ImageView;
        import android.widget.TextView;

import com.ozawa.android.UI.ImageGetter;
import com.ozawa.android.hexentities.AbstractCard;

/**
 * Created by ckinsella on 19/12/13.
 */

public class DeckListViewAdapter extends ImageAdapter{

    private Context mContext;
    private Activity activity;
    private static LayoutInflater inflater=null;
    private List<AbstractCard> deck;
    private Bitmap back;

    public DeckListViewAdapter(Context c, List<AbstractCard> deck) {
        mContext = c;
        inflater = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.deck = deck;
        back= BitmapFactory.decodeResource(c.getResources(), R.drawable.back);
    }

    @Override
    public int getCount() {
        return deck.size();
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
        if(convertView==null)
            vi = inflater.inflate(R.layout.deck_list_row, null);

        TextView cardName = (TextView)vi.findViewById(R.id.card_name); // title
        TextView gameText = (TextView)vi.findViewById(R.id.gametext); // artist name
        TextView duration = (TextView)vi.findViewById(R.id.duration); // duration
        ImageView thumb_image=(ImageView)vi.findViewById(R.id.list_image); // thumb image

        AbstractCard card = deck.get(position);

        // Setting all values in listview
        cardName.setText(card.name);
        gameText.setText(card.gameText);
        duration.setText(card.artistName);
        buildCardImage(card, thumb_image);
        return vi;
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
}