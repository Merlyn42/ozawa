package com.ozawa.hextcgdeckbuilder.hexentities;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.PorterDuff.Mode;
import android.graphics.Typeface;
import android.text.InputFilter.LengthFilter;

import com.google.gson.annotations.SerializedName;
import com.ozawa.hextcgdeckbuilder.R;
import com.ozawa.hextcgdeckbuilder.UI.CardTemplate;
import com.ozawa.hextcgdeckbuilder.UI.ImageCache;
import com.ozawa.hextcgdeckbuilder.UI.SymbolTemplate;
import com.ozawa.hextcgdeckbuilder.UI.ImageCache.CacheType;
import com.ozawa.hextcgdeckbuilder.UI.ImageCache.ImageType;
import com.ozawa.hextcgdeckbuilder.enums.Attribute;
import com.ozawa.hextcgdeckbuilder.enums.CardType;
import com.ozawa.hextcgdeckbuilder.enums.ColorFlag;
import com.ozawa.hextcgdeckbuilder.util.HexUtil;

/**
 * A non-resource card.
 * 
 * @author Chad Kinsella
 */
public class Card extends AbstractCard {

	@SerializedName("m_Faction")
	public String				faction;
	@SerializedName("m_SocketCount")
	public int					socketCount;
	@SerializedName("m_AttributeFlags")
	public Attribute[]			attributeFlags;
	@SerializedName("m_ResourceCost")
	public int					resourceCost;
	@SerializedName("m_Threshold")
	public ResourceThreshold[]	threshold;
	@SerializedName("m_BaseAttackValue")
	public String				baseAttackValue;
	@SerializedName("m_BaseHealthValue")
	public String				baseHealthValue;
	@SerializedName("m_FlavorText")
	public String				flavorText;
	@SerializedName("m_ResourceSymbolImagePath")
	public String				resourceSymbolImagePath;
	@SerializedName("m_Unique")
	public Boolean				unique;
	@SerializedName("m_EquipmentSlots")
	public GlobalIdentifier[]	equipmentSlots;
	@SerializedName("m_VariableCost")
	public int				variableCost;
	@SerializedName("m_VariableAttack")
	public int				variableAttack;
	@SerializedName("m_VariableHealth")
	public int				variableHealth;

	private float				line;
	private static final int	fullTemplateWidth		= 890;
	private static final int	fullTemplateHeigth		= 1240;
	private static final int	thumbnailTemplateWidth	= 500;
	private static final int	thumbnailTemplateHeigth	= 560;

	/**
	 * Creates or retrives the card image including portrait, template and text.
	 * 
	 * @param context
	 *            The context to use to retrieve the image.
	 * @return The bitmap of the card or null if no portrait is found
	 * @Author Laurence Reading
	 */

