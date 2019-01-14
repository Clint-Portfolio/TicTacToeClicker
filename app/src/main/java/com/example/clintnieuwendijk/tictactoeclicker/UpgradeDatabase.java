package com.example.clintnieuwendijk.tictactoeclicker;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class UpgradeDatabase extends SQLiteOpenHelper {

    SQLiteDatabase db;

    public UpgradeDatabase(@androidx.annotation.Nullable Context context, @androidx.annotation.Nullable String name, @androidx.annotation.Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        this.db = db;
        db.execSQL("CREATE TABLE 'upgrades' ('upgrade_id' integer PRIMARY KEY AUTOINCREMENT NOT NULL, 'name' text, 'description' text, 'cost', integer, 'unlocked' integer, 'max_unlock' integer)");
        db.execSQL("INSERT INTO 'upgrades' ('upgrade_id', 'name', 'description', 'cost', 'unlocked', 'max_unlock') VALUES (NULL, 'More tokens!', 'recieve more tokens per click (max tier: 5)', 10, 0, 5), (NULL, 'Larger board', 'Unlock a larger board (max size: 6x6)', 15, 0, 4)"); // An extra would be: , (NULL, 'AI', 'An AI to play against you (tiers: hard, medium, easy', 20, 0, 0")
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE 'entries'");
        onCreate(db);
    }

    public Cursor selectAll() {
        this.db = getWritableDatabase();
        return db.rawQuery("SELECT * FROM 'upgrades'", null);
    }

    Cursor selectUpgradeByID(long rowIndex) {
        db = getWritableDatabase();
        String[] queryIds = new String[1];
        queryIds[0] = Long.toString(rowIndex);
        String sql = String.format("SELECT * FROM 'upgrades' WHERE (rowid = %d)", rowIndex);
        return db.rawQuery(sql, null);
    }

    public void updateUpgrade(long rowIndex) {
        db = getWritableDatabase();
//        String[] queryIds = new String[1];
//        queryIds[0] = Long.toString(rowIndex);
        String sql = String.format("SELECT 'unlocked' FROM 'upgrades' WHERE (rowid = %d)", rowIndex);
        Cursor queryResponse = db.rawQuery(sql, null);
        int upgradeTier = queryResponse.getInt(queryResponse.getColumnIndex("unlocked"));

        String sql = String.format("UPDATE 'upgrades' SET 'unlocked' = %d WHERE (rowid = %d)", upgradeTier + 1, rowIndex);
        db.rawQuery(sql, null);
    }

}
