package com.udacity.gamedev.gigagal.entities;

public interface BounceableGround extends Ground {
    void setLoaded(boolean state);
    long getStartTime();
    void resetStartTime();
}
