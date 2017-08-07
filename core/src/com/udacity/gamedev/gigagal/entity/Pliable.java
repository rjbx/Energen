package com.udacity.gamedev.gigagal.entity;

import com.badlogic.gdx.math.Vector2;

public interface Pliable extends Moving {
    void setPosition(Vector2 position);
    Vector2 getVelocity();
    Dynamic getCarrier();
    void setCarrier(Dynamic entity);
    boolean isBeingCarried();
    boolean isAtopMovingGround();
    Moving getMovingGround();
    void setMovingGround(Moving ground);
    float weightFactor();
    boolean isAgainstStaticGround();
    void setAgainstStaticGround();
    void setVelocity(Vector2 velocity);
    void stopCarrying();
}
