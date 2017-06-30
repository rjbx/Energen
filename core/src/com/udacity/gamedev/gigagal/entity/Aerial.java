package com.udacity.gamedev.gigagal.entity;

import com.badlogic.gdx.math.Vector2;
import com.udacity.gamedev.gigagal.util.Enums;

public interface Aerial extends Nonstatic {

    Enums.Direction getDirectionY();
    void setDirectionY(Enums.Direction direction);
    Vector2 getVelocityY();
}
