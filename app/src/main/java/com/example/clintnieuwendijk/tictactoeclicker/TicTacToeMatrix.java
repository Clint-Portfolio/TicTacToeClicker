package com.example.clintnieuwendijk.tictactoeclicker;

/**
 * Design based on Matrix class by Taaqif Peck
 * https://github.com/Taaqif/TicTacToe-Android
 */
public class TicTacToeMatrix {

    private int[] Matrix;
    int width;
    int height;

    public TicTacToeMatrix(int width, int height) {
        this.width = width;
        this.height = height;

        Matrix = new int[width * height];

        for (int i=0; i < height; i++) {
            for (int j = 0; j < width; j++){
                Matrix[i*width + j] = 0;
            }
        }
    }

    public int getState(int row, int col){
        return Matrix[row*width + col];
    }

    public void setState(int row, int col, int state){
        Matrix[row*width + col] = state;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }
}
