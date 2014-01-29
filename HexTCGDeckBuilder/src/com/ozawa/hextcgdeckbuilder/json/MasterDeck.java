package com.ozawa.hextcgdeckbuilder.json;

import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.res.Resources;

import com.ozawa.hextcgdeckbuilder.R;
import com.ozawa.hextcgdeckbuilder.hexentities.AbstractCard;

public class MasterDeck {
	private static List<AbstractCard>	masterDeck;

	public static List<AbstractCard> getMasterDeck(Context context) {
		if (masterDeck == null) {
			JsonReader jsonReader = new JsonReader(context);
			try {
				masterDeck = jsonReader.deserializeJSONInputStreamsToCard(getJson(context.getResources()));
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
		}
		return masterDeck;
	}

	private static ArrayList<InputStream> getJson(Resources res) throws IllegalAccessException {
		Field[] rawFields = R.raw.class.getFields();
		ArrayList<InputStream> jsonFiles = new ArrayList<InputStream>();

		for (int count = 0; count < rawFields.length; count++) {
			int rid = rawFields[count].getInt(rawFields[count]);
			try {
				String name = res.getResourceName(rid);
				if (name.contains("hexcard")) {
					InputStream inputStream = res.openRawResource(rid);
					if (inputStream != null) {
						jsonFiles.add(inputStream);
					}
				}
			} catch (Exception e) {
			}
		}
		return jsonFiles;
	}

}
