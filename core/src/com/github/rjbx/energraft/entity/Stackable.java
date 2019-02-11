package com.github.rjbx.energraft.entity;

import com.badlogic.gdx.utils.Array;

public interface Stackable {
    boolean isBeneatheGround();
    Ground getTopGround();
}
