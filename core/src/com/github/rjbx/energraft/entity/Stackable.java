package com.github.rjbx.energen.entity;

import com.badlogic.gdx.utils.Array;

public interface Stackable {
    boolean isBeneatheGround();
    Ground getTopGround();
}
