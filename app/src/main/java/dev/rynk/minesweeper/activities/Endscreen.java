package dev.rynk.minesweeper.activities;

import static dev.rynk.minesweeper.utils.Constants.*;
import static dev.rynk.minesweeper.utils.MathUtils.ticksToTime;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import dev.rynk.minesweeper.R;
import dev.rynk.minesweeper.customactivities.BaseActivity;
import dev.rynk.minesweeper.databinding.ActivityEndscreenBinding;

public class Endscreen extends BaseActivity<ActivityEndscreenBinding> {
    final boolean ENABLE_ORIGINAL_BACK_PRESS = false;
    private String name;
    private int time;
    private boolean won;
    private boolean newPersonalBest;
    private boolean newLeader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getStats();
        setViews();
    }

    /**
     * sets username and user time views, sets hero and banner depending on win status,
     * shows toast if player won and the time is a personal best.
     */
    private void setViews() {
        binding.userName.setText(name);
        binding.userTime.setText(ticksToTime(time));
        if (won){
            if (newPersonalBest){
                Toast.makeText(this, getString(R.string.new_personal_best_message), Toast.LENGTH_SHORT).show();
            }
            if (newLeader){
                Toast.makeText(this, getString(R.string.new_lead_message), Toast.LENGTH_SHORT).show();
            }
            setWinView();
        }
        else {
            setLoseView();
        }
    }

    /**
     * sets hero and banner to lost state
     */
    private void setLoseView() {
        binding.logo.setImageResource(R.drawable.close);
        binding.gameStatus.setText(R.string.lose_message);
    }

    /**
     * sets hero and banner to win state
     */
    private void setWinView() {
        binding.logo.setImageResource(R.drawable.trophy);
        binding.gameStatus.setText(R.string.win_message);
    }

    /**
     * get game stats from passed intent
     */
    private void getStats() {
        Intent gameResults = getIntent();
        time = gameResults.getIntExtra(TIME_KEY, Integer.MAX_VALUE);
        name = gameResults.getStringExtra(NAME_KEY);
        won = gameResults.getBooleanExtra(WON_KEY, false);
        newPersonalBest = gameResults.getBooleanExtra(PERSONAL_BEST_KEY, false);
        newLeader = gameResults.getBooleanExtra(NEW_LEADER_KEY, false);
    }

    /**
     * Overrides default backpress action to prevent unauthorized return.
     */
    @Override
    public void onBackPressed(){
        Intent activityChangeIntent = new Intent (Endscreen.this, MainActivity.class);
        startActivity(activityChangeIntent);

        if (ENABLE_ORIGINAL_BACK_PRESS){
            super.onBackPressed();
        }
    }
    /**
     * implements interface to pass activity binding to parent
     * @return binding for the end screen activity
     */
    @Override
    protected ActivityEndscreenBinding getViewBinding() {
        return ActivityEndscreenBinding.inflate(getLayoutInflater());
    }
}
