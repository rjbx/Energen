package com.udacity.gamedev.gigagal.entities;

import com.udacity.gamedev.gigagal.util.Enums;

public interface HoveringGround extends Ground {

    void update(float delta);
    Enums.Direction getDirection();
}