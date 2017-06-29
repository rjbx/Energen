package com.udacity.gamedev.gigagal.entity;

import com.udacity.gamedev.gigagal.util.Enums;

public interface Weaponized extends Physical {

    boolean getDispatchStatus();
    Enums.ShotIntensity getIntensity();
    Enums.Orientation getOrientation();
}
