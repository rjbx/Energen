package com.udacity.gamedev.gigagal.entities;

import com.udacity.gamedev.gigagal.util.Enums;

public interface RideableGround extends Ground {
    Enums.Direction getDirection();
}
