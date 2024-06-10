package dev.rynk.minesweeper.customactivities;

import static dev.rynk.minesweeper.utils.Constants.*;
import static dev.rynk.minesweeper.enums.Rank.*;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.viewbinding.ViewBinding;

import dev.rynk.minesweeper.enums.Rank;

public abstract class BaseIOActivity<T extends ViewBinding> extends BaseActivity<T> {
    protected final int NAME_FILE = 0;
    protected final int PERSONAL_BEST_FILE = 1;
    protected final int PERSONAL_RECENT_FILE = 2;
    protected final int LEADERBOARD_FILE = 3;
    protected final int NUM_FILES = 4;
    protected final String[] FILE_NAMES = {"current name", "personal_best", "personal_recent", "leaderboard"};
    protected final String CURRENT_USER_KEY = "current_user";
    protected final String DIFFICULTY_KEY = "difficulty";

    protected SharedPreferences[] preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        initPreferences(this);
    }

    /**
     * Initializes array of SharedPreferences
     * @param context
     */
    protected void initPreferences(Context context){
        preferences = new SharedPreferences[NUM_FILES];
        for (int prefIdx = LOOP_START_INDEX; prefIdx < NUM_FILES; prefIdx ++){
            preferences[prefIdx] = context.getSharedPreferences(FILE_NAMES[prefIdx], Context.MODE_PRIVATE);
        }
    }

    /**
     * Saves the time associated with a name to a given file.
     * @param file int representing index of the target preference in the preferences array.
     *             Values: NAME_FILE, PERSONAL_BEST_FILE, PERSONAL_RECENT_FILE, LEADERBOARD_FILE
     * @param name String representing the name that will be the key.
     * @param time int representing the time value associated with the name.
     */
    protected void saveTime(int file, String name, int time){
        SharedPreferences.Editor editor = preferences[file].edit();
        editor.putInt(name, time);
        editor.apply();
    }
    /**
     * Gets the time associated with a name from a given file.
     * @param file int representing index of the target preference in the preferences array.
     *             Values: NAME_FILE, PERSONAL_BEST_FILE, PERSONAL_RECENT_FILE, LEADERBOARD_FILE
     * @param name String representing the name for whom the time is desired.
     * @return int representing time where 1 == 100ms.
     */
    protected int getTime(int file, String name){
        return preferences[file].getInt(name, SCORE_NOT_FOUND);
    }

    /**
     * Saves the given name to a category key in a given file.
     * @param file int representing index of the target preference in preferences array.
     *             Values: NAME_FILE, LEADERBOARD_FILE
     * @param category String representing category the saved name is.
     * @param name String representing the name to save.
     */
    protected void saveName(int file, String category, String name){
        SharedPreferences.Editor editor = preferences[file].edit();
        editor.putString(category, name);
        editor.apply();
    }
    /**
     * Gets the name associated with a category key from a given file.
     * @param file int representing index of the target preference in preferences array.
     *             Values: NAME_FILE, LEADERBOARD_FILE
     * @param category String representing category key the name is saved in.
     * @return String represeing the returned name.
     */
    protected String getName(int file, String category){
        return preferences[file].getString(category, NAME_NOT_FOUND);
    }
    /**
     * Saves the given difficulty to the NAME_FILE preference under the difficulty category.
     * @param difficulty String representing the difficulty to save.
     */
    protected void saveDifficulty(String difficulty){
        saveName(NAME_FILE, DIFFICULTY_KEY, difficulty);
    }
    /**
     * Gets the given difficulty from the NAME_FILE preference under the difficulty category.
     * @return String representing last selected difficulty.
     */
    protected String getDifficulty(){
        return getName(NAME_FILE, DIFFICULTY_KEY);
//        return preferences[NAME_FILE].getString(DIFFICULTY_KEY, NAME_NOT_FOUND);
    }

    /**
     * Gets the current user name value.
     * @return String representing current user name.
     */
    protected String getCurrentName(){
        return getName(NAME_FILE, CURRENT_USER_KEY);
    }

    /**
     * Saves the given user name and time under the given leaderboard position.
     * @param position Rank representing leaderboard position.
     * @param name String representing user name.
     * @param time String representing user time.
     */
    protected void saveLeaderboard(Rank position, String name, int time){
        saveName(LEADERBOARD_FILE, position.name, name);
        saveTime(LEADERBOARD_FILE, position.time, time);
    }

    /**
     * Recursive function that adds a given name and time to a leaderboard position, shifting
     * the previous position holder and those under down 1 rank.
     * @param currRank Rank representing leaderboard position.
     * @param name String representing user name.
     * @param time int representing user time.
     */
    protected void addToLeaderboard(Rank currRank, String name, int time){
        Rank nextRank;
        if (currRank != UNRANKED){  // while current rank is GOLD, SILVER, or BRONZE
            nextRank = nextRank(currRank);  //gets the next lower rank
            String oldName = getName(LEADERBOARD_FILE, currRank.name);
            int oldTime = getTime(LEADERBOARD_FILE, currRank.time);
            addToLeaderboard(nextRank, oldName, oldTime);   // move old record holder to lower position
            saveLeaderboard(currRank, name, time);  // add new record holder to position
        }
    }

    /**
     * Checks given time with times on the leaderboard, returning its ranking.
     * @param time int representing challenger time.
     * @return Rank representing leaderboard position.
     */
    protected Rank getLeaderboardRank(int time){
        Rank rank = GOLD;
        while (rank != UNRANKED){
            int rankTime = getTime(LEADERBOARD_FILE, rank.time);
            if (time < rankTime) {return rank;}
            rank = nextRank(rank);
        }
        return UNRANKED;
    }

    /**
     * Checks user time to previous personal best. If new personal best, saves new personal best
     * and returns true.
     * @param time int representing user time
     * @return boolean representing if a new personal best was achieved.
     */
    protected boolean handleIfPersonalBest(int time){
        String userName = getCurrentName();
        int previousBest = getTime(PERSONAL_BEST_FILE, userName);
        if (time < previousBest){
            saveTime(PERSONAL_BEST_FILE, userName, time);
            return true;
        }
        return false;
    }
    /**
     * Checks user time to leaderboard positions. If the time is ranked, saves to leaderboard
     * and returns true.
     * @param time int representing user time
     * @return boolean representing if a new leaderboard rank was achieved.
     */
    protected boolean handleIfLeaderboardRank(int time){
        Rank leaderboardRank = getLeaderboardRank(time);
        if (leaderboardRank != UNRANKED) {
            addToLeaderboard(leaderboardRank, getCurrentName(), time);
            return true;
        }
        return false;
    }
//    /**
//     *
//     * @param time
//     * @return
//     */
//    protected boolean saveScoresAndCheckIfPersonalBest(int time) {
//        String userName = getCurrentName();
//        Rank leaderboardRank = getLeaderboardRank(time);
//        if (leaderboardRank != UNRANKED) {
//            addToLeaderboard(leaderboardRank, userName, time);
//        }
//        saveTime(PERSONAL_RECENT_FILE, userName, time);
//        return handleIfNewPersonalBest(userName, time);
//    }

    /**
     * Saves time to the current user's personal recent score.
     * @param time
     */
    protected void saveRecentTime(int time){
        String userName = getCurrentName();
        saveTime(PERSONAL_RECENT_FILE, userName, time);
    }

    /**
     * deletes all saved preferences data.
     */
    protected void clearAll(){
        for (SharedPreferences sf : preferences){
            SharedPreferences.Editor editor = sf.edit();
            editor.clear();
            editor.apply();
        }
    }
}
