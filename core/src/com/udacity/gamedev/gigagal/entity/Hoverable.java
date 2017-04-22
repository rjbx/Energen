package com.udacity.gamedev.gigagal.entity;

import com.badlogic.gdx.math.Vector2;
import com.udacity.gamedev.gigagal.util.Enums;

public interface Hoverable extends Entity {

    void update(float delta);
    Enums.Direction getDirection();
    Enums.Orientation getOrientation();
    Vector2 getVelocity();
}