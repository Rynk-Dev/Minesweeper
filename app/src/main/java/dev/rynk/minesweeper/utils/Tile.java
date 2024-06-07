package dev.rynk.minesweeper.utils;



import static dev.rynk.minesweeper.utils.CursorMode.*;
import static dev.rynk.minesweeper.utils.State.*;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Toast;

@SuppressLint("AppCompatCustomView")
public class Tile extends ImageButton {
    public Tile(Context context) {
        super(context);
        init(context);
    }
    public Tile(Context context, int col, int row, int size, State state){
        super(context);
        init(context);
        this.col = col;
        this.row = row;
        setState(state);
        setLayoutParams(new ViewGroup.LayoutParams(size, size));
    }
    private int row;
    private int col;
    private int numFlaggedNeighbors;
    private int numNearbyMines;
    private State state;
    private boolean hasMine;
    public void setRow(int row){
        this.row = row;
    }
    public int getRow(){
        return this.row;
    }
    public void setCol(int col){
        this.col = col;
    }
    public int getCol(){
        return this.col;
    }
    private Resources rc;
    private String packageName;
    private Context context;
    public void setState(State newState){
        String newDrawableName = newState.drawableName;
        if (newState == SAFE){
            newDrawableName += Integer.toString(numNearbyMines);
        }
        setBackgroundResource(rc.getIdentifier(newDrawableName, "drawable", packageName));
        state = newState;
    }
    public void addNearbyMine(){
        numNearbyMines ++;
    }
    public void addToNumFlaggedNeighbors(int delta){
        numFlaggedNeighbors += delta;
    }
    public State getState(){
        return this.state;
    }
    private void init(Context context){
        rc = getResources();
        packageName = context.getPackageName();
        this.context = context;
    }
    public int testAction(){
        Toast.makeText(context, "BUTTON CLICKED", Toast.LENGTH_LONG).show();
        return col;
    }
    public int getNumNearbyMines(){
        return numNearbyMines;
    }
    public boolean isSafeAndFullyTagged(){
        return (state == SAFE && numNearbyMines <= numFlaggedNeighbors);
    }
    public boolean clickUnsafeTile(CursorMode cursorMode){
         boolean tileChanged = false;
         if (cursorMode == FLAG)
             tileChanged = flagClick();
         if (cursorMode == DIG)
             tileChanged = digClick();
         return tileChanged;
    }
    private boolean flagClick() {
        if (state == UNKNOWN) {
            setState(FLAGGED);
            return true;
        }
             else if (state == FLAGGED) {
            setState(UNKNOWN);
            return true;
        }
        return false;
    }
    private boolean digClick() {
        if (state == UNKNOWN) {
             if (hasMine) {
                 setState(EXPLODED);
             } else {
                 setState(SAFE);
             }
             return true;
        }
        return false;
    }
    public boolean getHasMine(){
        return hasMine;
    }
    public void setMine(){
        hasMine = true;
    }
}