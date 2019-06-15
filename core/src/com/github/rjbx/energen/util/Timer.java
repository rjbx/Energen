package com.github.rjbx.energen.util;

import com.badlogic.gdx.utils.TimeUtils;

import static com.github.rjbx.energen.util.Enums.TimerState.RUNNING;
import static com.github.rjbx.energen.util.Enums.TimerState.STOPPED;
import static com.github.rjbx.energen.util.Enums.TimerState.SUSPENDED;
import static com.github.rjbx.energen.util.Enums.TimerState.UNSTARTED;

// An implementation of apache.commons.lang3.time.StopWatch that eliminates unused methods and permits start time offset with additional start method
public class Timer {

    // fields
    public static final String TAG = Timer.class.getName();
    private static final Timer INSTANCE = new Timer();
    private Enums.TimerState state;
    private long startTime;
    private long stopTime;

    private Timer() {}

    public static Timer getInstance() {
        return INSTANCE;
    }

    public void create() {
        state = UNSTARTED;
    }

    public Timer start() {
        if (state == STOPPED) {
            throw new IllegalStateException("Cannot reset timer before restart.");
        } else if (state != UNSTARTED) {
            throw new IllegalStateException("Cannot start timer twice.");
        } else {
            startTime = TimeUtils.nanoTime();
            state = RUNNING;
        }
        return this;
    }

    public Timer start(long offsetTime) {
        if (state == STOPPED) {
            throw new IllegalStateException("Cannot reset timer before restart.");
        } else if (state != UNSTARTED) {
            throw new IllegalStateException("Cannot start timer twice.");
        } else {
            startTime = TimeUtils.nanoTime() - offsetTime;
            state = RUNNING;
        }
        return this;
    }

    public Timer stop() {
        if (state != RUNNING && state != SUSPENDED) {
            throw new IllegalStateException("Cannot stop timer that is neither running nor suspended.");
        } else {
            if (state == RUNNING) {
                stopTime = TimeUtils.nanoTime();
            }
            state = STOPPED;
        }
        return this;
    }

    public Timer suspend() {
        if (state != RUNNING) {
            throw new IllegalStateException("Cannot suspend timer that is not running.");
        } else {
            stopTime = TimeUtils.nanoTime();
            state = SUSPENDED;
        }
        return this;
    }

    public Timer resume() {
        if (state != SUSPENDED) {
            throw new IllegalStateException("Cannot resume timer that is not suspended.");
        } else {
            startTime += TimeUtils.nanoTime() - stopTime;
            state = RUNNING;
        }
        return this;
    }

    public Timer reset() {
        state = UNSTARTED;
        return this;
    }

    public long getMillis() {
        return TimeUtils.nanosToMillis(getNanos());
    }

    public long getNanos() {
        if (state != STOPPED && state != SUSPENDED) {
            if (state == UNSTARTED) {
                return 0L;
            } else if (state == RUNNING) {
                return TimeUtils.nanoTime() - startTime;
            }
        }
        return stopTime - startTime;
    }

    public String toString() {
        return Helpers.millisToString(getMillis());
    }
    public Enums.TimerState getState() { return state; }
}

