//package dev.rynk.minesweeper.utils;
//
//import static androidx.core.content.ContextCompat.startActivity;
//
//import android.app.Activity;
//import android.content.Intent;
//import android.view.View;
//
//import dev.rynk.minesweeper.activities.Endscreen;
//import dev.rynk.minesweeper.activities.Play;
//import dev.rynk.minesweeper.activities.MainActivity;
//import dev.rynk.minesweeper.activities.Rules;
//import dev.rynk.minesweeper.activities.Scores;
//
///**
// * handles menu and nav button actions.
// */
//public class MenuHandler {
//    private static final String RULES_BUTTON_TAG = "rules";
//    private static final String ENDSCREEN_BUTTON_TAG = "endscreen";
//    private static final String PLAY_BUTTON_TAG = "play";
//    private static final String MAIN_BUTTON_TAG = "main";
//    private static final String SCORES_BUTTON_TAG = "scores";
//    private static final String CLOSE_BUTTON_TAG = "close";
//
//    /**
//     * converts string values to Activity Class objects.
//     * @param target String representing Activity Class to convert to
//     * @return Activity Class object
//     */
//    static private Class<?> stringToClass(String target){
//        Class<?> destination = null;
//        switch (target){
//            case RULES_BUTTON_TAG:
//                destination = Rules.class;
//                break;
//            case ENDSCREEN_BUTTON_TAG:
//                destination = Endscreen.class;
//                break;
//            case PLAY_BUTTON_TAG:
//                destination = Play.class;
//                break;
//            case MAIN_BUTTON_TAG:
//                destination = MainActivity.class;
//                break;
//            case SCORES_BUTTON_TAG:
//                destination = Scores.class;
//                break;
//        }
//        return destination;
//    }
//    /**
//     * onclick listener for navigation buttons in derived activities.
//     * Creates and triggers activityChangeIntent
//     * @param v View that was clicked
//     * @param activity Activity that the clicked view belonged to.
//     */
//    static public void menu_click(View v, Activity activity) {
//        // destination activity is stored in view tags.
//        String target = v.getTag().toString();
//        if (target.equals(CLOSE_BUTTON_TAG)) activity.onBackPressed();
//        else {
//            Intent activityChangeIntent = new Intent (v.getContext(), stringToClass(target));
//            startActivity(v.getContext(), activityChangeIntent, null);
//        }
//    }
//}
//
