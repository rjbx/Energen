package com.udacity.gamedev.gigagal.util;

import com.badlogic.gdx.utils.TimeUtils;

import org.apache.commons.lang3.time.DurationFormatUtils;

// An implementation of apache.commons.lang3.time.StopWatch that eliminates unused methods and permits start time offset with additional start method
public class Timer {

    // fields
    public static final String TAG = Timer.class.getName();
    private static final Timer INSTANCE = new Timer();
    private State state;
    private long startTime;
    private long stopTime;

    private Timer() {
        state = State.UNSTARTED;
    }

    public static Timer getInstance() {
        return INSTANCE;
    }

    public Timer start() {
        if (state == State.STOPPED) {
            throw new IllegalStateException("Cannot reset timer before restart.");
        } else if (state != State.UNSTARTED) {
            throw new IllegalStateException("Cannot start timer twice.");
        } else {
            startTime = TimeUtils.nanoTime();
            state = State.RUNNING;
        }
        return this;
    }

    public Timer start(long offsetTime) {
        if (state == State.STOPPED) {
            throw new IllegalStateException("Cannot reset timer before restart.");
        } else if (state != State.UNSTARTED) {
            throw new IllegalStateException("Cannot start timer twice.");
        } else {
            startTime = TimeUtils.nanoTime() - offsetTime;
            state = State.RUNNING;
        }
        return this;
    }

    public Timer stop() {
        if (state != State.RUNNING && state != State.SUSPENDED) {
            throw new IllegalStateException("Cannot stop timer that is neither running nor suspended.");
        } else {
            if (state == State.RUNNING) {
                stopTime = TimeUtils.nanoTime();
            }
            state = State.STOPPED;
        }
        return this;
    }

    public Timer suspend() {
        if (state != State.RUNNING) {
            throw new IllegalStateException("Cannot suspend timer that is not running.");
        } else {
            stopTime = TimeUtils.nanoTime();
            state = State.SUSPENDED;
        }
        return this;
    }

    public Timer resume() {
        if (state != State.SUSPENDED) {
            throw new IllegalStateException("Cannot resume timer that is not suspended.");
        } else {
            startTime += TimeUtils.nanoTime() - stopTime;
            state = State.RUNNING;
        }
        return this;
    }

    public Timer reset() {
        state = State.UNSTARTED;
        return this;
    }

    public long getTime() {
        return TimeUtils.nanosToMillis(getNanoTime());
    }

    public long getNanoTime() {
        if (state != State.STOPPED && state != State.SUSPENDED) {
            if (state == State.UNSTARTED) {
                return 0L;
            } else if (state == State.RUNNING) {
                return TimeUtils.nanoTime() - startTime;
            }
        }
        return stopTime - startTime;
    }

    public String toString() {
        return DurationFormatUtils.formatDurationHMS(getTime());
    }

    private enum State {
        UNSTARTED,
        RUNNING,
        STOPPED,
        SUSPENDED
    }
}

