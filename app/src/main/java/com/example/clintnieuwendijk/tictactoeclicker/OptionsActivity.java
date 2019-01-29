/*
 * options menu for ClickTacToe by Clint Nieuwendijk
 * the menu has 1 button so far, to reset all progress.
 * It will drop the upgrade and player database and restart.
 */
package com.example.clintnieuwendijk.tictactoeclicker;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class OptionsActivity extends AppCompatActivity {

    PlayerDatabase playerDB;
    UpgradeDatabase upgradeDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_options);

        playerDB = PlayerDatabase.getInstance(getApplicationContext());
        upgradeDB = UpgradeDatabase.getInstance(getApplicationContext());
    }

    public void resetProgressClick(View view) {
        playerDB.resetProgress();
        upgradeDB.resetProgress();
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(OptionsActivity.this, MainActivity.class));
    }
}

