package com.github.rjbx.energen.entity;

import com.github.rjbx.energen.util.Enums;

public interface Aerial extends Vehicular {

    Enums.Direction getDirectionY();
    void setDirectionY(Enums.Direction direction);
}
