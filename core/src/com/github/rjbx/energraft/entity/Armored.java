package com.github.rjbx.energen.entity;

import com.github.rjbx.energen.util.Enums;

public interface Armored {
    long getStartTime();
    void resetStartTime();
    void strikeArmor();
    boolean isVulnerable();
    Enums.Direction getVulnerability();
    float getRecoverySpeed();
}
