package com.github.rjbx.energage.entity;

import com.badlogic.gdx.utils.Array;

public interface Stackable {
    boolean isBeneatheGround();
    Ground getTopGround();
}
