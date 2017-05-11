package com.udacity.gamedev.gigagal.entity;

public interface Switchable extends Nonstatic {

    void resetStartTime();
    void setState(boolean state);
    boolean getState();
}
