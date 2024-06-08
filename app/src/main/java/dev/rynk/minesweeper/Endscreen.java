package dev.rynk.minesweeper;

import static dev.rynk.minesweeper.utils.Constants.*;
import static dev.rynk.minesweeper.utils.MathUtils.ticksToTime;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import dev.rynk.minesweeper.customactivities.BaseActivity;
import dev.rynk.minesweeper.databinding.ActivityEndscreenBinding;
import dev.rynk.minesweeper.databinding.ActivityRulesBinding;
import dev.rynk.minesweeper.utils.MenuHandler;

public class Endscreen extends BaseActivity<ActivityEndscreenBinding> {
    final boolean ENABLE_ORIGINAL_BACK_PRESS = false;
    private String name;
    private int time;
    private boolean won;
    private boolean newPersonalBest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        getStats();
        setScoreView();
        if (won){
            if (newPersonalBest){
                Toast.makeText(this, getString(R.string.new_personal_best_message), Toast.LENGTH_LONG).show();
            }
            setWinView();
        }
        else {
            setLoseView();
        }
    }
    private void setScoreView() {
        binding.userName.setText(name);
        binding.userTime.setText(ticksToTime(time));
    }
    private void setLoseView() {
        binding.logo.setImageResource(R.drawable.close);
        binding.gameStatus.setText(R.string.lose_message);
    }
    private void setWinView() {
        binding.logo.setImageResource(R.drawable.trophy);
        binding.gameStatus.setText(R.string.win_message);
    }
    private void getStats() {
        Intent gameResults = getIntent();
        time = gameResults.getIntExtra(TIME_KEY, Integer.MAX_VALUE);
        name = gameResults.getStringExtra(NAME_KEY);
        won = gameResults.getBooleanExtra(WON_KEY, false);
        newPersonalBest = gameResults.getBooleanExtra(PERSONAL_BEST_KEY, false);
    }
    @Override
    public void onBackPressed(){
        Intent activityChangeIntent = new Intent (Endscreen.this, MainActivity.class);
        startActivity(activityChangeIntent);

        if (ENABLE_ORIGINAL_BACK_PRESS){
            super.onBackPressed();
        }
    }
    @Override
    protected ActivityEndscreenBinding getViewBinding() {
        return ActivityEndscreenBinding.inflate(getLayoutInflater());
    }
}
