package com.example.clintnieuwendijk.tictactoeclicker;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class UpgradeDatabase extends SQLiteOpenHelper {

    SQLiteDatabase db;
    private static UpgradeDatabase instance;

    public UpgradeDatabase(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        this.db = db;
        db.execSQL("CREATE TABLE 'upgrades' ('_id' integer PRIMARY KEY AUTOINCREMENT NOT NULL, 'name' text, 'description' text, 'cost', integer, 'unlocked' integer, 'max_unlock' integer)");
        db.execSQL("INSERT INTO 'upgrades' ('_id', 'name', 'description', 'cost', 'unlocked', 'max_unlock') VALUES (NULL, 'More tokens!', 'recieve more tokens per click (max tier: 5)', 10, 0, 5), (NULL, 'Larger board', 'Unlock a larger board (max size: 6x6)', 15, 0, 4)"); // An extra would be: , (NULL, 'AI', 'An AI to play against you (tiers: hard, medium, easy', 20, 0, 0")
    }

    public static UpgradeDatabase getInstance(Context context) {
        if (instance == null) {
            instance = new UpgradeDatabase(context, "upgrades", null, 1);
        }
        return instance;
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE 'entries'");
        onCreate(db);
    }

    public Cursor selectAll() {
        db = getWritableDatabase();
        return db.rawQuery("SELECT * FROM 'upgrades'", null);
    }

    public int getUpgradeCost(long rowIndex){
        db = getWritableDatabase();
        String sql = String.format("SELECT * FROM 'upgrades' WHERE (rowid = %d)", rowIndex);
        Cursor cursor = db.rawQuery(sql, null);
        cursor.moveToFirst();
        int tier = cursor.getInt(cursor.getColumnIndex("unlocked"));
        int cost = cursor.getInt(cursor.getColumnIndex("cost"));

        return tier * cost;
    }



    public void updateUpgrade(long rowIndex) {
        db = getWritableDatabase();

        String sql = String.format("SELECT * FROM 'upgrades' WHERE (rowid = %d)", rowIndex);
        Cursor queryResponse = db.rawQuery(sql, null);
        queryResponse.moveToFirst();

        int tier = queryResponse.getInt(queryResponse.getColumnIndex("unlocked"));
        int maxTier = queryResponse.getInt(queryResponse.getColumnIndex("max_unlock"));

        if (tier < maxTier) {

            tier++;

            sql = String.format("UPDATE 'upgrades' SET 'unlocked' = %d WHERE (rowid = %d)", tier, rowIndex);
            Log.d("sql", sql);

            db.execSQL(sql);
            sql = String.format("SELECT * FROM 'upgrades' WHERE (rowid = %d)", rowIndex);
            queryResponse = db.rawQuery(sql, null);
            queryResponse.moveToFirst();
            tier = queryResponse.getInt(queryResponse.getColumnIndex("unlocked"));

            Log.d("Tier", Integer.toString(tier));

        }
    }

}
