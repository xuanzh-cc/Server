package com.zxcc.engine.test;

import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;

/**
 * Created by xuanzh.cc on 2016/10/10.
 */

public abstract class Task implements Delayed {

    /** 任务ID */
    private int id;

    /** 延迟执行的毫秒数 */
    private long delayTime;

    private int actionCount = 1;

    public Task(int id, long delayTime, int actionCount) {
        this.id = id;
        this.delayTime = delayTime < 0 ? 0 : delayTime;
        if(actionCount == -1) {
            this.actionCount = -1;
        } else {
            this.actionCount = actionCount < 1 ? 1 : actionCount;
        }
    }

    @Override
    public long getDelay(TimeUnit unit) {
        switch (unit) {
            case MILLISECONDS:
                return delayTime;
            case SECONDS:
                return TimeUnit.MILLISECONDS.toSeconds(delayTime);
            case MINUTES:
                return TimeUnit.MILLISECONDS.toMinutes(delayTime);
            case HOURS:
                return TimeUnit.MILLISECONDS.toHours(delayTime);
            case DAYS:
                return TimeUnit.MILLISECONDS.toDays(delayTime);
            case MICROSECONDS:
                return TimeUnit.MILLISECONDS.toMicros(delayTime);
            case NANOSECONDS:
                return TimeUnit.MILLISECONDS.toNanos(delayTime);
            default:
                return delayTime;
        }
    }

    @Override
    public int compareTo(Delayed o) {
        long comp = this.getDelay(TimeUnit.MILLISECONDS) - o.getDelay(TimeUnit.MILLISECONDS);
        return comp > 0 ? 1 : -1;
    }

    public void action(){
        if (this.actionCount > 0) {
            this.actionCount--;
        }
        this.doAction();
    }

    protected abstract void doAction();

    public int getId() {
        return id;
    }

    public long getDelayTime() {
        return delayTime;
    }

    public int getActionCount() {
        return actionCount;
    }

    public boolean canNextAction(){
        return this.actionCount == -1 || this.actionCount > 0;
    }
}
