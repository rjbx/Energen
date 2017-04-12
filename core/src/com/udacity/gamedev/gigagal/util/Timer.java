package com.udacity.gamedev.gigagal.util;

import com.badlogic.gdx.utils.TimeUtils;

import org.apache.commons.lang3.time.DurationFormatUtils;

// An implementation of apache.commons.lang3.time.StopWatch that eliminates unused methods and permits start time offset with additional start method
public class Timer {
    private State state;
    private long startTime;
    private long stopTime;

    public Timer() {
        state = State.UNSTARTED;
    }

    public void start() {
        if (state == State.STOPPED) {
            throw new IllegalStateException("Cannot reset timer before restart.");
        } else if (state != State.UNSTARTED) {
            throw new IllegalStateException("Cannot start timer twice.");
        } else {
            startTime = System.nanoTime();
            state = State.RUNNING;
        }
    }

    public void start(long offsetTime) {
        if (state == State.STOPPED) {
            throw new IllegalStateException("Cannot reset timer before restart.");
        } else if (state != State.UNSTARTED) {
            throw new IllegalStateException("Cannot start timer twice.");
        } else {
            startTime = System.nanoTime() - offsetTime;
            state = State.RUNNING;
        }
    }

    public void stop() {
        if (state != State.RUNNING && state != State.SUSPENDED) {
            throw new IllegalStateException("Cannot stop timer that is neither running nor suspended.");
        } else {
            if (state == State.RUNNING) {
                stopTime = System.nanoTime();
            }
            state = State.STOPPED;
        }
    }

    public void suspend() {
        if (state != State.RUNNING) {
            throw new IllegalStateException("Cannot suspend timer that is not running.");
        } else {
            stopTime = System.nanoTime();
            state = State.SUSPENDED;
        }
    }

    public void resume() {
        if (state != State.SUSPENDED) {
            throw new IllegalStateException("Cannot resume timer that is not suspended.");
        } else {
            startTime += System.nanoTime() - stopTime;
            state = State.RUNNING;
        }
    }

    public void reset() {
        state = State.UNSTARTED;
    }

    public long getTime() {
        return TimeUtils.nanosToMillis(getNanoTime());
    }

    public long getNanoTime() {
        if (state != State.STOPPED && state != State.SUSPENDED) {
            if (state == State.UNSTARTED) {
                return 0L;
            } else if (state == State.RUNNING) {
                return System.nanoTime() - startTime;
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

