package com.udacity.gamedev.gigagal.entities;

import com.udacity.gamedev.gigagal.util.Enums;

public interface Moving {

    void update(float delta);
    Enums.Direction getDirection();
}
