package com.ozawa.hextcgdeckbuilder.UI;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

public class CustomViewPager extends ViewPager {
	
	public CustomViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
  
        return false;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
	    return false;
    }
    
    @Override
    protected boolean canScroll(View arg0, boolean arg1, int arg2, int arg3, int arg4) {        
        return true;
    }

}
