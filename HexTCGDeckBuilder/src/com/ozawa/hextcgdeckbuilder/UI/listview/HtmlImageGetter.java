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
package com.ozawa.hextcgdeckbuilder.UI.listview;

import com.ozawa.hextcgdeckbuilder.UI.SymbolTemplate;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.Html;

public class HtmlImageGetter implements Html.ImageGetter{

	private Context context;
	private int textSize;
	
	public HtmlImageGetter(Context context, int textSize) {
		super();
		this.context = context;
		this.textSize = textSize;
	}

	@Override
	public Drawable getDrawable(String arg0) {
		SymbolTemplate template;

		template = SymbolTemplate.findSymbolTemplate(arg0, SymbolTemplate.getAllTemplates(context));
		if(template==null){
			System.err.println("Unable to find drawable for:"+arg0);
			return null;
		}
		Drawable d=context.getResources().getDrawable(template.templateId);
		
		
		d.setBounds(0,0,(int) Math.round(textSize*template.sizeRatio),textSize);
		return d;
	}

}
