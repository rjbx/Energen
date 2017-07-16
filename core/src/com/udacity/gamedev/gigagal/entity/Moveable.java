package com.udacity.gamedev.gigagal.entity;

import com.badlogic.gdx.math.Vector2;

public interface Moveable extends Nonstatic, Physical {
    void setPosition(Vector2 position);
    Entity getCarrier();
    void setCarrier(Entity entity);
    boolean isBeingCarried();
    boolean isAtopMovingGround();
    Groundable getMovingGround();
    float weightFactor();
}
