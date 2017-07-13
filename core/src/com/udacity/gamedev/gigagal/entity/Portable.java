package com.udacity.gamedev.gigagal.entity;

import com.badlogic.gdx.math.Vector2;

public interface Portable extends Nonstatic {
    void setPosition(Vector2 position);
    Entity getCarrier();
    void setCarrier(Entity entity);
    boolean isBeingCarried();
}
