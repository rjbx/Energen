package com.github.rjbx.energen.entity;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.github.rjbx.energen.util.Enums;

public interface Humanoid extends Dynamic {

    Vector2 getVelocity();
    Rectangle getBounds();
    float getTurbo();
    float getHealth();
    boolean getJumpStatus();
    boolean getHoverStatus();
    boolean getRappelStatus();
    boolean getDashStatus();
    boolean getClimbStatus();
    boolean getDispatchStatus();
    Enums.GroundState getGroundState();
    Enums.Action getAction();
    Enums.ShotIntensity getShotIntensity();
    Enums.Energy getEnergy();
    Groundable getTouchedGround();
}
