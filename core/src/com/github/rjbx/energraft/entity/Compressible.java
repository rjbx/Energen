package com.github.rjbx.energen.entity;

public interface Compressible extends Stackable{

    void setState(boolean state);
    boolean getState();
    long getStartTime();
    void resetStartTime();
}
