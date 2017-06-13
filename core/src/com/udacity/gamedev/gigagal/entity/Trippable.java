package com.udacity.gamedev.gigagal.entity;

import com.badlogic.gdx.math.Rectangle;

public interface Trippable extends Nonstatic {

    void setState(boolean state);
    boolean tripped();
    boolean isActive();
    boolean hasSetCam();
    Rectangle getBounds();
}
