package dev.rynk.minesweeper;

import static dev.rynk.minesweeper.utils.Constants.*;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.RadioGroup;

import androidx.activity.EdgeToEdge;

import dev.rynk.minesweeper.customactivities.BaseIOActivity;
import dev.rynk.minesweeper.databinding.ActivityPlayBinding;
import dev.rynk.minesweeper.utils.CursorMode;
import dev.rynk.minesweeper.utils.GameManager;
import dev.rynk.minesweeper.utils.TimerThread;

public class Play extends BaseIOActivity<ActivityPlayBinding> {
    private TimerThread timer;
    int finishTime;
    private GameManager gm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        initPreferences(this);
        String difficulty = getDifficulty();
        gm = new GameManager(difficulty, this, binding);
        setCursorModeListener();
        timer = new TimerThread();
        timer.startTimer(this, binding.elapsedTime);
    }

    @Override
    protected ActivityPlayBinding getViewBinding() {
        return ActivityPlayBinding.inflate(getLayoutInflater());
    }

    public void endGameListener(View view) {
        gameFinishedActions();
    }
    void gameFinishedActions(){
        timer.pauseTimer();
        finishTime = timer.getTicks();
        Bundle gameStats = bundleAndSaveGameStats(finishTime);

        Intent activityChangeIntent = new Intent (Play.this, Endscreen.class);
        activityChangeIntent.putExtras(gameStats);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(activityChangeIntent);
            }
        }, EXPOSE_ALL_DELAY);

    }
    private Bundle bundleAndSaveGameStats(int finishTime){
        boolean isNewPersonalBest = false;
        if (gm.isGameWon){
            isNewPersonalBest = addScoresAndCheckIfPersonalBest(finishTime);
        }
        Bundle gameStats = new Bundle();
        gameStats.putString(NAME_KEY, getCurrentName());
        gameStats.putInt(TIME_KEY, finishTime);
        gameStats.putBoolean(WON_KEY, gm.isGameWon);
        gameStats.putBoolean(PERSONAL_BEST_KEY, isNewPersonalBest);
        return gameStats;
    }
    @Override
    protected void onStop() {
        super.onStop();
        timer.pauseTimer();
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        timer.clearTimer();
    }
    @Override
    protected void onRestart() {
        super.onRestart();
        timer.startTimer(this, binding.elapsedTime);
    }
    private void setCursorModeListener() {
        binding.cursorModeGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            /**
             * If the audio switch is on, finds the selected audio file and plays it.
             * @param group The RadioGroup that was changed.
             * @param checkedId  Id of checked child.
             */
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                gm.setCursorMode(CursorMode.valueOf(findViewById(checkedId).getTag().toString()));
            }
        });
    }

}