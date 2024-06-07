package dev.rynk.minesweeper.utils;

import static dev.rynk.minesweeper.utils.Constants.*;

import java.util.Locale;

public class MathUtils {

    static public String ticksToTime(int ticks){
        if (ticks == Integer.MAX_VALUE){
            return (NO_SCORE);
        }
        int seconds = (ticks / TICKS_PER_SECOND) % SECONDS_PER_MINUTE;
        int minutes = ticks / TICKS_PER_MINUTE;
        return String.format(Locale.CANADA, TIME_FORMAT, minutes,seconds, ticks % TICKS_PER_SECOND);
    }
}