	@Override
	public Bitmap getCardBitmap(Context context, CardTemplate template, int maxWidth) {
		Resources resources = context.getResources();
		final int portraitId = resources.getIdentifier(cardImagePath.split("\\.")[0], "drawable", context.getPackageName());

		// no resourceID found
		if (portraitId == 0)
			return null;

		// find the correct template
		Bitmap templateImage = template.getImage(context, maxWidth);

		// get the portrait image
		BitmapFactory.Options portraitFirstOptions = new BitmapFactory.Options();
		portraitFirstOptions.inJustDecodeBounds = true;
		BitmapFactory.decodeResource(resources, portraitId, portraitFirstOptions);
		// used to scale the image, use only the part of the image to determine
		// scaling.
		int cutPortraitWidth = Double.valueOf(
				portraitFirstOptions.outWidth * defaultLayout.portraitRight - portraitFirstOptions.outWidth * defaultLayout.portraitLeft)
				.intValue();
		int scale = 1;
		while (cutPortraitWidth / scale / 2 >= maxWidth)
			scale *= 2;
		// Decode with inSampleSize
		BitmapFactory.Options portraitSecondOptions = new BitmapFactory.Options();
		portraitSecondOptions = new BitmapFactory.Options();
		portraitSecondOptions.inSampleSize = scale;
		Bitmap portrait = BitmapFactory.decodeResource(resources, portraitId, portraitSecondOptions);

		Bitmap result = Bitmap.createBitmap(templateImage.getWidth(), templateImage.getHeight(), Bitmap.Config.ARGB_8888);
		Canvas combine = new Canvas(result);
		Rect dstRect = new Rect();
		Rect srcRect = new Rect();
		dstRect.top = (int) (templateImage.getHeight() * template.top);
		dstRect.bottom = (int) (templateImage.getHeight() * template.bottom);
		dstRect.right = (int) (templateImage.getWidth() * template.right);
		dstRect.left = (int) (templateImage.getWidth() * template.left);
		srcRect.left = Double.valueOf(portrait.getWidth() * defaultLayout.portraitLeft).intValue();
		srcRect.right = Double.valueOf(portrait.getWidth() * defaultLayout.portraitRight).intValue();
		srcRect.top = Double.valueOf(portrait.getWidth() * defaultLayout.portraitTop).intValue();
		srcRect.bottom = Double.valueOf(portrait.getWidth() * defaultLayout.portraitBottom).intValue();

		combine.drawBitmap(portrait, srcRect, dstRect, null);
		combine.drawBitmap(templateImage, 0f, 0f, null);

		Paint paint = new Paint();
		paint.setTextAlign(Paint.Align.LEFT);
		paint.setColor(-1);
		paint.setAntiAlias(true);

		Bitmap threshold = getCardThresholdImage(context, template, templateImage);
		if (template.fullCard) {
			drawFullImageText(combine, templateImage, paint, resources, context, template);
			if (threshold != null) {
				combine.drawBitmap(threshold, (templateImage.getWidth() / 14), (templateImage.getHeight() / 9.5f), null);
			}
		} else {
			drawThumbnailText(combine, templateImage, paint, template);
			if (threshold != null) {
				combine.drawBitmap(threshold, (templateImage.getWidth() / 14), (templateImage.getHeight() / 4.2f), null);
			}
		}

		return result;
	}

	private void drawThumbnailText(Canvas combine, Bitmap templateImage, Paint paint, CardTemplate template) {
		float imageHeight = templateImage.getHeight();
		paint.setTextSize(imageHeight * template.nameFontRatio);
		combine.drawText(getShortenedText(name, 17), templateImage.getWidth() / 3.3f, templateImage.getHeight() / 11f, paint);
		paint.setTextSize(imageHeight * template.costFontRatio);
		if (resourceCost > 9 ) {
			combine.drawText("" + resourceCost, templateImage.getWidth() / 9.5f, templateImage.getHeight() / 7.2f, paint);
		} else if(variableCost){
			if(resourceCost == 0)
				combine.drawText("X", templateImage.getWidth() / 9.5f, templateImage.getHeight() / 7.2f, paint);
			else
				combine.drawText(resourceCost + "X", templateImage.getWidth() / 9.5f, templateImage.getHeight() / 7.2f, paint);
		}else {
			combine.drawText("" + resourceCost, templateImage.getWidth() / 7.7f, templateImage.getHeight() / 7.2f, paint);
		}
		if (cardType[0].equals(CardType.TROOP)) {			
			if(variableAttack)
				combine.drawText("X", templateImage.getWidth() / 9, templateImage.getHeight() - (templateImage.getHeight() / 11f),paint);
			else
				combine.drawText(baseAttackValue, templateImage.getWidth() / 9, templateImage.getHeight() - (templateImage.getHeight() / 11f),paint);
			if(variableHealth)
				combine.drawText("X", templateImage.getWidth() - (templateImage.getWidth() / 6.8f), templateImage.getHeight()- (templateImage.getHeight() / 11f), paint);
			else
				combine.drawText(baseHealthValue, templateImage.getWidth() - (templateImage.getWidth() / 6.8f), templateImage.getHeight()- (templateImage.getHeight() / 11f), paint);
		} else {
			paint.setTextSize(imageHeight * template.typeFontRatio);
			String cardTypes = "";
			for (int i = 0; i < cardType.length; i++) {
				cardTypes += cardType[i].getCardType();
				if (i != cardType.length - 1)
					cardTypes += ", ";
			}
			if (!cardSubtype.equals(""))
				cardTypes += " -- " + cardSubtype;

			float tempDivider = 6.2f;
			combine.drawText(getShortenedText(cardTypes, 24), templateImage.getWidth() / 11,
					templateImage.getHeight() - (templateImage.getHeight() / tempDivider), paint);
		}
	}

