package com.github.rjbx.energage.entity;

import com.github.rjbx.energage.util.Enums;

public interface Armored {
    long getStartTime();
    void resetStartTime();
    void strikeArmor();
    boolean isVulnerable();
    Enums.Direction getVulnerability();
    float getRecoverySpeed();
}
