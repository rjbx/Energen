package com.udacity.gamedev.gigagal.entity;

import com.udacity.gamedev.gigagal.util.Enums;

public interface Roving extends Moving {

    Enums.Direction getDirectionX();
    void setDirectionX(Enums.Direction direction);
}
