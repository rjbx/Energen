package com.udacity.gamedev.gigagal.entity;

import com.badlogic.gdx.utils.Array;
import com.udacity.gamedev.gigagal.util.Enums;

public interface Bladed {
    Array<Enums.Direction> getEquippedRegions();
}
