package com.udacity.gamedev.gigagal.entities;

import com.badlogic.gdx.math.Rectangle;
import com.udacity.gamedev.gigagal.util.Enums;

public interface Humanoid extends Entity {

    Enums.Direction getFacing();
    float getTurbo();
    int getHealth();
    Rectangle getBounds();
    boolean getJumpStatus();
    boolean getHoverStatus();
    boolean getRicochetStatus();
    boolean getDashStatus();
    boolean getClimbStatus();
    Enums.AmmoIntensity getAmmoIntensity();
    Enums.WeaponType getWeapon();
}
