package com.ozawa.hextcgdeckbuilder.UI;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import android.content.Context;
import android.content.res.Resources;

import com.ozawa.hextcgdeckbuilder.R;
import com.ozawa.hextcgdeckbuilder.enums.CardType;
import com.ozawa.hextcgdeckbuilder.enums.ColorFlag;
import com.ozawa.hextcgdeckbuilder.hexentities.AbstractCard;
import com.ozawa.hextcgdeckbuilder.json.JsonReader;

/**
 * Stores reference data about CardTemplates
 * 
 * @author Laurence
 * 
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
					if (Arrays.asList(card.cardType).contains(CardType.TROOP) ? template.isTroop : !template.isTroop) {
						results.add(template);
					}
				}
			}
		}
		if(results.size()>1){
			System.err.println("More than one valid template found");
		}else if(results.size()==0){
			StringBuilder message = new StringBuilder("Color:");
			for(ColorFlag color :card.colorFlags){
				message.append(color.toString());
				message.append(" ");
			}
			message.append(" Type:");
			for(CardType type : card.cardType){
				message.append(type.toString());
				message.append(" ");
			}
			throw new RuntimeException("No valid card Template found, " +message.toString());
		}
		return results.get(0);
	}
	
	public static List<CardTemplate> getAllTemplates(Context context){
		if (ALLTEMPLATES == null){
			Resources res = context.getResources();
			JsonReader jsonReader = new JsonReader(context);
	        ALLTEMPLATES = Arrays.asList(jsonReader.deserializeJSONInputStreamToCardTemplates(res.openRawResource(R.raw.card_templates)));	
		}
		return ALLTEMPLATES;
		
	}
}
