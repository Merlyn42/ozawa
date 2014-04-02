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
package com.ozawa.hextcgdeckbuilder.database;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.google.gson.Gson;
import com.ozawa.hextcgdeckbuilder.R;
import com.ozawa.hextcgdeckbuilder.enums.GemType;
import com.ozawa.hextcgdeckbuilder.enums.ItemType;
import com.ozawa.hextcgdeckbuilder.hexentities.AbstractCard;
import com.ozawa.hextcgdeckbuilder.hexentities.Champion;
import com.ozawa.hextcgdeckbuilder.hexentities.Gem;
import com.ozawa.hextcgdeckbuilder.hexentities.GemResource;
import com.ozawa.hextcgdeckbuilder.hexentities.HexDeck;
import com.ozawa.hextcgdeckbuilder.hexentities.DeckResource;
import com.ozawa.hextcgdeckbuilder.hexentities.GlobalIdentifier;

import android.content.ContentValues;
import android.content.Context;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Database Handler classed used to read and write application information to
 * the SQLite Database
 */
public class DatabaseHandler extends SQLiteOpenHelper {

	// Database Version
	private static final int	DATABASE_VERSION			= 2;
	// Database Name
	private static final String	DATABASE_NAME				= "HexTCGDeckBuilderDB";

	// Table Names
	private static final String	TABLE_DECKS					= "decks";
	private static final String	TABLE_DECK_RESOURCES		= "deck_resources";
	private static final String	TABLE_CHAMPIONS				= "champions";
	private static final String	TABLE_GEMS					= "gems";
	private static final String	TABLE_GEM_RESOURCES			= "gem_resources";

	// Column Names
	private static final String	ID							= "id";
	private static final String	NAME						= "name";
	private static final String	CARD_ID						= "card_id";
	private static final String	CARD_COUNT					= "card_count";
	private static final String	DECK_ID						= "deck_id";
	private static final String	CHAMPION_NAME				= "champion_name";
	private static final String	GEM_ID						= "gem_id";
	private static final String	GEM_COUNT					= "gem_count";

	// Champion Columns
	private static final String	SET_ID						= "set_id";
	private static final String	HUD_PORTRAIT				= "hud_portrait";
	private static final String	HUD_PORTRAIT_SMALL			= "hud_portrait_small";
	private static final String	GAME_TEXT					= "game_text";

	// Gem Columns
	private static final String	BASEPRICE					= "base_price";
	private static final String	GEMTYPE						= "gem_type";
	private static final String	ITEMTYPE					= "item_type";
	private static final String	DESCRIPTION					= "description";

	public List<Champion>		allChampions;
	public List<Gem>			allGems;

	// Tables
	private String				CREATE_GEMS_TABLE			= "CREATE TABLE " + TABLE_GEMS + " (" + ID + " TEXT PRIMARY KEY, " + NAME
																	+ " TEXT," + DESCRIPTION + " TEXT, " + BASEPRICE + " INTEGER, "
																	+ GEMTYPE + " TEXT," + ITEMTYPE + " TEXT);";

	private String				CREATE_GEM_RESOURCES_TABLE	= "CREATE TABLE " + TABLE_GEM_RESOURCES + " ( " + CARD_ID + " TEXT, "
																	+ GEM_COUNT + " INTEGER, " + DECK_ID + " INTEGER NOT NULL, " + GEM_ID
																	+ " TEXT NOT NULL, " + "FOREIGN KEY(" + DECK_ID + ") REFERENCES "
																	+ TABLE_DECKS + "(" + ID + ") ON DELETE CASCADE, " + "FOREIGN KEY("
																	+ GEM_ID + ") REFERENCES " + TABLE_GEMS + "(" + ID
																	+ ") ON DELETE CASCADE);";

