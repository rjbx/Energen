package com.udacity.gamedev.gigagal.entity;

public interface Reboundable extends Entity {
    void setLoaded(boolean state);
    long getStartTime();
    void resetStartTime();
}
