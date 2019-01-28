package com.example.clintnieuwendijk.tictactoeclicker;

import android.content.Intent;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class MultiplayerGame extends AppCompatActivity implements MoveRequest.Callback {

    private TicTacToeGame Matrix;
    private int numRows;
    private int numCols;

    PlayerDatabase playerDB;
    UpgradeDatabase upgradeDB;

    Button buttons[];
    TextView playerTurnView;
    TextView scoreView;

    int cellIndex;
    int multiplier;

    int gameID;
    private int currentTokens;
    String thisPlayer;
    boolean thisPlayerMove;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_multiplayer_game);

        Intent intent = getIntent();
        String startingPlayer = Settings.Secure.getString(getApplicationContext().getContentResolver(),
                                Settings.Secure.ANDROID_ID);
        Log.d("androidID = ", startingPlayer);
        Log.d("Starting player = ", intent.getStringExtra("startingPlayer"));
        if (startingPlayer.equals(intent.getStringExtra("startingPlayer"))) {
            thisPlayer = "player1";
            thisPlayerMove = true;
        }
        else {
            thisPlayer = "player2";
            thisPlayerMove = false;
        }

        gameID = intent.getIntExtra("gameID", 0);
        int gridSize = intent.getIntExtra("gridSize", 3);

        numRows = gridSize;
        numCols = gridSize;
        buttons = new Button[numRows * numCols];
        createButtonGrid(R.id.gameGrid);

        if (savedInstanceState != null) {
            restoreGame((TicTacToeGame) savedInstanceState.getSerializable("TTTGame"));
        } else {
            initGame();
        }

        playerDB = PlayerDatabase.getInstance(getApplicationContext());
        upgradeDB = UpgradeDatabase.getInstance(getApplicationContext());

        scoreView = findViewById(R.id.ScoreView);
        currentTokens = 0;
        multiplier = upgradeDB.getMultiplier();
        Log.d("Multiplier = ", Integer.toString(multiplier));
        updateScore(currentTokens);
    }

    public void updateScore(int score) {
        scoreView = (TextView) findViewById(R.id.scoreView);
        scoreView.setText(String.format("Score: %d", score));
    }

    /**
     * initialise the game
     */
    private void initGame(){
        currentTokens = 0;
        updateScore(currentTokens);
        //if the board doesn't exist, create a new one
        if (Matrix == null){
            Matrix = new TicTacToeGame(numRows, numCols);
        }
        //populate the grid with blank data
        for (int i=0; i<numRows; i++){
            for (int j=0; j<numCols; j++){
                cellIndex = i*numCols  + j;
                buttons[cellIndex].setText("_");
            }
        }
        //clear the board data
        Matrix.clear();
        //set the first player
        playerTurnView = (TextView) findViewById(R.id.playerTurnTextView);
        setWhoIsPlayingTextView();
        //game is not over
        Matrix.setIsGameOver(false);
    }

    private void restoreGame(TicTacToeGame tttGame) {
        Matrix = new TicTacToeGame(tttGame.getWidth(), tttGame.getHeight());
        for (int i = 0; i < tttGame.getWidth(); i++) {
            for (int j = 0; j < tttGame.getHeight(); j++) {
                Matrix.setState(i, j, tttGame.getState(i, j));
            }
        }

        String resText;
        for (int i = 0; i < numRows; i++) {
            for (int j = 0; j < numCols; j++) {
                cellIndex = i * numCols + j;
                switch (Matrix.getState(i, j)) {
                    case TicTacToeGame.circle:
                        resText = "O";
                        break;
                    case TicTacToeGame.cross:
                        resText = "X";
                        break;
                    default:
                        resText = "_";
                }
                buttons[cellIndex].setText(resText);
            }
        }
    }

    public void createButtonGrid(int id) {
        GridLayout layout = findViewById(id);
        layout.setColumnCount(numCols);

        int count = 0;
        for (int i = 0; i < numRows; i++) {
            for (int j = 0; j < numCols; j++) {
                int n = i * numRows + j;
                //dynamically create buttons
                Button b = new Button(this);
                //set the tag to be referenced when clicking the button
                b.setTag(count);
                b.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onPlayerClick(v);
                    }
                });
                //simple styling
                b.setMinHeight(0);
                b.setMinimumHeight(0);
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(100, 100);
                b.setLayoutParams(params);
                //add the newly created button to the grid
                layout.addView(b);
                //assign the global buttons
                buttons[n] = b;
                count++;
            }
        }
    }

    public void onPlayerClick(View v){
        // if the gave is already over, do nothing
        if (Matrix.isGameOver()){
            Toast.makeText(this, "Start a new game?", Toast.LENGTH_SHORT).show();
            return;
        }
        MoveRequest mr = new MoveRequest(this);
        if (thisPlayerMove) {
            int id = getClickedButtonIndex(v);
            mr.postMove(this, id, gameID);
        }
        else {
            mr.postMove(this, -1, gameID);
        }
    }

    public int getClickedButtonIndex(View v) {

        return Integer.parseInt(v.getTag().toString());
    }

    public boolean updateCell(int index) {

        boolean isUpdated;
        int i = (int)(index / numCols);  // row index
        int j = index % numRows;         // column index


        isUpdated = Matrix.setCell(i, j);

        if (isUpdated){

            String textResId;

            if (whoIsPlaying() == TicTacToeGame.cross) {
                textResId = "X";
                Matrix.setWhoIsPlaying(TicTacToeGame.circle); // next player
                currentTokens += multiplier;
                updateScore(currentTokens);
            }
            else {
                textResId = "O";
                Matrix.setWhoIsPlaying(TicTacToeGame.cross);
            }
            buttons[index].setText(textResId);
        }
        return isUpdated;
    }

    /**
     * return who is playing
     * @return the playing person code
     */
    public int whoIsPlaying(){
        return Matrix.whoIsPlaying(); //mWhoIsPlaying;
    }

    /**
     * check the winner
     * @return the winner
     */
    public int checkWinner(){
        int i = Matrix.whoIsWinning();
        return i;
    }

    /**
     * sets the winning text view
     */
    protected void setWhoIsPlayingTextView(){

        if (thisPlayerMove) {
            playerTurnView.setText("It's your turn to play!");
        }
        else {
            playerTurnView.setText("It's your opponent's turn to play");
        }
    }

    /**
     * Initialize the grid of buttons when the user clicks in the button StartOver
     * @param v
     */
    public void onStartOverClick(View v) {
        playerDB.updateTokens(currentTokens);
        int totalTokens = playerDB.getTokens();
        Toast.makeText(this, String.format("%d tokens in total", totalTokens), Toast.LENGTH_LONG).show();
        initGame();
    }

    @Override
    public void onBackPressed() {
        playerDB.updateTokens(currentTokens);
        startActivity(new Intent(this, MainActivity.class));

    }

    @Override
    public void gotMove(JSONObject response) throws JSONException {
        int index = response.getInt("lastMove");
        String status = response.getString("status");
        if (status.equals(thisPlayer)) {
            thisPlayerMove = true;
        }
        else {
            thisPlayerMove = false;
        }

        // Update text and check whether there is a winner
        if (index >= 0) {
            boolean isUpdated = updateCell(index);

            // Now let's check whether there is a winner
            if (isUpdated) {
                int whoIsWinning = checkWinner();
                if (whoIsWinning == TicTacToeGame.cross) {
                    playerTurnView.setText("X wins!");
                    Toast.makeText(MultiplayerGame.this, "X is the winner!", Toast.LENGTH_SHORT).show();

                } else {
                    if (whoIsWinning == TicTacToeGame.circle) {
                        playerTurnView.setText("O wins!");
                        Toast.makeText(MultiplayerGame.this, "O is the winner!", Toast.LENGTH_SHORT).show();
                    }
                }
                if (Matrix.isDraw()) {
                    playerTurnView.setText("It's a draw!");
                    Toast.makeText(MultiplayerGame.this, "It's a draw!", Toast.LENGTH_SHORT).show();
                }
            }
            if (!Matrix.isGameOver())
                setWhoIsPlayingTextView();
        }
    }

    @Override
    public void gotMoveError(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }

    public void onRequestMove(View v) {
        MoveRequest mr = new MoveRequest(this);
        mr.postMove(this, -1, gameID);
    }
}
