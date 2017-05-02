package com.udacity.gamedev.gigagal.entity;

public interface Chargeable extends Solid {

    void activate();
    void deactivate();
    void charge(float seconds);
    boolean isActive();
}
