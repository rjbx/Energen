package com.udacity.gamedev.gigagal.entity;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.udacity.gamedev.gigagal.util.Enums;

public interface Humanoid extends MultidirectionalX, MultidirectionalY {

    Vector2 getVelocity();
    Rectangle getBounds();
    float getTurbo();
    float getHealth();
    boolean getJumpStatus();
    boolean getHoverStatus();
    boolean getClingStatus();
    boolean getDashStatus();
    boolean getClimbStatus();
    Enums.GroundState getGroundState();
    Enums.Action getAction();
    Enums.ShotIntensity getShotIntensity();
    Enums.Material getWeapon();
}
