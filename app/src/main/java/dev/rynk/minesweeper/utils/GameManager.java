package dev.rynk.minesweeper.utils;

import static android.os.SystemClock.sleep;
import static dev.rynk.minesweeper.utils.Constants.*;
import static dev.rynk.minesweeper.utils.CursorMode.*;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.GridLayout;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.ArrayList;
import dev.rynk.minesweeper.databinding.ActivityPlayBinding;
public class GameManager{
    private final Tile[][] tiles;
    private int cols;
    private int rows;
    private int numCells;
    private int numMinesTotal;
    private int numMinesRemaining;
    private int boardWidth;
    private int tileSize;
    private boolean firstMove;
    private GridLayout gameBoard;
    public boolean gameState;
    private CursorMode cursorMode = FLAG;
    private int numUnknown;
    private ActivityPlayBinding binding;
    private ImageButton gameEndButton;
    private TextView mineRemainderText;
    public GameManager(String difficulty, Context context, ActivityPlayBinding binding) {
        setDifficultySettings(difficulty);
        numCells = cols * rows;
        gameState = false;
        numUnknown = numCells;
        firstMove = true;
        numMinesRemaining = numMinesTotal;
        mineRemainderText = binding.mineRemainderText;
        mineRemainderText.setText(Integer.toString(numMinesRemaining));
        gameBoard = binding.gameBoard;
        gameEndButton = binding.navEndButton;
        tiles = new Tile[rows][cols];
        gameBoard.setRowCount(rows);
        gameBoard.setColumnCount(cols);
        gameBoard.post(new Runnable() {
            @Override
            public void run(){
                boardWidth = gameBoard.getWidth();
                tileSize = boardWidth / cols;
                createTiles(gameBoard, tileSize, context);
            }
        });
    }

    private void setDifficultySettings(String difficulty) {
        Log.d("ryan",difficulty);
        switch (difficulty){
            case "Easy":
                this.cols = COLS_SMALL;
                this.rows = ROWS_SMALL;
                this.numMinesTotal = MINES_SMALL;
                break;
            case "Medium":
                this.cols = COLS_MEDIUM;
                this.rows = ROWS_MEDIUM;
                this.numMinesTotal = MINES_MEDIUM;
                break;
            default:
                Log.d("ryan","no difficulty returned");
        }
    }

