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
package com.ozawa.hextcgdeckbuilder.UI;

import java.util.Map;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.ozawa.hextcgdeckbuilder.R;
import com.ozawa.hextcgdeckbuilder.json.JsonReader;

public class SymbolTemplate {

	public int templateId;
	public String cardText;
	public String imageName;
	public double sizeRatio;
	private Bitmap image;
	
	private static Map<String,SymbolTemplate>	ALLTEMPLATES;
	
	public static Map<String, SymbolTemplate> getAllTemplates(Context context) {
		if (ALLTEMPLATES == null) {
			Resources res = context.getResources();
			JsonReader jsonReader = new JsonReader(context);
			ALLTEMPLATES = jsonReader.deserializeJSONInputStreamToSymbolTemplates(res.openRawResource(R.raw.symbols));
		}		
		return ALLTEMPLATES;
	}
	
	public static SymbolTemplate findSymbolTemplate(String symbol, Map<String,SymbolTemplate> templates){
		return templates.get(symbol);
	}
	
	public Bitmap getImage(Context context,int width, int height) {
        if (image == null) {
        	image = decodeImage(context,width,height);
        }
		return image;
	}
	
	private Bitmap decodeImage(Context context,int width, int height){
		Resources resources = context.getResources();
        Bitmap symbolImage = BitmapFactory.decodeResource(resources, templateId, null);
        return Bitmap.createScaledBitmap(symbolImage, width,height, false);
	}
}
