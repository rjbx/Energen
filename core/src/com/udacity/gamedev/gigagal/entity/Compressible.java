package com.udacity.gamedev.gigagal.entity;

public interface Compressible {

    boolean isUnderneatheGround();
    Ground getTopGround();
    void setState(boolean state);
    boolean getState();
    long getStartTime();
    void resetStartTime();
}