	public DatabaseHandler(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
		populateChampionData(context);

		populateGemData(context);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		if (!db.isReadOnly()) {
			db.execSQL("PRAGMA foreign_keys=ON;");
		}
		// SQL statement to create book table
		String CREATE_DECKS_TABLE = "CREATE TABLE " + TABLE_DECKS + " (" + ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + NAME + " TEXT, "
				+ CHAMPION_NAME + " TEXT, " + "FOREIGN KEY(" + CHAMPION_NAME + ") REFERENCES " + TABLE_CHAMPIONS + "(" + NAME + "));";

		String CREATE_DECK_RESOURCES_TABLE = "CREATE TABLE " + TABLE_DECK_RESOURCES + " ( " + CARD_ID + " TEXT, " + CARD_COUNT
				+ " INTEGER, " + DECK_ID + " INTEGER NOT NULL, " + "FOREIGN KEY(" + DECK_ID + ") REFERENCES " + TABLE_DECKS + "(" + ID
				+ ") ON DELETE CASCADE);";

		String CREATE_CHAMPIONS_TABLE = "CREATE TABLE " + TABLE_CHAMPIONS + " (" + NAME + " TEXT PRIMARY KEY," + SET_ID + " TEXT, "
				+ HUD_PORTRAIT + " TEXT, " + HUD_PORTRAIT_SMALL + " TEXT," + GAME_TEXT + " TEXT);";

		db.execSQL(CREATE_CHAMPIONS_TABLE);
		db.execSQL(CREATE_DECKS_TABLE);
		db.execSQL(CREATE_DECK_RESOURCES_TABLE);
		db.execSQL(CREATE_GEMS_TABLE);
		db.execSQL(CREATE_GEM_RESOURCES_TABLE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		switch (oldVersion) {
		case 1: {
			db.execSQL(CREATE_GEMS_TABLE);
			db.execSQL(CREATE_GEM_RESOURCES_TABLE);
		}
		default: {
			break;
		}
		}

		/*
		 * db.execSQL("DROP TABLE IF EXISTS " + TABLE_CHAMPIONS);
		 * db.execSQL("DROP TABLE IF EXISTS " + TABLE_DECKS);
		 * db.execSQL("DROP TABLE IF EXISTS " + TABLE_DECK_RESOURCES);
		 * db.execSQL("DROP TABLE IF EXISTS " + TABLE_GEMS);
		 * 
		 * this.onCreate(db);
		 */
	}

	@Override
	public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		onUpgrade(db, oldVersion, newVersion);
	}

	/**
	 * Champion Table Database Methods
	 */

	public boolean addChampion(Champion champion) {
		SQLiteDatabase db = this.getWritableDatabase();
		boolean inserted = true;
		ContentValues values = new ContentValues();
		values.put(NAME, champion.name);
		values.put(SET_ID, champion.setID.gUID);
		values.put(HUD_PORTRAIT, champion.hudPortrait);
		values.put(HUD_PORTRAIT_SMALL, champion.hudPortraitSmall);
		values.put(GAME_TEXT, champion.gameText);

		try {
			db.beginTransaction();
			db.insert(TABLE_CHAMPIONS, null, values);
			db.setTransactionSuccessful();
		} catch (Exception ex) {
			inserted = false;
		} finally {
			db.endTransaction(); // If error occurs during transaction, no data
									// is committed
			db.close();
		}

		return inserted;
	}

	public Champion getChampion(String name) {
		SQLiteDatabase db = this.getReadableDatabase();
		Champion champion = null;
		try {
			Cursor results = db.query(TABLE_CHAMPIONS, new String[] { NAME, SET_ID, HUD_PORTRAIT, HUD_PORTRAIT_SMALL, GAME_TEXT }, NAME
					+ "= ?", new String[] { name }, null, null, null, null);

			if (results == null) {
				return null;
			}

			results.moveToFirst();

			champion = createNewChampion(results);
		} catch (Exception ex) {
		}

		return champion;
	}

	public boolean updateChampion(Champion champion) {
		SQLiteDatabase db = this.getWritableDatabase();
		boolean updated = true;
		ContentValues values = new ContentValues();
		values.put(NAME, champion.name);
		values.put(SET_ID, champion.getSetID());
		values.put(HUD_PORTRAIT, champion.hudPortrait);
		values.put(HUD_PORTRAIT_SMALL, champion.hudPortraitSmall);
		values.put(GAME_TEXT, champion.gameText);
		try {
			db.beginTransaction();
			db.update(TABLE_CHAMPIONS, values, NAME + "= ?", new String[] { champion.name });
			db.setTransactionSuccessful();
		} catch (Exception ex) {
			updated = false;
		} finally {
			db.endTransaction();
			db.close();
		}
		return updated;
	}

	public boolean deleteChampion(Champion champion) {
		SQLiteDatabase db = this.getWritableDatabase();
		boolean deleted = true;

		try {
			db.beginTransaction();
			db.delete(TABLE_CHAMPIONS, NAME + " = ?", new String[] { champion.name });
			db.setTransactionSuccessful();
		} catch (Exception ex) {
			deleted = false;
		} finally {
			db.endTransaction(); // If error occurs during transaction, no data
									// is committed
			db.close();
		}

		return deleted;
	}

	private Champion createNewChampion(Cursor results) {
		Champion champion = new Champion();

		champion.setID = new GlobalIdentifier(String.valueOf(results.getInt(results.getColumnIndex(SET_ID))));
		champion.name = results.getString(results.getColumnIndex(NAME));
		champion.hudPortrait = results.getString(results.getColumnIndex(HUD_PORTRAIT));
		champion.hudPortraitSmall = results.getString(results.getColumnIndex(HUD_PORTRAIT_SMALL));
		champion.gameText = results.getString(results.getColumnIndex(GAME_TEXT));

		return champion;
	}

