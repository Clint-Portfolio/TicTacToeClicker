package com.example.clintnieuwendijk.tictactoeclicker;

import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

public class UpgradeActivity extends AppCompatActivity {

    UpgradeDatabase upgradeDB;
    PlayerDatabase playerDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upgrade);

        upgradeDB = UpgradeDatabase.getInstance(getApplicationContext());
        playerDB = PlayerDatabase.getInstance(getApplicationContext());

        updateData();
    }

    private void updateData() {
        Cursor allEntries = upgradeDB.selectAll();
        ListView lv = findViewById(R.id.upgradeListView);
        UpgradeAdapter upgradeAdapter = new UpgradeAdapter(getApplicationContext(), allEntries);

        lv.setAdapter(upgradeAdapter);
        lv.setOnItemClickListener(new OnItemClickListener());
    }

    private class OnItemClickListener implements AdapterView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            int totalTokens = playerDB.getTokens();
            int upgradeCost = upgradeDB.getUpgradeCost(l);

            if (totalTokens >= upgradeCost) {
                playerDB.updateTokens(-upgradeCost);
                upgradeDB.updateUpgrade(l);
                updateData();
            }
        }
    }
    @Override
    public void onBackPressed() {
        startActivity(new Intent(UpgradeActivity.this, MainActivity.class));

    }

}


