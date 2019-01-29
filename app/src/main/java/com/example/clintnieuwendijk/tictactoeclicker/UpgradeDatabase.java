/*
 * The UpgradeDatabase class by Clint Nieuwendijk
 * this class handles all interactions with the upgrade database
 */

package com.example.clintnieuwendijk.tictactoeclicker;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.Locale;

public class UpgradeDatabase extends SQLiteOpenHelper {

    private SQLiteDatabase db;
    private static UpgradeDatabase instance;

    private UpgradeDatabase(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        this.db = db;
        db.execSQL("CREATE TABLE 'upgrades' ('_id' integer PRIMARY KEY AUTOINCREMENT NOT NULL, 'upgradeid' text, 'name' text, 'description' text, 'cost' integer, 'unlocked' integer, 'max_unlock' integer)");
        db.execSQL("INSERT INTO 'upgrades' ('_id', 'upgradeid', 'name', 'description', 'cost', 'unlocked', 'max_unlock') VALUES (NULL, 'multiplier', 'More tokens!', 'recieve more tokens per click (max tier: 5)', 10, 1, 5), (NULL, 'boardsize', 'Larger board', 'Unlock a larger board (max size: 6x6)', 15, 2, 6)"); // An extra would be: , (NULL, 'AI', 'An AI to play against you (tiers: hard, medium, easy', 20, 0, 0")
    }

    static UpgradeDatabase getInstance(Context context) {
        if (instance == null) {
            instance = new UpgradeDatabase(context, "upgrades", null, 1);
        }
        return instance;
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE 'upgrades'");
        onCreate(db);
    }

    Cursor selectAll() {
        db = getWritableDatabase();
        return db.rawQuery("SELECT * FROM 'upgrades'", null);
    }

    int getUpgradeCost(long rowIndex){
        db = getWritableDatabase();
        String sql = String.format(Locale.US, "SELECT * FROM 'upgrades' WHERE (rowid = %d)", rowIndex);
        Cursor cursor = db.rawQuery(sql, null);
        cursor.moveToFirst();
        int tier = cursor.getInt(cursor.getColumnIndex("unlocked"));
        int cost = cursor.getInt(cursor.getColumnIndex("cost"));
        cursor.close();
        return tier * cost;
    }

    void updateUpgrade(long rowIndex) {
        db = getWritableDatabase();

        String sql = String.format(Locale.US, "SELECT * FROM 'upgrades' WHERE (rowid = %d)", rowIndex);
        Cursor queryResponse = db.rawQuery(sql, null);
        queryResponse.moveToFirst();
        int tier = queryResponse.getInt(queryResponse.getColumnIndex("unlocked"));
        int maxTier = queryResponse.getInt(queryResponse.getColumnIndex("max_unlock"));
        queryResponse.close();
        if (tier < maxTier) {

            tier++;

            sql = String.format(Locale.US, "UPDATE 'upgrades' SET 'unlocked' = %d WHERE (rowid = %d)", tier, rowIndex);
            Log.d("sql", sql);

            db.execSQL(sql);

        }
    }

    public int getMultiplier() {
        db = getWritableDatabase();
        String sql = "SELECT * FROM 'upgrades' WHERE upgradeid = 'multiplier'";
        Cursor queryResponse = db.rawQuery(sql, null);

        queryResponse.moveToFirst();
        int response = queryResponse.getInt(queryResponse.getColumnIndex("unlocked"));
        queryResponse.close();
        return response;
    }

    int getBoardSize() {
        db = getWritableDatabase();
        String sql = "SELECT * FROM 'upgrades' WHERE upgradeid = 'boardsize'";
        Cursor queryResponse = db.rawQuery(sql, null);
        queryResponse.moveToFirst();
        int response = queryResponse.getInt(queryResponse.getColumnIndex("unlocked"));
        queryResponse.close();
        return response;
    }


    void resetProgress() {
        db = getWritableDatabase();
        db.execSQL("DROP TABLE 'upgrades'");
        onCreate(db);
    }
}
