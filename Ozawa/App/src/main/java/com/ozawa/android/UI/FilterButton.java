package com.ozawa.android.UI;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageButton;

import com.ozawa.android.enums.CardEnum;

/**
 * Created by lreading on 20/12/13.
 */
public class FilterButton extends ImageButton implements View.OnClickListener {
    private Bitmap imageOff;
    private Bitmap imageOn;
    private CardEnum e;
    private CardViewer cardViewer;

    public FilterButton(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public FilterButton(Context context) {
        super(context);
    }

    public FilterButton(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void onClick(View v) {
        cardViewer.toggleFilter(e);
        setImageBitmap(cardViewer.isActive(e) ? imageOn : imageOff);
    }

    public void setUp(Bitmap iImageOn, Bitmap iImageOff, CardEnum iE, CardViewer iCardViewer) {
        imageOff = iImageOff;
        imageOn = iImageOn;
        e = iE;
        cardViewer = iCardViewer;
        setImageBitmap(imageOn);
        setOnClickListener(this);
    }
}
