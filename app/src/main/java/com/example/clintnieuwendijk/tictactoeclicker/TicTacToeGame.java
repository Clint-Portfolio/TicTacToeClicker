package com.example.clintnieuwendijk.tictactoeclicker;

/**
 * Code based on TicTacToeGrid by Taaqif Peck
 * https://github.com/Taaqif/TicTacToe-Android
 */

import java.io.Serializable;

public class TicTacToeGame extends TicTacToeMatrix implements Serializable {
    public static final int cross = 1;
    public static final int circle = -1;

    private boolean mIsGameOver = false;

    private int mWhoIsPlaying = cross;

    public TicTacToeGame(int rows, int cols) {
        super(rows, cols);
    }

    /**
     * sets the cell
     *
     * @param i the rows
     * @param j the cols
     * @return true if successful
     */
    public boolean setCell(int i, int j) {

        int value = whoIsPlaying();

        if (value == cross) {
            return setToCross(i, j);
        }

        if (value == circle)
            return setToCircle(i, j);

        return false;
    }

    /**
     * sets cell to cross
     *
     * @param i the rows
     * @param j the cols
     * @return true if successful
     */
    public boolean setToCross(int i, int j) {
        // set it to cross only if the cell is empty
        // Otherwise do nothing

        boolean isUpdated = false;
        if (getState(i, j) == 0) {
            super.setState(i, j, cross);
            isUpdated = true;

            // Check whether there is a winner
            if (isCrossWinning()) {
                setIsGameOver(true);
            }
        }
        return isUpdated;
    }

    /**
     * sets cell to circle
     *
     * @param i the row
     * @param j the col
     * @return true if successful
     */
    public boolean setToCircle(int i, int j) {

        // set it to Circle only if the cell is empty
        // Otherwise do nothing

        boolean isUpdated = false;
        if (getState(i, j) == 0) {
            super.setState(i, j, circle);
            isUpdated = true;

            // Check whether there is a winner
            if (isCircleWinning()) {
                setIsGameOver(true);
            }
        }

        return isUpdated;
    }

    /**
     * Clear the cell by setting its value to 0
     *
     * @param i
     * @param j
     */
    public void clear(int i, int j) {
        setState(i, j, 0);
    }

    /**
     * Clear all cells by setting their vslues to 0
     */

    public void clear() {

        for (int i = 0; i < getHeight(); i++) {
            for (int j = 0; j < getWidth(); j++) {
                setState(i, j, 0);
            }
        }

        setWhoIsPlaying(cross);
    }
    //http://www.dreamincode.net/forums/topic/341163-n-x-n-gui-tictactoe-game-winning-algorithm/

    /**
     * check if a winning move
     * Check whether there is a winner
     *
     * @param playerCode takes values either crooss or circle
     */
    public boolean isWinning(int playerCode) {

        return (checkColumns(playerCode) || checkDiagonals(playerCode) || checkRows(playerCode));
    }

    /**
     * checks the rows for a win
     *
     * @param playerCode hte code to check for
     * @return true if winning move
     */
    public boolean checkRows(int playerCode) {
        boolean won = false;

        // check the rows
        for (int row = 0; row < getHeight(); row++) {
            int total = 0;
            for (int col = 0; col < getWidth(); col++) {
                total += getState(row, col);
                //if the addition adds up to the width, true
                if (total == playerCode * getWidth()) {
                    won = true;
                    return won;
                }
            }
        }
        return won;
    }

    /**
     * check the colums for a winning move
     *
     * @param playerCode the player code to check
     * @return true if winning move
     */
    public boolean checkColumns(int playerCode) {
        boolean won = false;
        for (int col = 0; col < getHeight(); col++) {
            int total = 0;
            for (int row = 0; row < getWidth(); row++) {
                //if the addition is equal to the widdth, true
                total += getState(row, col);
                if (total == playerCode * getWidth()) {
                    won = true;
                    return won;
                }
            }
        }
        return won;
    }

    /**
     * checks the diagonals for a win
     *
     * @param playerCode the player code to check for thr win
     * @return either true or false if a diagonal win occurs for the player
     */
    public boolean checkDiagonals(int playerCode) {
        int total = 0;
        boolean won = false;

        for (int lr = 0; lr < getWidth(); lr++) {
            total += getState(lr, lr);
        }

        if (total == playerCode * getWidth()) {
            won = true;
            return won;
        }

        total = 0;

        for (int rl = 0; rl < getWidth(); rl++) {
            total += getState(rl, getWidth() - rl - 1);

        }

        if (total == playerCode * getWidth()) {
            won = true;
            return won;
        }
        return won;
    }

    /**
     * Check whether cross is winning
     */
    public boolean isCrossWinning() {
        return isWinning(cross);
    }

    /**
     * Check whether cross is winning
     */
    public boolean isCircleWinning() {
        return isWinning(circle);
    }

    /**
     * Find who is the winner
     * Returns either 0 (no winner), 1 (cross) or 2 (circle)
     */
    public int whoIsWinning() {

        if (isCrossWinning())
            return cross;
        else {
            if (isCircleWinning()) {
                return circle;
            } else {
                return 0;       // no winner
            }
        }

    }

    public boolean isGameOver() {
        return mIsGameOver;
    }

    public void setIsGameOver(boolean v) {
        mIsGameOver = v;
    }

    public int whoIsPlaying() {
        return mWhoIsPlaying;
    }

    public void setWhoIsPlaying(int v) {
        mWhoIsPlaying = v;
    }

    public boolean isDraw() {
        for (int col = 0; col < getHeight(); col++) {
            for (int row = 0; row < getWidth(); row++) {
                if (getState(row, col) == 0){
                    return false;
                }
            }
        }
        setIsGameOver(true);
        return true;
    }
}