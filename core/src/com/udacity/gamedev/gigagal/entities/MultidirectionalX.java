package com.udacity.gamedev.gigagal.entities;

import com.udacity.gamedev.gigagal.util.Enums;

public interface MultidirectionalX extends Multidirectional {
    Enums.Direction getMoveDirectionX();
    void setMoveDirectionX(Enums.Direction direction);
}
