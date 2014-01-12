package com.ozawa.hextcgdeckbuilder.database;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.google.gson.Gson;
import com.ozawa.hextcgdeckbuilder.R;
import com.ozawa.hextcgdeckbuilder.hexentities.AbstractCard;
import com.ozawa.hextcgdeckbuilder.hexentities.Champion;
import com.ozawa.hextcgdeckbuilder.hexentities.Deck;
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
 * Database Handler classed used to read and write application information
 * to the SQLite Database
 * 
 * @author Chad Kinsella
 */
public class DatabaseHandler extends SQLiteOpenHelper {

	// Database Version
    private static final int DATABASE_VERSION = 2;
    // Database Name
    private static final String DATABASE_NAME = "HexTCGDeckBuilderDB";
    
    // Table Names
    private static final String TABLE_DECKS = "decks";
    private static final String TABLE_DECK_RESOURCES = "deck_resources";
    private static final String TABLE_CHAMPIONS = "champions";
    // Column Names
    private static final String ID = "id";
    private static final String NAME = "name";
    private static final String CARD_ID = "card_id";
    private static final String CARD_COUNT = "card_count";
    private static final String DECK_ID = "deck_id";
    private static final String CHAMPION_NAME = "champion_name";
    
    // Champion Columns
    private static final String SET_ID = "set_id";
    private static final String HUD_PORTRAIT = "hud_portrait";
    private static final String HUD_PORTRAIT_SMALL = "hud_portrait_small";
    private static final String GAME_TEXT = "game_text";
    
    public List<Champion> allChampions;
    
