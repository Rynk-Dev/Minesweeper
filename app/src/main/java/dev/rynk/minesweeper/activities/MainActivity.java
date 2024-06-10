package dev.rynk.minesweeper.activities;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatDelegate;

import dev.rynk.minesweeper.customactivities.BaseIOActivity;
import dev.rynk.minesweeper.databinding.ActivityMainBinding;
import dev.rynk.minesweeper.enums.Difficulty;

public class MainActivity extends BaseIOActivity<ActivityMainBinding> {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        setupDifficultyButton();
        setNameView();
        // Give toggle button null action
        binding.difficultyButton.setOnClickListener(v -> {});
    }

    /**
     * Assigns text to difficulty button programmatically, to ensure consistency.
     */
    private void setupDifficultyButton() {
        binding.difficultyButton.setTextOn(Difficulty.MEDIUM.toString());
        binding.difficultyButton.setTextOff(Difficulty.EASY.toString());
    }
    /**
     * Gets name saved in "current_user" preferences and updates nameInput text
     * adds validation listener to name edittext to prevent null values.
     */
    private void setNameView(){
        String username = getCurrentName();
        binding.nameInput.setText(username);
        binding.nameInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}
            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().trim().isEmpty()){
                    binding.nameInput.setError("Please give a name");
                }
            }
        });
    }
    /**
     * On pause, save the difficulty and save the name
     */
    @Override
    protected void onPause(){
        saveDifficulty(binding.difficultyButton.getText().toString());
        saveName(NAME_FILE, CURRENT_USER_KEY, binding.nameInput.getText().toString());
        super.onPause();
    }
    /**
     * Implements interface to pass activity binding to parent
     * @return binding for the main activity
     */
    @Override
    protected ActivityMainBinding getViewBinding() {
        return ActivityMainBinding.inflate(getLayoutInflater());
    }
}
