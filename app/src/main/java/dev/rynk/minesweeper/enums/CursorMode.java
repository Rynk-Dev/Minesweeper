package dev.rynk.minesweeper.enums;

/**
 * Represents the 2 modes a cursor can be in during gameplay. Flag, where the cursor flags and
 * un-flags potential mine locations, and dig, where the cursor exposes an unknown location.
 */
public enum CursorMode {
    FLAG,
    DIG;
}