	private void drawFullImageText(Canvas combine, Bitmap templateImage, Paint paint, Resources resources, Context context,
			CardTemplate template) {
		float imageHeight = templateImage.getHeight();
		float tempRatio = template.nameFontRatio;
		paint.setTextSize(imageHeight * tempRatio);
		combine.drawText(name, templateImage.getWidth() / 6, templateImage.getHeight() / 14, paint);
		if (resourceCost > 9) {
			combine.drawText("" + resourceCost, templateImage.getWidth() / 15, templateImage.getHeight() / 15, paint);
		}else if(variableCost){
			if(resourceCost == 0){
				combine.drawText("X", templateImage.getWidth() / 13, templateImage.getHeight() / 15, paint);
			}
			combine.drawText(resourceCost + "X", templateImage.getWidth() / 13, templateImage.getHeight() / 15, paint);
		}else {
			combine.drawText("" + resourceCost, templateImage.getWidth() / 13, templateImage.getHeight() / 15, paint);
		}

		if (cardType[0].equals(CardType.TROOP)) {
			if(variableAttack){
				combine.drawText("X", templateImage.getWidth() / 17, templateImage.getHeight() - (templateImage.getHeight() / 25),paint);
			}else{
				combine.drawText(baseAttackValue, templateImage.getWidth() / 17, templateImage.getHeight() - (templateImage.getHeight() / 25),paint);
			}
			if(variableHealth){
				combine.drawText("X", templateImage.getWidth() - (templateImage.getWidth() / 11.5f), templateImage.getHeight()
					- (templateImage.getHeight() / 25), paint);
			} else {
				combine.drawText(baseHealthValue, templateImage.getWidth() - (templateImage.getWidth() / 11.5f), templateImage.getHeight()
						- (templateImage.getHeight() / 25), paint);
			}
		}
		tempRatio = template.costFontRatio;
		paint.setTextSize(imageHeight * tempRatio);
		drawGameText(gameText, 64, combine, templateImage, paint, resources, context);
		tempRatio = template.typeFontRatio;
		paint.setTextSize(imageHeight * tempRatio);
		String cardTypes = "";
		for (int i = 0; i < cardType.length; i++) {
			cardTypes += cardType[i].getCardType();
			if (i != cardType.length - 1)
				cardTypes += ", ";
		}
		if (!cardSubtype.equals(""))
			cardTypes += " -- " + cardSubtype;

		combine.drawText(cardTypes, templateImage.getWidth() / 13, templateImage.getHeight() - (templateImage.getHeight() / 2.97f), paint);
	}

