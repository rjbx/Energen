package com.udacity.gamedev.gigagal.entity;

import com.badlogic.gdx.math.Rectangle;

public interface Trippable extends Nonstatic, Convertible {

    void setState(boolean state);
    boolean tripped();
    boolean isActive();
    void addCamAdjustment();
    boolean maxAdjustmentsReached();
    Rectangle getBounds();
}
