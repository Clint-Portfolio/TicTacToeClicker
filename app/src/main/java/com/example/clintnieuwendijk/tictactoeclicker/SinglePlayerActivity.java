package com.example.clintnieuwendijk.tictactoeclicker;

/**
 * Code based on MainActivity by Taaqif Peck
 * https://github.com/Taaqif/TicTacToe-Android
 */

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class SinglePlayerActivity extends AppCompatActivity {

    private TicTacToeGame Matrix;
    private int numRows;
    private int numCols;

    Button buttons[];
    TextView playerTurnView;

    int cellIndex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_player);

        Intent intent = getIntent();
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
    }

    /**
     * initialise the game
     */
    private void initGame(){
        //if the board doesnt exisit, create a new one
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
        playerTurnView = (TextView)findViewById(R.id.playerTurnTextView);
        setWhoIsPlayingTextView();
        //game is not over
        Matrix.setIsGameOver(false);
        Toast.makeText(SinglePlayerActivity.this, "Game is resetti", Toast.LENGTH_SHORT).show();
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
            Toast.makeText(SinglePlayerActivity.this, "Start a new game?", Toast.LENGTH_SHORT).show();
            return;
        }

        // find the index of the button to update
        int id = getClickedButtonIndex(v);

        // Update text and check whether there is a winner
        if (id >=0) {
            boolean isUpdated = updateCell(id);

            // Now let's check whether there is a winner
            if (isUpdated){
                int whoIsWinning = checkWinner();
                if (whoIsWinning == TicTacToeGame.cross){
                    playerTurnView.setText("X wins!");
                    Toast.makeText(SinglePlayerActivity.this, "X is the winner!", Toast.LENGTH_SHORT).show();

                }else{
                    if (whoIsWinning == TicTacToeGame.circle){
                        playerTurnView.setText("O wins!");
                        Toast.makeText(SinglePlayerActivity.this, "O is the winner!", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            if (!Matrix.isGameOver())
                setWhoIsPlayingTextView();
        }
    }

    public int getClickedButtonIndex(View v) {

        return Integer.parseInt(v.getTag().toString());
    }

    public boolean updateCell(int index){

        boolean isUpdated = false;
        int i = (int)(index / numCols);  // row index
        int j = index % numRows;         // column index

        isUpdated = Matrix.setCell(i, j);

        if (isUpdated){

            String textResId;

            if (whoIsPlaying() == TicTacToeGame.cross) {
                textResId = "X";
                Matrix.setWhoIsPlaying(TicTacToeGame.circle); // next player
            }else{
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

        if (whoIsPlaying() == TicTacToeGame.cross)
            playerTurnView.setText("It's X's turn to play!");
        else
            playerTurnView.setText("It's O's turn to play");
    }

    /**
     * Initialize the grid of buttons when the user clicks in the button StartOver
     * @param v
     */
    public void onStartOverClick(View v){
        initGame();
    }

    public void decreaseSizeReset(View v){
        if (numRows > 2){
            int newSize = numRows - 1;
            Intent intent = new Intent(SinglePlayerActivity.this, SinglePlayerActivity.class);
            intent.putExtra("gridSize", newSize);
            startActivity(intent);
        }
        else{
            initGame();
        }
    }

    public void increaseSizeReset(View v){
        if (numRows < 7){
            int newSize = numRows + 1;
            Intent intent = new Intent(SinglePlayerActivity.this, SinglePlayerActivity.class);
            intent.putExtra("gridSize", newSize);
            startActivity(intent);
        }
        else{
            initGame();
        }
    }

}