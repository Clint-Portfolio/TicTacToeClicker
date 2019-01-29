/*
 * PlayerDatabase class for ClickTacToe by Clint Nieuwendijk
 * The class handles all database manipulations for the player
 */
package com.example.clintnieuwendijk.tictactoeclicker;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.Settings;
import android.util.Log;

import java.util.Locale;

public class PlayerDatabase extends SQLiteOpenHelper {

    private SQLiteDatabase db;
    private static PlayerDatabase instance;

    private PlayerDatabase(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        this.db = db;
        db.execSQL("CREATE TABLE 'player' ('_id' integer PRIMARY KEY AUTOINCREMENT NOT NULL, 'name' text, 'tokens' integer)");
    }

    static PlayerDatabase getInstance(Context context) {
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

    void makePlayer(Context context){

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

    private Cursor selectAll() {
        db = getWritableDatabase();
        return db.rawQuery("SELECT * FROM 'player'", null);
    }

    // request tokens from database
    int getTokens() {
        this.db = getWritableDatabase();
        String sql = "SELECT * FROM 'player'";
        @SuppressLint("Recycle") Cursor queryResponse = db.rawQuery(sql, null);

        queryResponse.moveToFirst();

        return queryResponse.getInt(queryResponse.getColumnIndex("tokens"));
    }

    // update number of tokes
    void updateTokens(int numTokens) {

        this.db = getWritableDatabase();

        int currentTokens = getTokens();
        currentTokens += numTokens;
        String sql = String.format(Locale.US, "UPDATE 'player' SET 'tokens' = %d", currentTokens);
        db.execSQL(sql);
    }

    // reset all token progress
    void resetProgress(){
        db = getWritableDatabase();
        db.execSQL("UPDATE 'player' SET 'tokens' = 0");
    }
}
