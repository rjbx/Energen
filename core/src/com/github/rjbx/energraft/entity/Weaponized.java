package com.github.rjbx.energraft.entity;

import com.github.rjbx.energraft.util.Enums;

public interface Weaponized extends Physical {

    boolean getDispatchStatus();
    Enums.ShotIntensity getIntensity();
    Enums.Orientation getOrientation();
}
