package com.udacity.gamedev.gigagal.entity;

import com.udacity.gamedev.gigagal.util.Enums;

public interface Roveable extends Moveable {

    Enums.Direction getDirectionX();
    void setDirectionX(Enums.Direction direction);
}
