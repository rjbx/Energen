package com.github.rjbx.energraft.entity;

import com.badlogic.gdx.math.Vector2;
import com.github.rjbx.energraft.util.Enums;

public interface Hazardous extends Physical {
    int getDamage();
    Vector2 getKnockback();
    Enums.Material getType();
}
