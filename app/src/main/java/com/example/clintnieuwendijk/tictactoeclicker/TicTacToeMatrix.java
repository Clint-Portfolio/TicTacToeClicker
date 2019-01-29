/*
 * TicTacToe matrix class by Taaqif Peck
 * https://github.com/Taaqif/TicTacToe-Android
 */

package com.example.clintnieuwendijk.tictactoeclicker;


public class TicTacToeMatrix {

    private int[] Matrix;
    int width;
    int height;

    TicTacToeMatrix(int width, int height) {
        this.width = width;
        this.height = height;

        Matrix = new int[width * height];

        for (int i=0; i < height; i++) {
            for (int j = 0; j < width; j++){
                Matrix[i*width + j] = 0;
            }
        }
    }

    int getState(int row, int col){
        return Matrix[row*width + col];
    }

    void setState(int row, int col, int state){
        Matrix[row*width + col] = state;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }
}
