package dev.rynk.minesweeper.activities;

import android.os.Bundle;
import android.view.View;

import dev.rynk.minesweeper.customactivities.BaseActivity;
import dev.rynk.minesweeper.databinding.ActivityRulesBinding;

public class Rules extends BaseActivity<ActivityRulesBinding> {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding.closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }
    /**
     * implements interface to pass activity binding to parent
     * @return binding for the rules activity
     */
    @Override
    protected ActivityRulesBinding getViewBinding() {
        return ActivityRulesBinding.inflate(getLayoutInflater());
    }
}