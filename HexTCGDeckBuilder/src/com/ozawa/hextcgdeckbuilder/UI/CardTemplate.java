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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.ozawa.hextcgdeckbuilder.R;
import com.ozawa.hextcgdeckbuilder.enums.CardType;
import com.ozawa.hextcgdeckbuilder.enums.ColorFlag;
import com.ozawa.hextcgdeckbuilder.hexentities.AbstractCard;
import com.ozawa.hextcgdeckbuilder.json.JsonReader;

/**
 * Stores reference data about CardTemplates 
 */
public class CardTemplate {
	public double						top;
	public double						bottom;
	public double						left;
	public double						right;
	public String						templateName;
	transient public int				templateId;
	public boolean						isTroop;
	public ColorFlag					color;
	public boolean						fullCard;
	public float						nameFontRatio;
	public float						costFontRatio;
	public float						typeFontRatio;
	public float						socketRatio;
	private Bitmap						image;
	private int							cachedImageWidthLimit;

	private static List<CardTemplate>	ALLTEMPLATES;

	/**
	 * Find the appropriate template for a provided card.
	 * 
	 * @param card
	 *            The card to find the template for
	 * @param isFullscreen
	 *            Whether the image is going to be shown fullscreen
	 * @param templates
	 *            The list of templates to search
	 * @return The CardTemplate object that represents the correct template for
	 *         the card parameter
	 */
	public static CardTemplate findCardTemplate(AbstractCard card, boolean isFullscreen, List<CardTemplate> templates) {
		ArrayList<CardTemplate> results = new ArrayList<CardTemplate>();
		for (CardTemplate template : templates) {
			if (template.fullCard == isFullscreen) {
				if (Arrays.asList(card.colorFlags).contains(template.color)) {
					if (Arrays.asList(card.cardType).contains(CardType.TROOP) == template.isTroop) {
						results.add(template);
					}
				}
			}
		}
		if (results.size() > 1) {
			System.err.println("More than one valid template found for card:" + card.name);
		} else if (results.size() == 0) {
			StringBuilder message = new StringBuilder("No valid card template found for card:");
			message.append(card.name);
			message.append(" Color:");
			for (ColorFlag color : card.colorFlags) {
				message.append(color == null ? "null" : color.toString());
				message.append(" ");
			}
			message.append(" Type:");
			for (CardType type : card.cardType) {
				message.append(type == null ? "null" : type.toString());
				message.append(" ");
			}
			System.err.println(message.toString());
			results.add(templates.get(0));
		}
		return results.get(0);
	}

	public static List<CardTemplate> getAllTemplates(Context context) {
		if (ALLTEMPLATES == null) {
			Resources res = context.getResources();
			JsonReader jsonReader = new JsonReader(context);
			ALLTEMPLATES = Arrays.asList(jsonReader.deserializeJSONInputStreamToCardTemplates(res.openRawResource(R.raw.card_templates)));
		}
		return ALLTEMPLATES;

	}

	public Bitmap getImage(Context context,int maxWidth) {
        if (image == null || cachedImageWidthLimit !=maxWidth) {
        	image = decodeImage(context, maxWidth);
        	cachedImageWidthLimit=maxWidth;
        }
		return image;
	}
	
	private Bitmap decodeImage(Context context,int maxWidth){
		Resources resources = context.getResources();
        BitmapFactory.Options templateFirstOptions = new BitmapFactory.Options();
        templateFirstOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(resources, templateId, templateFirstOptions);
        int scale = 1;
        while (templateFirstOptions.outWidth / scale / 2 >= maxWidth)
            scale *= 2;
        //Decode with inSampleSize
        BitmapFactory.Options templateSecondOptions = new BitmapFactory.Options();
        templateSecondOptions.inSampleSize = scale;
        Bitmap templateImage = BitmapFactory.decodeResource(resources, templateId, templateSecondOptions);
        return templateImage;
	}
}
