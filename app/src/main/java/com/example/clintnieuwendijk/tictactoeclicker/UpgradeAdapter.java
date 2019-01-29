/*
 * UpgradeAdapter class by Clint Nieuwendijk
 * A simple adapter for the ListView in the class UpgradeActivity
 */
package com.example.clintnieuwendijk.tictactoeclicker;

import android.content.Context;
import android.database.Cursor;
import android.view.View;
import android.widget.ResourceCursorAdapter;
import android.widget.TextView;

import java.util.Locale;

public class UpgradeAdapter extends ResourceCursorAdapter {

    UpgradeAdapter(Context context, Cursor c){
        super(context, R.layout.activity_upgrade_entry, c, false);
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

        String upgradeCostFormat = String.format(Locale.US, "Cost: %d Ticcoin", upgradeCost);
        upgradeCostText.setText(upgradeCostFormat);

        String upgradeTierFormat = String.format(Locale.US, "Tier: %d", tierLevel);
        upgradeTier.setText(upgradeTierFormat);
    }
}
