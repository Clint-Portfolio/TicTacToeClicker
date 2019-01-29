/*
 * The main menu of "ClickTacToe" by Clint Nieuwendijk
 * The menu presents 4 options:
 * - Simgle player, to play a single player game
 * - Multiplayer, for multiplayer games
 * - Options, to reset progress
 * - Upgrades, the upgrade menu
 *
 * The main menu also functions as db initializer for the player- and upgrade database
 */
package com.example.clintnieuwendijk.tictactoeclicker;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    UpgradeDatabase upgradeDB;
    PlayerDatabase playerDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        upgradeDB = UpgradeDatabase.getInstance(getApplicationContext());
        playerDB = PlayerDatabase.getInstance(getApplicationContext());
        playerDB.makePlayer(this);

        TextView scoreView = findViewById(R.id.ScoreView);
        scoreView.setText(String.format("You have: %d Ticcoin", playerDB.getTokens()));

    }

    // go to menu item
    public void mainClick(View view){
        Intent intent;
        int maxSize;
        switch (view.getId()){
            case R.id.singlePlayerStartButton:
                intent = new Intent(MainActivity.this, SinglePlayerActivity.class);
                intent.putExtra("gridSize", 2);

                maxSize = upgradeDB.getBoardSize();
                intent.putExtra("maxSize", maxSize);
                break;
            case R.id.multiplayerStartButton:
                intent = new Intent(MainActivity.this, MultiplayerStartGameActivity.class);
                maxSize = upgradeDB.getBoardSize();
                intent.putExtra("maxSize", maxSize);
                break;
            case R.id.optionsButton:
                intent = new Intent(MainActivity.this, OptionsActivity.class);
                break;
            case R.id.upgradeButton:
                intent = new Intent(MainActivity.this, UpgradeActivity.class);
                break;
            default:
                intent = new Intent(MainActivity.this, MainActivity.class);
        }
        startActivity(intent);
    }
}
