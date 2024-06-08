package dev.rynk.minesweeper;

import static dev.rynk.minesweeper.utils.Constants.*;
import static dev.rynk.minesweeper.utils.Rank.GOLD;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;

import dev.rynk.minesweeper.databinding.ActivityMainBinding;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import dev.rynk.minesweeper.utils.MenuHandler;


public class MainActivity extends BaseIOActivity{
    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        initPreferences(this);
        getBindings();
        setNameStates();
        setDifficultySelectionListener();
    }
    public void menu_click(View v){
        MenuHandler.menu_click(v, this);
    }
    private void getBindings(){
        // view binding
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
    }
    private void setNameStates(){
        String username = getCurrentName();
        binding.nameInput.setText(username);
        binding.nameInput.setOnEditorActionListener(new TextView.OnEditorActionListener(){
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event){
                if (actionId == EditorInfo.IME_ACTION_DONE){
                    setName(NAME_FILE, CURRENT_USER_KEY, v.getText().toString());
                }
                return false;
            }
        });
    }
    private void setDifficultySelectionListener(){
        binding.difficultyButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                saveDifficulty();
            }
        });
    }
    @Override
    protected void onPause(){
        super.onPause();
        saveDifficulty();
    }
    private void saveDifficulty(){
        String selectedDifficulty = binding.difficultyButton.getText().toString();
        setDifficulty(selectedDifficulty);
    }
}