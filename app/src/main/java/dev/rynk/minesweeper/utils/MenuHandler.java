package dev.rynk.minesweeper.utils;

import static androidx.core.content.ContextCompat.startActivity;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.View;

import dev.rynk.minesweeper.Endscreen;
import dev.rynk.minesweeper.Play;
import dev.rynk.minesweeper.MainActivity;
import dev.rynk.minesweeper.Rules;
import dev.rynk.minesweeper.Scores;

public class MenuHandler {
    static private Class<?> stringToClass(String target){
        Class<?> destination = null;
        switch (target){
            case "rules":
                destination = Rules.class;
                break;
            case "endscreen":
                destination = Endscreen.class;
                break;
            case "play":
                destination = Play.class;
                break;
            case "main":
                destination = MainActivity.class;
                break;
            case "scores":
                destination = Scores.class;
                break;
        }
        return destination;
    }
    static public void menu_click(View v, Activity activity) {
        String target = v.getTag().toString();
        Log.d("ryan", "menu button clicked");
        if (target.equals("close")) activity.onBackPressed();
        else {
            Intent activityChangeIntent = new Intent (v.getContext(), stringToClass(target));
            startActivity(v.getContext(), activityChangeIntent, null);
        }
    }
}

