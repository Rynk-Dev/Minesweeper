package dev.rynk.minesweeper.customviews;



import static dev.rynk.minesweeper.enums.CursorMode.*;
import static dev.rynk.minesweeper.enums.State.*;
import static dev.rynk.minesweeper.enums.TileAction.*;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
import android.widget.ImageButton;

import dev.rynk.minesweeper.enums.TileAction;
import dev.rynk.minesweeper.enums.CursorMode;
import dev.rynk.minesweeper.enums.State;

@SuppressLint("AppCompatCustomView")
public class Tile extends ImageButton {
    private int row;
    private int col;
    private int numFlaggedNeighbors;
    private int numNearbyMines;
    private State state;
    private boolean hasMine;
    private Resources rc;
    private String packageName;

    /**
     * default constructo
     * @param context activity context
     */
    public Tile(Context context) {
        super(context);
        init(context);
    }
    /**
     * Constructor sets the tile's assigned col, row, size, and state.
     * @param context activity context
     * @param col int representing column coordinate
     * @param row int representing row coordinate
     * @param state State representing current state
     */
    public Tile(Context context, int col, int row, State state){
        super(context);
        init(context);
        this.col = col;
        this.row = row;
        setState(state);
    }

    /**
     * Setup contextual resources.
     * @param context Application context
     */
    private void init(Context context){
        rc = getResources();
        packageName = context.getPackageName();
    }

    /**
     * get this tile's row coordinate.
     * @return int representing tile's row coordinate.
     */
    public int getRow(){
        return this.row;
    }
    /**
     * get this tile's col coordinate.
     * @return int representing tile's col coordinate.
     */
    public int getCol(){
        return this.col;
    }

    /**
     * Changes tile's drawable resource and State member variable to the given new state.
     * @param newState State to change to.
     */
    public void setState(State newState){
        String newDrawableName = newState.drawableName;
        if (newState == SAFE){
            newDrawableName += Integer.toString(numNearbyMines);
        }
        setBackgroundResource(rc.getIdentifier(newDrawableName, "drawable", packageName));
        state = newState;
    }

    /**
     * Increments the nearby mines count by 1.
     */
    public void addNumNearbyMines(){
        numNearbyMines ++;
    }

    /**
     * Increments the number of flagged neighbors by the given delta.
     * @param delta int representing the change to the number of flagged neighbors.
     */
    public void addToNumFlaggedNeighbors(int delta){
        numFlaggedNeighbors += delta;
    }

    /**
     * Get the tile's current state.
     * @return State representing current tile state.
     */
    public State getState(){
        return this.state;
    }

    /**
     * Get the number of neighboring mines the tile has.
     * @return int representing number of neighboring mines.
     */
    public int getNumNearbyMines(){
        return numNearbyMines;
    }

    /**
     * Returns if the tile's number of flagged neighbors is at least equal to
     * the number of neighboring mines.
     * @return boolean representing if all un-flagged neighbors are considered safe.
     */
    public boolean isSafeAndFullyTagged(){
        return (state == SAFE && numNearbyMines <= numFlaggedNeighbors);
    }

    /**
     * Click action on a tile. Calls different methods depending on
     * cursorMode and returns TileAction taken
     * @param cursorMode CursorMode representing the click mode: dig or flag.
     * @return TileAction representing action taken
     * (FLAG_ADDED, FLAG_REMOVED, NO_ACTION, MINE_EXPLODED, SAFELY_DUG)
     */
    public TileAction click(CursorMode cursorMode){
        TileAction action = NO_ACTION;
        if (cursorMode == FLAG)
            action = flagClick();
        else if (cursorMode == DIG)
            action = digClick();
        return action;
    }

    /**
     * Click action on a tile with the flag cursor selected.
     * Toggle unknown and flagged states and return TileAction taken. If the target was neither state,
     * return NO_ACTION.
     * @return TileAction representing action taken (FLAG_ADDED, FLAG_REMOVED, NO_ACTION)
     */
    private TileAction flagClick() {
        if (state == UNKNOWN) {
            setState(FLAGGED);
            return FLAG_ADDED;
        }
        else if (state == FLAGGED) {
            setState(UNKNOWN);
            return FLAG_REMOVED;
        }
        return NO_ACTION;
    }

    /**
     * Click action on a tile with the dig cursor selected.
     * If state is unknown, sets state to exploded or safe depending on hasMine and return TileAction taken.
     * If state is anything else, return NO_ACTION
     * @return TileAction representing action taken (MINE_EXPLODED, SAFELY_DUG, NO_ACTION)
     */
    private TileAction digClick() {
        if (state == UNKNOWN) {
             if (hasMine) {
                 setState(EXPLODED);
                 return MINE_EXPLODED;
             } else {
                 setState(SAFE);
                 return SAFELY_DUG;
             }
        }
        return NO_ACTION;
    }

    /**
     * Get if the tile has a mine.
     * @return boolean representing if the tile has a mine.
     */
    public boolean hasMine(){
        return hasMine;
    }

    /**
     * Adds a mine to the tile.
     */
    public void setMine(){
        hasMine = true;
    }
}