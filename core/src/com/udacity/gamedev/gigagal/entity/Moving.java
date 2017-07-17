package com.udacity.gamedev.gigagal.entity;

import com.badlogic.gdx.math.Vector2;

public interface Moving extends Physical, Nonstatic {
    Vector2 getVelocity();
}
