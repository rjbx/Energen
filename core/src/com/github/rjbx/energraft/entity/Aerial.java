package com.github.rjbx.energage.entity;

import com.github.rjbx.energage.util.Enums;

public interface Aerial extends Vehicular {

    Enums.Direction getDirectionY();
    void setDirectionY(Enums.Direction direction);
}
