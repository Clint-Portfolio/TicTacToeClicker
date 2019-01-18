package com.example.clintnieuwendijk.tictactoeclicker;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MultiplayerStartGameActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_multiplayer_start_game);
    }

    public void onRequestGameClick(View v) {
        Intent intent = new Intent(MultiplayerStartGameActivity.this, MultiplayerGame.class);

    }
}
