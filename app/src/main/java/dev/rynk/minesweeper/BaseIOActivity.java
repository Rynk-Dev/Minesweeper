package dev.rynk.minesweeper;



import static dev.rynk.minesweeper.utils.Constants.*;

import static dev.rynk.minesweeper.utils.Rank.*;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.appcompat.app.AppCompatActivity;

import dev.rynk.minesweeper.utils.Rank;

public abstract class BaseIOActivity extends AppCompatActivity {
    protected SharedPreferences[] preferences;
    protected void initPreferences(Context context){
        preferences = new SharedPreferences[NUM_FILES];
        for (int prefIdx = LOOP_START; prefIdx < NUM_FILES; prefIdx ++){
            preferences[prefIdx] = context.getSharedPreferences(FILE_NAMES[prefIdx], Context.MODE_PRIVATE);
        }
    }
    protected void setTime(int file, String name, int time){
        SharedPreferences.Editor editor = preferences[file].edit();
        editor.putInt(name, time);
        editor.apply();
    }
    protected int getTime(int file, String name){
        return preferences[file].getInt(name, SCORE_NOT_FOUND);
    }
    protected void setName(int file, String category, String name){
        SharedPreferences.Editor editor = preferences[file].edit();
        editor.putString(category, name);
        editor.apply();
    }
    protected String getName(int file, String nameType){
        return preferences[file].getString(nameType, NAME_NOT_FOUND);
    }
    protected String getCurrentName(){
        return getName(NAME_FILE, CURRENT_USER_KEY);
    }
    protected void setLeaderboard(Rank position, String name, int time){
        setName(LEADERBOARD_FILE, position.name, name);
        setTime(LEADERBOARD_FILE, position.time, time);
    }
    protected void addToLeaderboard(Rank currRank, String name, int time){
        Rank nextRank;
        if (currRank != UNRANKED){  // while current rank is GOLD, SILVER, or BRONZE
            nextRank = nextRank(currRank);
            String oldName = getName(LEADERBOARD_FILE, currRank.name);
            int oldTime = getTime(LEADERBOARD_FILE, currRank.time);
            addToLeaderboard(nextRank, oldName, oldTime);   // move old record holder to lower position
            setLeaderboard(currRank, name, time);  // add new record holder to position
        }
    }
    protected Rank getLeaderboardRank(int time){
        Rank rank = GOLD;
        while (rank != UNRANKED){
            int rankTime = getTime(LEADERBOARD_FILE, rank.time);
            if (time < rankTime) {return rank;}
            rank = nextRank(rank);
        }
        return UNRANKED;
    }

    protected boolean handleIfNewPersonalBest(String name, int time){
        int previousBest = getTime(PERSONAL_BEST_FILE, name);
        if (time < previousBest){
            setTime(PERSONAL_BEST_FILE, name, time);
            return true;
        }
        return false;
    }
    protected boolean addScoresAndCheckIfPersonalBest(int time) {
        String userName = getCurrentName();
        Rank leaderboardRank = getLeaderboardRank(time);
        if (leaderboardRank != UNRANKED) {
            addToLeaderboard(leaderboardRank, userName, time);
        }
        setTime(PERSONAL_RECENT_FILE, userName, time);
        return handleIfNewPersonalBest(userName, time);
    }

    protected void clearAll(){
        for (SharedPreferences sf : preferences){
            SharedPreferences.Editor editor = sf.edit();
            editor.clear();
            editor.apply();
        }
    }
}
