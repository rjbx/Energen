package com.github.rjbx.energen.entity;

import com.badlogic.gdx.math.Vector2;
import com.github.rjbx.energen.util.Enums;

public interface Hazardous extends Physical {
    int getDamage();
    Vector2 getKnockback();
    Enums.Energy getType();
}
