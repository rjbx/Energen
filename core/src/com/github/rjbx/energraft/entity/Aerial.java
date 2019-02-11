package com.github.rjbx.energraft.entity;

import com.github.rjbx.energraft.util.Enums;

public interface Aerial extends Vehicular {

    Enums.Direction getDirectionY();
    void setDirectionY(Enums.Direction direction);
}
