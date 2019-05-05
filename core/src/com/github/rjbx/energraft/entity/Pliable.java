package com.github.rjbx.energage.entity;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

public interface Pliable extends Moving, Stackable {
    void setPosition(Vector2 position);
    Vector2 getVelocity();
    Humanoid getCarrier();
    void setCarrier(Humanoid entity);
    boolean isBeingCarried();
    boolean isAtopMovingGround();
    Moving getMovingGround();
    void setMovingGround(Moving ground);
    float weightFactor();
    boolean isAgainstStaticGround();
    void setAgainstStaticGround();
    void setVelocity(Vector2 velocity);
    void stopCarrying();
    boolean isBeneatheGround();
    Ground getTopGround();
}
