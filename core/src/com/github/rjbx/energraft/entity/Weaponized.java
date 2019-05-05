package com.github.rjbx.energage.entity;

import com.github.rjbx.energage.util.Enums;

public interface Weaponized extends Physical {

    boolean getDispatchStatus();
    Enums.ShotIntensity getIntensity();
    Enums.Orientation getOrientation();
}
