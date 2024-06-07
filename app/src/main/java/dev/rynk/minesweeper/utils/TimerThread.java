package dev.rynk.minesweeper.utils;

import static dev.rynk.minesweeper.utils.Constants.*;
import static dev.rynk.minesweeper.utils.MathUtils.ticksToTime;

import android.app.Activity;
import android.widget.TextView;
import java.util.Timer;
import java.util.TimerTask;

public class TimerThread {
    public Timer timer;
    private boolean isTimerRunning = false;
    private int ticks;
    public TimerThread(){
        ticks = START_TIME;
    }
    public void startTimer(Activity activity, TextView clockView){
        timer = new Timer();
        if (!isTimerRunning) {
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
        isTimerRunning = true;
    }
    public int getTicks(){
        return ticks;
    }
    public boolean getIsTimerRunning(){
        return isTimerRunning;
    }
    public void setIsTimerRunning(boolean state){
        isTimerRunning = state;
    }
    public void pauseTimer(){
            timer.cancel();
            isTimerRunning = false;
    }
    public void clearTimer() {
            timer.purge();
            ticks = START_TIME;
    }
}
