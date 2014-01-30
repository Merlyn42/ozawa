/*******************************************************************************
 * Hex TCG Deck Builder
 *     Copyright ( C ) 2014  Chad Kinsella, Dave Kerr and Laurence Reading
 * 
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 * 
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 * 
 *     You should have received a copy of the GNU General Public License
 *     along with this program.  If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/
package com.ozawa.hextcgdeckbuilder.UI.filter;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.AttributeSet;
import android.view.HapticFeedbackConstants;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.ozawa.hextcgdeckbuilder.UI.CardListViewer;
import com.ozawa.hextcgdeckbuilder.enums.CardEnum;

public class FilterButton extends ImageButton implements View.OnClickListener {
    private Bitmap imageOff;
    private Bitmap imageOn;
    private CardEnum e;
    private CardListViewer cardViewer;

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
    public void setUp(Bitmap iImageOn, Bitmap iImageOff, CardEnum iE, CardListViewer iCardViewer) {
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
