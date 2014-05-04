package hexentities;

import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import json.JsonReader;
import enums.CardType;
import enums.ColorFlag;

/**
 * Stores reference data about CardTemplates
 */
public class CardTemplate {
	public double						top;
	public double						bottom;
	public double						left;
	public double						right;
	public String						templateName;
	transient public URL				templateId;
	public boolean						isTroop;
	public ColorFlag[]					colors;
	public boolean						fullCard;
	public float						thresholdWidthRatio;
	public float						thresholdHeightRatio;
	public float						thresholdPaddingRatio;
	public float						nameFontRatio;
	public float						costFontRatio;
	public float						typeFontRatio;
	public float						socketRatio;
	public double						factionRatio;
	public Integer						currentSubsample;
	public int							nameShortenedText;
	public float						nameWidth;
	public float						nameHeight;
	public float						bigResourceWidth;
	public float						bigResourceHeight;
	public float						smallResourceWidth;
	public float						smallResourceHeight;
	public float						atkWidth;
	public float						atkHeight;
	public float						defWidth;
	public float						defHeight;
	public int							cardTypeShortenedText;
	public float						cardTypeWidth;
	public float						cardTypeHeight;
	public float						factionWidth;
	public float						factionHeight;
	public float						rarityWidth;
	public float						rarityHeight;
	public float						gameTextWidth;
	public float						gameTextHeight;
	public float						socketWidth;
	public float						socketHeight;
	public float						uniqueWidth;
	public float						uniqueHeight;
	public float						numberRatio;
	public float						gameTextLength;
	public float						thresholdWidth;
	public float						thresholdHeight;

	private static List<CardTemplate>	ALLTEMPLATES;
	
	public static final String templateJsonPath = "json/data/card_templates.json";

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
	public static CardTemplate findCardTemplate(Card card, boolean isFullscreen, List<CardTemplate> templates) {
		ArrayList<CardTemplate> results = new ArrayList<CardTemplate>();
		for (CardTemplate template : templates) {
			if (template.fullCard == isFullscreen) {
				if (Arrays.asList(template.colors).containsAll(Arrays.asList(card.colorFlags))) {
					if (card.colorFlags.length > 1 || template.colors.length == 1) {
						if (Arrays.asList(card.cardType).contains(CardType.TROOP) == template.isTroop) {
							results.add(template);
						}
					}
				}
			}
		}
		if (results.size() > 1) {
			System.err.println("More than one valid template found for card:" + card.getM_Name());
		} else if (results.size() == 0) {
			StringBuilder message = new StringBuilder("No valid card template found for card:");
			message.append(card.getM_Name());
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

	public static List<CardTemplate> getAllTemplates(String templateJson) {
		if (ALLTEMPLATES == null) {
			JsonReader jsonReader = new JsonReader();
			ALLTEMPLATES = Arrays.asList(jsonReader.deserializeJSONInputStreamToCardTemplates(ClassLoader.getSystemResourceAsStream(templateJson)));
		}
		return ALLTEMPLATES;

	}

	/*public Bitmap getImage(Context context, int maxWidth) {
		if (image == null || cachedImageWidthLimit != maxWidth) {
			image = decodeImage(context, maxWidth);
			cachedImageWidthLimit = maxWidth;
		}
		return image;
	}

	private Bitmap decodeImage(Context context, int maxWidth) {
		Resources resources = context.getResources();
		BitmapFactory.Options templateFirstOptions = new BitmapFactory.Options();
		templateFirstOptions.inJustDecodeBounds = true;
		BitmapFactory.decodeResource(resources, templateId, templateFirstOptions);
		currentSubsample = 1;
		while (templateFirstOptions.outWidth / currentSubsample / 2 >= maxWidth)
			currentSubsample *= 2;
		// Decode with inSampleSize
		BitmapFactory.Options templateSecondOptions = new BitmapFactory.Options();
		templateSecondOptions.inSampleSize = currentSubsample;
		Bitmap templateImage = BitmapFactory.decodeResource(resources, templateId, templateSecondOptions);
		return templateImage;
	}*/
}