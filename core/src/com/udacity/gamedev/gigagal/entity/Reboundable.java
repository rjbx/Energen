package com.udacity.gamedev.gigagal.entity;

public interface Reboundable extends Nonstatic {

    void setState(boolean state);
    boolean getState();
    long getStartTime();
    void resetStartTime();
    float jumpMultiplier();
}
