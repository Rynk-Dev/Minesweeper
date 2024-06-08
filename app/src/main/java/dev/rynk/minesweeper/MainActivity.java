package dev.rynk.minesweeper;

import static dev.rynk.minesweeper.utils.Constants.*;

import android.os.Bundle;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatDelegate;

import dev.rynk.minesweeper.customactivities.BaseIOActivity;
import dev.rynk.minesweeper.databinding.ActivityMainBinding;

public class MainActivity extends BaseIOActivity<ActivityMainBinding> {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        EdgeToEdge.enable(this);
        initPreferences(this);
        setNameView();
        // Give toggle button null action
        binding.difficultyButton.setOnClickListener(v -> {});
    }

    @Override
    protected ActivityMainBinding getViewBinding() {
        return ActivityMainBinding.inflate(getLayoutInflater());
    }
    private void setNameView(){
        String username = getCurrentName();
        binding.nameInput.setText(username);
    }
    //On pause, save the difficulty and save the name;
    @Override
    protected void onPause(){
        setDifficulty(binding.difficultyButton.getText().toString());
        setName(NAME_FILE, CURRENT_USER_KEY, binding.nameInput.getText().toString());
        super.onPause();
    }
}