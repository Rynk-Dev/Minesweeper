package dev.rynk.minesweeper;

import static dev.rynk.minesweeper.utils.Constants.*;
import static dev.rynk.minesweeper.utils.CursorMode.*;
import static dev.rynk.minesweeper.utils.MathUtils.ticksToTime;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.activity.EdgeToEdge;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.Timer;
import java.util.TimerTask;

import dev.rynk.minesweeper.databinding.ActivityPlayBinding;
import dev.rynk.minesweeper.utils.CursorMode;
import dev.rynk.minesweeper.utils.GameManager;
import dev.rynk.minesweeper.utils.MenuHandler;
import dev.rynk.minesweeper.utils.TimerThread;

public class Play extends BaseIOActivity {
    private TimerThread timer;
    int finishTime;
    boolean gameOver = false;
    boolean gameWon = false;
    private ActivityPlayBinding binding;
    private GameManager gm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_play);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        initPreferences(this);
        getBindings();
        String difficulty = getDifficulty();
        gm = new GameManager(difficulty, this, binding);
//        gm = new GameManager(COLS_SMALL, ROWS_SMALL, MINES_SMALL, this, binding);
        setCursorModeListener();
        timer = new TimerThread();
        timer.startTimer(this, binding.elapsedTime);
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
        startActivity(activityChangeIntent);
    }
    private Bundle bundleAndSaveGameStats(int finishTime){
        boolean isNewPersonalBest = false;
        if (gm.gameState){
            isNewPersonalBest = addScoresAndCheckIfPersonalBest(finishTime);
        }
        Bundle gameStats = new Bundle();
        gameStats.putString(NAME_KEY, getCurrentName());
        gameStats.putInt(TIME_KEY, finishTime);
        gameStats.putBoolean(WON_KEY, gm.gameState);
        gameStats.putBoolean(PERSONAL_BEST_KEY, isNewPersonalBest);
        return gameStats;
    }

    public void menu_click(View v){
        MenuHandler.menu_click(v, this);
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
    private void getBindings(){
        // view binding
        binding = ActivityPlayBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
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