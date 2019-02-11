package com.github.rjbx.energraft.entity;

public interface Compressible extends Stackable{

    void setState(boolean state);
    boolean getState();
    long getStartTime();
    void resetStartTime();
}
