package com.udacity.gamedev.gigagal.entity;

import com.badlogic.gdx.utils.Array;

public interface Stackable {
    boolean isBeneatheGround();
    Ground getTopGround();
}
