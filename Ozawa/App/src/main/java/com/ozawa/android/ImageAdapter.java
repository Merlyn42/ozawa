package com.ozawa.android;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

/**
 * Created by dkerr on 12/16/13.
 */
public class ImageAdapter extends BaseAdapter {
    private Context mContext;

    public ImageAdapter(Context c) {
        mContext = c;
    }

    public int getCount() {
        return mThumbIds.length;
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
            imageView.setLayoutParams(new GridView.LayoutParams(180, 240));
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageView.setPadding(8, 8, 8, 8);
        } else {
            imageView = (ImageView) convertView;
        }

        imageView.setImageResource(mThumbIds[position]);
        return imageView;
    }

    // references to our images
    private Integer[] mThumbIds = {
            R.drawable.hex000034,
            R.drawable.hex000038,
            R.drawable.hex000039,
            R.drawable.hex000040,
            R.drawable.hex000045,
            R.drawable.hex000053,
            R.drawable.hex000054,
            R.drawable.hex000060,
            R.drawable.hex000061,
            R.drawable.hex000070,
            R.drawable.hex000071,
            R.drawable.hex000072,
            R.drawable.hex000091,
            R.drawable.hex000105,
            R.drawable.hex000110,
            R.drawable.hex000114,
            R.drawable.hex000121,
            R.drawable.hex000122,
            R.drawable.hex000131,
            R.drawable.hex000132,
            R.drawable.hex000138,
            R.drawable.hex000144,
            R.drawable.hex000148,
            R.drawable.hex000153,
            R.drawable.hex000154,
            R.drawable.hex000155,
            R.drawable.hex000157,
            R.drawable.hex000164,
            R.drawable.hex000166,
            R.drawable.hex000171,
            R.drawable.hex000176,
            R.drawable.hex000181,
            R.drawable.hex000184,
            R.drawable.hex000191,
            R.drawable.hex000204,
            R.drawable.hex000211,
            R.drawable.hex000214,
            R.drawable.hex000219,
            R.drawable.hex000220,
            R.drawable.hex000223,
            R.drawable.hex000237,
            R.drawable.hex000239,
            R.drawable.hex000247,
            R.drawable.hex000259,
            R.drawable.hex000266,
            R.drawable.hex000270,
            R.drawable.hex000280,
            R.drawable.hex000283,
            R.drawable.hex000286,
            R.drawable.hex000299,
            R.drawable.hex000305,
            R.drawable.hex000311,
            R.drawable.hex000312,
            R.drawable.hex000314
    };
}