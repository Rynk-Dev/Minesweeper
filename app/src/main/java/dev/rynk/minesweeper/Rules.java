package dev.rynk.minesweeper;

import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import dev.rynk.minesweeper.customactivities.BaseActivity;
import dev.rynk.minesweeper.databinding.ActivityRulesBinding;
import dev.rynk.minesweeper.utils.MenuHandler;

public class Rules extends BaseActivity<ActivityRulesBinding> {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_rules);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
    @Override
    protected ActivityRulesBinding getViewBinding() {
        return ActivityRulesBinding.inflate(getLayoutInflater());
    }
}