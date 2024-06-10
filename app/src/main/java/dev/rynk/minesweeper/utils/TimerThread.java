package dev.rynk.minesweeper.utils;

import static dev.rynk.minesweeper.utils.Constants.*;
import static dev.rynk.minesweeper.utils.MathUtils.ticksToTime;

import android.app.Activity;
import android.util.Log;
import android.widget.TextView;
import java.util.Timer;
import java.util.TimerTask;

/**
 * 10ms timer that keeps track of elapsed time.
 */
public class TimerThread {
    public final int START_TIME = 0;
    public final int TIMER_DELAY = 0;
    public final int TIMER_INTERVAL = 100;
    public Timer timer;
    private int ticks;

    /**
     * Constructor initializes elapsed time to 0
     */
    public TimerThread(){
        ticks = START_TIME;
    }

    /**
     * Starts a 100 ms interval timer from the value of ticks
     * and updates a given textview with the elapsed time.
     * @param activity Activity to run timer on
     * @param clockView TextView object to display the time on.
     */
    public void startTimer(Activity activity, TextView clockView){
        timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ticks++;
                        clockView.setText(ticksToTime(ticks));
                    }
                });
            }
        }, TIMER_DELAY, TIMER_INTERVAL);
    }

    /**
     * Getter for elapsed time in 100ms ticks.
     * @return int representing number of 100ms since start of timer.
     */
    public int getTicks(){
        return ticks;
    }

    /**
     * pauses timer
     */
    public void pauseTimer(){
            timer.cancel();
    }
    /**
     * clears timer
     */
    public void clearTimer() {
            ticks = START_TIME;
    }


}
