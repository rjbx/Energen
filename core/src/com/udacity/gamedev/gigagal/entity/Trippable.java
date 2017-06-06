package com.udacity.gamedev.gigagal.entity;

public interface Trippable extends Nonstatic {

    void resetStartTime();
    void setState(boolean state);
    boolean getState();
}
