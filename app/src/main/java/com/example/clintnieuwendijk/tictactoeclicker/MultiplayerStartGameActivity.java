package com.example.clintnieuwendijk.tictactoeclicker;

import android.content.Intent;
import android.provider.Settings;
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
    int gridSize;
    String playerID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_multiplayer_start_game);
        spinner = findViewById(R.id.gridSizeSpinner);
        ArrayList<Integer> items = new ArrayList<>();
        for (int i = 2; i < getIntent().getIntExtra("maxSize", 3); i++) {
            items.add(i);
        }
        ArrayAdapter spinnerList = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, items);
        spinner.setAdapter(spinnerList);
        playerID = Settings.Secure.getString(getApplicationContext().getContentResolver(),
                   Settings.Secure.ANDROID_ID);
    }

    public void onRequestGameClick(View v) {

        gridSize = Integer.valueOf(spinner.getSelectedItem().toString());
        gameRequester(gridSize, playerID, "looking_for_game");
    }

    public void gameRequester(int gridSize, String playerID, String status){
        Log.d("lookrequestsmade", Integer.toString(lookRequestsMade));
        GameRequest gameRequest = new GameRequest(this);
        gameRequest.requestGame(this, gridSize, playerID, status);
        try {
            TimeUnit.SECONDS.sleep(5);
        } catch (InterruptedException e) {
            e.printStackTrace();
            gameRequest.requestGame(this, gridSize, playerID, "cancel");
        }
    }

    @Override
    public void gotGame(JSONObject response) throws JSONException {
        int gameID = response.getInt("gameID");
        Log.d("gameID", Integer.toString(gameID));
        if (gameID > 0) {
            gridSize = response.getInt("gridSize");
            String startingPlayer = response.getString("startingPlayer");

            Intent intent = new Intent(MultiplayerStartGameActivity.this, MultiplayerGame.class);
            intent.putExtra("gameID", gameID);
            intent.putExtra("gridSize", gridSize);
            intent.putExtra("startingPlayer", startingPlayer);
            startActivity(intent);
        }
        else if (lookRequestsMade < 10) {
            Toast.makeText(this, "Still looking for game", Toast.LENGTH_SHORT).show();
            gameRequester(gridSize, playerID, "waiting_for_game");
            lookRequestsMade++;
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