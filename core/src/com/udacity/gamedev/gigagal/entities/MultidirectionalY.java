package com.udacity.gamedev.gigagal.entities;

import com.udacity.gamedev.gigagal.util.Enums;

public interface MultidirectionalY extends Multidirectional {
    Enums.Direction getMoveDirectionY();
    void setMoveDirectionY(Enums.Direction direction);
}
