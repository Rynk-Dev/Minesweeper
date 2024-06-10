package dev.rynk.minesweeper.utils;

import static dev.rynk.minesweeper.enums.TileAction.NO_ACTION;
import static dev.rynk.minesweeper.utils.Constants.*;
import static dev.rynk.minesweeper.enums.CursorMode.*;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;
import android.widget.ImageButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import java.util.ArrayList;

import dev.rynk.minesweeper.customviews.Tile;
import dev.rynk.minesweeper.databinding.ActivityPlayBinding;
import dev.rynk.minesweeper.enums.TileAction;
import dev.rynk.minesweeper.enums.CursorMode;
import dev.rynk.minesweeper.enums.Difficulty;
import dev.rynk.minesweeper.enums.State;

/**
 * Handles gameplay related objects and logic.
 */
public class GameManager{
    public static final int NO_NEARBY_MINES = 0;
    public final int[][] NEIGHBOR_COORDS = {{-1,0,1},{-1,0,1}};
    public final int ROW_INDEX = 0;
    public final int COL_INDEX = 1;
    public final int COORD_SELF = 0;
    public final int COORD_MINIMUM = 0;
    public final int ADD_FLAGGED_NEIGHBOR = 1;
    public final int REMOVE_FLAGGED_NEIGHBOR = -1;

    private Tile[][] tiles;
    private int cols;
    private int rows;
    private int numMinesTotal;
    private int numMinesRemaining;
    private GridLayout gameBoard;
    public boolean isGameWon;
    private CursorMode cursorMode = FLAG;
    private int numUnknown;
    private ImageButton gameEndButton;
    private TextView mineRemainderText;

    /**
     * constructor. Sets up game-related views, creates the game board, starts the game.
     * @param difficulty Difficulty representing selected game difficulty.
     * @param context Context in which to create the game board.
     * @param binding ViewBinding for parent UI updates
     */
    public GameManager(Difficulty difficulty, Context context, ActivityPlayBinding binding) {
        setBindings(binding);
        setCursorModeListener(binding);
        setDifficultySettings(difficulty);
        setGameStartConditions();
        createBoard(context);
    }

    /**
     * sets parent UI elements that need to be controlled.
     * @param binding ViewBinding for parent UI
     */
    private void setBindings(ActivityPlayBinding binding) {
        mineRemainderText = binding.mineRemainderText;
        gameBoard = binding.gameBoard;
        gameEndButton = binding.closeButton;
    }
    /**
     * Sets tags programmatically to cursor mode buttons to ensure consistency.
     * Adds listener to cursor button group to update game manager's cursor mode state.
     */
    private void setCursorModeListener(ActivityPlayBinding binding) {
        binding.flagCursor.setTag(CursorMode.FLAG);
        binding.digCursor.setTag(CursorMode.DIG);
        binding.cursorModeGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                cursorMode = (CursorMode) binding.getRoot().findViewById(checkedId).getTag();
            }
        });
    }
    /**
     * Sets game difficulty values from the given difficulty.
     * @param difficulty Difficulty representing selected game difficulty.
     */
    private void setDifficultySettings(Difficulty difficulty) {
        this.cols = difficulty.cols;
        this.rows = difficulty.rows;
        this.numMinesTotal = difficulty.numMines;
        numUnknown = cols * rows;
        tiles = new Tile[rows][cols];
        numMinesRemaining = numMinesTotal;
    }
    /**
     * Sets game initialization values.
     */
    @SuppressLint("SetTextI18n")
    private void setGameStartConditions() {
        // game start conditionals.
        isGameWon = false;
        // update view settings
        mineRemainderText.setText(Integer.toString(numMinesRemaining));
        gameBoard.setRowCount(rows);
        gameBoard.setColumnCount(cols);
    }
    /**
     * Waits for the gameBoard view to be generated with its width, then calculates
     * tile size based in the gameBoard width and triggers tiles creation.
     * @param context Activity context.
     */
    private void createBoard(Context context) {
        // wait for gameBoard view to be created, so width can be retrieved.
        gameBoard.post(new Runnable() {
            @Override
            public void run(){
                // get tile size, dependent on gameBoard's width and column count.
                int boardWidth = gameBoard.getWidth();
                int tileSize = boardWidth / cols;
                createTiles(gameBoard, tileSize, context);
            }
        });
    }
    /**
     * Creates number of blank tiles as defined by difficulty settings,
     * Gives them a listener for the game's first click
     * add them to the gameBoard view,
     * and stores reference to tiles in the 2d Tile array tiles.
     * @param gameBoard Gridlayout view that holds the tiles.
     * @param tileSize Width and height of the created tiles.
     * @param context Activity context.
     */
    public void createTiles(GridLayout gameBoard, int tileSize, Context context){
        for (int row = LOOP_START_INDEX; row < rows; row++){
            for (int col = LOOP_START_INDEX; col < cols; col++){
                Tile tile = new Tile(context, col, row, State.UNKNOWN);
                tile.setOnClickListener(tileFirstClickListener);
                tile.setLayoutParams(new ViewGroup.LayoutParams(tileSize, tileSize));
                gameBoard.addView(tile);
                tiles[row][col] = tile;
            }
        }
    }
