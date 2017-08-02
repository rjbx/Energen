package com.udacity.gamedev.gigagal.entity;

import com.udacity.gamedev.gigagal.util.Enums;

public interface Armored {
    void strikeArmor();
    boolean isVulnerable();
    Enums.Direction getVulnerability();
}
