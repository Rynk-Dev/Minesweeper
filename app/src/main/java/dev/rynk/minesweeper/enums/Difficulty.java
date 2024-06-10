package dev.rynk.minesweeper.enums;

/**
 * Stores game difficulty values, to be used in gameboard instantiation.
 */
public enum Difficulty {
    EASY(9, 9, 10),
    MEDIUM(9, 18, 25);

    public final int cols;
    public final int rows;
    public final int numMines;
    Difficulty(int cols, int rows, int numMines){
        this.cols = cols;
        this.rows = rows;
        this.numMines = numMines;
    }
}
