package com.udacity.gamedev.gigagal.entity;

public interface Switchable {

    void update();
    void resetStartTime();
    void setState(boolean state);
    boolean getState();
}
