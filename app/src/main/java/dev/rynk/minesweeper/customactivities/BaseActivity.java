package dev.rynk.minesweeper.customactivities;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewbinding.ViewBinding;

import dev.rynk.minesweeper.utils.MenuHandler;

public abstract class BaseActivity<T extends ViewBinding> extends AppCompatActivity {
    protected T binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = getViewBinding();
        setContentView(binding.getRoot());
    }
    protected abstract T getViewBinding();
    public void menu_click(View v){
        MenuHandler.menu_click(v, this);
    }
}
