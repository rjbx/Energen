package com.udacity.gamedev.gigagal.entity;

public interface BounceableGround extends Ground {
    void setLoaded(boolean state);
    long getStartTime();
    void resetStartTime();
}
