package dev.rynk.minesweeper.utils;

import static dev.rynk.minesweeper.utils.Constants.*;
import java.util.Locale;

public class MathUtils {
    private static final int TICKS_PER_SECOND = 10;
    private static final int TICKS_PER_MINUTE = 600;
    private static final int SECONDS_PER_MINUTE = 60;
    private static final String NO_SCORE = "N/A";
    private static final String TIME_FORMAT = "%02d:%02d.%01d";

    /**
     * Takes integer representing game time ticks and returns as a formatted string, or "N/A" if no score
     * exists.
     * @param ticks integer where 1 == 100ms.
     * @return String containing ticks formatted to 11:11.1. If ticks == SCORE_NOT_FOUND
     * (i.e., Integer.MAX_VALUE), returns NO_SCORE ("N/A").
     */
    static public String ticksToTime(int ticks) {
        if (ticks == SCORE_NOT_FOUND) {
            return (NO_SCORE);
        }
        int seconds = (ticks / TICKS_PER_SECOND) % SECONDS_PER_MINUTE;
        int minutes = ticks / TICKS_PER_MINUTE;
        return String.format(Locale.CANADA, TIME_FORMAT, minutes, seconds, ticks % TICKS_PER_SECOND);
    }
}
