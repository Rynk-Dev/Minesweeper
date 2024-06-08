package dev.rynk.minesweeper.utils;

public class Constants {
    private Constants(){};
    public static final int SCORE_NOT_FOUND = Integer.MAX_VALUE;
    public static final int TICKS_PER_SECOND = 10;
    public static final int TICKS_PER_MINUTE = 600;
    public static final int SECONDS_PER_MINUTE = 60;
    public static final int TIMER_DELAY = 0;
    public static final int TIMER_INTERVAL = 100;
    public static final int LOOP_START = 0;
    public static final String NO_SCORE = "No Score";
    public static final String TIME_FORMAT = "%02d:%02d.%01d";
    public static final String NAME_KEY = "name";
    public static final String TIME_KEY = "time";
    public static final String WON_KEY = "isGameWon";
    public static final String PERSONAL_BEST_KEY = "isPersonalBest";
    public static final String NAME_NOT_FOUND = "NoName";
    public static final String CURRENT_USER_KEY = "current_user";
    public static final int NAME_FILE = 0;
    public static final int PERSONAL_BEST_FILE = 1;
    public static final int PERSONAL_RECENT_FILE = 2;
    public static final int LEADERBOARD_FILE = 3;
    public static final int NUM_FILES = 4;
    public static final String[] FILE_NAMES = {"current name", "personal_best", "personal_recent", "leaderboard"};
    public static final String DIFFICULTY_KEY = "difficulty";

    public static final int START_TIME = 0;

    public static final int ROWS_SMALL = 9;
    public static final int COLS_SMALL = 9;
    public static final int MINES_SMALL = 10;
    public static final int ROWS_MEDIUM = 18;
    public static final int COLS_MEDIUM = 9;
    public static final int MINES_MEDIUM = 25;
    public static final int COORDINATE_ERROR = -1;

    public static final boolean HAS_MINE = true;
    public static final boolean NO_MINE = false;
    public static final int NO_NEARBY_MINES = 0;
    public static final boolean GAME_WON = true;
    public static boolean GAME_LOST = false;
    public static final int[][] NEIGHBOR_COORDS = {{-1,0,1},{-1,0,1}};
    public static final int ROW_INDEX = 0;
    public static final int COL_INDEX = 1;
    public static final int COORD_SELF = 0;
    public static final int COORD_MINIMUM = 0;
    public static final int ADD_FLAGGED_NEIGHBOR = 1;
    public static final int REMOVE_FLAGGED_NEIGHBOR = -1;

}
