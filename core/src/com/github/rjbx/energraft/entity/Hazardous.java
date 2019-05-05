package com.github.rjbx.energage.entity;

import com.badlogic.gdx.math.Vector2;
import com.github.rjbx.energage.util.Enums;

public interface Hazardous extends Physical {
    int getDamage();
    Vector2 getKnockback();
    Enums.Material getType();
}
