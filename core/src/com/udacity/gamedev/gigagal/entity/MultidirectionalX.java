package com.udacity.gamedev.gigagal.entity;

import com.udacity.gamedev.gigagal.util.Enums;

public interface MultidirectionalX extends Multidirectional {
    Enums.Direction getDirectionX();
    void setDirectionX(Enums.Direction direction);
}
