package com.github.rjbx.energen.entity;

import com.badlogic.gdx.math.Rectangle;

public interface Trippable extends Nonstatic, Physical, Convertible {

    void setState(boolean state);
    boolean tripped();
    boolean isActive();
    void addCamAdjustment();
    boolean maxAdjustmentsReached();
    Rectangle getConvertBounds();
}
