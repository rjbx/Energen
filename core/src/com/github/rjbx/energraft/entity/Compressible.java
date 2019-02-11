package com.udacity.gamedev.gigagal.entity;

public interface Compressible extends Stackable{

    void setState(boolean state);
    boolean getState();
    long getStartTime();
    void resetStartTime();
}
