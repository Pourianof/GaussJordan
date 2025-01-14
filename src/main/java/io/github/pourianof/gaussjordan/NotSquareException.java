package io.github.pourianof.gaussjordan;


public class NotSquareException extends Exception {

    private int row;
    private int col;

    public NotSquareException(String msg, int row, int col) {
        this.row = row;
        this.col = col;
    }

    public int getRow() {
        return this.row;
    }

    public int getCol() {
        return this.col;
    }

}
