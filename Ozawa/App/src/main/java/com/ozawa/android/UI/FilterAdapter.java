package com.ozawa.android.UI;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.ozawa.android.R;

/**
 * Created by Laurence on 19/12/13.
 */
public class FilterAdapter extends BaseAdapter {
    private Context mContext;

    public FilterAdapter(Context c){
        mContext = c;
    }

    @Override
    public int getCount() {
        return 5;
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ImageView imageView;
        if (view == null) {  // if it's not recycled, initialize some attributes
            imageView = new ImageView(mContext);
            imageView.setLayoutParams(new GridView.LayoutParams(100, 100));
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageView.setPadding(8, 8, 8, 8);
        } else {
            imageView = (ImageView) view;
        }
        Resources r = mContext.getResources();
        Bitmap back = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.back);
        imageView.setImageBitmap(back);
        return imageView;
    }
}
