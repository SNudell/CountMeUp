package utils;

import android.os.Handler;

import networking.Task;

public class Repeater {

    private Task task;
    private int delay;
    private Handler handler;
    private Runnable runnable;

    public Repeater(Task t, int delay) {
        this.task = t;
        this.delay = delay;
        handler = new Handler();
        this.runnable = new Runnable(){
            public void run(){
                task.execute();
                handler.postDelayed(this, delay);
            }
        };
    }

    public void start() {
        handler.postDelayed(runnable, delay);
    }

    public void terminate() {
        if (handler != null) {
            handler.removeCallbacks(runnable);
        }
    }
}
