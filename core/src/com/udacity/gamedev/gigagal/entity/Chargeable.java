package com.udacity.gamedev.gigagal.entity;

public interface Chargeable {

    void activate();
    void deactivate();
    void charge();
    void uncharge();
    void charge(float seconds);
    boolean isActive();
}
