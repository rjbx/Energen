package com.udacity.gamedev.gigagal.entities;

import com.badlogic.gdx.math.Vector2;

public interface Physical {

    Vector2 getPosition();
    float getWidth();
    float getHeight();
    float getLeft();
    float getRight();
    float getTop();
    float getBottom();
}