	public DatabaseHandler(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
		allChampions = generateAllChampionsFromJson(context);
		
		for(Champion champion : generateAllChampionsFromJson(context)){
			if(getChampion(champion.name) != null){
				updateChampion(champion);
			}else{
				addChampion(champion);
			}
		}
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		if(!db.isReadOnly()){
			db.execSQL("PRAGMA foreign_keys=ON;");
		}
		// SQL statement to create book table
        String CREATE_DECKS_TABLE = "CREATE TABLE " + TABLE_DECKS + " (" +
        		ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                NAME + " TEXT, " + 
        		CHAMPION_NAME + " TEXT, " +
                "FOREIGN KEY(" + CHAMPION_NAME + ") REFERENCES " + TABLE_CHAMPIONS + "(" + NAME + "));";
        
        String CREATE_DECK_RESOURCES_TABLE = "CREATE TABLE " + TABLE_DECK_RESOURCES + " ( " +
                CARD_ID + " TEXT, " +
                CARD_COUNT + " INTEGER, " +
                DECK_ID + " INTEGER NOT NULL, " +
                "FOREIGN KEY("+ DECK_ID +") REFERENCES " + TABLE_DECKS + "(" + ID + ") ON DELETE CASCADE);";
        
        String CREATE_CHAMPIONS_TABLE = "CREATE TABLE " + TABLE_CHAMPIONS + " (" +
                NAME + " TEXT PRIMARY KEY," + 
        		SET_ID + " TEXT, " +
                HUD_PORTRAIT + " TEXT, " +
        		HUD_PORTRAIT_SMALL + " TEXT," +
                GAME_TEXT + " TEXT);";
        
        db.execSQL(CREATE_CHAMPIONS_TABLE);
        db.execSQL(CREATE_DECKS_TABLE);
        db.execSQL(CREATE_DECK_RESOURCES_TABLE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_CHAMPIONS);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_DECKS);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_DECK_RESOURCES);
		
		this.onCreate(db);
	}
	
	@Override
	public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
	    onUpgrade(db, oldVersion, newVersion);
	}
	
	/**
	 * Champion Table Database Methods
	 */
	
	public boolean addChampion(Champion champion){
		SQLiteDatabase db = this.getWritableDatabase();
		boolean inserted = true;
		ContentValues values = new ContentValues();
		values.put(NAME, champion.name);
		values.put(SET_ID, champion.setID.gUID);
		values.put(HUD_PORTRAIT, champion.hudPortrait);
		values.put(HUD_PORTRAIT_SMALL, champion.hudPortraitSmall);
		values.put(GAME_TEXT, champion.gameText);
		
		try{	
			db.beginTransaction();
			db.insert(TABLE_CHAMPIONS, null, values);
			db.setTransactionSuccessful();
		}catch(Exception ex){
			inserted = false;
		}finally{
			db.endTransaction(); // If error occurs during transaction, no data is committed
			db.close();
		}
		
		return inserted;
	}
	
	public Champion getChampion(String name){
		SQLiteDatabase db = this.getReadableDatabase();
		Champion champion = null;
		try{
			Cursor results = db.query(TABLE_CHAMPIONS, new String[]{NAME, SET_ID, HUD_PORTRAIT, HUD_PORTRAIT_SMALL, GAME_TEXT}, NAME + "= ?", new String[]{name}, null, null, null, null);
			
			if(results == null){
				return null;
			}
			
			results.moveToFirst();
			
			champion = createNewChampion(results);
		}catch(Exception ex){
			System.out.println("****** ERROR GETTING CHAMPION ******");
		}
		
		return champion;
	}
	
	public boolean updateChampion(Champion champion){
		SQLiteDatabase db = this.getWritableDatabase();
		boolean updated = true;
		ContentValues values = new ContentValues();
		values.put(NAME, champion.name);
		values.put(SET_ID, champion.getSetID());
		values.put(HUD_PORTRAIT, champion.hudPortrait);
		values.put(HUD_PORTRAIT_SMALL, champion.hudPortraitSmall);
		values.put(GAME_TEXT, champion.gameText);
		try{
			db.beginTransaction();
			db.update(TABLE_CHAMPIONS, values, NAME + "= ?", new String[]{champion.name});
			db.setTransactionSuccessful();
		}catch(Exception ex){
			updated = false;
		}finally{
			db.endTransaction();
			db.close();
		}
		return updated;
	}
	
	public boolean deleteChampion(Champion champion) {
	    SQLiteDatabase db = this.getWritableDatabase();
	    boolean deleted = true;
	    
	    try{
			db.beginTransaction();
			db.delete(TABLE_CHAMPIONS, NAME + " = ?", new String[] { champion.name });
			db.setTransactionSuccessful();
		}catch(Exception ex){
			deleted = false;
		}finally{
			db.endTransaction(); // If error occurs during transaction, no data is committed
			db.close();
		}
	    
	    return deleted;
	}
	
	private Champion createNewChampion(Cursor results){
		Champion champion = new Champion();
		
		champion.setID = new GlobalIdentifier(String.valueOf(results.getInt(results.getColumnIndex(SET_ID))));
		champion.name = results.getString(results.getColumnIndex(NAME));
		champion.hudPortrait = results.getString(results.getColumnIndex(HUD_PORTRAIT));
		champion.hudPortraitSmall = results.getString(results.getColumnIndex(HUD_PORTRAIT_SMALL));
		champion.gameText = results.getString(results.getColumnIndex(GAME_TEXT));
		
		return champion;		
	}
	
	private List<Champion> generateAllChampionsFromJson(Context context){
		ArrayList<Champion> allChamps = new ArrayList<Champion>();
		
		try {
			ArrayList<InputStream> championFiles = getChampionJson(context);
			Gson gson = new Gson();
			for(InputStream json : championFiles){
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
                if(name.contains("champion") && !name.contains("portrait")){
                    InputStream inputStream = res.openRawResource(rid);
                    if(inputStream != null){
                        jsonFiles.add(inputStream);
                    }
                }
            } catch (Exception e) {
            }
        }
        return jsonFiles;
    }
	
	/**
	 * Deck Table Database Methods
	 */
	
	/**
	 * Add a new Deck to the database
	 * 
	 * @param deck - the deck to be added
	 * @return the primary key ID for the new deck
	 */
	public long addDeck(Deck deck){
		SQLiteDatabase db = this.getWritableDatabase();
		long rowID = -1;
		ContentValues values = new ContentValues();
		values.put(NAME, deck.name);
		if(deck.champion != null){
			values.put(CHAMPION_NAME, deck.champion.name);
		}
		
		try{	
			db.beginTransaction();
			rowID = db.insert(TABLE_DECKS, null, values);
			db.setTransactionSuccessful();
		}catch(Exception ex){
			
		}finally{
			db.endTransaction(); // If error occurs during transaction, no data is committed
			db.close();
		}
		
		return rowID;
	}
	
	/**
	 * Update the deck data in the database
	 * 
	 * @param deck
	 */
	public boolean updateDeck(Deck deck){
		SQLiteDatabase db = this.getWritableDatabase();
		boolean updated = true;
		ContentValues values = new ContentValues();
		values.put(NAME, deck.name);
		if(deck.champion != null){
			values.put(CHAMPION_NAME, deck.champion.name);
		}
		
		try{
			db.beginTransaction();
			db.update(TABLE_DECKS, values, ID + "= ?", new String[]{deck.getID()});
			db.setTransactionSuccessful();
		}catch(Exception ex){
			updated = false;
		}finally{
			db.endTransaction(); // If error occurs during transaction, no data is committed
			db.close();
		}
		
		return updated;
	}
	
	/**
	 * Delete a Deck from the database
	 * 
	 * @param deck - the Deck to be deleted
	 * @return true if the Deck was successfully deleted, otherwise false
	 */
	public boolean deleteDeck(Deck deck) {
	    SQLiteDatabase db = this.getWritableDatabase();
	    boolean deleted = true;
	    try{
			db.beginTransaction();
			db.delete(TABLE_DECKS, ID + " = ?", new String[] { deck.getID() });
			db.setTransactionSuccessful();
		}catch(Exception ex){
			deleted = false;
		}finally{
			db.endTransaction(); // If error occurs during transaction, no data is committed
			db.close();
		}
	    
	    return deleted;
	}
	
	/**
	 * Get a Deck from the database
	 * 
	 * @param id - the ID of the Deck
	 * @return the Deck with the given ID if found, otherwise null
	 */
	public Deck getDeck(String id){
		SQLiteDatabase db = this.getReadableDatabase();
		Deck deck = null;
		try{
			Cursor results = db.query(TABLE_DECKS, new String[]{ID, NAME, CHAMPION_NAME}, ID + "= ?", new String[]{id}, null, null, null, null);
			
			if(results == null){
				return null;
			}
			
			results.moveToFirst();
			
			deck = createNewDeck(results);
		}catch(SQLiteException ex){
			
		}finally{
			db.close();
		}
		
		return deck;
	}
	
	/**
	 * Get all the Decks from the database
	 * 
	 * @return all of the stored Decks
	 */
	public List<Deck> getAllDecks(){
		ArrayList<Deck> decks = new ArrayList<Deck>();
	    String selectQuery = "SELECT * FROM " + TABLE_DECKS;
		SQLiteDatabase db = this.getReadableDatabase();
		
		try{
			Cursor results = db.rawQuery(selectQuery, null);
			if(results.moveToFirst()){
				do{
					decks.add(createNewDeck(results));
				}while(results.moveToNext());
			}
		}catch(SQLiteException ex){
			
		}finally{
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
	 * @param deck - the Deck to update
	 * @param cardData - the cards to add
	 */
	public void addDeckResources(Deck deck, HashMap<AbstractCard, Integer> cardData){
		SQLiteDatabase db = this.getWritableDatabase();
		
		try{
			db.beginTransaction();
			
			addDeckResources(deck, cardData, db);
			
			db.setTransactionSuccessful();
		}catch(Exception ex){
			
		}finally{
			db.endTransaction(); // If error occurs during transaction, no data is committed
			db.close();
		}
	}
	
	/**
	 * Add a Deck Resource to a Deck
	 * 
	 * @param deck - the Deck to update
	 * @param card - the cards to add
	 * @param cardCount - the amount of that card the Deck has
	 */
	public void addDeckResource(Deck deck, AbstractCard card, int cardCount){
		SQLiteDatabase db = this.getWritableDatabase();
		
		ContentValues values = new ContentValues();
		values.put(DECK_ID, deck.getID());
		values.put(CARD_COUNT, cardCount);
		values.put(CARD_ID, card.getID());		
		
		try{
			db.beginTransaction();
			db.insert(TABLE_DECK_RESOURCES, null, values);
			db.setTransactionSuccessful();
		}catch(Exception ex){
			
		}finally{
			db.endTransaction(); // If error occurs during transaction, no data is committed
			db.close();
		}
	}
	
	/**
	 * Add Deck Resources to a Deck
	 * 
	 * @param deck - the Deck to update
	 * @param cardData - the cards to add
	 * @param db - the database
	 */
	private void addDeckResources(Deck deck, HashMap<AbstractCard, Integer> cardData, SQLiteDatabase db){
		List<AbstractCard> cards = new ArrayList<AbstractCard>(cardData.keySet());
		
		for(AbstractCard card : cards){
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
	 * @param deck - the Deck to update
	 * @param cardData - the cards to add
	 * @return true if updated successfully, otherwise false
	 */
	public boolean updateDeckResources(Deck deck, HashMap<AbstractCard, Integer> cardData){
		SQLiteDatabase db = this.getWritableDatabase();
		boolean updated = true;
		
		try{
			db.beginTransaction();
			
			deleteDeckResources(deck, db);
			addDeckResources(deck, cardData, db);
			
			db.setTransactionSuccessful();
		}catch(Exception ex){
			updated = false;
		}finally{
			db.endTransaction();
			db.close();
		}
		
		return updated;
		
	}
	
	/**
	 * Update the Deck Resources of the Deck
	 * 
	 * @param deck - the Deck to update
	 * @param card - the card to add
	 * @param cardCount - the number of the card in the Deck
	 */
	public void updateDeckResource(Deck deck, AbstractCard card, int cardCount){
		SQLiteDatabase db = this.getWritableDatabase();
		
		ContentValues values = new ContentValues();
		values.put(CARD_COUNT, cardCount);
		values.put(CARD_ID, card.getID());
		
		try{
			db.beginTransaction();
			db.update(TABLE_DECK_RESOURCES, values, DECK_ID + "= ? AND " + CARD_ID + "= ?", new String[]{deck.getID(), card.getID()});
			db.setTransactionSuccessful();
		}catch(Exception ex){
			
		}finally{
			db.endTransaction(); // If error occurs during transaction, no data is commited
			db.close();
		}
		
	}
	
	/**
	 * Delete the Deck Resources from a Deck
	 * 
	 * @param deck - the Deck to update
	 * @param cardData - the cards to delete
	 */
	public void deleteDeckResources(Deck deck, HashMap<AbstractCard, Integer> cardData){
		SQLiteDatabase db = this.getWritableDatabase();
		
		try{
			db.beginTransaction();
			deleteDeckResources(deck, db);
			db.setTransactionSuccessful();
		}catch(Exception ex){
			
		}finally{
			db.endTransaction(); // If error occurs during transaction, no data is committed
			db.close();
		}
	}
	
	/**
	 * Delete the Deck Resources from a Deck
	 * 
	 * @param deck - the Deck to update
	 * @param db - the database
	 */
	private void deleteDeckResources(Deck deck, SQLiteDatabase db){
		db.delete(TABLE_DECK_RESOURCES, DECK_ID + "= ?", new String[]{deck.getID()});
	}
	
	/**
	 * Delete a Deck Resource from a Deck
	 * 
	 * @param deck - the Deck to update
	 * @param card - the card to delete
	 * @param cardCount - the number of that card to delete
	 */
	public void deleteDeckResource(Deck deck, AbstractCard card, int cardCount){
		SQLiteDatabase db = this.getWritableDatabase();
		
		try{
			db.beginTransaction();
			db.delete(TABLE_DECK_RESOURCES, DECK_ID + "= ? AND " + CARD_ID + "= ?", new String[]{deck.getID(), card.getID()});
			db.setTransactionSuccessful();
		}catch(Exception ex){
			
		}finally{
			db.endTransaction(); // If error occurs during transaction, no data is committed
			db.close();
		}		
	}
	
	/**
	 * Get a Deck Resource
	 * 
	 * @param deckID - the Deck ID
	 * @param cardID - the card ID
	 * @return the Deck Resource for the given IDs
	 */
	public DeckResource getDeckResource(String deckID, String cardID){
		SQLiteDatabase db = this.getReadableDatabase();
		DeckResource deckResource = null;
		try{
			Cursor results = db.query(TABLE_DECKS, new String[]{ID, NAME}, DECK_ID + "= ? AND " + CARD_ID + "= ?", new String[]{deckID, cardID}, null, null, null, null);
			
			if(results == null){
				return null;
			}
			
			results.moveToFirst();
			deckResource = createNewDeckResource(results);
		}catch(SQLiteException ex){
			
		}finally{
			db.close();
		}
		
		return deckResource;
	}
	
	/**
	 * Get all the Deck Resources for a Deck
	 * 
	 * @param deckID - the Deck ID
	 * @return all the Deck Resources for the given ID
	 */
	public List<DeckResource> getAllDeckResourcesForDeck(String deckID){
		ArrayList<DeckResource> deckResources = new ArrayList<DeckResource>();
	    String selectQuery = "SELECT  * FROM " + TABLE_DECK_RESOURCES + " WHERE " + DECK_ID + " = '" + deckID + "'";
		SQLiteDatabase db = this.getReadableDatabase();
		
		try{
			Cursor results = db.rawQuery(selectQuery, null);
			if(results.moveToFirst()){
				do{
					deckResources.add(createNewDeckResource(results));
				}while(results.moveToNext());
			}
		}catch(SQLiteException ex){
			
		}finally{
			db.close();
		}
		
		return deckResources;
	}
	
	/**
	 * Get every Deck Resource for all Decks
	 * 
	 * @return every Deck Resource for all Decks
	 */
	public List<DeckResource> getAllDeckResources(){
		ArrayList<DeckResource> deckResources = new ArrayList<DeckResource>();
	    String selectQuery = "SELECT  * FROM " + TABLE_DECK_RESOURCES;
		SQLiteDatabase db = this.getReadableDatabase();
		
		try{
			Cursor results = db.rawQuery(selectQuery, null);
			if(results.moveToFirst()){
				do{
					deckResources.add(createNewDeckResource(results));
				}while(results.moveToNext());
			}
		}catch(SQLiteException ex){
			
		}finally{
			db.close();
		}
		
		return deckResources;
	}
	
	/**
	 * HELPER METHODS
	 */
	
	/**
	 * Get a Champion from the list of all champions
	 * 
	 * @param name - the name of the champion
	 * @return the champion with the given name, otherwise null
	 */
	private Champion getChampionFromAllChampions(String name){
		for(Champion champion : allChampions){
			if(name != null && champion.name.contentEquals(name)){
				return champion;
			}
		}
		
		return null;
	}
	
	/**
	 * Create a new Deck instance for the given database data
	 * 
	 * @param results - database data for a Deck
	 * @return a Deck from the given data
	 */
	private Deck createNewDeck(Cursor results){
		Deck deck = new Deck();
		deck.id = new GlobalIdentifier(String.valueOf(results.getInt(results.getColumnIndex(ID))));
		deck.name = results.getString(results.getColumnIndex(NAME));
		deck.champion = getChampionFromAllChampions(results.getString(results.getColumnIndex(CHAMPION_NAME)));
		
		List<DeckResource> deckResources = getAllDeckResourcesForDeck(deck.getID());
		if(!deckResources.isEmpty()){
			deck.deckResources = deckResources.toArray(new DeckResource[]{});
		}
		
		return deck;
	}
	
	/**
	 * Create a new Deck Resource instance for the given database data
	 * 
	 * @param results - database data for a Deck Resource
	 * @return a Deck Resource for the given data
	 */
	private DeckResource createNewDeckResource(Cursor results){
		DeckResource deckResource = new DeckResource();
		deckResource.cardID = new GlobalIdentifier(results.getString(results.getColumnIndex(CARD_ID)));
		deckResource.cardCount = Integer.valueOf(results.getString(results.getColumnIndex(CARD_COUNT)));
		
		return deckResource;
	}

}
