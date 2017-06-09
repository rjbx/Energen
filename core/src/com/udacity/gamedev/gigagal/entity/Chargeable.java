package com.udacity.gamedev.gigagal.entity;

public interface Chargeable {

    void setState(boolean state);
    void charge();
    void uncharge();
    void charge(float seconds);
    boolean isActive();
}
