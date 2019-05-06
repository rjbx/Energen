package com.github.rjbx.energen.entity;

import com.github.rjbx.energen.util.Enums;

public interface Energized extends Physical {

    boolean getDispatchStatus();
    Enums.ShotIntensity getIntensity();
    Enums.Orientation getOrientation();
}
