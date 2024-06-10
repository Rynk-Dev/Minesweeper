package dev.rynk.minesweeper.activities;

import static dev.rynk.minesweeper.utils.Constants.*;
import static dev.rynk.minesweeper.utils.MathUtils.ticksToTime;
import static dev.rynk.minesweeper.enums.Rank.*;

import android.os.Bundle;
import android.view.View;

import dev.rynk.minesweeper.customactivities.BaseIOActivity;
import dev.rynk.minesweeper.databinding.ActivityScoresBinding;
import dev.rynk.minesweeper.enums.Rank;

public class Scores extends BaseIOActivity<ActivityScoresBinding> {

    private int personalBest;
    private int[] leaderboardTimes;
    private String[] leaderboardNames;
    private String currentName;
    private int previousTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getCurrentUserData();
        getLeaderboard();
        updateScoreViews();
        setClearButton();

    }
    /**
     * implements interface to pass activity binding to parent
     * @return binding for the scores activity
     */
    @Override
    protected ActivityScoresBinding getViewBinding() {
        return ActivityScoresBinding.inflate(getLayoutInflater());
    }

    /**
     * gets the current user's last and personal best times from preferences.
     */
    private void getCurrentUserData() {
        currentName = getCurrentName();
        previousTime = getTime(PERSONAL_RECENT_FILE, currentName);
        personalBest = getTime(PERSONAL_BEST_FILE, currentName);
    }

    /**
     * gets the leaderboard names and times from preferences.
     */
    private void getLeaderboard(){
        leaderboardNames = new String[Rank.numRanks()];
        leaderboardTimes = new int[Rank.numRanks()];
        for (int i = LOOP_START_INDEX; i < Rank.numRanks(); i++){
            Rank rank = Rank.rankByIndex(i);
            leaderboardNames[i] = getName(LEADERBOARD_FILE, rank.name);
            leaderboardTimes[i] = getTime(LEADERBOARD_FILE,rank.time);
        }
    }

    /**
     * updates score names and times with retrieved data.
     */
    private void updateScoreViews() {
        binding.userName.setText(currentName);
        binding.userTime.setText(ticksToTime(previousTime));
        binding.userTopName.setText(currentName);
        binding.userTopTime.setText(ticksToTime(personalBest));
        binding.goldUserName.setText(leaderboardNames[GOLD.index]);
        binding.goldUserTime.setText(ticksToTime(leaderboardTimes[GOLD.index]));
        binding.silverUserName.setText(leaderboardNames[SILVER.index]);
        binding.silverUserTime.setText(ticksToTime(leaderboardTimes[SILVER.index]));
        binding.bronzeUserName.setText(leaderboardNames[BRONZE.index]);
        binding.bronzeUserTime.setText(ticksToTime(leaderboardTimes[BRONZE.index]));
    }

    /**
     * Add onclick listener to clear button that clears all preferences data and refreshes the page.
     */
    private void setClearButton() {
        binding.debugReset.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                clearAll();
                recreate();
            }
        });
    }


}