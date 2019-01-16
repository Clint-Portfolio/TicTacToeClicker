package com.example.clintnieuwendijk.tictactoeclicker;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ResourceCursorAdapter;
import android.widget.TextView;

import java.util.Locale;

public class UpgradeAdapter extends ResourceCursorAdapter {

    public UpgradeAdapter(Context context, Cursor c){
        super(context, R.layout.activity_upgrade_entry, c, false);
    }

    public UpgradeEntry getItem(Cursor cursor) {
        int id = cursor.getInt(cursor.getColumnIndex("_id"));
        String name = cursor.getString(cursor.getColumnIndex("name"));
        String description = cursor.getString(cursor.getColumnIndex("description"));
        int cost = cursor.getInt(cursor.getColumnIndex("cost"));
        int tier = cursor.getInt(cursor.getColumnIndex("unlocked"));

        return new UpgradeEntry(id, cost, tier, name, description);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        TextView upgradeName = view.findViewById(R.id.upgradeName);
        TextView upgradeDescription = view.findViewById(R.id.upgradeDescription);
        TextView upgradeCostText = view.findViewById(R.id.upgradeCostText);
        TextView upgradeTier = view.findViewById(R.id.upgradeTierText);

        upgradeName.setText(cursor.getString(cursor.getColumnIndex("name")));
        upgradeDescription.setText(cursor.getString(cursor.getColumnIndex("description")));

        int tierLevel = cursor.getInt(cursor.getColumnIndex("unlocked"));
        int upgradeCost = cursor.getInt(cursor.getColumnIndex("cost")) * tierLevel;

        String upgradeCostFormat = String.format(Locale.US, "Cost: %d", upgradeCost);
        upgradeCostText.setText(upgradeCostFormat);

        String upgradeTierFormat = String.format(Locale.US, "Tier: %d", tierLevel);
        upgradeTier.setText(upgradeTierFormat);
    }
}
