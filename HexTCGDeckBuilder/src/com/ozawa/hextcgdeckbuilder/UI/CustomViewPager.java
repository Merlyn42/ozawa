package com.ozawa.hextcgdeckbuilder.UI;

import android.content.Context;
import android.graphics.Point;
import android.os.Build;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;

public class CustomViewPager extends ViewPager {

	private boolean enabled;
	private int screenWidth;
	private int swipeBoundaryRight;
	private int swipeBoundaryLeft;

    @SuppressWarnings("deprecation")
	public CustomViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        /*this.enabled = true;
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Point size = new Point();
        //different methods based on SDK version
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            wm.getDefaultDisplay().getSize(size);
            screenWidth = size.x;
        } else {
            screenWidth = wm.getDefaultDisplay().getWidth();
        }
        
        swipeBoundaryRight = screenWidth - (screenWidth / 15);
        swipeBoundaryLeft = (screenWidth / 15);*/
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        /*if (this.enabled) {
            return super.onTouchEvent(event);
        }*/
        
        /*float location = event.getX();
        System.out.println("******* Screen Width: " + screenWidth);
        System.out.println("******* Location: " + location);
        System.out.println("******* Boundary Right: " + swipeBoundaryRight);
        System.out.println("******* Boundary Left: " + swipeBoundaryLeft);
        if(location < swipeBoundaryRight && location > swipeBoundaryLeft){
        	return super.onTouchEvent(event);
        }*/
  
        return false;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
    	/*if (this.enabled) {
        	return super.onTouchEvent(event);
	    }*/
	    
	
	    return false;
    }
 
    public void setPagingEnabled(boolean enabled) {
        this.enabled = enabled;
    }
    
    @Override
    protected boolean canScroll(View arg0, boolean arg1, int arg2, int arg3, int arg4) {        
        return true;
    }

}
