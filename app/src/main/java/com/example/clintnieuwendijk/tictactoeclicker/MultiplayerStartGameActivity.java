package com.example.clintnieuwendijk.tictactoeclicker;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MultiplayerStartGameActivity extends AppCompatActivity implements GameRequest.Callback {

    Spinner spinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_multiplayer_start_game);
        spinner = findViewById(R.id.boardSizeSpinner);
        ArrayList<Integer> items = new ArrayList<>();
        for (int i = 2; i < getIntent().getIntExtra("maxSize", 3); i++) {
            items.add(i);
        }
        ArrayAdapter spinnerList = new ArrayAdapter<Integer>(this, android.R.layout.simple_spinner_item, items);
        spinner.setAdapter(spinnerList);
    }

    public void onRequestGameClick(View v) {

        int boardSize = Integer.valueOf(spinner.getSelectedItem().toString());
        GameRequest gameRequest = new GameRequest(this);
        gameRequest.requestGame(this, boardSize);
    }

    @Override
    public void gotGame(JSONObject response) throws JSONException {
        int gameID = response.getInt("gameID");
        int gridSize = response.getInt("boardSize");
        String startingPlayer = response.getString("StartingPlayer");

        Intent intent = new Intent(MultiplayerStartGameActivity.this, MultiplayerGame.class);
        intent.putExtra("gridSize", gridSize);
        intent.putExtra("gameID", gameID);
        intent.putExtra("playerSymbol", startingPlayer);
        startActivity(intent);
    }

    @Override
    public void gotGameError(String message) {
        Toast.makeText(this, "There was an error requesting a game, please try again later", Toast.LENGTH_LONG).show();
    }
}