	private List<Champion> generateAllChampionsFromJson(Context context) {
		ArrayList<Champion> allChamps = new ArrayList<Champion>();

		try {
			ArrayList<InputStream> championFiles = getChampionJson(context);
			Gson gson = new Gson();
			for (InputStream json : championFiles) {
				allChamps.add(gson.fromJson(new InputStreamReader(json), Champion.class));
			}
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}

		return allChamps;
	}

	private ArrayList<InputStream> getChampionJson(Context context) throws IllegalAccessException {
		Field[] rawFields = R.raw.class.getFields();
		ArrayList<InputStream> jsonFiles = new ArrayList<InputStream>();

		for (int count = 0; count < rawFields.length; count++) {
			int rid = rawFields[count].getInt(rawFields[count]);
			try {
				Resources res = context.getResources();
				String name = res.getResourceName(rid);
				if (name.contains("champion") && !name.contains("portrait")) {
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

	/**
	 * Add or update Champion data in database
	 * 
	 * @param context
	 */
	private void populateChampionData(Context context) {
		allChampions = generateAllChampionsFromJson(context);

		for (Champion champion : allChampions) {
			if (getChampion(champion.name) != null) {
				updateChampion(champion);
			} else {
				addChampion(champion);
			}
		}
	}

	/**
	 * Gem Table Database Methods
	 */

	public boolean addGem(Gem gem) {
		SQLiteDatabase db = this.getWritableDatabase();
		boolean inserted = true;
		ContentValues values = new ContentValues();
		values.put(NAME, gem.name);
		values.put(ID, gem.id.gUID);
		values.put(BASEPRICE, gem.basePrice);
		values.put(DESCRIPTION, gem.description);
		values.put(GEMTYPE, gem.gemType.toString());
		values.put(ITEMTYPE, gem.type.toString());

		try {
			db.beginTransaction();
			db.insert(TABLE_GEMS, null, values);
			db.setTransactionSuccessful();
		} catch (Exception ex) {
			inserted = false;
		} finally {
			db.endTransaction(); // If error occurs during transaction, no data
									// is committed
			db.close();
		}

		return inserted;
	}

	public Gem getGem(String id) {
		SQLiteDatabase db = this.getReadableDatabase();
		Gem gem = null;
		try {
			Cursor results = db.query(TABLE_GEMS, new String[] { NAME, ID, BASEPRICE, DESCRIPTION, GEMTYPE, ITEMTYPE }, ID + "= ?",
					new String[] { id }, null, null, null, null);

			if (results == null) {
				return null;
			}

			results.moveToFirst();

			gem = createNewGem(results);
		} catch (Exception ex) {
		}

		return gem;
	}

	public boolean updateGem(Gem gem) {
		SQLiteDatabase db = this.getWritableDatabase();
		boolean updated = true;
		ContentValues values = new ContentValues();
		values.put(NAME, gem.name);
		values.put(ID, gem.id.gUID);
		values.put(BASEPRICE, gem.basePrice);
		values.put(DESCRIPTION, gem.description);
		values.put(GEMTYPE, gem.gemType.toString());
		values.put(ITEMTYPE, gem.type.toString());
		try {
			db.beginTransaction();
			db.update(TABLE_GEMS, values, ID + "= ?", new String[] { gem.id.gUID });
			db.setTransactionSuccessful();
		} catch (Exception ex) {
			updated = false;
		} finally {
			db.endTransaction();
			db.close();
		}
		return updated;
	}

	public boolean deleteGem(Gem gem) {
		SQLiteDatabase db = this.getWritableDatabase();
		boolean deleted = true;

		try {
			db.beginTransaction();
			db.delete(TABLE_GEMS, ID + " = ?", new String[] { gem.id.gUID });
			db.setTransactionSuccessful();
		} catch (Exception ex) {
			deleted = false;
		} finally {
			db.endTransaction(); // If error occurs during transaction, no data
									// is committed
			db.close();
		}

		return deleted;
	}

	private Gem createNewGem(Cursor results) {
		Gem gem = new Gem();

		gem.id = new GlobalIdentifier(String.valueOf(results.getInt(results.getColumnIndex(ID))));
		gem.name = results.getString(results.getColumnIndex(NAME));
		gem.basePrice = results.getInt(results.getColumnIndex(BASEPRICE));
		gem.description = results.getString(results.getColumnIndex(DESCRIPTION));
		gem.gemType = GemType.valueOf(results.getString(results.getColumnIndex(GEMTYPE)));
		gem.type = ItemType.valueOf(results.getString(results.getColumnIndex(ITEMTYPE)));

		return gem;
	}

	private List<Gem> generateAllGemsFromJson(Context context) {
		ArrayList<Gem> allGems = new ArrayList<Gem>();

		try {
			ArrayList<InputStream> gemFiles = getGemJson(context);
			Gson gson = new Gson();
			for (InputStream json : gemFiles) {
				allGems.add(gson.fromJson(new InputStreamReader(json), Gem.class));
			}
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}

		return allGems;
	}

	private ArrayList<InputStream> getGemJson(Context context) throws IllegalAccessException {
		Field[] rawFields = R.raw.class.getFields();
		ArrayList<InputStream> jsonFiles = new ArrayList<InputStream>();

		for (int count = 0; count < rawFields.length; count++) {
			int rid = rawFields[count].getInt(rawFields[count]);
			try {
				Resources res = context.getResources();
				String name = res.getResourceName(rid);
				if (name.contains("gemdata")) {
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

	/**
	 * Add or update Gem data in database
	 * 
	 * @param context
	 */
	private void populateGemData(Context context) {
		allGems = generateAllGemsFromJson(context);

		for (Gem gem : allGems) {
			if (getGem(gem.id.gUID) != null) {
				updateGem(gem);
			} else {
				addGem(gem);
			}
		}
	}

	/**
	 * Deck Table Database Methods
	 */

	/**
	 * Add a new Deck to the database
	 * 
	 * @param deck
	 *            - the deck to be added
	 * @return the primary key ID for the new deck
	 */
	public long addDeck(HexDeck deck) {
		SQLiteDatabase db = this.getWritableDatabase();
		long rowID = -1;
		ContentValues values = new ContentValues();
		values.put(NAME, deck.name);
		if (deck.champion != null) {
			values.put(CHAMPION_NAME, deck.champion.name);
		}

		try {
			db.beginTransaction();
			rowID = db.insert(TABLE_DECKS, null, values);
			db.setTransactionSuccessful();
		} catch (Exception ex) {

		} finally {
			db.endTransaction(); // If error occurs during transaction, no data
									// is committed
			db.close();
		}

		return rowID;
	}

	/**
	 * Update the deck data in the database
	 * 
	 * @param deck
	 */
	public boolean updateDeck(HexDeck deck) {
		SQLiteDatabase db = this.getWritableDatabase();
		boolean updated = true;
		ContentValues values = new ContentValues();
		values.put(NAME, deck.name);
		if (deck.champion != null) {
			values.put(CHAMPION_NAME, deck.champion.name);
		}

		try {
			db.beginTransaction();
			db.update(TABLE_DECKS, values, ID + "= ?", new String[] { deck.getID() });
			db.setTransactionSuccessful();
		} catch (Exception ex) {
			updated = false;
		} finally {
			db.endTransaction(); // If error occurs during transaction, no data
									// is committed
			db.close();
		}

		return updated;
	}

	/**
	 * Delete a Deck from the database
	 * 
	 * @param deck
	 *            - the Deck to be deleted
	 * @return true if the Deck was successfully deleted, otherwise false
	 */
	public boolean deleteDeck(HexDeck deck) {
		SQLiteDatabase db = this.getWritableDatabase();
		boolean deleted = true;
		try {
			db.beginTransaction();
			db.delete(TABLE_DECKS, ID + " = ?", new String[] { deck.getID() });
			db.setTransactionSuccessful();
		} catch (Exception ex) {
			deleted = false;
		} finally {
			db.endTransaction(); // If error occurs during transaction, no data
									// is committed
			db.close();
		}

		return deleted;
	}

	/**
	 * Get a Deck from the database
	 * 
	 * @param id
	 *            - the ID of the Deck
	 * @return the Deck with the given ID if found, otherwise null
	 */
	public HexDeck getDeck(String id) {
		SQLiteDatabase db = this.getReadableDatabase();
		HexDeck deck = null;
		try {
			Cursor results = db.query(TABLE_DECKS, new String[] { ID, NAME, CHAMPION_NAME }, ID + "= ?", new String[] { id }, null, null,
					null, null);

			if (results == null) {
				return null;
			}

			results.moveToFirst();

			deck = createNewDeck(results);
		} catch (SQLiteException ex) {

		} finally {
			db.close();
		}

		return deck;
	}

	/**
	 * Get all the Decks from the database
	 * 
	 * @return all of the stored Decks
	 */
	public List<HexDeck> getAllDecks() {
		ArrayList<HexDeck> decks = new ArrayList<HexDeck>();
		String selectQuery = "SELECT * FROM " + TABLE_DECKS;
		SQLiteDatabase db = this.getReadableDatabase();

		try {
			Cursor results = db.rawQuery(selectQuery, null);
			if (results.moveToFirst()) {
				do {
					decks.add(createNewDeck(results));
				} while (results.moveToNext());
			}
		} catch (SQLiteException ex) {

		} finally {
			db.close();
		}

		return decks;
	}

	/**
	 * Deck Resources Table Database Methods
	 */

	/**
	 * Add Deck Resources to a Deck
	 * 
	 * @param deck
	 *            - the Deck to update
	 * @param cardData
	 *            - the cards to add
	 */
	public void addDeckResources(HexDeck deck, HashMap<AbstractCard, Integer> cardData) {
		SQLiteDatabase db = this.getWritableDatabase();

		try {
			db.beginTransaction();

			addDeckResources(deck, cardData, db);

			db.setTransactionSuccessful();
		} catch (Exception ex) {

		} finally {
			db.endTransaction(); // If error occurs during transaction, no data
									// is committed
			db.close();
		}
	}

	/**
	 * Add a Deck Resource to a Deck
	 * 
	 * @param deck
	 *            - the Deck to update
	 * @param card
	 *            - the cards to add
	 * @param cardCount
	 *            - the amount of that card the Deck has
	 */
	public void addDeckResource(HexDeck deck, AbstractCard card, int cardCount) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(DECK_ID, deck.getID());
		values.put(CARD_COUNT, cardCount);
		values.put(CARD_ID, card.getID());

		try {
			db.beginTransaction();
			db.insert(TABLE_DECK_RESOURCES, null, values);
			db.setTransactionSuccessful();
		} catch (Exception ex) {

		} finally {
			db.endTransaction(); // If error occurs during transaction, no data
									// is committed
			db.close();
		}
	}

	/**
	 * Add Deck Resources to a Deck
	 * 
	 * @param deck
	 *            - the Deck to update
	 * @param cardData
	 *            - the cards to add
	 * @param db
	 *            - the database
	 */
	private void addDeckResources(HexDeck deck, HashMap<AbstractCard, Integer> cardData, SQLiteDatabase db) {
		List<AbstractCard> cards = new ArrayList<AbstractCard>(cardData.keySet());

		for (AbstractCard card : cards) {
			ContentValues values = new ContentValues();
			values.put(DECK_ID, deck.getID());
			values.put(CARD_COUNT, cardData.get(card));
			values.put(CARD_ID, card.getID());
			db.insert(TABLE_DECK_RESOURCES, null, values);
		}
	}

	/**
	 * Update the Deck Resources of the Deck
	 * 
	 * @param deck
	 *            - the Deck to update
	 * @param cardData
	 *            - the cards to add
	 * @return true if updated successfully, otherwise false
	 */
	public boolean updateDeckResources(HexDeck deck, HashMap<AbstractCard, Integer> cardData) {
		SQLiteDatabase db = this.getWritableDatabase();
		boolean updated = true;

		try {
			db.beginTransaction();

			deleteDeckResources(deck, db);
			addDeckResources(deck, cardData, db);

			db.setTransactionSuccessful();
		} catch (Exception ex) {
			updated = false;
		} finally {
			db.endTransaction();
			db.close();
		}

		return updated;

	}

	/**
	 * Update the Deck Resources of the Deck
	 * 
	 * @param deck
	 *            - the Deck to update
	 * @param card
	 *            - the card to add
	 * @param cardCount
	 *            - the number of the card in the Deck
	 */
	public void updateDeckResource(HexDeck deck, AbstractCard card, int cardCount) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(CARD_COUNT, cardCount);
		values.put(CARD_ID, card.getID());

		try {
			db.beginTransaction();
			db.update(TABLE_DECK_RESOURCES, values, DECK_ID + "= ? AND " + CARD_ID + "= ?", new String[] { deck.getID(), card.getID() });
			db.setTransactionSuccessful();
		} catch (Exception ex) {

		} finally {
			db.endTransaction(); // If error occurs during transaction, no data
									// is commited
			db.close();
		}

	}

	/**
	 * Delete the Deck Resources from a Deck
	 * 
	 * @param deck
	 *            - the Deck to update
	 * @param cardData
	 *            - the cards to delete
	 */
	public void deleteDeckResources(HexDeck deck, HashMap<AbstractCard, Integer> cardData) {
		SQLiteDatabase db = this.getWritableDatabase();

		try {
			db.beginTransaction();
			deleteDeckResources(deck, db);
			db.setTransactionSuccessful();
		} catch (Exception ex) {

		} finally {
			db.endTransaction(); // If error occurs during transaction, no data
									// is committed
			db.close();
		}
	}

	/**
	 * Delete the Deck Resources from a Deck
	 * 
	 * @param deck
	 *            - the Deck to update
	 * @param db
	 *            - the database
	 */
	private void deleteDeckResources(HexDeck deck, SQLiteDatabase db) {
		db.delete(TABLE_DECK_RESOURCES, DECK_ID + "= ?", new String[] { deck.getID() });
	}

	/**
	 * Delete a Deck Resource from a Deck
	 * 
	 * @param deck
	 *            - the Deck to update
	 * @param card
	 *            - the card to delete
	 * @param cardCount
	 *            - the number of that card to delete
	 */
	public void deleteDeckResource(HexDeck deck, AbstractCard card, int cardCount) {
		SQLiteDatabase db = this.getWritableDatabase();

		try {
			db.beginTransaction();
			db.delete(TABLE_DECK_RESOURCES, DECK_ID + "= ? AND " + CARD_ID + "= ?", new String[] { deck.getID(), card.getID() });
			db.setTransactionSuccessful();
		} catch (Exception ex) {

		} finally {
			db.endTransaction(); // If error occurs during transaction, no data
									// is committed
			db.close();
		}
	}

	/**
	 * Get a Deck Resource
	 * 
	 * @param deckID
	 *            - the Deck ID
	 * @param cardID
	 *            - the card ID
	 * @return the Deck Resource for the given IDs
	 */
	public DeckResource getDeckResource(String deckID, String cardID) {
		SQLiteDatabase db = this.getReadableDatabase();
		DeckResource deckResource = null;
		try {
			Cursor results = db.query(TABLE_DECKS, new String[] { ID, NAME }, DECK_ID + "= ? AND " + CARD_ID + "= ?", new String[] {
					deckID, cardID }, null, null, null, null);

			if (results == null) {
				return null;
			}

			results.moveToFirst();
			deckResource = createNewDeckResource(results);
		} catch (SQLiteException ex) {

		} finally {
			db.close();
		}

		return deckResource;
	}

	/**
	 * Get all the Deck Resources for a Deck
	 * 
	 * @param deckID
	 *            - the Deck ID
	 * @return all the Deck Resources for the given ID
	 */
	public List<DeckResource> getAllDeckResourcesForDeck(String deckID) {
		ArrayList<DeckResource> deckResources = new ArrayList<DeckResource>();
		String selectQuery = "SELECT  * FROM " + TABLE_DECK_RESOURCES + " WHERE " + DECK_ID + " = '" + deckID + "'";
		SQLiteDatabase db = this.getReadableDatabase();

		try {
			Cursor results = db.rawQuery(selectQuery, null);
			if (results.moveToFirst()) {
				do {
					deckResources.add(createNewDeckResource(results));
				} while (results.moveToNext());
			}
		} catch (SQLiteException ex) {

		} finally {
			db.close();
		}

		return deckResources;
	}

	/**
	 * Get every Deck Resource for all Decks
	 * 
	 * @return every Deck Resource for all Decks
	 */
	public List<DeckResource> getAllDeckResources() {
		ArrayList<DeckResource> deckResources = new ArrayList<DeckResource>();
		String selectQuery = "SELECT  * FROM " + TABLE_DECK_RESOURCES;
		SQLiteDatabase db = this.getReadableDatabase();

		try {
			Cursor results = db.rawQuery(selectQuery, null);
			if (results.moveToFirst()) {
				do {
					deckResources.add(createNewDeckResource(results));
				} while (results.moveToNext());
			}
		} catch (SQLiteException ex) {

		} finally {
			db.close();
		}

		return deckResources;
	}

	/**
	 * Gem Resources Table Database Methods
	 */

	/**
	 * Add Gem Resources to a Deck
	 * 
	 * @param gemResources
	 *            - the list of GemResources to add
	 * @param cardData
	 *            - the cards to add
	 */
	public boolean addGemResources(List<GemResource> gemResources) {
		SQLiteDatabase db = this.getWritableDatabase();
		boolean gemResourcesAdded = true;
		try {
			db.beginTransaction();

			addGemResources(gemResources, db);

			db.setTransactionSuccessful();
		} catch (Exception ex) {
			gemResourcesAdded = false;
		} finally {
			db.endTransaction(); // If error occurs during transaction, no data
									// is committed
			db.close();
		}

		return gemResourcesAdded;
	}

	/**
	 * Add a Gem Resource to a Deck
	 * 
	 * @param gemResource
	 *            - the GemResources to add
	 */
	public boolean addGemResource(GemResource gemResource) {
		SQLiteDatabase db = this.getWritableDatabase();
		boolean gemResourcesAdded = true;

		ContentValues values = new ContentValues();
		values.put(DECK_ID, gemResource.deckId.gUID);
		values.put(GEM_COUNT, gemResource.gemCount);
		values.put(CARD_ID, gemResource.cardId.gUID);
		values.put(GEM_ID, gemResource.gemId.gUID);

		try {
			db.beginTransaction();
			db.insert(TABLE_GEM_RESOURCES, null, values);
			db.setTransactionSuccessful();
		} catch (Exception ex) {
			gemResourcesAdded = false;
		} finally {
			db.endTransaction(); // If error occurs during transaction, no data
									// is committed
			db.close();
		}

		return gemResourcesAdded;
	}

	/**
	 * Add Gem Resources to a Deck
	 * 
	 * @param gemResources
	 *            - the list of GemResources to update
	 * @param db
	 *            - the database
	 */
	private void addGemResources(List<GemResource> gemData, SQLiteDatabase db) {
		for (GemResource gemResource : gemData) {
			ContentValues values = new ContentValues();
			values.put(DECK_ID, gemResource.deckId.gUID);
			values.put(GEM_COUNT, gemResource.gemCount);
			values.put(CARD_ID, gemResource.cardId.gUID);
			values.put(GEM_ID, gemResource.gemId.gUID);
			db.insert(TABLE_GEM_RESOURCES, null, values);
		}
	}

	/**
	 * Update the Gem Resources of the Deck
	 * 
	 * @param gemResources
	 *            - the list of GemResources to update
	 * @return true if updated successfully, otherwise false
	 */
	public boolean updateGemResources(List<GemResource> gemResources) {
		SQLiteDatabase db = this.getWritableDatabase();
		boolean updated = true;
		if (!gemResources.isEmpty()) {
			try {
				db.beginTransaction();

				deleteGemResourceFromDeck(gemResources.get(0).deckId.gUID, db);
				addGemResources(gemResources, db);

				db.setTransactionSuccessful();
			} catch (Exception ex) {
				updated = false;
			} finally {
				db.endTransaction();
				db.close();
			}
		} else {
			db.close();
		}

		return updated;

	}

	/**
	 * Update the Gem Resources of the Deck
	 * 
	 * @param gemResources
	 *            - the GemResources to update
	 */
	public void updateGemResource(GemResource gemResource) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(GEM_COUNT, gemResource.gemCount);

		try {
			db.beginTransaction();
			db.update(TABLE_GEM_RESOURCES, values, GEM_ID + " = ? AND " + DECK_ID + " = ? AND " + CARD_ID + " = ?", new String[] {
					gemResource.gemId.gUID, gemResource.deckId.gUID, gemResource.cardId.gUID });
			db.setTransactionSuccessful();
		} catch (Exception ex) {

		} finally {
			db.endTransaction(); // If error occurs during transaction, no data
									// is commited
			db.close();
		}

	}

	/**
	 * Delete
	 * 
	 * @param gemId
	 * @param deckId
	 * @param db
	 */
	private void deleteGemResourceFromDeck(String deckId, SQLiteDatabase db) {
		db.delete(TABLE_GEM_RESOURCES, DECK_ID + "= ?", new String[] { deckId });
	}

	/**
	 * Delete the Gem Resources from a Deck
	 * 
	 * @param deck
	 *            - the Deck to update
	 * @param cardData
	 *            - the cards to delete
	 */
	public void deleteAllGemResourcesForDeck(String deckId) {
		SQLiteDatabase db = this.getWritableDatabase();

		try {
			db.beginTransaction();
			deleteAllGemResourcesForDeck(deckId, db);
			db.setTransactionSuccessful();
		} catch (Exception ex) {

		} finally {
			db.endTransaction(); // If error occurs during transaction, no data
									// is committed
			db.close();
		}
	}

	/**
	 * Delete the Gem Resources from a Deck
	 * 
	 * @param deck
	 *            - the Deck to update
	 * @param db
	 *            - the database
	 */
	private void deleteAllGemResourcesForDeck(String deckId, SQLiteDatabase db) {
		db.delete(TABLE_GEM_RESOURCES, DECK_ID + "= ?", new String[] { deckId });
	}

	/**
	 * Delete a Gem Resource from a Deck
	 * 
	 * @param deck
	 *            - the Deck to update
	 * @param card
	 *            - the card to delete
	 * @param cardCount
	 *            - the number of that card to delete
	 */
	public void deleteGemResource(GemResource gemResource) {
		SQLiteDatabase db = this.getWritableDatabase();

		try {
			db.beginTransaction();
			db.delete(TABLE_GEM_RESOURCES, GEM_ID + " = ? AND " + DECK_ID + "= ? AND " + CARD_ID + "= ?", new String[] {
					gemResource.gemId.gUID, gemResource.deckId.gUID, gemResource.cardId.gUID });
			db.setTransactionSuccessful();
		} catch (Exception ex) {

		} finally {
			db.endTransaction(); // If error occurs during transaction, no data
									// is committed
			db.close();
		}
	}

	/**
	 * Get a Gem Resource
	 * 
	 * @param deckID
	 *            - the Deck ID
	 * @param cardID
	 *            - the card ID
	 * @return the Gem Resource for the given IDs
	 */
	public GemResource getGemResource(String gemID, String deckID, String cardID) {
		SQLiteDatabase db = this.getReadableDatabase();
		GemResource newGemResource = null;
		try {
			Cursor results = db.query(TABLE_GEM_RESOURCES, new String[] { GEM_ID, DECK_ID, CARD_ID }, GEM_ID + " = ? AND " + DECK_ID
					+ "= ? AND " + CARD_ID + "= ?", new String[] { gemID, deckID, cardID }, null, null, null, null);

			if (results == null) {
				return null;
			}

			results.moveToFirst();
			newGemResource = createNewGemResource(results);
		} catch (SQLiteException ex) {

		} finally {
			db.close();
		}

		return newGemResource;
	}

	/**
	 * Get all the Gem Resources for a Deck
	 * 
	 * @param deckID
	 *            - the Deck ID
	 * @return all the Gem Resources for the given ID
	 */
	public List<GemResource> getAllGemResourcesForDeck(String deckID) {
		ArrayList<GemResource> gemResources = new ArrayList<GemResource>();
		String selectQuery = "SELECT  * FROM " + TABLE_GEM_RESOURCES + " WHERE " + DECK_ID + " = '" + deckID + "'";
		SQLiteDatabase db = this.getReadableDatabase();

		try {
			Cursor results = db.rawQuery(selectQuery, null);
			if (results.moveToFirst()) {
				do {
					gemResources.add(createNewGemResource(results));
				} while (results.moveToNext());
			}
		} catch (SQLiteException ex) {

		} finally {
			db.close();
		}

		return gemResources;
	}

	/**
	 * Get every Gem Resource for all Decks
	 * 
	 * @return every Gem Resource for all Decks
	 */
	public List<GemResource> getAllGemResources() {
		ArrayList<GemResource> gemResources = new ArrayList<GemResource>();
		String selectQuery = "SELECT  * FROM " + TABLE_GEM_RESOURCES;
		SQLiteDatabase db = this.getReadableDatabase();

		try {
			Cursor results = db.rawQuery(selectQuery, null);
			if (results.moveToFirst()) {
				do {
					gemResources.add(createNewGemResource(results));
				} while (results.moveToNext());
			}
		} catch (SQLiteException ex) {

		} finally {
			db.close();
		}

		return gemResources;
	}

	/**
	 * HELPER METHODS
	 */

	/**
	 * Get a Champion from the list of all champions
	 * 
	 * @param name
	 *            - the name of the champion
	 * @return the champion with the given name, otherwise null
	 */
	private Champion getChampionFromAllChampions(String name) {
		for (Champion champion : allChampions) {
			if (name != null && champion.name.contentEquals(name)) {
				return champion;
			}
		}

		return null;
	}

	/**
	 * Create a new Deck instance for the given database data
	 * 
	 * @param results
	 *            - database data for a Deck
	 * @return a Deck from the given data
	 */
	private HexDeck createNewDeck(Cursor results) {
		HexDeck deck = new HexDeck();
		deck.id = new GlobalIdentifier(String.valueOf(results.getInt(results.getColumnIndex(ID))));
		deck.name = results.getString(results.getColumnIndex(NAME));
		deck.champion = getChampionFromAllChampions(results.getString(results.getColumnIndex(CHAMPION_NAME)));

		List<DeckResource> deckResources = getAllDeckResourcesForDeck(deck.getID());
		if (!deckResources.isEmpty()) {
			deck.deckResources = deckResources.toArray(new DeckResource[] {});
		}

		return deck;
	}

	/**
	 * Create a new Deck Resource instance for the given database data
	 * 
	 * @param results
	 *            - database data for a Deck Resource
	 * @return a Deck Resource for the given data
	 */
	private DeckResource createNewDeckResource(Cursor results) {
		DeckResource deckResource = new DeckResource();
		deckResource.cardID = new GlobalIdentifier(results.getString(results.getColumnIndex(CARD_ID)));
		deckResource.cardCount = Integer.valueOf(results.getString(results.getColumnIndex(CARD_COUNT)));

		return deckResource;
	}

	/**
	 * Create a new Gem Resource instance for the given database data
	 * 
	 * @param results
	 *            - database data for a Gem Resource
	 * @return a Gem Resource for the given data
	 */
	private GemResource createNewGemResource(Cursor results) {
		GemResource gemResource = new GemResource();
		gemResource.gemId = new GlobalIdentifier(results.getString(results.getColumnIndex(GEM_ID)));
		gemResource.deckId = new GlobalIdentifier(results.getString(results.getColumnIndex(DECK_ID)));
		gemResource.cardId = new GlobalIdentifier(results.getString(results.getColumnIndex(CARD_ID)));
		gemResource.gemCount = Integer.valueOf(results.getString(results.getColumnIndex(GEM_COUNT)));

		return gemResource;
	}

}