    public void setCursorMode(CursorMode cursorMode){
        this.cursorMode = cursorMode;
    }
    public void createTiles(GridLayout gameBoard, int tileSize, Context context){
        for (int row = LOOP_START; row < rows; row++){
            for (int col = LOOP_START; col < cols; col++){
                Tile tile = new Tile(context, col, row, tileSize, State.UNKNOWN);
                tile.setOnClickListener(tileFirstClickListener);
                gameBoard.addView(tile);
                tiles[row][col] = tile;
            }
        }
    }
    private View.OnClickListener tileFirstClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Tile t = (Tile) v;
            populateMines(t.getCol(), t.getRow(), numMinesTotal);
            for (Tile [] row : tiles){
                for (Tile cell : row){
                    cell.setOnClickListener(tileListener);
                }
            }
            clickTile(t, DIG);
        }
    };

    private void populateMines(int col, int row, int numMinesTotal) {
        int numMinesMade = 0;
        Tile firstClicked = tiles[row][col];
        ArrayList <Tile> neighborhood = getNeighbors(firstClicked);
        neighborhood.add(firstClicked);
        while (numMinesMade < numMinesTotal){
            int targetRow = (int) (Math.random() * rows);
            int targetCol = (int) (Math.random() * cols);
            Tile targetTile = tiles[targetRow][targetCol];
            if (!targetTile.getHasMine() && !neighborhood.contains(targetTile)){
                targetTile.setMine();
                notifyNeighborsMined(targetTile);
                numMinesMade++;
            }
        }
    }
    private View.OnClickListener tileListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (firstMove){

                firstMove = false;
            }
            Tile t = (Tile) v;
            if (t.isSafeAndFullyTagged()) {
                digAll(getUnknownNeighbors(t));
            }
            else {
                clickTile(t, cursorMode);
            }
        }
    };
    private void digAll(ArrayList<Tile> neighbors) {
        neighbors.forEach(t -> clickTile(t, DIG));
    }
    private void clickTile(Tile t, CursorMode cursorMode){
        boolean tileChanged = t.clickUnsafeTile(cursorMode);
        if (tileChanged) {
            processTileChange(t);
            testForWin();
        }
    }
    private void testForWin(){
        if (numMinesRemaining == 0){
            numUnknown = getNumUnknownTiles();
            if (numUnknown == 0) {
                gameState = GAME_WON;
                endGame();
            }
        }
    }
    private int getNumUnknownTiles() {
        int numUnknownTiles = 0;
        for (Tile[] tileRow : tiles){
            for (Tile cell : tileRow){
                if (cell.getState() == State.UNKNOWN){
                    numUnknownTiles ++;
                }
            }
        }
        return numUnknownTiles;
    }
    private void endGame(){
        exposeAllMines(gameState);
        gameEndButton.performClick();
    }

    private void exposeAllMines(boolean winStatus) {
        State mineState = State.EXPLODED;
        if (winStatus){
            mineState = State.UNEXPLODED;
        }
        for (Tile[] tileRow : tiles){
            for (Tile cell : tileRow){
                if (cell.getHasMine()){
                    cell.setState(mineState);

                }
            }
        }
    }
    private void processTileChange(Tile t) {
        switch (t.getState()) {
            case FLAGGED:
                numMinesRemaining--;
                notifyNeighborsFlagged(t, ADD_FLAGGED_NEIGHBOR);
                break;
            case UNKNOWN:
                numMinesRemaining++;
                notifyNeighborsFlagged(t, REMOVE_FLAGGED_NEIGHBOR);
                break;
            case SAFE:
                if (t.getNumNearbyMines() == NO_NEARBY_MINES) {
                    digAll(getUnknownNeighbors(t));
                }
                break;
            case EXPLODED:
                endGame();
                break;
        }
        Log.d("ryan","is this hit?");
        mineRemainderText.setText(Integer.toString(numMinesRemaining));
    }
    private void notifyNeighborsFlagged(Tile t, int flaggedNeighborDelta) {
        ArrayList<Tile> neighbors = getNeighbors(t);
        neighbors.forEach(neighbor -> neighbor.addToNumFlaggedNeighbors(flaggedNeighborDelta));
    }
    private void notifyNeighborsMined(Tile t){
        ArrayList<Tile> neighbors = getNeighbors(t);
        neighbors.forEach(neighbor -> neighbor.addNearbyMine());
    }
    private ArrayList<Tile> getNeighbors(Tile tile) {
        int col = tile.getCol();
        int row = tile.getRow();
        int neighborRow;
        int neighborCol;
        ArrayList<Tile> neighbors = new ArrayList<>();
        for (int rowOffset : NEIGHBOR_COORDS[ROW_INDEX]){
            for (int colOffset : NEIGHBOR_COORDS[COL_INDEX]){
                if (rowOffset != COORD_SELF || colOffset != COORD_SELF){
                    neighborRow = row + rowOffset;
                    neighborCol = col + colOffset;
                    if (inRowBounds(neighborRow) && inColBounds(neighborCol)){
                        neighbors.add(tiles[neighborRow][neighborCol]);
                    }
                }
            }
        }
        return neighbors;
    }
    private ArrayList<Tile> getUnknownNeighbors(Tile tile) {
        ArrayList<Tile> neighbors = getNeighbors(tile);
        neighbors.removeIf(t -> t.getState() != State.UNKNOWN);
        return neighbors;
    }
    private boolean inColBounds(int neighborCol) {
        return neighborCol >= COORD_MINIMUM && neighborCol < cols;
    }
    private boolean inRowBounds(int neighborRow) {
        return neighborRow >= COORD_MINIMUM && neighborRow < rows;
    }
}

