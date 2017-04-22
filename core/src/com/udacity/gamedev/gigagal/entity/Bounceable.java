package com.udacity.gamedev.gigagal.entity;

public interface Bounceable extends Entity {
    void setLoaded(boolean state);
    long getStartTime();
    void resetStartTime();
}
