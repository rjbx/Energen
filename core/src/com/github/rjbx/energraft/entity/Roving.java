package com.github.rjbx.energage.entity;

import com.github.rjbx.energage.util.Enums;

public interface Roving extends Vehicular {
    Enums.Direction getDirectionX();
    void setDirectionX(Enums.Direction direction);
}
