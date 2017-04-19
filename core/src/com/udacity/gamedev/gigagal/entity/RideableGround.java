package com.udacity.gamedev.gigagal.entity;

import com.udacity.gamedev.gigagal.util.Enums;

public interface RideableGround extends SolidGround {
    Enums.Direction getDirection();
}