//LISTENERS
    /**
     * Initial listener given to all tiles. The first clicked tile will populate mines in the
     * board, ensuring the clicked tile and its neighbors do not have any mines.
     * The clicked tile is then dug, revealing itself and its neighbors.
     */
    private final View.OnClickListener tileFirstClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Tile t = (Tile) v;
            populateMines(t, numMinesTotal);
            setGameplayTileClickListeners();
            clickTile(t, DIG);
        }
    };

    /**
     * Adds numMinesTotal number of mines in random positions, excluding the firstClicked
     * tile and its neighbors.
     * When a mine is added to a tile, notifies its neighbors that a neighbor has a mine.
     * @param firstClicked Tile that was the first clicked tile of the game.
     * @param numMinesTotal Number of mines according to the difficulty.
     */
    private void populateMines(Tile firstClicked, int numMinesTotal) {
        int numMinesMade = 0;
        ArrayList <Tile> neighborhood = getNeighbors(firstClicked);
        neighborhood.add(firstClicked);
        while (numMinesMade < numMinesTotal){
            int targetRow = (int) (Math.random() * rows);
            int targetCol = (int) (Math.random() * cols);
            Tile targetTile = tiles[targetRow][targetCol];
            if (!targetTile.hasMine() && !neighborhood.contains(targetTile)){
                targetTile.setMine();
                notifyNeighborsMined(targetTile);
                numMinesMade++;
            }
        }
    }

    /**
     * gives second listener to all tiles once the first tile click occurs and mines are populated.
     * implements minesweeper function.
     * Minesweeper logic is as follows:
     *
     * If a clicked tile state is safe (no mine, has been dug), and the number of flagged tiles around it
     * matches the number of mines around it:
     *      click all un-flagged neighbors with cursor mode dig.
     * If a clicked tile state is unknown (has not been dug, can be flagged or unflagged):
     *      if the cursor mode is dig:
     *          if the tile has a mine:
     *              explode all mines and game ends.
     *          if the tile doesn't have a mine:
     *              reveal the tile as safe.
     *              if the tile has 0 mines in its neighbors:
     *                  click all un-flagged neighbors with cursor mode dig.
     *      if the cursor mode is flag:
     *          if the tile is unknown:
     *              change the tile to flagged
     *              tell its neighbors it has a flagged neighbor.
     *              decrement number of remaining mines by 1
     *          if the tile is known:
     *              change the tile to unflagged
     *              tell its neighbors it lost a flagged neighbor.
     *              increment number of remaining mines by 1
     *     if a state change happened to a tile:
     *          test if the game is won. (all mines flagged and all non-mined tiles revealed)
     *          end game if won.
     *
     */
    private void setGameplayTileClickListeners(){
        for (Tile [] row : tiles){
            for (Tile cell : row){
                cell.setOnClickListener(v -> {
                    Tile t = (Tile) v;
                    if (t.isSafeAndFullyTagged()) {
                        digAll(getUnknownNeighbors(t));
                    } else {
                        clickTile(t, cursorMode);
                    }
                });
            }
        }
    }
    /**
     * Perform tile's click action with cursorMode setting. If an action occurred
     * to a tile, perform subsequent game logic.
     * Then checks for win condition.
     * @param t Tile that was clicked.
     * @param cursorMode Cursor mode. Dig or Flag.
     */
    private void clickTile(Tile t, CursorMode cursorMode){
        TileAction action = t.click(cursorMode);
        switch (action){
            case NO_ACTION:
                return;
            case FLAG_ADDED:
                numMinesRemaining--;
                notifyNeighborsFlagged(t, ADD_FLAGGED_NEIGHBOR);
                numUnknown--;
                break;
            case FLAG_REMOVED:
                numMinesRemaining++;
                notifyNeighborsFlagged(t, REMOVE_FLAGGED_NEIGHBOR);
                numUnknown++;
                break;
            case SAFELY_DUG:
                if (t.getNumNearbyMines() == NO_NEARBY_MINES) {
                    digAll(getUnknownNeighbors(t));
                }
                numUnknown--;
                break;
            case MINE_EXPLODED:
                endGame();
                break;
        }
        mineRemainderText.setText(Integer.toString(numMinesRemaining));
        testForWin();
    }
    /**
     * If the number of flagged mines and number of unknown mines both == 0, triggers game win.
     */
    private void testForWin(){
        if (numMinesRemaining == 0 && numUnknown == 0){
            isGameWon = true;
            endGame();
        }
    }

    /**
     * Performs dig actions on all tiles inside given ArrayList.
     * @param targets ArrayList of tiles.
     */
    private void digAll(ArrayList<Tile> targets) {
        targets.forEach(t -> clickTile(t, DIG));
    }

    /**
     * gets the neighbors of the given tile.
     * @param tile Tile to get neighbors of.
     * @return ArrayList containing neighboring tiles.
     */
    private ArrayList<Tile> getNeighbors(Tile tile) {
        int col = tile.getCol();
        int row = tile.getRow();
        int neighborRow;
        int neighborCol;
        ArrayList<Tile> neighbors = new ArrayList<>();
        for (int rowOffset : NEIGHBOR_COORDS[ROW_INDEX]){
            for (int colOffset : NEIGHBOR_COORDS[COL_INDEX]){
                // exclude self from neighbor list
                if (rowOffset != COORD_SELF || colOffset != COORD_SELF){
                    neighborRow = row + rowOffset;
                    neighborCol = col + colOffset;
                    // exclude out of bounds coordinates
                    if (inRowBounds(neighborRow) && inColBounds(neighborCol)){
                        neighbors.add(tiles[neighborRow][neighborCol]);
                    }
                }
            }
        }
        return neighbors;
    }

    /**
     * Gets neighbors of given tile in state UNKNOWN
     * @param tile Tile to get neighbors of.
     * @return ArrayList containing neighboring tiles in state UNKNOWN.
     */
    private ArrayList<Tile> getUnknownNeighbors(Tile tile) {
        ArrayList<Tile> neighbors = getNeighbors(tile);
        neighbors.removeIf(t -> t.getState() != State.UNKNOWN);
        return neighbors;
    }

    /**
     * Notify all neighbors of given tile that a neighbor's flagged state has changed.
     * @param t Tile whose neighbors will be notified.
     * @param flaggedNeighborDelta int representing change in flagged neighbor count.
     */
    private void notifyNeighborsFlagged(Tile t, int flaggedNeighborDelta) {
        ArrayList<Tile> neighbors = getNeighbors(t);
        neighbors.forEach(neighbor -> neighbor.addToNumFlaggedNeighbors(flaggedNeighborDelta));
    }

    /**
     * Notify all neighbors of given tile that a neighbor contains a mine.
     * @param t Tile whose neighbors will be notified.
     */
    private void notifyNeighborsMined(Tile t){
        ArrayList<Tile> neighbors = getNeighbors(t);
        neighbors.forEach(neighbor -> neighbor.addNumNearbyMines());
    }

    /**
     * triggers game end view updates and triggers game end action.
     */
    private void endGame(){
        exposeAllMines();
        gameEndButton.performClick();
    }

    /**
     * Depending on winStatus, explode or safely expose all mines
     */
    public void exposeAllMines() {
        State mineState = State.EXPLODED;
        if (isGameWon){
            mineState = State.UNEXPLODED;
        }
        State finalMineState = mineState;
        for (Tile[] tileRow : tiles){
            for (Tile cell : tileRow){
                if (cell.hasMine()){
                    cell.setState(finalMineState);
                }
            }
        }
    }

    /**
     * test if given column coordinate is within game board range.
     * @param testCol int representing column coordinate
     * @return boolean representing if column is inside valid range.
     */
    private boolean inColBounds(int testCol) {
        return testCol >= COORD_MINIMUM && testCol < cols;
    }
    /**
     * test if given row coordinate is within game board range.
     * @param testRow int representing row coordinate
     * @return boolean representing if row is inside valid range.
     */
    private boolean inRowBounds(int testRow) {
        return testRow >= COORD_MINIMUM && testRow < rows;
    }
}

