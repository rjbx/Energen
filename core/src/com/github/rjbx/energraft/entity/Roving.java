package com.github.rjbx.energraft.entity;

import com.github.rjbx.energraft.util.Enums;

public interface Roving extends Vehicular {
    Enums.Direction getDirectionX();
    void setDirectionX(Enums.Direction direction);
}
