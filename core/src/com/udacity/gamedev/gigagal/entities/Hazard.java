package com.udacity.gamedev.gigagal.entities;


import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.udacity.gamedev.gigagal.util.Enums;

public interface Hazard extends Entity {

    int getDamage();
    Vector2 getKnockback();
    Enums.WeaponType getType();
}
