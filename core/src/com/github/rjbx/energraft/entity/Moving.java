package com.github.rjbx.energen.entity;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

public interface Moving extends Physical, Nonstatic {
    Vector2 getVelocity();
}
