package com.shwy.bestjoy.utils;

/**
 * Created by bestjoy on 16/3/9.
 */
import android.os.Handler;
import android.os.Looper;

public class HandlerTimer implements Runnable {
    private Handler handler;
    private long interval;
    private TimerStatus status;
    private Runnable task;

    public HandlerTimer(long interval, Runnable task) {
        this(interval, task, new Handler(Looper.getMainLooper()));
    }

    public HandlerTimer(long interval, Runnable task, Handler handler) {
        if ((handler == null) || (task == null))
            throw new NullPointerException("handler or task must not be null");
        this.interval = interval;
        this.task = task;
        this.handler = handler;
        this.status = TimerStatus.Waiting;
    }

    public HandlerTimer(Runnable task) {
        this(1000L, task);
    }

    public void cancel() {
        this.status = TimerStatus.Stopped;
        this.handler.removeCallbacks(this);
    }

    public void pause() {
        this.status = TimerStatus.Paused;
        this.handler.removeCallbacks(this);
    }

    public void restart() {
        this.handler.removeCallbacks(this);
        this.status = TimerStatus.Running;
        this.handler.postDelayed(this, this.interval);
    }

    public final void run() {
        if ((this.status == TimerStatus.Waiting)
                || (this.status == TimerStatus.Paused)
                || (this.status == TimerStatus.Stopped)) {
            return;
        }

        this.task.run();
        this.handler.postDelayed(this, this.interval);
    }

    public void start() {
        if(this.status != HandlerTimer.TimerStatus.Running) {
            this.handler.removeCallbacks(this);
            this.status = TimerStatus.Running;
            this.handler.postDelayed(this, this.interval);
        }
    }

    public void start(long interval) {
        if(this.status != HandlerTimer.TimerStatus.Running) {
            this.handler.removeCallbacks(this);
            this.status = TimerStatus.Running;
            this.handler.postDelayed(this, interval);
        }
    }

    public void stop() {
        this.status = TimerStatus.Stopped;
        this.handler.removeCallbacks(this);
    }

    static enum TimerStatus {
        Waiting("Waiting", 0, "等待"),
        Running("Running", 1,  "运行中"),
        Paused("Paused", 2, "暂停"),
        Stopped("Stopped", 3, "停止");

        private int code;
        private String desc;
        private String desc_en;
        private TimerStatus(String desc_en, int code, String desc) {
            this.code = code;
            this.desc = desc;
            this.desc_en = desc_en;
        }

        public int getCode() {
            return this.code;
        }

        public String getDesc() {
            return this.desc;
        }

        public void setCode(int code) {
            this.code = code;
        }

        public void setDesc(String desc) {
            this.desc = desc;
        }
    }
}
