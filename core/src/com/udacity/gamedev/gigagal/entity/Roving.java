package com.udacity.gamedev.gigagal.entity;

import com.badlogic.gdx.math.Vector2;
import com.udacity.gamedev.gigagal.util.Enums;

public interface Roving extends Nonstatic {

    Enums.Direction getDirectionX();
    void setDirectionX(Enums.Direction direction);
    Vector2 getVelocityX();
}
