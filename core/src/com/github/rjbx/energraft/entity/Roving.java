package com.github.rjbx.energen.entity;

import com.github.rjbx.energen.util.Enums;

public interface Roving extends Vehicular {
    Enums.Direction getDirectionX();
    void setDirectionX(Enums.Direction direction);
}
