package dev.rynk.minesweeper.enums;

/**
 * Represents tile states.
 */
public enum State{
    UNKNOWN ("unknown"),
    FLAGGED("flag"),
    SAFE("t"),
    EXPLODED("exploded"),
    UNEXPLODED("mine");

    public final String drawableName;
    State(String drawableName){
        this.drawableName = drawableName;
    }
}