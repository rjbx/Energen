package com.udacity.gamedev.gigagal.entity;

public interface Reboundable extends Entity {
    void setState(boolean state);
    boolean getState();
    long getStartTime();
    void resetStartTime();
    void update();
}
