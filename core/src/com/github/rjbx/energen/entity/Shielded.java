package com.github.rjbx.energen.entity;

import com.github.rjbx.energen.util.Enums;

public interface Shielded {
    long getStartTime();
    void resetStartTime();
    void strikeArmor();
    Enums.Direction getInvulnerability();
    float getRecoverySpeed();
}