	private void drawGameText(String gameText, int length, Canvas combine, Bitmap templateImage, Paint paint, Resources resources,
			Context context) {
		line = 0f;
		ArrayList<Integer> lengths = new ArrayList<Integer>();
		adjust(length, gameText, lengths);
		if (gameText.length() < lengths.get(0)) {
			if (gameText.contains("<p>")) {
				int pLocation = gameText.lastIndexOf("<p>") + 3;
				String paragraph = gameText.substring(0, pLocation);
				drawTextWithImages(paragraph, templateImage, combine, paint, resources, context);
				drawTextWithImages(gameText.substring(pLocation, gameText.length()), templateImage, combine, paint, resources, context);
			} else {
				drawTextWithImages(gameText, templateImage, combine, paint, resources, context);
			}
		} else {
			String displayText = "";
			String[] words = gameText.split(" ");
			int lineCount = 0;
			for (String word : words) {
				if (word.contains("<p>")) {
					int pLocation = word.lastIndexOf("<p>") + 3;
					String paragraph = word.substring(0, pLocation);
					displayText += paragraph;
					drawTextWithImages(displayText, templateImage, combine, paint, resources, context);
					displayText = "" + word.substring(pLocation, word.length()) + " ";
					lineCount++;
				} else {
					if ((displayText + word).length() > lengths.get(lineCount)) {
						drawTextWithImages(displayText, templateImage, combine, paint, resources, context);
						displayText = "";
						line += .06f;
						lineCount++;
					}
					displayText += word + " ";
				}
			}
			drawTextWithImages(displayText, templateImage, combine, paint, resources, context);
		}
	}

	private void adjust(int length, String gameText, ArrayList<Integer> lengths) {
		String displayText = "";
		String[] words = gameText.split(" ");
		for (String word : words) {
			if ((displayText + word).length() >= length) {
				if (displayText.contains("[")) {
					int adjuster = length + ((displayText.split("\\[").length - 1) * 4);
					lengths.add(adjuster);
				} else {
					lengths.add(length);
				}
				if (displayText.contains("<p>"))
					lengths.add(length);
				displayText = "";
			}
			displayText += word + " ";
		}
		if (displayText.contains("[")) {
			int adjuster = length + ((displayText.split("\\[").length - 1) * 4);
			lengths.add(adjuster);
		} else {
			lengths.add(length);
		}
		if (displayText.contains("<p>"))
			lengths.add(length);
	}

