package com.github.rjbx.energage.entity;

public interface Chargeable extends Physical {

    void setState(boolean state);
    void charge();
    void uncharge();
    void setChargeTime(float seconds);
    boolean isCharged();
    boolean isActive();
}