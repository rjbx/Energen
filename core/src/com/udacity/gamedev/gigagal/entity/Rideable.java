package com.udacity.gamedev.gigagal.entity;

import com.udacity.gamedev.gigagal.util.Enums;

public interface Rideable extends Solid {
    Enums.Direction getDirection();
}
