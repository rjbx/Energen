package com.udacity.gamedev.gigagal.entities;

import com.udacity.gamedev.gigagal.util.Enums;

public interface Directional {
    Enums.Direction getFacing();
    void setFacing(Enums.Direction direction);
}
