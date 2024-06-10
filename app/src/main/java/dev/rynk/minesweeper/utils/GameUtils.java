//package dev.rynk.minesweeper.utils;
//
//import static dev.rynk.minesweeper.enums.CursorMode.*;
//
//import java.util.ArrayList;
//
//import dev.rynk.minesweeper.customviews.Tile;
//import dev.rynk.minesweeper.enums.CursorMode;
//import dev.rynk.minesweeper.enums.State;
//
//public class GameBoard {
//    public final int[][] NEIGHBOR_COORDS = {{-1,0,1},{-1,0,1}};
//    public final int ROW_INDEX = 0;
//    public final int COL_INDEX = 1;
//    public final int COORD_SELF = 0;
//    public boolean isGameWon;
//    protected Tile[][] tiles;
//    public final int COORD_MINIMUM = 0;
//    public final int ADD_FLAGGED_NEIGHBOR = 1;
//    public final int REMOVE_FLAGGED_NEIGHBOR = -1;
//
//    protected int cols;
//    protected int rows;
//
//    /**
//     * gets the neighbors of the given tile.
//     * @param tile Tile to get neighbors of.
//     * @return ArrayList containing neighboring tiles.
//     */
//    protected ArrayList<Tile> getNeighbors(Tile tile) {
//        int col = tile.getCol();
//        int row = tile.getRow();
//        int neighborRow;
//        int neighborCol;
//        ArrayList<Tile> neighbors = new ArrayList<>();
//        for (int rowOffset : NEIGHBOR_COORDS[ROW_INDEX]){
//            for (int colOffset : NEIGHBOR_COORDS[COL_INDEX]){
//                // exclude self from neighbor list
//                if (rowOffset != COORD_SELF || colOffset != COORD_SELF){
//                    neighborRow = row + rowOffset;
//                    neighborCol = col + colOffset;
//                    // exclude out of bounds coordinates
//                    if (inRowBounds(neighborRow) && inColBounds(neighborCol)){
//                        neighbors.add(tiles[neighborRow][neighborCol]);
//                    }
//                }
//            }
//        }
//        return neighbors;
//    }
//    /**
//     * Gets neighbors of given tile in state UNKNOWN
//     * @param tile Tile to get neighbors of.
//     * @return ArrayList containing neighboring tiles in state UNKNOWN.
//     */
//    protected ArrayList<Tile> getUnknownNeighbors(Tile tile) {
//        ArrayList<Tile> neighbors = getNeighbors(tile);
//        neighbors.removeIf(t -> t.getState() != State.UNKNOWN);
//        return neighbors;
//    }
//
//    /**
//     * Notify all neighbors of given tile that a neighbor's flagged state has changed.
//     * @param t Tile whose neighbors will be notified.
//     * @param flaggedNeighborDelta int representing change in flagged neighbor count.
//     */
//    protected void notifyNeighborsFlagged(Tile t, int flaggedNeighborDelta) {
//        ArrayList<Tile> neighbors = getNeighbors(t);
//        neighbors.forEach(neighbor -> neighbor.addToNumFlaggedNeighbors(flaggedNeighborDelta));
//    }
//    protected void notifyNeighborsMined(Tile t){
//        ArrayList<Tile> neighbors = getNeighbors(t);
//        neighbors.forEach(neighbor -> neighbor.addNumNearbyMines());
//    }
//    /**
//     * Performs dig actions on all tiles inside given ArrayList.
//     * @param targets ArrayList of tiles.
//     */
//    protected void digAll(ArrayList<Tile> targets) {
//        targets.forEach(t -> clickTile(t, DIG));
//    }
//    /**
//     * Tile click action. perform tile's click action with cursorMode setting. If the tile's
//     * status changed as a result, pass the tile to processTileChange to process game logic.
//     * Then checks for win condition.
//     * @param t Tile that was clicked.
//     * @param cursorMode Cursor mode. Dig or Flag.
//     */
//    protected void clickTile(Tile t, CursorMode cursorMode){
//        boolean tileChanged = t.clickUnknownTile(cursorMode);
//        if (tileChanged) {
//            processTileChange(t);
//            testForWin();
//        }
//    }
//    /**
//     * Processes game logic actions if clickTile resulted in a tile's state change.
//     * @param t Tile that was clicked
//     */
//    protected void processTileChange(Tile t) {
//        switch (t.getState()) {
//            // if tile was UNKNOWN and was clicked with FLAG cursor
//            case FLAGGED:
//                numMinesRemaining--;
//                notifyNeighborsFlagged(t, ADD_FLAGGED_NEIGHBOR);
//                break;
//            // if tile was FLAGGED and was clicked with FLAG cursor
//            case UNKNOWN:
//                numMinesRemaining++;
//                notifyNeighborsFlagged(t, REMOVE_FLAGGED_NEIGHBOR);
//                break;
//            // if tile was UNKNOWN without a mine and an action exposed the tile
//            case SAFE:
//                if (t.getNumNearbyMines() == NO_NEARBY_MINES) {
//                    digAll(getUnknownNeighbors(t));
//                }
//                break;
//            // if tile was UNKNOWN with a mine and an action exposed the tile
//            case EXPLODED:
//                endGame();
//                break;
//        }
//        mineRemainderText.setText(Integer.toString(numMinesRemaining));
//    }
//
//    /**
//     * If the number of flagged mines and number of unknown mines both == 0, triggers game win.
//     */
//    protected void testForWin(){
//        if (numMinesRemaining == 0){
//            numUnknown = getNumUnknownTiles();
//            if (numUnknown == 0) {
//                isGameWon = true;
//                endGame();
//            }
//        }
//    }
//    /**
//     * Get the number of tiles that are in the UNKNOWN state.
//     * @return int representing number of UNKNOWN state tiles.
//     */
//    protected int getNumUnknownTiles() {
//        int numUnknownTiles = 0;
//        for (Tile[] tileRow : tiles){
//            for (Tile cell : tileRow){
//                if (cell.getState() == State.UNKNOWN){
//                    numUnknownTiles ++;
//                }
//            }
//        }
//        return numUnknownTiles;
//    }
//
//    /**
//     * Depending on winStatus, explode or safely expose all mines
//     */
//    public void exposeAllMines() {
//        State mineState = State.EXPLODED;
//        if (isGameWon){
//            mineState = State.UNEXPLODED;
//        }
//        State finalMineState = mineState;
//        for (Tile[] tileRow : tiles){
//            for (Tile cell : tileRow){
//                if (cell.hasMine()){
//                    cell.setState(finalMineState);
//                }
//            }
//        }
//    }
//
//    /**
//     * test if given column coordinate is within game board range.
//     * @param testCol int representing column coordinate
//     * @return boolean representing if column is inside valid range.
//     */
//    protected boolean inColBounds(int testCol) {
//        return testCol >= COORD_MINIMUM && testCol < cols;
//    }
//    /**
//     * test if given row coordinate is within game board range.
//     * @param testRow int representing row coordinate
//     * @return boolean representing if row is inside valid range.
//     */
//    protected boolean inRowBounds(int testRow) {
//        return testRow >= COORD_MINIMUM && testRow < rows;
//    }
//}
