package dev.rynk.minesweeper;

import static dev.rynk.minesweeper.utils.Constants.*;
import static dev.rynk.minesweeper.utils.Constants.LOOP_START;
import static dev.rynk.minesweeper.utils.MathUtils.ticksToTime;
import static dev.rynk.minesweeper.utils.Rank.*;

import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import dev.rynk.minesweeper.databinding.ActivityScoresBinding;
import dev.rynk.minesweeper.utils.MenuHandler;
import dev.rynk.minesweeper.utils.Rank;

public class Scores extends BaseIOActivity {

    private int personalBest;
    private int[] leaderboardTimes;
    private String[] leaderboardNames;
    private String currentName;
    private int previousTime;
    private ActivityScoresBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_scores);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        initPreferences(this);
        getBindings();
        getCurrentUserData();
        getLeaderboard();
        updateViews();
        binding.debugReset.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                clearAll();
                updateViews();
            }
        });
    }

    private void updateViews() {
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

    private void getCurrentUserData() {
        currentName = getCurrentName();
        previousTime = getTime(PERSONAL_RECENT_FILE, currentName);
        personalBest = getTime(PERSONAL_BEST_FILE, currentName);
    }

    private void getBindings(){
        // view binding
        binding = ActivityScoresBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
    }
    public void menu_click(View v){
        MenuHandler.menu_click(v, this);
    }
    private void getLeaderboard(){
        leaderboardNames = new String[Rank.numRanks()];
        leaderboardTimes = new int[Rank.numRanks()];
        for (int i = LOOP_START; i < Rank.numRanks(); i++){
            Rank rank = Rank.rankByIndex(i);
            leaderboardNames[i] = getName(LEADERBOARD_FILE, rank.name);
            leaderboardTimes[i] = getTime(LEADERBOARD_FILE,rank.time);
        }
    }

}