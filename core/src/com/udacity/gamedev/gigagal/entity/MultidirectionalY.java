package com.udacity.gamedev.gigagal.entity;

import com.udacity.gamedev.gigagal.util.Enums;

public interface MultidirectionalY extends Multidirectional {
    Enums.Direction getDirectionY();
    void setDirectionY(Enums.Direction direction);
}
