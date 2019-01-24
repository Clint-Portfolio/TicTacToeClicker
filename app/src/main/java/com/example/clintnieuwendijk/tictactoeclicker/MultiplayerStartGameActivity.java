package com.example.clintnieuwendijk.tictactoeclicker;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class MultiplayerStartGameActivity extends AppCompatActivity implements GameRequest.Callback {

    Spinner spinner;
    int lookRequestsMade;
    int boardSize;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_multiplayer_start_game);
        spinner = findViewById(R.id.boardSizeSpinner);
        ArrayList<Integer> items = new ArrayList<>();
        for (int i = 2; i < getIntent().getIntExtra("maxSize", 3); i++) {
            items.add(i);
        }
        ArrayAdapter spinnerList = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, items);
        spinner.setAdapter(spinnerList);
    }

    public void onRequestGameClick(View v) {

        boardSize = Integer.valueOf(spinner.getSelectedItem().toString());
        gameRequester(boardSize, 4);
    }

    public void gameRequester(int boardSize, int playerID){
        Log.d("lookrequestsmade", Integer.toString(lookRequestsMade));
        GameRequest gameRequest = new GameRequest(this);
        if (lookRequestsMade >= 0 && lookRequestsMade < 10) {
            gameRequest.requestGame(this, boardSize, "looking_for_game");
            try {
                TimeUnit.SECONDS.sleep(5);
                lookRequestsMade++;
            } catch (InterruptedException e) {
                e.printStackTrace();
                gameRequest.requestGame(this, boardSize, "cancel");
            }
        }
    }

    @Override
    public void gotGame(JSONObject response) throws JSONException {
        int gameID = response.getInt("gameID");
        if (gameID > 0) {
            int gridSize = response.getInt("boardSize");
            String startingPlayer = response.getString("StartingPlayer");

            Intent intent = new Intent(MultiplayerStartGameActivity.this, MultiplayerGame.class);
            intent.putExtra("gameID", gameID);
            intent.putExtra("gridSize", gridSize);
            intent.putExtra("playerSymbol", startingPlayer);
            startActivity(intent);
        }
        else if (lookRequestsMade < 11) {
            Toast.makeText(this, "Still looking for game", Toast.LENGTH_SHORT).show();
            gameRequester(boardSize, 4);
        }
        else {
            Toast.makeText(this, "No multiplayer games found, try again later.", Toast.LENGTH_LONG).show();
            lookRequestsMade = 0;
        }

    }

    @Override
    public void gotGameError(String message) {
        Toast.makeText(this, "There was an error requesting a game, please try again later", Toast.LENGTH_LONG).show();
    }
}
