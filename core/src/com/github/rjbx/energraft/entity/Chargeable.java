package com.github.rjbx.energraft.entity;

public interface Chargeable extends Physical {

    void setState(boolean state);
    void charge();
    void uncharge();
    void setChargeTime(float seconds);
    boolean isCharged();
    boolean isActive();
}