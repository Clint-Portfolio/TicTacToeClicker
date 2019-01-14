package com.example.clintnieuwendijk.tictactoeclicker;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    UpgradeDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void mainClick(View view){
        Intent intent;
        switch (view.getId()){
            case R.id.singlePlayerStartButton:
                intent = new Intent(MainActivity.this, SinglePlayerActivity.class);
                intent.putExtra("gridSize", 2);
                break;
            case R.id.multiplayerStartButton:
                intent = new Intent(MainActivity.this, MultiplayerActivity.class);
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
