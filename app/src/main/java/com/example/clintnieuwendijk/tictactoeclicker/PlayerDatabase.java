package com.example.clintnieuwendijk.tictactoeclicker;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.Settings;
import android.util.Log;

public class PlayerDatabase extends SQLiteOpenHelper {

    SQLiteDatabase db;
    private static PlayerDatabase instance;

    public PlayerDatabase(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        this.db = db;
        db.execSQL("CREATE TABLE 'player' ('_id' integer PRIMARY KEY AUTOINCREMENT NOT NULL, 'name' text, 'tokens' integer)");
    }

    public static PlayerDatabase getInstance(Context context) {
        if (instance == null) {
            instance = new PlayerDatabase(context, "player", null, 1);
        }
        return instance;
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE 'entries'");
        onCreate(db);
    }

    public void makePlayer(Context context){

        this.db = getWritableDatabase();
        Cursor cursor = selectAll();

        if (cursor.getCount() < 1) {
            String android_id = Settings.System.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
            String sql = String.format("INSERT INTO 'player' ('_id', 'name', 'tokens') VALUES (NULL, '%s', 0)", android_id);
            Log.d("sql", sql);
            db.execSQL(sql);
        }

        cursor = selectAll();
        Log.d("dbsize", Integer.toString(cursor.getCount()));
    }

    public Cursor selectAll() {
        this.db = getWritableDatabase();
        return db.rawQuery("SELECT * FROM 'player'", null);
    }

    public int getTokens() {
        this.db = getWritableDatabase();
        String sql = String.format("SELECT * FROM 'player'");
        Cursor queryResponse = db.rawQuery(sql, null);

        queryResponse.moveToFirst();

        return queryResponse.getInt(queryResponse.getColumnIndex("tokens"));
    }

    public int updateTokens(int numTokens) {

        this.db = getWritableDatabase();

        int currentTokens = getTokens();
        currentTokens += numTokens;
        String sql = String.format("UPDATE 'player' SET 'tokens' = %d", currentTokens);
        db.execSQL(sql);
        return currentTokens;
    }

    public void resetProgress(){
        db = getWritableDatabase();
        db.execSQL("UPDATE 'player' SET 'tokens' = 0");
    }
}