	private void drawTextWithImages(String displayText, Bitmap templateImage, Canvas combine, Paint paint, Resources resources,
			Context context) {
		String delims = "[\\[\\]<>]";
		String[] stuff = displayText.split(delims);
		float width = templateImage.getWidth() / 14;
		float baseline = (int) -paint.ascent();
		int height = (int) (baseline + paint.descent());

		for (int i = 0; i < stuff.length; i++) {
			if (i % 2 == 0) {
				if (stuff[i].equals(""))
					continue;
				Bitmap startImage = textAsBitmap(stuff[i], paint, templateImage, baseline, height);
				combine.drawBitmap(startImage, width, templateImage.getHeight() / (1.46f - line), paint);
				width += startImage.getWidth();

			} else {
				if (stuff[i].equalsIgnoreCase("p")) {
					line += 0.06f;
					width = templateImage.getWidth() / 14;
				} else if (stuff[i].equalsIgnoreCase("b")) {
					paint.setFakeBoldText(true);
					paint.setTextSize(paint.getTextSize() + 1);
				} else if (stuff[i].equalsIgnoreCase("/b")) {
					paint.setFakeBoldText(false);
					paint.setTextSize(paint.getTextSize() - 1);
				} else if (stuff[i].equalsIgnoreCase("i")) {
					paint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.ITALIC));
				} else if (stuff[i].equalsIgnoreCase("/i")) {
					paint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.NORMAL));
				} else {
					Bitmap symbolImage;
					if (stuff[i].equalsIgnoreCase("BASIC")) {
						symbolImage = getSymbolImage(stuff[i], context, height);
					} else if (stuff[i].startsWith("L") && i <= stuff.length - 2 && stuff[i + 2].startsWith("R")) {
						symbolImage = getSymbolImage(stuff[i], context, height);
						if (stuff[i + 1].equals("/"))
							stuff[i + 1] = "";
					} else if (stuff[i].startsWith("R") && i >= 2 && stuff[i - 2].startsWith("L")) {
						symbolImage = getSymbolImage(stuff[i], context, height);
					} else if (stuff[i].equalsIgnoreCase("ONE-SHOT")) {
						symbolImage = getSymbolImage(stuff[i], context, height);
					} else {
						symbolImage = getSymbolImage(stuff[i], context, height);
					}
					combine.drawBitmap(symbolImage, width, templateImage.getHeight() / (1.46f - line), paint);
					width += symbolImage.getWidth();
				}
			}
		}
	}

	private Bitmap textAsBitmap(String DisplayText, Paint paint, Bitmap templateImage, float baseline, int height) {
		int width = (int) (paint.measureText(DisplayText) + 0.5f);
		Bitmap displayTextImage = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
		Canvas canvas = new Canvas(displayTextImage);
		canvas.drawText(DisplayText, 0, baseline, paint);
		return displayTextImage;
	}

	private Bitmap getSymbolImage(String symbol, Context context, int height) {
		SymbolTemplate symTemp = SymbolTemplate.findSymbolTemplate(symbol, SymbolTemplate.getAllTemplates(context));
		if (symTemp != null) {
			Bitmap symbolImage = symTemp.getImage(context, (int) (height * symTemp.sizeRatio), height);
			return symbolImage;
		}
		return BitmapFactory.decodeResource(context.getResources(), R.drawable.blank);
	}

	private String getShortenedText(String name, int length) {
		if (name.length() > length) {
			String displayName = "";
			String[] words = name.split(" ");
			for (String word : words) {
				if ((displayName + word).length() >= length) {
					return displayName.concat("..");
				}
				displayName += word + " ";
			}
		}
		return name;
	}

	/**
	 * Create the bitmap for card threshold data
	 * 
	 * @param mContext
	 *            - the context for the application
	 * @return a bitmap of the card threshold, otherwise null
	 */
	public Bitmap getCardThresholdImage(Context mContext, CardTemplate template, Bitmap templateImage) {
		if (this.threshold != null && this.threshold.length > 0) {
			ArrayList<Bitmap> thresholds = new ArrayList<Bitmap>();
			for (ResourceThreshold threshold : this.threshold) {
				if (threshold.colorFlags != null && threshold.colorFlags.length > 0 && threshold.colorFlags[0] != null) {
					switch (threshold.colorFlags[0]) {
					case COLORLESS: {
						break;
					}
					case BLOOD: {
						addCardThresholdBitmapToList(mContext, thresholds, "cardthresholdblood", threshold.thresholdColorRequirement);
						break;
					}
					case DIAMOND: {
						addCardThresholdBitmapToList(mContext, thresholds, "cardthresholddiamond", threshold.thresholdColorRequirement);
						break;
					}
					case RUBY: {
						addCardThresholdBitmapToList(mContext, thresholds, "cardthresholdruby", threshold.thresholdColorRequirement);
						break;
					}
					case SAPPHIRE: {
						addCardThresholdBitmapToList(mContext, thresholds, "cardthresholdsapphire", threshold.thresholdColorRequirement);
						break;
					}
					case WILD: {
						addCardThresholdBitmapToList(mContext, thresholds, "cardthresholdwild", threshold.thresholdColorRequirement);
						break;
					}
					default: {
						break;
					}
					}
				}
			}

			Bitmap allThresholds = null;
			if (!thresholds.isEmpty()) {
				if (template == null) {
					allThresholds = Bitmap.createBitmap(HexUtil.convertDensityPixelsToPixels(mContext, 100),
							HexUtil.convertDensityPixelsToPixels(mContext, 8), Bitmap.Config.ARGB_8888);

					Canvas canvas = new Canvas(allThresholds);
					canvas.drawColor(0, Mode.CLEAR);
					int left = HexUtil.convertDensityPixelsToPixels(mContext, 92);
					int top = 0;
					int dimensions = HexUtil.convertDensityPixelsToPixels(mContext, 8);
					for (Bitmap image : thresholds) {
						image = Bitmap.createScaledBitmap(image, dimensions, dimensions, true);
						canvas.drawBitmap(image, left, top, null);
						left -= image.getWidth();
					}
				} else if (template.fullCard) {
					int width = templateImage.getWidth() / fullTemplateWidth;
					int height = templateImage.getHeight() / fullTemplateHeigth;
					allThresholds = Bitmap.createBitmap(width * 30, height * 500, Bitmap.Config.ARGB_8888);

					Canvas canvas = new Canvas(allThresholds);
					canvas.drawColor(0, Mode.CLEAR);
					int left = 0;
					int top = 0;
					int padding = height * 23;
					int dimensions = width * 30;
					for (Bitmap image : thresholds) {
						image = Bitmap.createScaledBitmap(image, dimensions, dimensions, true);
						canvas.drawBitmap(image, left, top, null);
						top += image.getHeight() + padding;
					}
				} else {
					int width = templateImage.getWidth();
					int height = templateImage.getHeight();
					allThresholds = Bitmap.createBitmap((width * 18) / thumbnailTemplateWidth, (height * 300) / thumbnailTemplateHeigth,
							Bitmap.Config.ARGB_8888);

					Canvas canvas = new Canvas(allThresholds);
					canvas.drawColor(0, Mode.CLEAR);
					int left = 0;
					int top = 0;
					int padding = height * 12 / thumbnailTemplateHeigth;
					int dimensions = width * 18 / thumbnailTemplateWidth;
					for (Bitmap image : thresholds) {
						image = Bitmap.createScaledBitmap(image, dimensions, dimensions, true);
						canvas.drawBitmap(image, left, top, null);
						top += image.getHeight() + padding;
					}
				}
			}

			return allThresholds;
		}

		return null;
	}

	private void addCardThresholdBitmapToList(Context mContext, List<Bitmap> thresholds, String resourcesName, int thresholdCount) {
		for (int i = 0; i < thresholdCount; i++) {
			Bitmap thresh = BitmapFactory.decodeResource(mContext.getResources(), HexUtil.getResourceID(resourcesName, R.drawable.class),
					null);
			thresholds.add(thresh);
		}
	}

	@Override
	public Bitmap getCardPortait(Context mContext) {
		try {
			if (portrait == null) {
				BitmapFactory.Options portraitOptions = new BitmapFactory.Options();
				portraitOptions.inSampleSize = 4;
				portrait = BitmapFactory.decodeResource(mContext.getResources(),
						HexUtil.getResourceID(this.cardImagePath, R.drawable.class), portraitOptions);
				if (portrait != null) {
					double pL = defaultLayout.portraitLeft * portrait.getWidth();
					double pR = defaultLayout.portraitRight * portrait.getWidth();
					double pT = defaultLayout.portraitTop * portrait.getHeight();
					double pB = defaultLayout.portraitBottom * portrait.getHeight();

					double xD = pL + 0.0567 * (pR - pL);
					double widthD = (pR) - (0.0113 * (pR - pL)) - xD;
					double yD = pT + 0.0130 * (pB - pT);
					double heightD = pB - (0.0078 * (pB - pT)) - yD;

					int x = (int) Math.round(xD);
					int width = (int) Math.round(widthD);
					int y = (int) Math.round(yD);
					int height = (int) Math.round(heightD);
					portrait = Bitmap.createBitmap(portrait, x, y, width, height);
					ImageCache.getInstance(CacheType.ListView).queueForRemovalFromCache(mContext, this, ImageType.WithoutTemplate);
				}
			}
		} catch (OutOfMemoryError e) {
			System.err.println("Ran out of memory, dumping some images from the cache");
			ImageCache.emergencyDump(CacheType.ListView);
			return getCardPortait(mContext);
		}

		return portrait;
	}

}
