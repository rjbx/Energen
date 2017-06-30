package com.udacity.gamedev.gigagal.entity;

import com.udacity.gamedev.gigagal.util.Enums;

public interface Dynamic extends Roving, Aerial {
    Enums.Orientation getOrientation();
}
