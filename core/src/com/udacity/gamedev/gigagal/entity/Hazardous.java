package com.udacity.gamedev.gigagal.entity;

import com.badlogic.gdx.math.Vector2;
import com.udacity.gamedev.gigagal.util.Enums;

public interface Hazardous {
    public int getDamage();
    public Vector2 getKnockback();
    public Enums.Material getType();
}
