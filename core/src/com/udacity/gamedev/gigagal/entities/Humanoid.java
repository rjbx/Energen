package com.udacity.gamedev.gigagal.entities;

import com.badlogic.gdx.math.Rectangle;
import com.udacity.gamedev.gigagal.util.Enums;

public interface Humanoid extends MultidirectionalX, MultidirectionalY {

    float getTurbo();
    int getHealth();
    Rectangle getBounds();
    boolean getJumpStatus();
    boolean getHoverStatus();
    boolean getClingStatus();
    boolean getDashStatus();
    boolean getClimbStatus();
    Enums.AmmoIntensity getAmmoIntensity();
    Enums.WeaponType getWeapon();
}
