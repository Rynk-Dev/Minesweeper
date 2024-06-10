package dev.rynk.minesweeper.activities;

import static dev.rynk.minesweeper.utils.Constants.*;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.RadioGroup;

import dev.rynk.minesweeper.customactivities.BaseIOActivity;
import dev.rynk.minesweeper.databinding.ActivityPlayBinding;
import dev.rynk.minesweeper.enums.CursorMode;
import dev.rynk.minesweeper.enums.Difficulty;
import dev.rynk.minesweeper.utils.GameManager;
import dev.rynk.minesweeper.utils.TimerThread;

public class Play extends BaseIOActivity<ActivityPlayBinding> {
    public static final int EXPLOSION_DELAY = 1000;
    private TimerThread timer;
    int finishTime;
    private GameManager gm;
    private Difficulty difficulty;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);;
        // get saved game difficulty selection
        difficulty = Difficulty.valueOf(getDifficulty());
        // create game manager, which handles all game logic
        gm = new GameManager(difficulty, this, binding);
        // Play hosts the game manager, the timer, and the game end trigger.
        setGameEndListener();
        timer = new TimerThread();
        timer.startTimer(this, binding.elapsedTime);
    }
    /**
     * Sets listener to close button that pauses timer, gets game end stats bundle,
     * and triggers change to endscreen activity after a delay.
     */
    private void setGameEndListener(){
        binding.closeButton.setOnClickListener(v -> {
            timer.pauseTimer();
            finishTime = timer.getTicks();
            Bundle gameStats = getGameStatsBundle(finishTime);

            Intent activityChangeIntent = new Intent (Play.this, Endscreen.class);
            activityChangeIntent.putExtras(gameStats);
            // Delay before proceeding with intent change, to show mine positions.
            new Handler().postDelayed(() -> startActivity(activityChangeIntent), EXPLOSION_DELAY);
        });
    }
    /**
     * Add game end stats to a bundle and return the bundle.
     * @param finishTime time representing the user's finishing time.
     * @return Bundle containing game end stats.
     */
    private Bundle getGameStatsBundle(int finishTime){
        boolean isNewPersonalBest = false;
        boolean isNewLeader = false;
        if (gm.isGameWon){
            saveRecentTime(finishTime);
            isNewPersonalBest = handleIfPersonalBest(finishTime);
            isNewLeader = handleIfLeaderboardRank(finishTime);
        }
        Bundle gameStats = new Bundle();
        gameStats.putString(NAME_KEY, getCurrentName());
        gameStats.putInt(TIME_KEY, finishTime);
        gameStats.putBoolean(WON_KEY, gm.isGameWon);
        gameStats.putBoolean(PERSONAL_BEST_KEY, isNewPersonalBest);
        gameStats.putBoolean(NEW_LEADER_KEY, isNewLeader);
        return gameStats;
    }
    /**
     * pauses timer on stop (e.g., briefly exiting activity, reading rules).
     */
    @Override
    protected void onStop() {
        super.onStop();
        timer.pauseTimer();
    }
    /**
     * Clears timer on exit.
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        timer.clearTimer();
    }
    /**
     * resume timer when returning to app or returning from rules page.
     */
    @Override
    protected void onRestart() {
        super.onRestart();
        timer.startTimer(this, binding.elapsedTime);
    }
    /**
     * implements interface to pass activity binding to parent
     * @return binding for the play activity
     */
    @Override
    protected ActivityPlayBinding getViewBinding() {
        return ActivityPlayBinding.inflate(getLayoutInflater());
    }
}