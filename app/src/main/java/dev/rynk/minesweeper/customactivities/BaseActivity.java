package dev.rynk.minesweeper.customactivities;

import static dev.rynk.minesweeper.utils.Constants.LOOP_START_INDEX;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewbinding.ViewBinding;

import dev.rynk.minesweeper.R;
import dev.rynk.minesweeper.activities.MainActivity;
import dev.rynk.minesweeper.activities.Play;
import dev.rynk.minesweeper.activities.Rules;
import dev.rynk.minesweeper.activities.Scores;
//import dev.rynk.minesweeper.utils.MenuHandler;

public abstract class BaseActivity<T extends ViewBinding> extends AppCompatActivity {
    private final int[] buttons = {R.id.main_button, R.id.play_button, R.id.rules_button, R.id.scores_button};
    private final Class<?>[] activityClasses = {MainActivity.class, Play.class, Rules.class, Scores.class};
    protected T binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = getViewBinding();
        setContentView(binding.getRoot());
        setupNavButtonTags(binding.getRoot());

    }
    /**
     * adds intent change action to navigational buttons by id.
     * @param root root view of Activity.
     */
    private void setupNavButtonTags(View root) {
        for (int i = LOOP_START_INDEX; i < buttons.length; i++){
            View btn = root.findViewById(buttons[i]);
            Class<?> destination = activityClasses[i];
            if (btn != null) {
                btn.setOnClickListener(v -> {
                    Intent activityChangeIntent = new Intent (v.getContext(), destination);
                    startActivity(activityChangeIntent);
                });
            }
        }
    }
    /**
     * Interface to pass activity binding to parent
     * @return binding for the implementing activity
     */
    protected abstract T getViewBinding();

}
