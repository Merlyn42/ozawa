package com.ozawa.hextcgdeckbuilder.UI;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.AttributeSet;
import android.view.HapticFeedbackConstants;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.ozawa.hextcgdeckbuilder.enums.CardEnum;

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
        boolean result  = cardViewer.toggleFilter(e);
        setImageBitmap(result ? imageOn : imageOff);
        this.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);
        StringBuilder toastBuilder = new StringBuilder(result?"Showing ":"Hiding ");
        	toastBuilder.append(e.toString()).append(" cards");
        Toast.makeText(v.getContext(), toastBuilder.toString(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {   	
    	LinearLayout parent = (LinearLayout) getParent();
    	int dim = parent.getMeasuredWidth()/6;
    	
    	setMeasuredDimension(dim, dim);
    }
    public void setUp(Bitmap iImageOn, Bitmap iImageOff, CardEnum iE, CardViewer iCardViewer) {
        imageOff = iImageOff;
        imageOn = iImageOn;
        e = iE;
        cardViewer = iCardViewer;
        setImageBitmap(imageOn);
        setOnClickListener(this);
    }

	public void updateImage() {
		setImageBitmap(cardViewer.isActive(e) ? imageOn : imageOff);
	}
}
